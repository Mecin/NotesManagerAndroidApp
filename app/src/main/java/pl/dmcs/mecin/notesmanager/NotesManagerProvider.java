package pl.dmcs.mecin.notesmanager;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.util.Log;

import java.util.HashMap;

/**
 * Created by mecin on 29.10.14.
 */
public class NotesManagerProvider extends ContentProvider {


    private static String DATABASE_NAME = "notes.db";

    private static int DATABASE_VERSION = 1;

    private static String USERS_TABLE_NAME = "users";

    private static final int USERS = 1;

    private static final int USERS_ID = 2;

    public static String AUTHORITY = "pl.dmcs.notesmanager.providers.NotesManagerProvider";

    private static HashMap<String, String> notesProjectionMap;

    private static UriMatcher uriMatcher;

    private static class DatabaseHelper extends SQLiteOpenHelper {
        DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            Log.d(DATABASE_NAME, "onCreate");
            db.execSQL("CREATE TABLE " + USERS_TABLE_NAME + " (" + Tables.Users.USER_ID
                    + " INTEGER PRIMARY KEY AUTOINCREMENT," + Tables.Users.USERNAME + " VARCHAR(255)," + Tables.Users.PASSWORD + " VARCHAR(255)" + ");");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.w(DATABASE_NAME, "Upgrading database from version " + oldVersion + " to " + newVersion + ", which will destroy all old data");

            db.execSQL("DROP TABLE IF EXISTS " + USERS_TABLE_NAME);
            onCreate(db);
        }
    }

    private DatabaseHelper databaseHelper;

    @Override
    public int delete(Uri uri, String where, String[] whereArgs) {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        switch (uriMatcher.match(uri)) {
            case USERS:
                break;
            case USERS_ID:
                where = where + "_id = " + uri.getLastPathSegment();
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }

        int count = db.delete(USERS_TABLE_NAME, where, whereArgs);
        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }

    @Override
    public String getType(Uri uri) {
        switch (uriMatcher.match(uri)) {
            case USERS:
                return Tables.Users.CONTENT_TYPE;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues initialValues) {
        if (uriMatcher.match(uri) != USERS) {
            throw new IllegalArgumentException("Unknown URI " + uri);
        }

        ContentValues values;
        if (initialValues != null) {
            values = new ContentValues(initialValues);
        } else {
            values = new ContentValues();
        }

        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        long rowId = db.insert(USERS_TABLE_NAME, Tables.Users.USERNAME, values);
        if (rowId > 0) {
            Uri noteUri = ContentUris.withAppendedId(Tables.Users.CONTENT_URI, rowId);
            getContext().getContentResolver().notifyChange(noteUri, null);
            return noteUri;
        }

        throw new SQLException("Failed to insert row into " + uri);
    }

    @Override
    public boolean onCreate() {
        databaseHelper = new DatabaseHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        qb.setTables(USERS_TABLE_NAME);
        qb.setProjectionMap(notesProjectionMap);

        switch (uriMatcher.match(uri)) {
            case USERS:
                break;
            case USERS_ID:
                selection = selection + "_id = " + uri.getLastPathSegment();
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }

        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        Cursor c = qb.query(db, projection, selection, selectionArgs, null, null, sortOrder);

        c.setNotificationUri(getContext().getContentResolver(), uri);
        return c;
    }

    @Override
    public int update(Uri uri, ContentValues values, String where, String[] whereArgs) {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        int count;
        switch (uriMatcher.match(uri)) {
            case USERS:
                count = db.update(USERS_TABLE_NAME, values, where, whereArgs);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }

    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(AUTHORITY, USERS_TABLE_NAME, USERS);
        uriMatcher.addURI(AUTHORITY, USERS_TABLE_NAME + "/#", USERS_ID);

        notesProjectionMap = new HashMap<String, String>();
        notesProjectionMap.put(Tables.Users.USER_ID, Tables.Users.USER_ID);
        notesProjectionMap.put(Tables.Users.USERNAME, Tables.Users.USERNAME);
        notesProjectionMap.put(Tables.Users.PASSWORD, Tables.Users.PASSWORD);
    }
}
