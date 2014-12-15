package pl.dmcs.mecin.notesmanager;


import android.app.ListFragment;
import android.app.ProgressDialog;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class DatabaseExplorer extends ListFragment {

    protected ArrayAdapter<String> adapter;
    public ArrayList<String> queryResultArrayList = null;

    public DatabaseExplorer() {
        // Required empty public constructor
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        adapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_list_item_1, queryResultArrayList);
        setListAdapter(adapter);
        final ListView scannerListView = getListView();

        Log.d("onActivityCreated", "enter onActivityCreated");

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
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //super.onCreateView(savedInstanceState);
        //setRetainInstance(true);
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_database_explorer, container, false);
        queryResultArrayList = new ArrayList<String>();

        // Get users/user
        Button getUsersButton = (Button) view.findViewById(R.id.get_users_db);
        getUsersButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity().getApplicationContext(), "Reading from db", Toast.LENGTH_SHORT).show();
                EditText userIdEditText = (EditText) getActivity().findViewById(R.id.user_id_db);
                String userId = "";
                if(userIdEditText != null) {
                    if(!userIdEditText.getText().toString().equals("")) {
                        userId = "/" + userIdEditText.getText().toString();
                        Log.d("GET_USERS", "userId : " + userId);
                    }
                }
                Uri getUsersUri = Uri.withAppendedPath(NotesManagerProvider.CONTENT_URI, Tables.Users.TABLE_NAME + userId);

                Log.d("GET_USERS", "before query.");
                // Select username, email from users like - returns cursor
                Cursor resultCursor = getActivity().getContentResolver().query(getUsersUri, new String[]{Tables.Users.USERNAME, Tables.Users.EMAIL, Tables.Users.PASSWORD}, "_id", null, null );

                String userToList;

                if(resultCursor != null) {
                    queryResultArrayList.clear();
                    while(resultCursor.moveToNext()) {
                        userToList = "";
                        userToList += "Username: " + resultCursor.getString(0) + "\nEmail: " + resultCursor.getString(1);
                        queryResultArrayList.add(userToList);
                        Log.d("GET_USERS", "username: " + resultCursor.getString(0) );
                    }

                    adapter.notifyDataSetChanged();
                }

            }
        });

        // Get notes/note
        Button getNotesButton = (Button) view.findViewById(R.id.get_notes_db);
        getNotesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity().getApplicationContext(), "Reading from db", Toast.LENGTH_SHORT).show();
                EditText noteIdEditText = (EditText) getActivity().findViewById(R.id.note_id_db);
                String noteId = "";
                if(noteIdEditText != null) {
                    if(!noteIdEditText.getText().toString().equals("")) {
                        noteId = "/" + noteIdEditText.getText().toString();
                        Log.d("GET_NOTES", "userId : " + noteId);
                    }
                }
                Uri getUsersUri = Uri.withAppendedPath(NotesManagerProvider.CONTENT_URI, Tables.Notes.TABLE_NAME + noteId);

                Log.d("GET_NOTES", "before query.");
                // Select username, email from users like - returns cursor
                Cursor resultCursor = getActivity().getContentResolver().query(getUsersUri, new String[]{Tables.Notes.NOTETITLE, Tables.Notes.NOTECONTENT, Tables.Notes.NOTEOWNER}, "_id", null, null );

                String userToList;

                if(resultCursor != null) {
                    queryResultArrayList.clear();
                    while(resultCursor.moveToNext()) {
                        userToList = "";
                        userToList += "Title: " + resultCursor.getString(0)
                        + "\nContent: " + resultCursor.getString(1)
                        + "\nOwner: " + resultCursor.getString(2);
                        queryResultArrayList.add(userToList);
                        Log.d("GET_NOTES", "Title: " + resultCursor.getString(0));
                    }

                    adapter.notifyDataSetChanged();
                }

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
