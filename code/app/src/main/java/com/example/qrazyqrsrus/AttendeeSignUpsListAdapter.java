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
public class AttendeeSignUpsListAdapter extends ArrayAdapter<Attendee> {

    private ArrayList<Attendee> attendees;
    private Context context;

    /**
     * Constructor to hold the attendee content
     * @param context
     * @param attendees
     */
    public AttendeeSignUpsListAdapter(Context context, ArrayList<Attendee> attendees){
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
     * @return
     */
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
//        return super.getView(position, convertView, parent);
        View view = convertView;

        if(view == null){
            view = LayoutInflater.from(context).inflate(R.layout.attendee_signups_list_content, parent,false);
        }
        // get position of the attendee
        Attendee attendee = attendees.get(position);

        // get name and num_checkins from the content fragment
        TextView Name = view.findViewById(R.id.name_signedup_attendee);

        // change value of name and num_checkins from content value
        Name.setText(attendee.getName());

        return view;
    }
    //
}
