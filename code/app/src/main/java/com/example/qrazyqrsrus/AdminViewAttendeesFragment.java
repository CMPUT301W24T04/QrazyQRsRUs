// Shows all the attendees
package com.example.qrazyqrsrus;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 *
 */
public class AdminViewAttendeesFragment extends Fragment {
    private ImageView imageView;
    private EditText nameView;
    private EditText emailView;
    private Switch geolocationSwitch;
    private Button deleteButton;
    private Button cancelButton;
    private Button confirmButton;
    private TextView confirmTextView;


    private ArrayList<Attendee> allAttendees;
    private Integer currentPosition;

    public AdminViewAttendeesFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //TODO: change to fragment_admin_view_attendees
        View rootView = inflater.inflate(R.layout.fragment_admin_view_attendees, container, false);

        imageView = rootView.findViewById(R.id.imgProfilePicture);
        nameView = rootView.findViewById(R.id.etFullName);
        emailView = rootView.findViewById(R.id.etEmailAddress);
        geolocationSwitch = rootView.findViewById(R.id.switchGeolocation);
        FloatingActionButton backButton = rootView.findViewById(R.id.back_button);
        deleteButton = rootView.findViewById(R.id.delete_button);
        cancelButton = rootView.findViewById(R.id.cancel_button);
        confirmButton = rootView.findViewById(R.id.confirm_button);
        confirmTextView = rootView.findViewById(R.id.confirm_text_view);
        //TODO: update button colors and images with Mikael's work
        Button nextButton = rootView.findViewById(R.id.next_event_button);
        Button previousButton = rootView.findViewById(R.id.previous_event_button);

        currentPosition = 0;
        allAttendees = new ArrayList<>();

        //we get the initial list of all Attendees
        FirebaseDB.getInstance().getAllUsers(allAttendees, () -> updateView());

        backButton.setOnClickListener(view -> {
            try{
                Navigation.findNavController(rootView).popBackStack();
            } catch (Exception e){
                backButton.setVisibility(View.GONE);
            }
        });

        deleteButton.setOnClickListener(v -> changeState());
        cancelButton.setOnClickListener(v -> changeState());

        //we set an onclicklistener for the confirm button
        confirmButton.setOnClickListener(v -> {
            //we will delete the currently viewed image, clear our list of attendees, and get the updated list from firebase
            FirebaseDB.getInstance().deleteProfile(allAttendees.get(currentPosition));
            allAttendees.clear();
            //when the database is finished this operation, we update what is being displayed on screen
            FirebaseDB.getInstance().getAllUsers(allAttendees, () -> {
                //we view the previous image unless we are viewing the first image in the list
                if (currentPosition != 0){
                    currentPosition -= 1;
                }
                updateView();
            });
            //we reset the confirm, cancel, and delete button
            changeState();
        });
        nextButton.setOnClickListener(v -> {
            if (currentPosition != allAttendees.size() - 1) {
                currentPosition += 1;
                updateView();
            }
        });
        previousButton.setOnClickListener(v -> {
            if (currentPosition != 0) {
                currentPosition -= 1;
                updateView();
            }
        });

        return rootView;
    }

    /**
     * This function updates the screen with the new currently viewed Attendee
     */
    public void updateView() {
        Attendee currentAttendee = allAttendees.get(currentPosition);

        nameView.setText(currentAttendee.getName() != null ? currentAttendee.getName() : "N/A");
        emailView.setText(currentAttendee.getEmail() != null ? currentAttendee.getEmail() : "N/A");
        if (currentAttendee.getGeolocationOn() == null){
            geolocationSwitch.setChecked(false);
        } else{
            geolocationSwitch.setChecked(currentAttendee.getGeolocationOn());
        }

        if (currentAttendee.getProfilePicturePath() == null){
            imageView.setImageBitmap(InitialsPictureGenerator.createInitialsImage(InitialsPictureGenerator.getInitials(currentAttendee.getName())));
        } else{
            FirebaseDB.getInstance().retrieveImage(currentAttendee, bitmap -> imageView.setImageBitmap(bitmap));
        }
    }

    /**
     * This function will change the browse screen to and from the mode where the Confirm and Cancel deletion button are shown
     */
    public void changeState() {
        deleteButton.setVisibility(deleteButton.getVisibility() == View.GONE ? View.VISIBLE : View.GONE);
        cancelButton.setVisibility(cancelButton.getVisibility() == View.GONE ? View.VISIBLE : View.GONE);
        confirmButton.setVisibility(confirmButton.getVisibility() == View.GONE ? View.VISIBLE : View.GONE);
        confirmTextView.setVisibility(confirmTextView.getVisibility() == View.GONE ? View.VISIBLE : View.GONE);
    }
}