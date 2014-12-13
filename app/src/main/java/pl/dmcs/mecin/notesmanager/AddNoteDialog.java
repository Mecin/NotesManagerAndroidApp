package pl.dmcs.mecin.notesmanager;


import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;


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
