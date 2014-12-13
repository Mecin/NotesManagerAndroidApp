package pl.dmcs.mecin.notesmanager;



import android.content.ContentUris;
import android.content.ContentValues;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

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
import java.util.Objects;



/**
 * A simple {@link Fragment} subclass.
 *
 */
public class SignUp extends Fragment {


    public SignUp() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_sign_up, container, false);

        // Sigup button
        Button signUpButton = (Button) view.findViewById(R.id.register_button);
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                EditText userEditText = (EditText)getView().findViewById(R.id.user_name_sign_up);
                EditText passEditText = (EditText)getView().findViewById(R.id.user_passwd_sign_up);
                EditText emailEditText = (EditText)getView().findViewById(R.id.user_email_sign_up);

                String user = userEditText.getText().toString();
                String pass = passEditText.getText().toString();
                String email = emailEditText.getText().toString();

                // Simply check
                if(!user.equals("") && !pass.equals("") && !email.equals("")) {

                    ContentValues contentValues = new ContentValues();
                    contentValues.put(Tables.Users.USERNAME, user);
                    contentValues.put(Tables.Users.PASSWORD, pass); // TODO add encryption and some strip functions
                    contentValues.put(Tables.Users.EMAIL, email);

                    // JSON
                    JSONObject userJsonObject = new JSONObject();
                    String userJson = "";
                    try {
                        userJsonObject.put(Tables.Users.USERNAME, user);
                        userJsonObject.put(Tables.Users.PASSWORD, pass); // TODO add encryption and some strip functions
                        userJsonObject.put(Tables.Users.EMAIL, email);

                        Log.d("JSON", "before execute");

                        new HttpAsyncTask().execute(userJsonObject, Tables.API_SERVER + Tables.API_REGISTER_USER);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }



                    Uri addUserUri = Uri.withAppendedPath(NotesManagerProvider.CONTENT_URI, Tables.Users.TABLE_NAME);

                    //Uri addUserUri = getActivity().getContentResolver().insert(Tables.Users.CONTENT_URI, contentValues);

                    Log.d("REGISTER", "before inserting.");
                    Uri resultUri = getActivity().getContentResolver().insert(addUserUri, contentValues);



                    Toast.makeText(getActivity().getApplicationContext(), "New record inserted!", Toast.LENGTH_SHORT).show();
                    // To get my ContentProvider for sql calls
                    //getActivity().getContentResolver().insert("USERS" ,contentValues)

                } else {
                    Toast.makeText(getActivity().getApplicationContext(), "Not inserted!", Toast.LENGTH_SHORT).show();
                    //switchFragment();
                }
            }
        });



        return view;
    }


}
