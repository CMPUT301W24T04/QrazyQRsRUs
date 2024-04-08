package com.example.qrazyqrsrus;

import android.app.Activity;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;



/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileFragment} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {
    /**
     * Default constructor required for instantiating the fragment. Initializes a new instance of the {@link ProfileFragment}.
     */

    public ProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Called to do initial creation of the fragment. This is called after {@link #onAttach(Activity)} and before
     * {@link #onCreateView(LayoutInflater, ViewGroup, Bundle)}. Any saved state is delivered to the method in the savedInstanceState parameter.
     *
     * @param savedInstanceState If the fragment is being re-created from a previous saved state, this is the state.
     */

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /**
     * Called to have the fragment instantiate its user interface view. This is optional, and non-graphical fragments can return null.
     * This will be called between {@link #onCreate(Bundle)} and {@link #onActivityCreated(Bundle)}.
     *
     * @param inflater The LayoutInflater object that can be used to inflate any views in the fragment.
     * @param container If non-null, this is the parent view that the fragment's UI should be attached to. The fragment should not add the view itself.
     * @param savedInstanceState If non-null, this fragment is being re-constructed from a previous saved state as given here.
     * @return Return the View for the fragment's UI, or null.
     */

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        // Inflate the layout for this fragment
        return view;
    }
}