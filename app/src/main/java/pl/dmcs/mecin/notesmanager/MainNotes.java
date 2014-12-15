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

    protected ArrayAdapter<String> adapter;

    public MainNotes() {
        // Required empty public constructor
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        adapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_list_item_1, Tables.notesArrayList);
        setListAdapter(adapter);
        final ListView notesListView = getListView();

        if(getArguments() != null) {
            if (getArguments().getString("user") != null) {
                Log.d("onActivityCreated", "received " + getArguments().getString("user"));
                //SIGNED_USER = getArguments().getString("user");
                //SIGNED_USER_ID = getArguments().getString("id");
                //textView.append(" IP: " + RECEIVED_IP);
            }
        }

        if (savedInstanceState != null) {
            Log.d("onActivityCreated", "savedInstanceState != null");
            String[] values = savedInstanceState.getStringArray("storedAdapter");
            if (values != null) {
                Log.d("onActivityCreated", "values != null");
                adapter.addAll(values);
                adapter.notifyDataSetChanged();
                //setListAdapter(adapter);
            }
        }

        // JSON getUser
        // JSONObject getUserJsonObject = new JSONObject();



        //try {

        //    getUserJsonObject.put(Tables.Users.USERNAME, SIGNED_USER);
        //    new HttpAsyncTask().execute(getUserJsonObject, Tables.API_SERVER + Tables.API_GET_USER);

        //} catch (JSONException e) {
        //    e.printStackTrace();
        //}
        //ListView listView = (ListView) getActivity().findViewById(R.id.list);

        TextView signedUserTextView = (TextView) getActivity().findViewById(R.id.signed_user);
        signedUserTextView.append(" " + Tables.SIGNED_USERNAME + ".");

        // getting notes from server
        while(Tables.fetchNotes) {

        }
        Log.d("fetch NOTES", "DONE, notify changes.");

        adapter.notifyDataSetChanged();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_main_notes, container, false);

        Tables.notesArrayList = new ArrayList<String>();


        Tables.progressDialog = ProgressDialog.show(getActivity(), "", "Please wait...", true, true);

        // Get notes
        // JSON
        JSONObject getNotesJsonObject = new JSONObject();
        try {
            Log.d("JSON", Tables.Notes.NOTEOWNER + "Id");

            getNotesJsonObject.put(Tables.Notes.NOTEOWNER + "Id", Tables.SIGNED_USER_ID);

            Log.d("JSON", "before execute");

            Tables.fetchNotes = true;

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

        Button getNotesButton = (Button) view.findViewById(R.id.fetch_notes);
        getNotesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!Tables.progressDialog.isShowing()) {
                    Tables.progressDialog = ProgressDialog.show(getActivity(), "", "Please wait...", true, true);
                }
                //Toast.makeText(getActivity().getApplicationContext(), "Fetch notes", Toast.LENGTH_SHORT).show();
                // Get notes
                // JSON
                JSONObject fetchNotesJsonObject = new JSONObject();
                try {
                    Log.d("JSON", Tables.Notes.NOTEOWNER + "Id and value: " + Tables.SIGNED_USER_ID);

                    fetchNotesJsonObject.put(Tables.Notes.NOTEOWNER + "Id", Tables.SIGNED_USER_ID);

                    Log.d("JSON", "before execute: " + fetchNotesJsonObject);

                    //Tables.progressDialog = ProgressDialog.show(getActivity(), "", "Please wait...", true, true);

                    Log.d("fetch NOTES", "before execute");
                    Log.d("fetch NOTES", "signed user: " + Tables.SIGNED_USERNAME + ", id: " + Tables.SIGNED_USER_ID);

                    Tables.fetchNotes = true;

                    new HttpAsyncTask().execute(fetchNotesJsonObject, Tables.API_SERVER + Tables.API_GET_NOTES);

                    // getting notes from server
                    while(Tables.fetchNotes) {

                    }
                    Log.d("fetch NOTES", "DONE, notify changes.");

                    adapter.notifyDataSetChanged();

                    for(String s : Tables.notesArrayList) {
                        Log.d("NOTES TEST", s);
                    }



                } catch (JSONException e) {
                    e.printStackTrace();
                }


                // Switch to sign up fragment
                //mCallback.onButtonClick(new SignUp());
            }
        });

        return view;
    }


    @Override
    public void onSaveInstanceState(Bundle savedState) {
        // To avoid null pointer exception when fragment is shadowed
        if(adapter != null) {
            String[] storedAdapter = new String[adapter.getCount()];
            for (int i = 0; i < adapter.getCount(); i++) {
                storedAdapter[i] = adapter.getItem(i);
            }

            savedState.putStringArray("storedAdapter", storedAdapter);
        }
        super.onSaveInstanceState(savedState);
        Log.d("onSaveInstanceState", "savedState put data");
    }

}
