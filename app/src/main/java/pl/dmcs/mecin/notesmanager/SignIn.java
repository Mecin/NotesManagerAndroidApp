package pl.dmcs.mecin.notesmanager;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.database.Cursor;
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
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;


/**
 * A simple {@link Fragment} subclass.
 */
public class SignIn extends Fragment {

    OnSignIn mCallback;

    public SignIn() {
        // Required empty public constructor
    }

    // Interface for parent to comunicate
    public interface OnSignIn {
        public void onSignInSuccess(Fragment fragment, String signedUserName);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mCallback = (OnSignIn) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement OnSignIn");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_sign_in, container, false);

        // SigIp button
        Button signInButton = (Button) view.findViewById(R.id.sign_in_button);
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                EditText userEditText = (EditText)getView().findViewById(R.id.user_name_sign_in);
                EditText passEditText = (EditText)getView().findViewById(R.id.user_passwd_sign_in); //TODO still not encrypted

                String user = userEditText.getText().toString();
                String pass = passEditText.getText().toString();

                // Simply check
                if(!user.equals("") && !pass.equals("")) {
                    Log.d("LOGIN", "before login.");

                    // JSON login
                    JSONObject userJsonObject = new JSONObject();

                    // JSON getUser
                    // JSONObject getUserJsonObject = new JSONObject();

                    try {
                        userJsonObject.put("login", user);
                        userJsonObject.put("password", pass);

                        //getUserJsonObject.put(Tables.Users.USERNAME, user);
                        Log.d("progressDialog", "before progressDialog");
                        Tables.progressDialog = ProgressDialog.show(getActivity(), "", "Please wait...", true, true);

                        Tables.loggingFlag = true;

                        new HttpAsyncTask().execute(userJsonObject, Tables.API_SERVER + Tables.API_LOGIN_USER);
                        Tables.SIGNED_USERNAME = user;
                        //new HttpAsyncTask().execute(getUserJsonObject, Tables.API_SERVER + Tables.API_GET_USER);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    Uri getUsersUri = Uri.withAppendedPath(NotesManagerProvider.CONTENT_URI, Tables.Users.TABLE_NAME);

                    Log.d("LOGIN", "last path segment: " + getUsersUri.getLastPathSegment());

                    // Select userid, username, email from users like - returns cursor
                    Cursor resultCursor = getActivity().getContentResolver().query(getUsersUri, new String[]{Tables.Users.USER_ID, Tables.Users.USERNAME, Tables.Users.EMAIL, Tables.Users.PASSWORD}, Tables.Users.USERNAME + "=? AND " + Tables.Users.PASSWORD + "=?" , new String[]{user, pass}, null );

                    if(resultCursor != null && resultCursor.getCount() > 0) {

                        String userName = "";
                        String userId = "";

                        while(resultCursor.moveToNext()) {
                            Log.d("LOGIN", "Selected: username: " + resultCursor.getString(1)
                            + " mail: " + resultCursor.getString(2)
                            + " pass: " + resultCursor.getString(3)
                            );
                            userId = resultCursor.getString(0);
                            userName = resultCursor.getString(1);

                        }

                        Toast.makeText(getActivity().getApplicationContext(), "Login success!", Toast.LENGTH_SHORT).show();
                        mCallback.onSignInSuccess(new MainNotes(), userName);

                    } else {

                        Log.d("LOGIN", "Logging in");
                        while (Tables.loggingFlag) {
                            // Logging in

                        }


                        if(!Tables.SIGNED_USERNAME.equals("")) {
                            Toast.makeText(getActivity().getApplicationContext(), "Login success!", Toast.LENGTH_SHORT).show();
                            mCallback.onSignInSuccess(new MainNotes(), Tables.SIGNED_USERNAME);
                        } else {
                            Toast.makeText(getActivity().getApplicationContext(), "Login failed!", Toast.LENGTH_SHORT).show();
                        }
                    }

                } else {
                    Toast.makeText(getActivity().getApplicationContext(), "Empty fields!", Toast.LENGTH_SHORT).show();
                    //switchFragment();
                }
            }
        });

        return view;
    }



}