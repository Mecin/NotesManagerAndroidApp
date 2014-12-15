package pl.dmcs.mecin.notesmanager;


import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;


/**
 * A simple {@link Fragment} subclass.
 */
public class AddNoteDialog extends DialogFragment {


    public AddNoteDialog() {
        // Required empty public constructor
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        LayoutInflater li = LayoutInflater.from(getActivity().getApplicationContext());
        View addNoteView = li.inflate(R.layout.fragment_add_note_dialog, null);

        final EditText noteTitleEditText = (EditText) addNoteView.findViewById(R.id.note_title);

        final EditText noteTextEditText = (EditText) addNoteView.findViewById(R.id.note_text);

        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setTitle(R.string.add_new_note)
                .setView(addNoteView)
                .setPositiveButton(R.string.add, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        LayoutInflater li = LayoutInflater.from(getActivity().getApplicationContext());
                        View connectView = li.inflate(R.layout.fragment_add_note_dialog, null);


                        //IP_ADDR = inputIpEditText.getText().toString();

                        Log.d("add note", "title: " + noteTitleEditText.getText().toString() + " text: " + noteTextEditText.getText().toString());

                        if (!noteTitleEditText.getText().toString().equals("") && !noteTextEditText.getText().toString().equals("")) {

                            // JSON
                            JSONObject addNoteJsonObject = new JSONObject();

                            try {
                                addNoteJsonObject.put(Tables.Notes.NOTEOWNER + "Id", Tables.SIGNED_USER_ID);
                                addNoteJsonObject.put(Tables.Notes.NOTETITLE, noteTitleEditText.getText().toString());
                                addNoteJsonObject.put(Tables.Notes.NOTECONTENT, noteTextEditText.getText().toString());

                                Log.d("JSON", "before execute add note");

                                new HttpAsyncTask().execute(addNoteJsonObject, Tables.API_SERVER + Tables.API_ADD_NOTE);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                            ContentValues contentValues = new ContentValues();
                            contentValues.put(Tables.Notes.NOTEOWNER, Tables.SIGNED_USERNAME);
                            contentValues.put(Tables.Notes.NOTETITLE, noteTitleEditText.getText().toString());
                            contentValues.put(Tables.Notes.NOTECONTENT, noteTextEditText.getText().toString());

                            Uri addNote = Uri.withAppendedPath(NotesManagerProvider.CONTENT_URI, Tables.Notes.TABLE_NAME);

                            //Uri addUserUri = getActivity().getContentResolver().insert(Tables.Users.CONTENT_URI, contentValues);

                            Log.d("ADD NOTE DB", "before inserting.");
                            Uri resultUri = getActivity().getContentResolver().insert(addNote, contentValues);

                            Log.d("ADD NOTE DB", "success.");

                            //mCallback.onConnectDialogFragment(IP_ADDR, selectedPosition);
                        } else {
                            Toast.makeText(getActivity().getApplicationContext(), "Fill empty fields!", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                    }
                });

        // Create the AlertDialog object and return it
        return builder.create();
    }


}
