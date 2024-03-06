package com.example.qrazyqrsrus;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.qrazyqrsrus.Event;

import java.util.ArrayList;
/**
 * Updates the attendee list contents and saves it
 */
public class EventListAdapter extends ArrayAdapter<Event> {
    private ArrayList<Event> events;
    private Context context;

    public EventListAdapter(Context context, ArrayList<Event> events){
        super(context,0, events);
        this.events = events;
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
//        return super.getView(position, convertView, parent);
        View view = convertView;

        if(view == null){
            view = LayoutInflater.from(context).inflate(R.layout.event_list_content, parent,false);
        }
        // get position of the attendee
        Event event = events.get(position);

        // get textviews from content fragment
        TextView Name = view.findViewById(R.id.name_event);
        TextView location = view.findViewById(R.id.event_location); // = view.findViewById(R.id.number_check_ins);
        TextView date = view.findViewById(R.id.event_date);
        TextView details = view.findViewById(R.id.event_details);

        // set values of textviews based on the object attributes
        Name.setText(event.getName()); //.getEventName());
        location.setText(event.getLocation());
//        date.setText(event.getStartDate().toString());
        details.setText(event.getDetails());
//        num_checkins = "0"; //.setText(attendee.getNum_checkins().toString());

        return view;
    }
}
