package pl.dmcs.mecin.notesmanager;


import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


/**
 * A simple {@link Fragment} subclass.
 */
public class SignIn extends Fragment {


    public SignIn() {
        // Required empty public constructor
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

                    Uri getUsersUri = Uri.withAppendedPath(NotesManagerProvider.CONTENT_URI, Tables.Users.TABLE_NAME);

                    Log.d("LOGIN", "last path segment: " + getUsersUri.getLastPathSegment());

                    // Select username, email from users like - returns cursor
                    Cursor resultCursor = getActivity().getContentResolver().query(getUsersUri, new String[]{Tables.Users.USERNAME, Tables.Users.EMAIL, Tables.Users.PASSWORD}, Tables.Users.USERNAME + "=? AND " + Tables.Users.PASSWORD + "=?" , new String[]{user, pass}, null );

                    if(resultCursor != null) {

                        while(resultCursor.moveToNext()) {
                            Log.d("LOGIN", "Selected: username: " + resultCursor.getString(0)
                            + " mail: " + resultCursor.getString(1)
                            + " pass: " + resultCursor.getString(2)
                            );
                        }


                    }

                    Toast.makeText(getActivity().getApplicationContext(), "Login success!", Toast.LENGTH_SHORT).show();


                } else {
                    Toast.makeText(getActivity().getApplicationContext(), "Empty fields!", Toast.LENGTH_SHORT).show();
                    //switchFragment();
                }
            }
        });

        return view;
    }


}
