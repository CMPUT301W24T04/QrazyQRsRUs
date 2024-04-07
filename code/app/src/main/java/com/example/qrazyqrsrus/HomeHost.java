package com.example.qrazyqrsrus;

// This fragment hosts the starting view when the user opens the app
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeHost#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeHost extends Fragment {

    public HomeHost() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home_host, container, false);
        // Inflate the layout for this fragment
        return view;
    }
}