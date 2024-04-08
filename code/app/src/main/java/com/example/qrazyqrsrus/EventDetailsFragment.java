package com.example.qrazyqrsrus;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

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
    private FirebaseDB firebaseDB;
    private QRCodeGenerator qrCodeGenerator;
    private QRCodeScanHandler qrCodeScanHandler;
  
    public EventDetailsFragment() {
        // Required empty public constructor
    }
    /**
     * creates view
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (qrCodeGenerator == null) {
            qrCodeGenerator = new QRCodeGenerator();
        }
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

        if (this.firebaseDB == null) {
            this.firebaseDB = FirebaseDB.getInstance();
        }

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
        //we check if attendee is set, logging in the user if not
        //then we update the images that are displayed, and the visibility of the signup button once we have the attendee
        if (this.attendee == null){
            String userId = Settings.Secure.getString(getContext().getContentResolver(), Settings.Secure.ANDROID_ID);
            firebaseDB.loginUser(userId, new FirebaseDB.GetAttendeeCallBack() {
                @Override
                public void onResult(Attendee attendee) {
                    setAttendee(attendee);
                    setImages(posterView, promoQRView, checkInQRView);
                    setButtonVisibility(signUpEvent, viewAttendeesButton, viewLocations, EventDetailsFragment.this.event, rootView);
                }

                @Override
                public void onNoResult() {
                    new ErrorDialog(R.string.login_error).show(getActivity().getSupportFragmentManager(), "Error Dialog");
                }
            });
        } else{
            setImages(posterView, promoQRView, checkInQRView);
            setButtonVisibility(signUpEvent, viewAttendeesButton, viewLocations, this.event, rootView);
        }

        //Change view to attendee list when click on view attendees button
        viewAttendeesButton.setOnClickListener(v -> {
            // pass the event as a bundle to the attendeeList so we know which event to get from
            Bundle args = new Bundle();
            args.putSerializable("event", event);
            Navigation.findNavController(rootView).navigate(R.id.action_eventDetailsFragment_to_attendeeList2,args);
        });

        //Change view to locations when click on view geolocation button
        viewLocations.setOnClickListener(v -> {
            // pass the event as a bundle to the attendeeList so we know which event to get from
            Bundle args = new Bundle();
            args.putSerializable("event", event);
            Navigation.findNavController(rootView).navigate(R.id.action_eventDetailsFragment_to_geoLocation,args);
        });

        // need to get attendeeID and eventID first
        backButton.setOnClickListener(view -> {
            //currently it is possible that the view could have no NavController if we got to the screen from the qr scanner
            //TODO: hide back button instead of do nothing
            try{
                Navigation.findNavController(rootView).popBackStack();
            } catch (Exception e){
                backButton.setVisibility(View.GONE);
            }
        });
        // need to get attendeeID and eventID first
        signUpEvent.setOnClickListener(view -> {
            event.addSignUp(attendee.getDocumentId());
            firebaseDB.updateEvent(event);
            firebaseDB.subscribeAttendeeToEventTopic(event.getDocumentId());
            signUpEvent.setVisibility(View.GONE);
        });

        viewAnnouncementsButton.setOnClickListener(v -> {
            Bundle args = new Bundle();
            args.putSerializable("event", event);
            if (Objects.equals(attendee.getDocumentId(), event.getOrganizerId())) {
                Navigation.findNavController(rootView).navigate(R.id.action_eventDetailsFragment_to_AnnouncementEditFragment, args);
            } else {
                Navigation.findNavController(rootView).navigate(R.id.action_eventDetailsFragment_to_AnnouncementsFragment, args);
            }
        });

        promoQRShare.setOnClickListener(v -> {
            //we start by creating new intent with binary content that contains the image of the QR code we would like to share
            //this implementation is from Android Developer's example binary share intent from https://developer.android.com/training/sharing/send, Accessed on Mar. 22nd, 2024
            Intent shareIntent = new Intent();
            shareIntent.setAction(Intent.ACTION_SEND);
            Uri uriToImage = getUriToShare(promoBitmap);
            shareIntent.putExtra(Intent.EXTRA_STREAM, uriToImage);
            shareIntent.setType("image/jpeg");
            startActivity(Intent.createChooser(shareIntent, null));
        });

        checkInQRShare.setOnClickListener(v -> {
            //we start by creating new intent with binary content that contains the image of the QR code we would like to share
            //this implementation is from Android Developer's example binary share intent from https://developer.android.com/training/sharing/send, Accessed on Mar. 22nd, 2024
            Intent shareIntent = new Intent();
            shareIntent.setAction(Intent.ACTION_SEND);
            Uri uriToImage = getUriToShare(checkInBitmap);
            shareIntent.putExtra(Intent.EXTRA_STREAM, uriToImage);
            shareIntent.setType("image/jpeg");
            startActivity(Intent.createChooser(shareIntent, null));
        });





        String nameString = ""+event.getName();
        //String organizerString = "Organized by: ";
        firebaseDB.getUserName(event.getOrganizerId(), string -> updateOrganizerString(string, rootView));
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
    public void setFirebaseDB(FirebaseDB firebaseDB) {
        this.firebaseDB = firebaseDB;
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

    /**
     * Updates the text of the organizer field in the fragment's view with the provided string.
     *
     * @param string The name of the organizer to display.
     * @param view   The current view of the fragment where the organizer's name will be updated.
     */
    private void updateOrganizerString(String string, View view){
        ((TextView) view.findViewById(R.id.event_detail_organizer)).setText("Organizer:     " + string);
    }

    /**
     * Sets the attendee object for this fragment. This is called after successfully logging in an attendee.
     *
     * @param attendee The attendee object to set.
     */
    private void setAttendee(Attendee attendee){
        this.attendee = attendee;
    }

    /**
     * Determines and sets the visibility of the sign-up button based on the event and attendee information.
     * The sign-up button is hidden for the event organizer, attendees who have already signed up or checked in,
     * and when the maximum number of attendees for the event has been reached.
     *
     * @param signUpButton    The sign-up button to modify the visibility of.
     * @param viewAttendees   Button to view attendees, made visible for event organizers.
     * @param viewLocations   Button to view locations, made visible if geolocation is enabled for the event.
     * @param event           The current event being viewed.
     * @param view            The current view of the fragment.
     */
    private void setButtonVisibility(Button signUpButton, Button viewAttendees, Button viewLocations, Event event, View view){
        if (Objects.equals(this.attendee.getDocumentId(), event.getOrganizerId())) {
            //leave early, we should not show signup button if person viewing is owner
            viewAttendees.setVisibility(View.VISIBLE);
            if (this.event.getGeolocationOn()) {
                viewLocations.setVisibility(View.VISIBLE);
            }
            return;
        }
        else {
            view.findViewById(R.id.check_in_qr_view).setVisibility(View.GONE);
            view.findViewById(R.id.check_in_share_button).setVisibility(View.GONE);
            view.findViewById(R.id.check_in_qr_text).setVisibility(View.GONE);
        }

        if (event.getSignUps().contains(this.attendee.getDocumentId())){
            //we shouldn't show signup button to those already signed up
            return;
        }

        if (event.getMaxAttendees() != null){
            if (event.getAttendeeCount() >= event.getMaxAttendees()){
                //we shouldn't show signup if the event is full
                return;
            }
        }
        if (this.isCheckedIn != null && this.isCheckedIn) {
            //we shouldn't show signup button to those already checked in
            return;
        }
        signUpButton.setVisibility(View.VISIBLE);

    }

    /**
     * This function sets the images on the event details screen, fetching the poster from firebase, and generating QR codes from the event's QR contents
     * @param posterView The ImageView that should display the event poster
     * @param promoQRView The ImageView that should display the event's promo QR code
     * @param checkInQRView The ImageView that should display the event's check-in QR code
     */
    private void setImages(ImageView posterView, ImageView promoQRView, ImageView checkInQRView){
        if (this.event.getPosterPath() != null) {
            firebaseDB.retrieveImage(this.event, bitmap -> posterView.setImageBitmap(bitmap));
        }
        this.promoBitmap = qrCodeGenerator.generateBitmap(this.event.getQrCodePromo());
        if (this.promoBitmap == null){
            new ErrorDialog(R.string.qr_generation_failed).show(getActivity().getSupportFragmentManager(), "Error Dialog");
        } else{
            promoQRView.setImageBitmap(this.promoBitmap);
        }

        this.checkInBitmap = qrCodeGenerator.generateBitmap(this.event.getQrCode());
        if (this.checkInBitmap == null){
            Log.d("checkInBitmap", "failure");
            new ErrorDialog(R.string.qr_generation_failed).show(getActivity().getSupportFragmentManager(), "Error Dialog");
        } else{
            checkInQRView.setImageBitmap(this.checkInBitmap);
        }

    }

    /**
     * Retrieves a URI for sharing a bitmap image. This is used for sharing QR codes via external intents.
     *
     * @param bitmap The bitmap image to share.
     * @return The URI of the image stored in the device's media store, ready to be shared.
     */
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

    public void setQrCodeGenerator(QRCodeGenerator instance){
        this.qrCodeGenerator = instance;
    }

}