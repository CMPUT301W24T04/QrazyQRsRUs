package com.example.qrazyqrsrus;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EventDetailsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EventDetailsFragment extends Fragment {

    public EventDetailsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if (getArguments() == null){
            System.out.println("No Arguments provided");
            return null;
        }
        View view = inflater.inflate(R.layout.fragment_event_details, container, false);

        TextView nameView = view.findViewById(R.id.event_detail_name);
        TextView organizerView = view.findViewById(R.id.event_detail_organizer);
        TextView descriptionView = view.findViewById(R.id.event_detail_details);
        TextView startDateView = view.findViewById(R.id.event_detail_start_date);
        TextView endDateView = view.findViewById(R.id.event_detail_end_date);
        ImageView posterView = view.findViewById(R.id.posterView);
        ListView announcementListView = view.findViewById(R.id.announcement_list_view);


        // Inflate the layout for this fragment
        return view;
    }
    static EventDetailsFragment newInstance(String i){
        Bundle args = new Bundle();
        args.putSerializable("event", i);

        EventDetailsFragment fragment = new EventDetailsFragment();
        fragment.setArguments(args);
        return fragment;
    }
    public void setValues(DocumentReference docRef) {

    }
}