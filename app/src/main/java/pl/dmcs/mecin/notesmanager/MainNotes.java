package pl.dmcs.mecin.notesmanager;


import android.app.FragmentTransaction;
import android.app.ListFragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.net.wifi.WifiConfiguration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class MainNotes extends ListFragment {

    protected NoteAdapter adapter;

    public MainNotes() {
        // Required empty public constructor
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        adapter = new NoteAdapter(getActivity(), R.layout.note_adapter_row, Tables.notesArrayList);
        setListAdapter(adapter);
        final ListView notesListView = getListView();

        notesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
                //Toast.makeText(getActivity().getApplicationContext(), "position: " + position + " id: " + id, Toast.LENGTH_SHORT).show();

                Bundle args = new Bundle();
                args.putParcelable("note", Tables.notesArrayList.get(position));

                ShowNoteDialog showNoteDialog = new ShowNoteDialog();

                showNoteDialog.setArguments(args);

                showNoteDialog.show(getFragmentManager(), "ShowNoteDialog");

            }
        });

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

            if (Tables.notesArrayList != null) {
                Log.d("onActivityCreated", "values != null");
                ArrayList<Note> values = savedInstanceState.getParcelableArrayList("storedAdapter");
                if(values != null) {
                    adapter.addAll(values);
                    adapter.notifyDataSetChanged();
                    //setListAdapter(adapter);
                }
            }
        }

        TextView signedUserTextView = (TextView) getActivity().findViewById(R.id.signed_user);
        signedUserTextView.append(" " + Tables.SIGNED_USERNAME + ".");

        TextView signoutTextView = (TextView)getActivity().findViewById(R.id.sign_out_text);

        String signOutText = "Sign out.";
        SpannableString spannableString = new SpannableString(signOutText);
        spannableString.setSpan(new UnderlineSpan(), 0, signOutText.length(), 0);
        signoutTextView.setText(spannableString);

        signoutTextView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Log.d("LINK", "ON LINK CLICK.");

                Tables.SIGNED_USERNAME = "";

                Tables.SIGNED_USER_ID = "";

                // Pop MainNotes fragment
                getFragmentManager().popBackStack();

                // Pop SignIn fragment
                getFragmentManager().popBackStack();

            }

        });

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

        Tables.notesArrayList = new ArrayList<Note>();

        if(savedInstanceState != null) {
            Log.d("SIS", "NOTES ALREADY FETCHED");
        } else {

            Tables.progressDialog = ProgressDialog.show(view.getContext(), "", "Please wait...", true, true);

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
                    Tables.progressDialog = ProgressDialog.show(v.getContext(), "", "Please wait...", true, true);
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

                    //for(String s : Tables.notesArrayList) {
                    //    Log.d("NOTES TEST", s);
                    //}



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
        if(adapter != null && Tables.notesArrayList != null) {

            savedState.putParcelableArrayList("storedAdapter", Tables.notesArrayList);
        }
        super.onSaveInstanceState(savedState);
        Log.d("onSaveInstanceState", "savedState put data");
    }

    private class NoteAdapter extends ArrayAdapter<Note> {

        private ArrayList<Note> items;

        public NoteAdapter(Context context, int textViewResourceId,
                         ArrayList<Note> items) {
            super(context, textViewResourceId, items);
            this.items = items;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View v = convertView;
            if (v == null) {
                LayoutInflater vi = LayoutInflater.from(getContext());
                v = vi.inflate(R.layout.note_adapter_row, null);
            }
            Note o = items.get(position);
            if (o != null) {

                TextView titleTextView = (TextView) v.findViewById(R.id.note_title_row);

                TextView textTextView = (TextView) v.findViewById(R.id.note_text_row);

                if (titleTextView != null) {
                    titleTextView.setText(o.getTitle());
                }

                if(textTextView != null) {
                    textTextView.setText(o.getNote());
                }
            }
            return v;
        }

    }

}
