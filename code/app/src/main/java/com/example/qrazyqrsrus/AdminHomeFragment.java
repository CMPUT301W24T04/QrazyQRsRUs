// This fragment displays the initial view for the admin
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
        Button imagesButton = view.findViewById(R.id.admin_images_button);


        eventsButton.setOnClickListener(v -> {
            Navigation.findNavController(view).navigate(R.id.action_adminHomeFragment_to_adminViewEventsFragment);
        });

        profilesButton.setOnClickListener(v -> {
            Navigation.findNavController(view).navigate(R.id.action_adminHomeFragment_to_adminViewAttendeesFragment);
        });

        imagesButton.setOnClickListener(v -> {
            Navigation.findNavController(view).navigate(R.id.action_adminHomeFragment_to_adminViewImagesFragment);
        });


        return view;
    }

    public static AdminHomeFragment newInstance(){
        AdminHomeFragment fragment = new AdminHomeFragment();
        return fragment;
    }
}
