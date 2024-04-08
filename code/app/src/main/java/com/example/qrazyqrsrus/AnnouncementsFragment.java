// This fragment displays all the announcements
package com.example.qrazyqrsrus;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import java.util.ArrayList;

/**
 * Fragment to display the Announcements section which users
 * can access through events. Enables them to view announcements
 * sent by the organizers.
 * @author Ikjyot - icansingh
 * @see AnnouncementEditFragment
 * @version 1
 */
public class AnnouncementsFragment extends Fragment {

    public AnnouncementsFragment() {
        // Constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.layout_announcements, container, false);

        if (getArguments() == null){
            System.out.println("No Arguments provided");
            return null;
        }

        Event event = (Event) getArguments().get("event");
        assert event != null;

        ListView announcementListView = rootView.findViewById(R.id.list_announcements);
        Button backButton = rootView.findViewById(R.id.button_back);

        ArrayList<String> announcements = event.getAnnouncements();
        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_list_item_1, announcements);
        announcementListView.setAdapter(adapter);

        backButton.setOnClickListener(v -> {
            Navigation.findNavController(rootView).popBackStack(); // Not sure how to do this (Used john's implementation from elsewhere
        });

        return rootView;
    }

    public static AnnouncementsFragment newInstance(Event i){
        Bundle args = new Bundle();
        args.putSerializable("event", i);

        AnnouncementsFragment fragment = new AnnouncementsFragment();
        fragment.setArguments(args);
        return fragment;
    }

}

