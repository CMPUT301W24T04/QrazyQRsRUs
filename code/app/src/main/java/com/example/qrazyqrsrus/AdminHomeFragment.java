// This fragment displays the initial view for the admin
package com.example.qrazyqrsrus;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;


/**
 * The {@code AdminHomeFragment} class is responsible for displaying the admin home screen
 * in the application. It provides navigation options to manage events, profiles, and images.
 * <p>
 * This fragment includes buttons that navigate to different sections of the admin interface,
 * allowing the admin to view and manage the corresponding data.
 */
public class AdminHomeFragment extends Fragment {


    /**
     * Initializes the fragment. This method is called when the fragment is being created.
     *
     * @param savedInstanceState If the fragment is being re-created from a previous saved state,
     *                           this is the state. This value may be {@code null}.
     */
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    /**
     * Creates and returns the view hierarchy associated with the fragment.
     * <p>
     * This method inflates the layout for the admin home screen, which includes buttons
     * to navigate to the admin views for events, profiles, and images.
     *
     * @param inflater           The LayoutInflater object that can be used to inflate any views
     *                           in the fragment.
     * @param container          If non-null, this is the parent view that the fragment's UI
     *                           should be attached to. The fragment should not add the view itself,
     *                           but this can be used to generate the LayoutParams of the view.
     * @param savedInstanceState If non-null, this fragment is being re-constructed from a previous
     *                           saved state as given here.
     * @return Returns the View for the fragment's UI, or null.
     */
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //return super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.admin_home_fragment, container, false);

        Button eventsButton = view.findViewById(R.id.admin_events_button);
        Button profilesButton = view.findViewById(R.id.admin_profiles_button);
        Button imagesButton = view.findViewById(R.id.admin_images_button);


        eventsButton.setOnClickListener(v -> Navigation.findNavController(view).navigate(R.id.action_adminHomeFragment_to_adminViewEventsFragment));

        profilesButton.setOnClickListener(v -> Navigation.findNavController(view).navigate(R.id.action_adminHomeFragment_to_adminViewAttendeesFragment));

        imagesButton.setOnClickListener(v -> Navigation.findNavController(view).navigate(R.id.action_adminHomeFragment_to_adminViewImagesFragment));


        return view;
    }

    /**
     * Factory method to create a new instance of this fragment.
     *
     * @return A new instance of {@link AdminHomeFragment}.
     */
    public static AdminHomeFragment newInstance(){
        return new AdminHomeFragment();
    }
}
