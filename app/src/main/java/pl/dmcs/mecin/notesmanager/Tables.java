package pl.dmcs.mecin.notesmanager;

import android.app.ProgressDialog;
import android.net.Uri;
import android.provider.BaseColumns;
import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * Created by mecin on 29.10.14.
 */
public class Tables {
    public Tables() {

    }
    public static ProgressDialog progressDialog;
    public static boolean loggingFlag = false;
    public static boolean getUserFlag = false;
    public static boolean fetchNotes = false;
    public static boolean registerFlag = false;
    public static boolean success = false;

    public static final String API_SERVER = "http://mkolodziejski.eu/api/";
    public static final String API_REGISTER_USER = "registerUser";
    public static final String API_ADD_NOTE = "createNote";
    public static final String API_GET_NOTES = "getNotes";
    public static final String API_GET_USER = "getUser";
    public static final String API_LOGIN_USER = "loginUser";
    public static final String API_CREATE_CATEGORY = "createCategory";

    public static String SIGNED_USERNAME = "";
    public static String SIGNED_USER_ID = "";

    public static ArrayList<Note> notesArrayList = null;

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
        return convertInputStreamToJSONObject(inputStream, url);
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

    private static JSONObject convertInputStreamToJSONObject(InputStream inputStream, String operation) throws JSONException, IOException {
        BufferedReader bufferedReader = new BufferedReader( new InputStreamReader(inputStream));
        String line = "";
        String result = "";
        while((line = bufferedReader.readLine()) != null)
            result += line;

        inputStream.close();
        Log.d("int2json result: ", result);
        JSONObject resultJSNObject = new JSONObject(result);

        try {
            if(operation.equals(Tables.API_SERVER + Tables.API_ADD_NOTE)) {
                Log.d("OP POST", "Operation: " + operation);
                Log.d("CREATE CATEGORY", resultJSNObject.toString());
                if(resultJSNObject.getString("success").equals("true")) {
                    Log.d("OP POST", "API_ADD_NOTE successful.");
                    //Tables.loggingFlag = false;
                } else {
                    Log.d("OP POST", "API_ADD_NOTE could not register.");
                    //Tables.loggingFlag = false;
                    //Tables.SIGNED_USERNAME = "";
                }
            } else if(operation.equals(Tables.API_SERVER + Tables.API_CREATE_CATEGORY)) {
                Log.d("OP POST", "Operation: " + operation);
                Log.d("CREATE CATEGORY", resultJSNObject.toString());
                if(resultJSNObject.getString("success").equals("true")) {
                    Log.d("OP POST", "API_CREATE_CATEGORY successful.");
                    Tables.loggingFlag = false;
                } else {
                    Log.d("OP POST", "API_CREATE_CATEGORY could not register.");
                    Tables.loggingFlag = false;
                    //Tables.SIGNED_USERNAME = "";
                }
            } else if(operation.equals(Tables.API_SERVER + Tables.API_REGISTER_USER)) {
                Log.d("OP POST", "Operation: " + operation);

                if(resultJSNObject.getString("success").equals("true")) {
                    Log.d("OP POST", "API_REGISTER_USER successful.");
                    Tables.registerFlag = false;
                    Tables.success = true;
                    //Tables.loggingFlag = false;
                } else {
                    Log.d("OP POST", "API_REGISTER_USER could not register.");
                    Tables.registerFlag = false;
                    Tables.success = false;
                    //Tables.loggingFlag = false;
                    //Tables.SIGNED_USERNAME = "";
                }
            } else if (operation.equals(Tables.API_SERVER + Tables.API_LOGIN_USER)) {
                Log.d("OP POST", "Operation: " + operation);

                // Set username
                if(resultJSNObject.getString("success").equals("true")) {
                    Log.d("OP POST", "SIGNED_USERNAME was set to " + Tables.SIGNED_USERNAME);
                    // Log.d("OP POST", "creating default category");

                    // JSON getUser
                    JSONObject getUserJsonObject = new JSONObject();
                    try {

                        getUserJsonObject.put(Tables.Users.USERNAME, Tables.SIGNED_USERNAME);
                        Log.d("OP", "Operation: http://mkolodziejski.eu/api/getUser");
                        Tables.getUserFlag = true;
                        Tables.POST(getUserJsonObject, Tables.API_SERVER + Tables.API_GET_USER);
                        //new HttpAsyncTask().execute(getUserJsonObject, Tables.API_SERVER + Tables.API_GET_USER);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    Log.d("OP POST", "login before obtain id.");
                    // Obtain id.
                    while(Tables.getUserFlag) {

                    }

                    Log.d("OP POST", "after obtain id: " + Tables.SIGNED_USER_ID);

                    Tables.loggingFlag = false;
                } else {
                    Log.d("OP POST", "SIGNED_USERNAME could not be set.");
                    Tables.SIGNED_USERNAME = "";
                    Tables.loggingFlag = false;
                }

                if(Tables.progressDialog != null) {
                    if (Tables.progressDialog.isShowing()) {
                        Tables.progressDialog.dismiss();
                    }
                }

            } else if (operation.equals(Tables.API_SERVER + Tables.API_GET_USER)) {
                Log.d("OP POST", "Operation: " + operation);

                // Set userId
                if(resultJSNObject.getString("success").equals("true") && !Tables.SIGNED_USERNAME.equals("")) {
                    Tables.SIGNED_USER_ID = resultJSNObject.getString("message");
                    //Tables.loggingFlag = false;
                    Log.d("OP POST", "SIGNED_USER_ID was set to " + Tables.SIGNED_USER_ID);
                    Tables.getUserFlag = false;
                } else {
                    Log.d("OP POST", "SIGNED_USER_ID could not be set.");
                    Tables.SIGNED_USER_ID = "";
                    Tables.getUserFlag = false;
                    //Tables.loggingFlag = false;
                }

                //return Tables.POST((JSONObject) params[0], (String) params[1]);
            } else if(operation.equals(Tables.API_SERVER + Tables.API_GET_NOTES)) {
                Log.d("OP POST", "Operation: " + operation);
                Log.d("OP POST", resultJSNObject.toString());

                if(resultJSNObject.getString("success").equals("true")) {

                    JSONArray notesJsonArray = new JSONArray(resultJSNObject.getString("message"));
                    //notesJsonArray.put(resultJSNObject.get("message"));
                    Log.d("notes ARRAY", notesJsonArray.toString());
                    Tables.notesArrayList.clear();
                    for(int i = 0; i < notesJsonArray.length(); i++) {
                        JSONObject row = notesJsonArray.getJSONObject(i);
                        Log.d("FOREACH","note: " + i + " " + row.getString(Notes.NOTETITLE) + " == " + row.getString(Notes.NOTECONTENT));
                        Tables.notesArrayList.add(new Note(row.getString(Notes.NOTETITLE), row.getString(Notes.NOTECONTENT)));
                    }
                    Log.d("notes get message", resultJSNObject.getString("message"));
                    Log.d("OP POST", "API_GET_NOTES was executed successfully.");
                } else {
                    Log.d("OP POST", "API_GET_NOTES could not be executed.");

                }

                Tables.fetchNotes = false;

                if(Tables.progressDialog != null) {
                    if (Tables.progressDialog.isShowing()) {
                        Tables.progressDialog.dismiss();
                    }
                }


            } else {
                Log.d("OP", "Unexpected operation: " + operation);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return resultJSNObject;
    }
}

