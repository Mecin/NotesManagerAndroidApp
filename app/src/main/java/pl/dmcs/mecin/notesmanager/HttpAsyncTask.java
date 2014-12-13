package pl.dmcs.mecin.notesmanager;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

/**
 * Created by mecin on 13.12.14.
 */
public class HttpAsyncTask extends AsyncTask<Object, Void, JSONObject> {
    @Override
    protected JSONObject doInBackground(Object... params) {

        String operation = (String)params[1];
        try {
            if(operation.equals(Tables.API_SERVER + Tables.API_ADD_NOTE)) {
                Log.d("OP", "Operation: " + operation);
                return Tables.POST((JSONObject) params[0], (String) params[1]);
            } else if(operation.equals(Tables.API_SERVER + Tables.API_CREATE_CATEGORY)) {
                Log.d("OP", "Operation: " + operation);
                return Tables.POST((JSONObject) params[0], (String) params[1]);
            } else if(operation.equals(Tables.API_SERVER + Tables.API_REGISTER_USER)) {
                Log.d("OP", "Operation: " + operation);
                return Tables.POST((JSONObject) params[0], (String) params[1]);
            } else if (operation.equals(Tables.API_SERVER + Tables.API_LOGIN_USER)) {
                Log.d("OP", "Operation: " + operation);
                return Tables.POST((JSONObject) params[0], (String) params[1]);
            } else if (operation.equals(Tables.API_SERVER + Tables.API_GET_USER)) {
                Log.d("OP", "Operation: " + operation);
                return Tables.POST((JSONObject) params[0], (String) params[1]);
            } else if (operation.equals(Tables.API_SERVER + Tables.API_GET_NOTES)) {
                Log.d("OP", "Operation: " + operation);
                return Tables.POST((JSONObject) params[0], (String) params[1]);
            } else {
                Log.d("OP", "Unexpected operation: " + operation);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return new JSONObject();
    }
    // onPostExecute displays the results of the AsyncTask.
    @Override
    protected void onPostExecute(JSONObject result) {
        Log.d("onPostExecute POST", result.toString());

        try {
            if(result.getString("success").equals("true")) {
                //Toast.makeText(getActivity().getApplicationContext(), "Data Sent!", Toast.LENGTH_SHORT).show();

            } else {
                if(result.get("message") != null) {
                    //Toast.makeText(getActivity().getApplicationContext(), result.getString("message"), Toast.LENGTH_SHORT).show();
                } else {
                    //Toast.makeText(getActivity().getApplicationContext(), "Error!", Toast.LENGTH_SHORT).show();
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if(Tables.progressDialog != null) {
            if (Tables.progressDialog.isShowing()) {
                Tables.progressDialog.dismiss();
            }
        }

    }
}