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

        //public static final Uri CONTENT_URI = Uri.parse("content://"
        //+ NotesManagerProvider.AUTHORITY + "/users");

        //public static final String CONTENT_TYPE = "vnd.android.cursor.dir/users";

        public static final String TABLE_NAME = "users";

        public static final Uri CONTENT_URI = Uri.parse("content://" + NotesManagerProvider.AUTHORITY + "/" + TABLE_NAME);

        public static final String USER_ID = "_id";
        public static final String USERNAME = "username";
        public static final String EMAIL = "email";
        public static final String PASSWORD = "password";
    }
}
