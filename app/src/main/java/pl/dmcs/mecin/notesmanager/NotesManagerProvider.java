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

    private static final int USERS = 1;

    private static final int USERS_ID = 2;

    private static final int CATEGORIES = 3;

    private static final int CATEGORIES_ID = 4;

    private static final int NOTES = 5;

    private static final int NOTES_ID = 6;

    public static String AUTHORITY = "pl.dmcs.notesmanager.NotesManagerProvider";

    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY);

    private static HashMap<String, String> notesProjectionMap;

    private static UriMatcher uriMatcher;

    private static class DatabaseHelper extends SQLiteOpenHelper {
        DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            Log.d(DATABASE_NAME, "onCreate");
            db.execSQL("CREATE TABLE " + Tables.Users.TABLE_NAME + " ("
                    + Tables.Users.USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + Tables.Users.USERNAME + " VARCHAR(255),"
                    + Tables.Users.EMAIL + " VARCHAR(255),"
                    + Tables.Users.PASSWORD + " VARCHAR(255)"
                    + ");");
            Log.d("SQLite", "Table " + Tables.Users.TABLE_NAME + " created.");

            db.execSQL("CREATE TABLE " + Tables.Categories.TABLE_NAME + " ("
                    + Tables.Categories.CATEGORY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + Tables.Categories.CATEGORYNAME + " VARCHAR(255),"
                    + Tables.Categories.CATEGORYOWNER + " INTEGER"
                    + ");");
            Log.d("SQLite", "Table " + Tables.Categories.TABLE_NAME + " created.");

            db.execSQL("CREATE TABLE " + Tables.Notes.TABLE_NAME + " ("
                    + Tables.Notes.NOTE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + Tables.Notes.NOTETITLE + " VARCHAR(255),"
                    + Tables.Notes.NOTECONTENT + " VARCHAR(1000),"
                    + Tables.Notes.NOTECOLOR + " VARCHAR(255),"
                    + Tables.Notes.NOTECATEGORY + " INTEGER,"
                    + Tables.Notes.NOTEOWNER + " INTEGER"
                    + ");");
            Log.d("SQLite", "Table " + Tables.Notes.TABLE_NAME + " created.");


            Log.d("SQLite", "onCreate finished.");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.w(DATABASE_NAME, "Upgrading database from version " + oldVersion + " to " + newVersion + ", which will destroy all old data");

            db.execSQL("DROP TABLE IF EXISTS " + Tables.Users.TABLE_NAME);
            db.execSQL("DROP TABLE IF EXISTS " + Tables.Categories.TABLE_NAME);
            db.execSQL("DROP TABLE IF EXISTS " + Tables.Notes.TABLE_NAME);

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

        int count = db.delete(Tables.Users.TABLE_NAME, where, whereArgs);
        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }

    @Override
    public String getType(Uri uri) {
        switch (uriMatcher.match(uri)) {
            case USERS:
                return Tables.Users.TABLE_NAME;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues initialValues) {
        Log.d("SQLite", "insert start");

        // To avoid multiple inserting and replacing
        if ( (uriMatcher.match(uri) != USERS)
                && (uriMatcher.match(uri) != CATEGORIES)
                && (uriMatcher.match(uri) != NOTES) ) {
            throw new IllegalArgumentException("Unknown URI " + uri);
        }

        ContentValues values;
        if (initialValues != null) {
            values = new ContentValues(initialValues);
        } else {
            values = new ContentValues();
        }

        String insertIntoTable = "";
        switch (uriMatcher.match(uri)) {
            case USERS:
                insertIntoTable = Tables.Users.TABLE_NAME;
                Log.d("INSERT", "Inserting into users.");
                break;
            case CATEGORIES:
                insertIntoTable = Tables.Categories.TABLE_NAME;
                Log.d("INSERT", "Inserting into categories.");
                break;
            case NOTES:
                insertIntoTable = Tables.Notes.TABLE_NAME;
                Log.d("INSERT", "Inserting into notes.");
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);

        }

        Log.d("SQLite", "insert middle");
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        Log.d("URIPARSE", " : toString " + uri.toString());
        Log.d("URIPARSE", " : getLastPathSegment " + uri.getLastPathSegment());

        long rowId = db.insert(insertIntoTable, null, values);
        if (rowId > 0) {
            Uri noteUri = ContentUris.withAppendedId(Uri.parse("content://" + NotesManagerProvider.AUTHORITY + "/" + insertIntoTable), rowId);
            getContext().getContentResolver().notifyChange(noteUri, null);
            Log.d("SQLite", "Inserted");
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
        Log.d("QUERY", "query begin.");

        String selectFromTable = "";
        switch (uriMatcher.match(uri)) {
            case USERS:
                selectFromTable = Tables.Users.TABLE_NAME;
                Log.d("QUERY", "Selecting users.");
                break;
            case USERS_ID:
                selectFromTable = Tables.Users.TABLE_NAME;
                selection = selection + "_id = " + uri.getLastPathSegment();
                Log.d("QUERY", "Selecting user by id.");
                break;
            case CATEGORIES:
                selectFromTable = Tables.Categories.TABLE_NAME;
                Log.d("QUERY", "Selecting categories.");
                break;
            case CATEGORIES_ID:
                selectFromTable = Tables.Categories.TABLE_NAME;
                selection = selection + "_id = " + uri.getLastPathSegment();
                Log.d("QUERY", "Selecting category by id.");
                break;
            case NOTES:
                selectFromTable = Tables.Notes.TABLE_NAME;
                Log.d("QUERY", "Selecting notes.");
                break;
            case NOTES_ID:
                selectFromTable = Tables.Notes.TABLE_NAME;
                selection = selection + "_id = " + uri.getLastPathSegment();
                Log.d("QUERY", "Selecting note by id.");
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);

        }

        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        qb.setTables(selectFromTable);
        qb.setProjectionMap(notesProjectionMap);

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
                count = db.update(Tables.Users.TABLE_NAME, values, where, whereArgs);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }

    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(AUTHORITY, Tables.Users.TABLE_NAME, USERS);
        uriMatcher.addURI(AUTHORITY, Tables.Users.TABLE_NAME + "/#", USERS_ID);

        uriMatcher.addURI(AUTHORITY, Tables.Categories.TABLE_NAME, CATEGORIES);
        uriMatcher.addURI(AUTHORITY, Tables.Categories.TABLE_NAME + "/#", CATEGORIES_ID);

        uriMatcher.addURI(AUTHORITY, Tables.Notes.TABLE_NAME, NOTES);
        uriMatcher.addURI(AUTHORITY, Tables.Notes.TABLE_NAME + "/#", NOTES_ID);

        notesProjectionMap = new HashMap<String, String>();
        notesProjectionMap.put(Tables.Users.USER_ID, Tables.Users.USER_ID);
        notesProjectionMap.put(Tables.Users.USERNAME, Tables.Users.USERNAME);
        notesProjectionMap.put(Tables.Users.EMAIL, Tables.Users.EMAIL);
        notesProjectionMap.put(Tables.Users.PASSWORD, Tables.Users.PASSWORD);

        notesProjectionMap.put(Tables.Categories.CATEGORY_ID, Tables.Categories.CATEGORY_ID);
        notesProjectionMap.put(Tables.Categories.CATEGORYNAME, Tables.Categories.CATEGORYNAME);
        notesProjectionMap.put(Tables.Categories.CATEGORYOWNER, Tables.Categories.CATEGORYOWNER);

        notesProjectionMap.put(Tables.Notes.NOTE_ID, Tables.Notes.NOTE_ID);
        notesProjectionMap.put(Tables.Notes.NOTETITLE, Tables.Notes.NOTETITLE);
        notesProjectionMap.put(Tables.Notes.NOTECONTENT, Tables.Notes.NOTECONTENT);
        notesProjectionMap.put(Tables.Notes.NOTECATEGORY, Tables.Notes.NOTECATEGORY);
        notesProjectionMap.put(Tables.Notes.NOTECOLOR, Tables.Notes.NOTECOLOR);
        notesProjectionMap.put(Tables.Notes.NOTEOWNER, Tables.Notes.NOTEOWNER);
    }
}
