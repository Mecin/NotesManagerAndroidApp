package pl.dmcs.mecin.notesmanager;


import android.app.ListFragment;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class MainNotes extends ListFragment {

    private String SIGNED_USER = "";
    private String SIGNED_USER_ID = "";

    protected ArrayAdapter<String> adapter;
    public ArrayList<String> notesArrayList = null;


    public MainNotes() {
        // Required empty public constructor
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        //recLifeCycle_with_savedInstanceState(savedInstanceState);
        super.onActivityCreated(savedInstanceState);

        adapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_list_item_1, notesArrayList);
        setListAdapter(adapter);
        final ListView notesListView = getListView();

        if(getArguments() != null) {
            if (getArguments().getString("user") != null) {
                Log.d("onActivityCreated", "received " + getArguments().getString("user"));
                SIGNED_USER = getArguments().getString("user");
                SIGNED_USER_ID = getArguments().getString("id");
                //textView.append(" IP: " + RECEIVED_IP);
            }
        }

        // JSON getUser
        JSONObject getUserJsonObject = new JSONObject();



        try {

            getUserJsonObject.put(Tables.Users.USERNAME, SIGNED_USER);
            new HttpAsyncTask().execute(getUserJsonObject, Tables.API_SERVER + Tables.API_GET_USER);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        //ListView listView = (ListView) getActivity().findViewById(R.id.list);

        TextView signedUserTextView = (TextView) getActivity().findViewById(R.id.signed_user);
        signedUserTextView.append(" " + SIGNED_USER + ".");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_main_notes, container, false);

        notesArrayList = new ArrayList<String>();

        // Get notes
        // JSON
        JSONObject getNotesJsonObject = new JSONObject();
        try {
            Log.d("JSON", Tables.Notes.NOTEOWNER + "Id");

            getNotesJsonObject.put(Tables.Notes.NOTEOWNER + "Id", SIGNED_USER_ID);

            Log.d("JSON", "before execute");

            Tables.progressDialog = ProgressDialog.show(getActivity(), "", "Please wait...", true, true);

            new HttpAsyncTask().execute(getNotesJsonObject, Tables.API_SERVER + Tables.API_GET_NOTES);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        final Button addNoteButton = (Button) view.findViewById(R.id.add_note_button);
        addNoteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity().getApplicationContext(), "Add new note", Toast.LENGTH_SHORT).show();

                AddNoteDialog addNoteDialog = new AddNoteDialog();

                addNoteDialog.show(getFragmentManager(), "AddNoteDialog");
                // Switch to sign up fragment
                //mCallback.onButtonClick(new SignUp());
            }
        });

        return view;
    }


}
