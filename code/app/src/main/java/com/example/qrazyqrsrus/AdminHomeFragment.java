package com.example.qrazyqrsrus;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

public class AdminHomeFragment extends Fragment {
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //return super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.admin_home_fragment, container, false);

        Button eventsButton = view.findViewById(R.id.admin_events_button);
        Button profilesButton = view.findViewById(R.id.admin_profiles_button);
        Button postersButton = view.findViewById(R.id.admin_posters_button);
        Button profilePicturesButton = view.findViewById(R.id.admin_profile_pictures_button);

        ImageButton backButton = view.findViewById(R.id.back_button);

        eventsButton.setOnClickListener(v -> {
            //TODO: Add corresponding fragment to home_events_nav_graph.xml, and add an action from AdminHomeScreen to the corresponding fragment
            //TODO: Navigate to corresponding fragment similar to this:
            //Navigation.findNavController(view).navigate(R.id.action_newEventPromoQrFragment_to_newEventQrFragment);
        });

        profilesButton.setOnClickListener(v -> {
            //TODO: Add corresponding fragment to home_events_nav_graph.xml, and add an action from AdminHomeScreen to the corresponding fragment
            //TODO: Navigate to corresponding fragment similar to this:
            //Navigation.findNavController(view).navigate(R.id.action_newEventPromoQrFragment_to_newEventQrFragment);
        });

        postersButton.setOnClickListener(v -> {
            //TODO: Add corresponding fragment to home_events_nav_graph.xml, and add an action from AdminHomeScreen to the corresponding fragment
            //TODO: Navigate to corresponding fragment similar to this:
            Navigation.findNavController(view).navigate(R.id.action_adminHomeFragment_to_adminViewImagesFragment);
        });

        profilePicturesButton.setOnClickListener(v -> {
            //TODO: Add corresponding fragment to home_events_nav_graph.xml, and add an action from AdminHomeScreen to the corresponding fragment
            //TODO: Navigate to corresponding fragment similar to this:
            //Navigation.findNavController(view).navigate(R.id.action_newEventPromoQrFragment_to_newEventQrFragment);
        });

        backButton.setOnClickListener(v -> {
            Navigation.findNavController(view).popBackStack();
        });

        return view;
    }
}
