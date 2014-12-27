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
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;


/**
 * A simple {@link Fragment} subclass.
 */
public class ShowNoteDialog extends DialogFragment {

    private Note note;

    public ShowNoteDialog() {
        // Required empty public constructor

    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        LayoutInflater li = LayoutInflater.from(getActivity().getApplicationContext());
        View addNoteView = li.inflate(R.layout.fragment_show_note_dialog, null);

        Bundle bundle = getArguments();

        if (bundle != null) {
            Log.d("ConnectDialog", "onCreateDialog bundle != null");
            note = bundle.getParcelable("note");
        }

        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        TextView noteTextView = (TextView) addNoteView.findViewById(R.id.note_title_show_note);

        noteTextView.setText(note.getNote());

        builder.setTitle(note.getTitle())
                .setView(addNoteView)
                .setPositiveButton(R.string.action_ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        LayoutInflater li = LayoutInflater.from(getActivity().getApplicationContext());
                        View connectView = li.inflate(R.layout.fragment_show_note_dialog, null);



                    }
                });


        // Create the AlertDialog object and return it
        return builder.create();
    }


}
