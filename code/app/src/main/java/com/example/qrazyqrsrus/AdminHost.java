package com.example.qrazyqrsrus;
// This fragment holds the location for AdminHomeFragment to be displayed
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AdminHost#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AdminHost extends Fragment {

    public AdminHost() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_admin_host, container, false);
        // Inflate the layout for this fragment
        return view;
    }

    public static AdminHost newInstance(){
        AdminHost fragment = new AdminHost();
        return fragment;
    }
}