package com.example.qrazyqrsrus;

// This fragment hosts the starting view when the user opens the app
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * A simple {@link Fragment} subclass.
 * Use the  factory method to
 * create an instance of this fragment.
 */
public class HomeHost extends Fragment {


    /**
     * Required empty public constructor for creating an instance of this fragment.
     */
    public HomeHost() {
        // Required empty public constructor
    }

    /**
     * Called to do initial creation of the fragment. This is called after {@link #onAttach(android.content.Context)}
     * and before {@link #onCreateView(LayoutInflater, ViewGroup, Bundle)}.
     *
     * @param savedInstanceState If the fragment is being re-created from a previous saved state, this
     *                           is the state. This value may be {@code null}.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /**
     * Called to have the fragment instantiate its user interface view. This is optional, and
     * non-graphical fragments can return {@code null}. This will be called between {@link #onCreate(Bundle)}
     * and {@link #onViewCreated(View, Bundle)}.
     *
     * @param inflater The LayoutInflater object that can be used to inflate any views in the fragment.
     * @param container If non-null, this is the parent view that the fragment's UI should be attached to.
     *                  The fragment should not add the view itself, but this can be used to generate the
     *                  LayoutParams of the view.
     * @param savedInstanceState If non-null, this fragment is being re-constructed from a previous saved state
     *                           as given here.
     * @return Return the View for the fragment's UI, or {@code null}.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home_host, container, false);
    }
}