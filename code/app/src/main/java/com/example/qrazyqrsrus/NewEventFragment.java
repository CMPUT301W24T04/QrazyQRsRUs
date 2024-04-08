package com.example.qrazyqrsrus;
// THis fragment shows a create event button
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;

/**
 * Holds new event in a bundle
 */
public class NewEventFragment extends Fragment {
    /**
     * create view
     * @param savedInstanceState If the fragment is being re-created from
     * a previous saved state, this is the state.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    /**
     * when view created, give options to switch between views
     * @param inflater The LayoutInflater object that can be used to inflate
     * any views in the fragment,
     * @param container If non-null, this is the parent view that the fragment's
     * UI should be attached to.  The fragment should not add the view itself,
     * but this can be used to generate the LayoutParams of the view.
     * @param savedInstanceState If non-null, this fragment is being re-constructed
     * from a previous saved state as given here.
     *
     * @return a view
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.new_event_fragment, container, false);
        Button button = view.findViewById(R.id.new_event_button);
        button.setOnClickListener(v -> {
            //Navigation.findNavController(view).navigate(R.id.action_newEventFragment_to_newEventTextFragment);
        });

        return view;
    }
}
