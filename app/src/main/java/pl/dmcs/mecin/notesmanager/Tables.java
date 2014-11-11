package pl.dmcs.mecin.notesmanager;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by mecin on 29.10.14.
 */
public class Tables {
    public Tables() {

    }


    public static final class Users implements BaseColumns {
        private Users() {

        }


        public static final String TABLE_NAME = "users";

        public static final Uri CONTENT_URI = Uri.parse("content://" + NotesManagerProvider.AUTHORITY + "/" + TABLE_NAME);

        public static final String USER_ID = "_id";
        public static final String USERNAME = "username";
        public static final String EMAIL = "email";
        public static final String PASSWORD = "password";
    }

    public static final class Categories implements BaseColumns {
        private Categories() {

        }


        public static final String TABLE_NAME = "categories";

        public static final Uri CONTENT_URI = Uri.parse("content://" + NotesManagerProvider.AUTHORITY + "/" + TABLE_NAME);

        public static final String CATEGORY_ID = "_id";
        public static final String CATEGORYNAME = "name";
        public static final String CATEGORYOWNER = "owner";
    }

    public static final class Notes implements BaseColumns {
        private Notes() {

        }


        public static final String TABLE_NAME = "notes";

        public static final Uri CONTENT_URI = Uri.parse("content://" + NotesManagerProvider.AUTHORITY + "/" + TABLE_NAME);

        public static final String NOTE_ID = "_id";
        public static final String NOTETITLE = "title";
        public static final String NOTECONTENT = "content";
        public static final String NOTECATEGORY = "category_id";
        // bardzo smieszne ...
        public static final String NOTECOLOR = "color";
        public static final String NOTEOWNER = "owner";
    }
}
