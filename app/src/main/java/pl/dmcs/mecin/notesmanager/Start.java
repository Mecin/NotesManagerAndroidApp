package pl.dmcs.mecin.notesmanager;


import android.app.Activity;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;


/**
 * A simple {@link Fragment} subclass.
 */
public class Start extends Fragment {

    OnClickActivityAction mCallback;

    public Start() {
        // Required empty public constructor
    }

    // Interface for parent to comunicate
    public interface OnClickActivityAction {
        public void onButtonClick(Fragment fragment);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mCallback = (OnClickActivityAction) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement OnArticleSelectedListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_start, container, false);

        // Sign up button
        Button signUpButton = (Button) view.findViewById(R.id.signup_button);
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity().getApplicationContext(), "Sign up!", Toast.LENGTH_SHORT).show();

                // Switch to sign up fragment
                mCallback.onButtonClick(new SignUp());
            }
        });

        // Sign in button
        Button signInButton = (Button) view.findViewById(R.id.signin_button);
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity().getApplicationContext(), "Sign in!", Toast.LENGTH_SHORT).show();
                mCallback.onButtonClick(new SignIn());
            }
        });

        // Database Explorer button
        Button databaseButton = (Button) view.findViewById(R.id.database_button);
        databaseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity().getApplicationContext(), "Explore database!", Toast.LENGTH_SHORT).show();

                // Switch to sign up fragment
                mCallback.onButtonClick(new DatabaseExplorer());
            }
        });

        return view;
    }


}
