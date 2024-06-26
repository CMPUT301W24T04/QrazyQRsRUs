// This fragment gets the attendee information to display the name and number of check-ins on the list
// Problems: gets the name from usersCollection but gets number of check ins from checkIns collection but still works
// This class holds the attendee information to be displayed for check ins list through the content view
package com.example.qrazyqrsrus;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
/**
 * Saves the attendee class in a content view and adds that content to the list
 */
public class AttendeeListAdapter extends ArrayAdapter<Attendee> {

    private final ArrayList<Attendee> attendees;
    private final Context context;
    private FirebaseDB firebaseDB;

    /**
     * Constructor to hold the attendee content
     */
    public AttendeeListAdapter(Context context, ArrayList<Attendee> attendees){
        super(context,0, attendees);
        this.attendees = attendees;
        this.context = context;
    }

    /**
     * Add info to the content view
     * @param position The position of the item within the adapter's data set of the item whose view
     *        we want.
     * @param convertView The old view to reuse, if possible. Note: You should check that this view
     *        is non-null and of an appropriate type before using. If it is not possible to convert
     *        this view to display the correct data, this method can create a new view.
     *        Heterogeneous lists can specify their number of view types, so that this View is
     *        always of the right type (see {@link #getViewTypeCount()} and
     *        {@link #getItemViewType(int)}).
     * @param parent The parent that this view will eventually be attached to
     */
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;

        if(view == null){
            view = LayoutInflater.from(context).inflate(R.layout.attendee_list_content, parent,false);
        }

        if(firebaseDB == null){
            firebaseDB = FirebaseDB.getInstance();
        }

        // get position of the attendee
        Attendee attendee = attendees.get(position);

        // get name and num_checkins from the content fragment
        TextView Name = view.findViewById(R.id.name_attendee);
        TextView checkins = view.findViewById(R.id.number_check_ins);


        // change value of name and num_checkins from content value
        firebaseDB.getUserName(attendee.getId(), new FirebaseDB.GetStringCallBack() {
            @Override
            public void onResult(String string) {
                attendee.setName(string);
                Name.setText(attendee.getName());

            String checkins_string = "" + attendee.getCheckins();

                //concatenate number of checkins to the sentence using .concat()
                checkins.setText("# Check Ins: ".concat(checkins_string)); // https://www.w3schools.com/jsref/jsref_concat_string.asp
            }
        });

        return view;
    }

    public void setAdapterFirebaseDB(FirebaseDB instance){
        this.firebaseDB = instance;
    }
}
