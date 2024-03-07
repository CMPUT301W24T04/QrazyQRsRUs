package com.example.qrazyqrsrus;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Shows attendee information when list is clicked
 */
public class EventInfoView extends Fragment {
    /**
     *
     * @param inflater The LayoutInflater object that can be used to inflate
     * any views in the fragment,
     * @param container If non-null, this is the parent view that the fragment's
     * UI should be attached to.  The fragment should not add the view itself,
     * but this can be used to generate the LayoutParams of the view.
     * @param savedInstanceState If non-null, this fragment is being re-constructed
     * from a previous saved state as given here.
     *
     * @return attendeeInfoLayout
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View eventInfoLayout = inflater.inflate(R.layout.fragment_event_info_view, container, false);

        //https://stackoverflow.com/questions/42266436/passing-objects-between-fragments
        Bundle bundle = getArguments();
        Event event = (Event) bundle.getSerializable("current_event");

        TextView eventName = eventInfoLayout.findViewById(R.id.event_name);
        eventName.setText(event.getName());
        eventInfoLayout.findViewById(R.id.button_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(eventInfoLayout).navigate(R.id.action_eventInfoView_to_eventList);
            }
        });
        // Inflate the layout for this fragment
        return eventInfoLayout;
    }
}