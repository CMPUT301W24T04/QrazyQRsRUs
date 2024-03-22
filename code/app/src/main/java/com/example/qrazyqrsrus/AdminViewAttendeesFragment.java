package com.example.qrazyqrsrus;

import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Objects;

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
    private Button nextButton;
    private Button previousButton;



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
        nextButton = rootView.findViewById(R.id.next_event_button);
        previousButton = rootView.findViewById(R.id.previous_event_button);

        currentPosition = 0;
        allAttendees = new ArrayList<Attendee>();

        //we get the initial list of all Attendees
        FirebaseDB.getAllUsers(allAttendees, new FirebaseDB.OnFinishedCallback() {
            @Override
            public void onFinished() {
                updateView();
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{
                    Navigation.findNavController(rootView).popBackStack();
                } catch (Exception e){
                    backButton.setVisibility(View.GONE);
                }
            }
        });

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeState();
            }
        });
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeState();
            }
        });

        //we set an onclicklistener for the confirm button
        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //we will delete the currently viewed image, clear our list of attendees, and get the updated list from firebase
                //TODO: see if we need to put the clear and getAllUsers into a onFinished callback with deleteProfile
                FirebaseDB.deleteProfile(allAttendees.get(currentPosition));
                allAttendees.clear();
                FirebaseDB.getAllUsers(allAttendees, new FirebaseDB.OnFinishedCallback() {
                    //when the database is finished this operation, we update what is being displayed on screen
                    @Override
                    public void onFinished() {
                        //we view the previous image unless we are viewing the first image in the list
                        if (currentPosition != 0){
                            currentPosition -= 1;
                        }
                        updateView();
                    }
                });
                //we reset the confirm, cancel, and delete button
                changeState();
            }
        });
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentPosition != allAttendees.size() - 1) {
                    currentPosition += 1;
                    updateView();
                }
            }
        });
        previousButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentPosition != 0) {
                    currentPosition -= 1;
                    updateView();
                }
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
            //TODO: nothing?
        } else{
            FirebaseDB.retrieveImage(currentAttendee, new FirebaseDB.GetBitmapCallBack() {
                @Override
                public void onResult(Bitmap bitmap) {
                    imageView.setImageBitmap(bitmap);
                }
            });
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