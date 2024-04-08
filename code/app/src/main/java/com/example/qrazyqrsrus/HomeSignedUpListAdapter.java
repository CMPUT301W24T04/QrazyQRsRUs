package com.example.qrazyqrsrus;

// This class holds the information for users signed up on the content view for the events signed up list
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class HomeSignedUpListAdapter extends ArrayAdapter<Event> {
    private final ArrayList<Event> events;
    private final Context context;

    /**
     * Constructor for the adapter
     */
    public HomeSignedUpListAdapter(Context context, ArrayList<Event> events){
        super(context,0, events);
        this.events = events;
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
     * @return view
     */
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
//        return super.getView(position, convertView, parent);
        View view = convertView;

        if(view == null){
            view = LayoutInflater.from(context).inflate(R.layout.signed_up_list_content, parent,false);
        }
        // get position of the attendee
        Event event = events.get(position);

        // get textviews from content fragment
        TextView Name = view.findViewById(R.id.signed_up_name);
        TextView Location = view.findViewById(R.id.signed_up_location);


        // set values of textviews based on the object attributes

        Name.setText(event.getName()); //.getEventName());
        Location.setText(event.getLocation());

        String tempName = event.getName();
        if (tempName.length() > 20) {
            String newText = tempName.substring(0, 20) + "...";
            Name.setText(newText);
        } else {
            Name.setText(tempName);
        }

        return view;
    }

}
