package pl.dmcs.mecin.notesmanager;



import android.content.ContentUris;
import android.content.ContentValues;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


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

                String user = userEditText.getText().toString();
                String pass = passEditText.getText().toString();

                // Simply check
                if(!user.equals("") && !pass.equals("")) {

                    ContentValues contentValues = new ContentValues();
                    contentValues.put(Tables.Users.USERNAME, user);
                    contentValues.put(Tables.Users.PASSWORD, pass); // TODO add encryption

                    Uri addUserUri = getActivity().getContentResolver().insert(Tables.Users.CONTENT_URI, contentValues);

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
