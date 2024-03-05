package com.example.qrazyqrsrus;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.qrazyqrsrus.Attendee;

import java.util.ArrayList;

public class AttendeeListAdapter extends ArrayAdapter<Attendee> {
    /**
     * Updates the attendee list contents and saves it
     */
    private ArrayList<Attendee> attendees;
    private Context context;

    public AttendeeListAdapter(Context context, ArrayList<Attendee> attendees){
        super(context,0, attendees);
        this.attendees = attendees;
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
//        return super.getView(position, convertView, parent);
        View view = convertView;

        if(view == null){
            view = LayoutInflater.from(context).inflate(R.layout.attendee_list_content, parent,false);
        }
        // get position of the attendee
        Attendee attendee = attendees.get(position);

        // get name and num_checkins from the content fragment
        TextView Name = view.findViewById(R.id.name_attendee);
        TextView num_checkins = view.findViewById(R.id.number_check_ins); // = view.findViewById(R.id.number_check_ins);

        // change value of name and num_checkins from content value
        Name.setText(attendee.getName());
//        num_checkins = "0"; //.setText(attendee.getNum_checkins().toString());

        return view;
    }
}
