package pl.dmcs.mecin.notesmanager;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by mecin on 27.12.14.
 */


public class Note implements Parcelable {

    private String title;

    private String note;

    public Note(String title, String note) {
        this.title = title;
        this.note = note;
    }

    public String getTitle() {
        return this.title;
    }

    public String getNote() {
        return this.note;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {

    }
}

