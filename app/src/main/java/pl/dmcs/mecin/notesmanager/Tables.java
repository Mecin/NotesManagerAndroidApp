package pl.dmcs.mecin.notesmanager;

import android.net.Uri;
import android.provider.BaseColumns;
import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by mecin on 29.10.14.
 */
public class Tables {
    public Tables() {

    }

    public static final String API_SERVER = "http://mkolodziejski.eu/api/";
    public static final String API_REGISTER_USER = "registerUser";
    public static final String API_ADD_NOTE = "createNote";
    public static final String API_GET_NOTES = "getNotes";
    public static final String API_GET_USER = "getUser";

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


    protected static JSONObject POST(JSONObject object, String url) throws JSONException, IOException {
        InputStream inputStream = null;
        JSONObject result;
        try {

            // 1. create HttpClient
            HttpClient httpclient = new DefaultHttpClient();

            // 2. make POST request to the given URL
            HttpPost httpPost = new HttpPost(url);

            String json = "";

            // 3. build jsonObject
            // done

            // 4. convert JSONObject to JSON to String
            json = object.toString();
            Log.d("JSON POST", json);

            // ** Alternative way to convert Person object to JSON string usin Jackson Lib
            // ObjectMapper mapper = new ObjectMapper();
            // json = mapper.writeValueAsString(person);

            // 5. set json to StringEntity
            StringEntity se = new StringEntity(json);

            // 6. set httpPost Entity
            httpPost.setEntity(se);

            // 7. Set some headers to inform server about the type of the content
            httpPost.setHeader("Accept", "application/json");
            httpPost.setHeader("Content-type", "application/json");

            Log.d("JSON POST", "before execute httpPost");
            // 8. Execute POST request to the given URL
            HttpResponse httpResponse = httpclient.execute(httpPost);
            Log.d("JSON POST", "after execute httpPost");

            // 9. receive response as inputStream
            inputStream = httpResponse.getEntity().getContent();

            // 10. convert inputstream to string
            //if(inputStream != null)
            //    result = convertInputStreamToJSONObject(inputStream);
            //else
            //    result = "Did not work!";

        } catch (Exception e) {
            Log.d("InputStream", e.getLocalizedMessage());
        }

        // 11. return result
        return convertInputStreamToJSONObject(inputStream);
    }

    private static String convertInputStreamToString(InputStream inputStream) throws IOException {
        BufferedReader bufferedReader = new BufferedReader( new InputStreamReader(inputStream));
        String line = "";
        String result = "";
        while((line = bufferedReader.readLine()) != null)
            result += line;

        inputStream.close();
        return result;

    }

    private static JSONObject convertInputStreamToJSONObject(InputStream inputStream) throws JSONException, IOException {
        BufferedReader bufferedReader = new BufferedReader( new InputStreamReader(inputStream));
        String line = "";
        String result = "";
        while((line = bufferedReader.readLine()) != null)
            result += line;

        inputStream.close();
        return new JSONObject(result);
    }
}

