package com.example.qrazyqrsrus;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.provider.MediaStore;
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
import android.widget.Toast;

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

    private Bitmap promoBitmap;
    private Bitmap checkInBitmap;
    private Boolean isCheckedIn;
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

        View rootView = inflater.inflate(R.layout.fragment_event_details, container, false);

        TextView nameView = rootView.findViewById(R.id.event_detail_name);
        TextView locationView = rootView.findViewById(R.id.event_detail_location);
        TextView descriptionView = rootView.findViewById(R.id.event_detail_details);
        TextView startDateView = rootView.findViewById(R.id.event_detail_start_date);
        TextView endDateView = rootView.findViewById(R.id.event_detail_end_date);
        ImageView posterView = rootView.findViewById(R.id.posterView);
        Button signUpEvent = rootView.findViewById(R.id.sign_up_button);
        Button viewAttendeesButton = rootView.findViewById(R.id.attendee_list_button);
        Button viewAnnouncementsButton = rootView.findViewById(R.id.view_announcements_button);
        Button viewLocations = rootView.findViewById(R.id.button_geolocation);
        FloatingActionButton backButton = rootView.findViewById(R.id.back_button);
        ImageView promoQRView = rootView.findViewById(R.id.promo_qr_view);
        ImageView checkInQRView = rootView.findViewById(R.id.check_in_qr_view);
        Button promoQRShare = rootView.findViewById(R.id.promo_share_button);
        Button checkInQRShare = rootView.findViewById(R.id.check_in_share_button);


        //try to get event and attendee from bundle
        //if attendee is not there, that's fine, if event not there, very bad.
        if (getArguments() == null){
            System.out.println("No Arguments provided");
            return null;
        } else{
            this.event = (Event) getArguments().get("event");
            this.attendee = (Attendee) getArguments().get("attendee");
            this.isCheckedIn = (Boolean) getArguments().get("isCheckedIn");

        }

        if (this.event.getGeolocationOn()) {
            viewLocations.setVisibility(View.VISIBLE);
        }
        //we check if attendee is set, logging in the user if not
        //then we update the images that are displayed, and the visibility of the signup button once we have the attendee
        if (this.attendee == null){
            String userId = Settings.Secure.getString(getContext().getContentResolver(), Settings.Secure.ANDROID_ID);
            FirebaseDB.getInstance().loginUser(userId, new FirebaseDB.GetAttendeeCallBack() {
                @Override
                public void onResult(Attendee attendee) {
                    setAttendee(attendee);
                    setImages(posterView, promoQRView, checkInQRView);
                    setButtonVisibility(signUpEvent, viewAttendeesButton, EventDetailsFragment.this.event);
                }
            });
        } else{
            setImages(posterView, promoQRView, checkInQRView);
            setButtonVisibility(signUpEvent, viewAttendeesButton, this.event);
        }

        //Change view to attendee list when click on view attendees button
        viewAttendeesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // pass the event as a bundle to the attendeeList so we know which event to get from
                Bundle args = new Bundle();
                args.putSerializable("event", event);
                Navigation.findNavController(rootView).navigate(R.id.action_eventDetailsFragment_to_attendeeList2,args);
            }
        });

        //Change view to locations when click on view geolocation button
        viewLocations.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // pass the event as a bundle to the attendeeList so we know which event to get from
                Bundle args = new Bundle();
                args.putSerializable("event", event);
                Navigation.findNavController(rootView).navigate(R.id.action_eventDetailsFragment_to_geoLocation,args);
            }
        });



        backButton.setOnClickListener(new View.OnClickListener() {
            // need to get attendeeID and eventID first
            @Override
            public void onClick(View view) {
                //currently it is possible that the view could have no NavController if we got to the screen from the qr scanner
                //TODO: hide back button instead of do nothing
                try{
                    Navigation.findNavController(rootView).popBackStack();
                } catch (Exception e){
                    backButton.setVisibility(View.GONE);
                }
            }
        });
        signUpEvent.setOnClickListener(new View.OnClickListener() {
            // need to get attendeeID and eventID first
            @Override
            public void onClick(View view) {
                event.addSignUp(attendee.getDocumentId());
                FirebaseDB.getInstance().updateEvent(event);
                FirebaseDB.getInstance().subscribeAttendeeToEventTopic(event.getDocumentId());
                signUpEvent.setVisibility(View.GONE);
            }
        });

        viewAnnouncementsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle args = new Bundle();
                args.putSerializable("event", event);
                if (Objects.equals(attendee.getDocumentId(), event.getOrganizerId())) {
                    Navigation.findNavController(rootView).navigate(R.id.action_eventDetailsFragment_to_AnnouncementEditFragment, args);
                } else {
                    Navigation.findNavController(rootView).navigate(R.id.action_eventDetailsFragment_to_AnnouncementsFragment, args);
                }
            }
        });

        promoQRShare.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //we start by creating new intent with binary content that contains the image of the QR code we would like to share
                //this implementation is from Android Developer's example binary share intent from https://developer.android.com/training/sharing/send, Accessed on Mar. 22nd, 2024
                Intent shareIntent = new Intent();
                shareIntent.setAction(Intent.ACTION_SEND);
                Uri uriToImage = getUriToShare(promoBitmap);
                shareIntent.putExtra(Intent.EXTRA_STREAM, uriToImage);
                shareIntent.setType("image/jpeg");
                startActivity(Intent.createChooser(shareIntent, null));
            }
        });

        checkInQRShare.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //we start by creating new intent with binary content that contains the image of the QR code we would like to share
                //this implementation is from Android Developer's example binary share intent from https://developer.android.com/training/sharing/send, Accessed on Mar. 22nd, 2024
                Intent shareIntent = new Intent();
                shareIntent.setAction(Intent.ACTION_SEND);
                Uri uriToImage = getUriToShare(checkInBitmap);
                shareIntent.putExtra(Intent.EXTRA_STREAM, uriToImage);
                shareIntent.setType("image/jpeg");
                startActivity(Intent.createChooser(shareIntent, null));
            }
        });





        String nameString = ""+event.getName();
        //String organizerString = "Organized by: ";
        FirebaseDB.getInstance().getUserName(event.getOrganizerId(), new FirebaseDB.GetStringCallBack() {
            @Override
            public void onResult(String string) {
                updateOrganizerString(string, rootView);
            }
        });
        String locationString = "Location:     "+event.getLocation();
        String descriptionString = "Description:     "+event.getDetails();
        String startDateString = "Start DateTime:     "+event.getStartDate();
        String endDateString = "End DateTime    "+event.getEndDate();


        nameView.setText(nameString);
        locationView.setText(locationString);
        descriptionView.setText(descriptionString);
        startDateView.setText(startDateString);
        endDateView.setText(endDateString);

        //we set all the images on screen.


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
        ((TextView) view.findViewById(R.id.event_detail_organizer)).setText("Organizer:     " + string);
    }

    private void setAttendee(Attendee attendee){
        this.attendee = attendee;
    }

    /**
     * This function hides the signup button if the user is the organizer of the event, or has already signed up for the event
     * @param signUpButton The button to hide
     */
    private void setButtonVisibility(Button signUpButton, Button viewAttendees, Event event){
        if (Objects.equals(this.attendee.getDocumentId(), event.getOrganizerId())) {
            //leave early, we should not show signup button if person viewing is owner
            signUpButton.setVisibility(View.GONE);
            return;
        }

        if (event.getSignUps().contains(this.attendee.getDocumentId())){
            //we shouldn't show signup button to those already signed up
            signUpButton.setVisibility(View.GONE);
            return;
        }

        if (event.getMaxAttendees() != null){
            if (event.getAttendeeCount() >= event.getMaxAttendees()){
                //we shouldn't show signup if the event is full
                signUpButton.setVisibility(View.GONE);
                return;
            }
        }
        FirebaseDB.getInstance().userCheckedIntoEvent(this.attendee, this.event, new FirebaseDB.UniqueCheckCallBack() {
            @Override
            public void onResult(boolean isUnique) {
                if (!isUnique) {
                    //if user is already checked in, we should not show signup button
//                    signUpButton.setVisibility(View.GONE);
                    return;
                }
            }
        });

    }

    /**
     * This function sets the images on the event details screen, fetching the poster from firebase, and generating QR codes from the event's QR contents
     * @param posterView The ImageView that should display the event poster
     * @param promoQRView The ImageView that should display the event's promo QR code
     * @param checkInQRView The ImageView that should display the event's check-in QR code
     */
    private void setImages(ImageView posterView, ImageView promoQRView, ImageView checkInQRView){
        if (this.event.getPosterPath() != null) {
            FirebaseDB.getInstance().retrieveImage(this.event, new FirebaseDB.GetBitmapCallBack() {
                @Override
                public void onResult(Bitmap bitmap) {
                    posterView.setImageBitmap(bitmap);
                }
            });
        }
        Log.d("setImages", this.event.getQrCodePromo());
        Log.d("setImages", this.event.getQrCode());
        this.promoBitmap = QRCodeGenerator.generateBitmap(this.event.getQrCodePromo());
        if (this.promoBitmap == null){
            Log.d("promoBitmap", "failure");
            new ErrorDialog(R.string.qr_generation_failed).show(getActivity().getSupportFragmentManager(), "Error Dialog");
        } else{
            promoQRView.setImageBitmap(this.promoBitmap);
        }

        this.checkInBitmap = QRCodeGenerator.generateBitmap(this.event.getQrCode());
        if (this.checkInBitmap == null){
            Log.d("checkInBitmap", "failure");
            new ErrorDialog(R.string.qr_generation_failed).show(getActivity().getSupportFragmentManager(), "Error Dialog");
        } else{
            checkInQRView.setImageBitmap(this.checkInBitmap);
        }

    }

    private Uri getUriToShare(Bitmap bitmap){
        Uri uri = null;
        String localFilePath = MediaStore.Images.Media.insertImage(getContext().getContentResolver(), bitmap, "QrToBeShared", "the qr we are sharing");
        if (localFilePath != null) {
            uri = Uri.parse(localFilePath);
        }
        else{
            Log.e("ShareQRCode", "Failed to create Uri from bitmap");
        }
        return uri;
    }

}