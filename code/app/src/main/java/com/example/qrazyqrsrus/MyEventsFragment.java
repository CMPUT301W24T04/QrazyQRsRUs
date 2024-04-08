package com.example.qrazyqrsrus;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MyEventsFragment} factory method to
 * create an instance of this fragment.
 */

public class MyEventsFragment extends Fragment {

    /**
     * Required empty public constructor for the fragment. This constructor is necessary for the Android
     * framework to instantiate the fragment correctly.
     */
    public MyEventsFragment() {
        // Required empty public constructor
    }

    /**
     * Called to do the initial creation of the fragment. This method is called after the fragment is
     * attached but before it has been presented in the UI. Use this method for initial setup and
     * configuration of the fragment, such as setting up event listeners, adapters, or other initialization tasks.
     *
     * @param savedInstanceState If the fragment is being re-created from a previous saved state, this
     *                           is the state. This parameter may be null if there is no saved state.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /**
     * Called to have the fragment instantiate its user interface view. This is the point at which
     * the fragment's view hierarchy is created and returned to the host activity. The layout for
     * this fragment's view is defined in an XML layout file, which is inflated here.
     *
     * @param inflater The LayoutInflater object that can be used to inflate views in the fragment.
     * @param container The parent view that the fragment's UI should be attached to. This may be null.
     * @param savedInstanceState If non-null, this fragment is being re-constructed from a previous saved state.
     * @return The View for the fragment's UI.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_events, container, false);
        // Inflate the layout for this fragment
        return view;
    }
}