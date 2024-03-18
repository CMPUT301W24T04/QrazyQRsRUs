package com.example.qrazyqrsrus;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import android.widget.ArrayAdapter;

import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EventDetailsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EventDetailsFragment extends Fragment {

    private Attendee attendee;
    private Event event;
    public EventDetailsFragment() {
        // Required empty public constructor
    }
    /**
     * creates view
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /**
     * show details when view is created
     * @param inflater The LayoutInflater object that can be used to inflate
     * any views in the fragment,
     * @param container If non-null, this is the parent view that the fragment's
     * UI should be attached to.  The fragment should not add the view itself,
     * but this can be used to generate the LayoutParams of the view.
     * @param savedInstanceState If non-null, this fragment is being re-constructed
     * from a previous saved state as given here.
     *
     * @return
     */
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if (getArguments() == null){
            System.out.println("No Arguments provided");
            return null;
        }
        this.event = (Event) getArguments().get("event");
        this.attendee = (Attendee) getArguments().get("attendee");

        if (this.attendee == null){
            String userId = Settings.Secure.getString(getContext().getContentResolver(), Settings.Secure.ANDROID_ID);
            FirebaseDB.loginUser(userId, new FirebaseDB.GetAttendeeCallBack() {
                @Override
                public void onResult(Attendee attendee) {
                    setAttendee(attendee);
                }
            });
        }
        View rootView = inflater.inflate(R.layout.fragment_event_details, container, false);

        TextView nameView = rootView.findViewById(R.id.event_detail_name);
        //TextView organizerView = rootView.findViewById(R.id.event_detail_organizer);
        TextView locationView = rootView.findViewById(R.id.event_detail_location);
        TextView descriptionView = rootView.findViewById(R.id.event_detail_details);
        TextView startDateView = rootView.findViewById(R.id.event_detail_start_date);
        TextView endDateView = rootView.findViewById(R.id.event_detail_end_date);
        ImageView posterView = rootView.findViewById(R.id.posterView);
        ListView announcementListView = rootView.findViewById(R.id.announcement_list_view);
        Button signUpEvent = rootView.findViewById(R.id.sign_up_button);
        Button viewAttendeesButton = rootView.findViewById(R.id.attendee_list_button);
        FloatingActionButton backButton = rootView.findViewById(R.id.back_button);
        ImageView promoQRView = rootView.findViewById(R.id.promo_qr_view);
        ImageView checkInQRView = rootView.findViewById(R.id.check_in_qr_view);


        backButton.setOnClickListener(new View.OnClickListener() {
            // need to get attendeeID and eventID first
            @Override
            public void onClick(View view) {
                //currently it is possible that the view could have no NavController if we got to the screen from the qr scanner
                //TODO: hide back button instead of do nothing
                try{
                    Navigation.findNavController(rootView).popBackStack();
                } catch (Exception e){

                }
            }
        });
        signUpEvent.setOnClickListener(new View.OnClickListener() {
            // need to get attendeeID and eventID first
            @Override
            public void onClick(View view) {
                event.addSignUp(attendee.getDocumentId());
                FirebaseDB.updateEvent(event);
            }
        });

        String nameString = "Name: "+event.getName();
        //String organizerString = "Organized by: ";
        FirebaseDB.getUserName(event.getOrganizerId(), new FirebaseDB.GetStringCallBack() {
            @Override
            public void onResult(String string) {
                updateOrganizerString(string, rootView);
            }
        });
        String locationString = "Location: "+event.getLocation();
        String descriptionString = "Description: "+event.getDetails();
        String startDateString = "Starts: "+event.getStartDate();
        String endDateString = "Ends: "+event.getEndDate();


        nameView.setText(nameString);
        //organizerView.setText(organizerString);
        locationView.setText(locationString);
        descriptionView.setText(descriptionString);
        startDateView.setText(startDateString);
        endDateView.setText(endDateString);

        //we set all the images on screen.
        setImages(this.event, posterView, promoQRView, checkInQRView);

        setButtonVisibility(this.attendee, signUpEvent);

        ArrayList<String> announcementsList = event.getAnnouncements();
        if (announcementsList == null){
            announcementsList = new ArrayList<String>();
        }
        ArrayAdapter<String> announcementsAdapter = new ArrayAdapter<String>(requireContext(), android.R.layout.simple_list_item_1, announcementsList);
        announcementListView.setAdapter(announcementsAdapter);

        // Inflate the layout for this fragment
        return rootView;
    }
    public static EventDetailsFragment newInstance(Event i, Attendee attendee, Boolean isCheckIn){
        Bundle args = new Bundle();
        args.putSerializable("event", i);
        args.putSerializable("attendee", attendee);
        args.putSerializable("isCheckIn", isCheckIn);

        EventDetailsFragment fragment = new EventDetailsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private void updateOrganizerString(String string, View view){
        ((TextView) view.findViewById(R.id.event_detail_organizer)).setText("Organizer: " + string);
    }

    private void setAttendee(Attendee attendee){
        this.attendee = attendee;
    }

    /**
     * This function hides the signup button if the user is the organizer of the event, or has already signed up for the event
     * @param attendee The attendee that is viewing the event details
     * @param signUpButton The button to hide
     */
    private void setButtonVisibility(Attendee attendee, Button signUpButton){
        if (event.getSignUps().contains(attendee.getDocumentId()) || event.getOrganizerId() == attendee.getDocumentId()){
            signUpButton.setVisibility(View.GONE);
        }
    }

    /**
     * This function sets the images on the event details screen, fetching the poster from firebase, and generating QR codes from the event's QR contents
     * @param event The event that is being viewed
     * @param posterView The ImageView that should display the event poster
     * @param promoQRView The ImageView that should display the event's promo QR code
     * @param checkInQRView The ImageView that should display the event's check-in QR code
     */
    private void setImages(Event event, ImageView posterView, ImageView promoQRView, ImageView checkInQRView){
        if (event.getPosterPath() != null) {
            FirebaseDB.retrieveImage(event, new FirebaseDB.GetBitmapCallBack() {
                @Override
                public void onResult(Bitmap bitmap) {
                    posterView.setImageBitmap(bitmap);
                }
            });
        }

        promoQRView.setImageBitmap(QRCodeGenerator.generateBitmap(event.getQrCodePromo(), getActivity()));
        checkInQRView.setImageBitmap(QRCodeGenerator.generateBitmap(event.getQrCode(), getActivity()));
    }

}