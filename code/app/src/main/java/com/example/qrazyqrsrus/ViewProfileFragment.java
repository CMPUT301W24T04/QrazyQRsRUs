package com.example.qrazyqrsrus;

// This fragment contains the profile information to be displayed on the profile view

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

public class ViewProfileFragment extends Fragment {
    private Attendee attendee;
    private EditText etFullName, etEmailAddress;
    private ImageView imgProfilePicture;
    private Button btnUpdateProfile;
    private SwitchCompat switchGeolocation;
    private Button btnDone, btnCancel;
    private Boolean imageUpdates = false;
    private Uri newImageUri;
    private Boolean imageDeleted = false;

    String userId;

    private ActivityResultLauncher<String> galleryActivityResultLauncher;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        galleryActivityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.GetContent(),
                uri -> {
                    if (uri != null) {
                        //we update the state to communicate the user has uploaded an image, that may need to be stored to firebase
                        imgProfilePicture.setImageURI(uri);
                        newImageUri = uri;
                        imageDeleted = false;
                    }
                }
        );
    }

    @SuppressLint("HardwareIds")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        return super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.update_profile, container, false);

        btnDone = view.findViewById(R.id.btnDone);
        btnCancel = view.findViewById(R.id.btnCancel);

        etFullName = view.findViewById(R.id.etFullName);
        etEmailAddress = view.findViewById(R.id.etEmailAddress);

        btnUpdateProfile = view.findViewById(R.id.btnUpdateProfile);
        imgProfilePicture = view.findViewById(R.id.imgProfilePicture);
        imgProfilePicture.setOnClickListener(v -> {
            //only open the dialog if we are in the edit mode
            if (imageUpdates){
                showProfilePictureOptionsDialog();
            }
        });
        switchGeolocation = view.findViewById(R.id.switchGeolocation);


        // Set the EditTexts to non-editable initially
        etFullName.setEnabled(false);
        etEmailAddress.setEnabled(false);
        // Set the buttons to invisible initially
        btnDone.setVisibility(View.INVISIBLE);
        btnCancel.setVisibility(View.INVISIBLE);

        //TextWatcher
        etFullName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Nothing needed here
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Nothing needed here
            }

            @Override
            public void afterTextChanged(Editable s) {
                //don't make any updates until the user presses the confirm button
            }
        });



        userId = Settings.Secure.getString(requireContext().getContentResolver(), Settings.Secure.ANDROID_ID);

        Bundle args = getArguments();


        if (args != null && args.containsKey("attendee")) {
            Attendee attendeeClicked = (Attendee) args.getSerializable("attendee");
            if (attendeeClicked != null) {
                if (!Objects.equals(userId, attendeeClicked.getId())) {
                    restrictEdits();
                }
                // Load initial attendee data
                loadInitialAttendee(attendeeClicked);
            } else {
                Log.e("ViewProfileFragment", "Attendee object not found in arguments.");
                // Handle the case when attendee is not passed in arguments
                // Show error dialog or toast

//         Attendee attendeeClicked;
// //        Log.d("profile_error", userId);
// //        Log.d("profile_error", ((Attendee) args.getSerializable("attendee")).getId());
//         if (args != null){
//             if(!Objects.equals(userId, ((Attendee) args.getSerializable("attendee")).getId())){
//                 restrictEdits();

            }
        } else {
            Log.e("ViewProfileFragment", "No arguments found.");
            // Handle the case when no arguments were set for this fragment
            // Show error dialog or toast
        }

        // In onCreateView after initializing views
        btnUpdateProfile.setOnClickListener(v -> enterEditMode());
        Button btnDone = view.findViewById(R.id.btnDone);
        Button btnCancel = view.findViewById(R.id.btnCancel);

        btnDone.setOnClickListener(v -> saveChanges());
        btnCancel.setOnClickListener(v -> revertChanges());


        assert args != null;
        if (args.getSerializable("userId") != null && args.getSerializable("attendee") != null){
            if(!Objects.equals(userId, ((Attendee) Objects.requireNonNull(args.getSerializable("attendee"))).getId())){
                restrictEdits();
            }
        }
        loadInitialAttendee(((Attendee) Objects.requireNonNull(args.getSerializable("attendee"))));
        return view;
    }

    private void loadInitialAttendee(Attendee attendee){
        this.attendee = attendee;
        etFullName.setText(attendee.getName());
        etEmailAddress.setText(attendee.getEmail());
        if (attendee.getGeolocationOn() != null){
            switchGeolocation.setChecked(attendee.getGeolocationOn());
        } else{
            switchGeolocation.setChecked(false);
        }

        if (attendee.getProfilePicturePath() == null){
            Bitmap profileBitmap;
            if (attendee.getName() == null){
                profileBitmap = InitialsPictureGenerator.createInitialsImage(InitialsPictureGenerator.getInitials(userId));
            } else{
                profileBitmap = InitialsPictureGenerator.createInitialsImage(InitialsPictureGenerator.getInitials(attendee.getName()));
            }
            imgProfilePicture.setImageBitmap(profileBitmap);
        } else{
            FirebaseDB.getInstance().retrieveImage(attendee, bitmap -> imgProfilePicture.setImageBitmap(bitmap));
        }
    }

    private void updateUserProfile(Attendee attendee){
        //if the user has uploaded a new profile picture, we save it to firebase
        if (newImageUri != null){
            //if the user has changed their profile picture, we save it
            String pathname = generatePathName(attendee.getName());
            FirebaseDB.getInstance().uploadImage(newImageUri,pathname);
            //we delete their old profile picture if there was one
            if (attendee.getProfilePicturePath() != null){
                FirebaseDB.getInstance().deleteImage(attendee.getProfilePicturePath());
            }
            attendee.setProfilePicturePath(pathname);
            FirebaseDB.getInstance().updateUser(attendee);
            newImageUri = null;
        } else{
            //if the user's profile picture path is null, we generate a new bitmap for them
            //this occurs when the user has no profile picture uploaded, and changes their name
            if (attendee.getProfilePicturePath() == null){
                Bitmap profileBitmap = InitialsPictureGenerator.createInitialsImage(InitialsPictureGenerator.getInitials(etFullName.getText().toString()));
                imgProfilePicture.setImageBitmap(profileBitmap);
            }
            //if the user has deleted their previous profile picture, we check if we need to delete it from firebase
            //we will, unless the user clicked delete on their generated profile picture
            if (imageDeleted){
                if (attendee.getProfilePicturePath() != null){
                    FirebaseDB.getInstance().deleteImage(attendee.getProfilePicturePath());
                    attendee.setProfilePicturePath(null);
                    FirebaseDB.getInstance().updateUser(attendee);
                    imageDeleted = false;
                }

            }
        }
        //if the user has not

        attendee.setName(etFullName.getText().toString());
        attendee.setGeolocationOn(switchGeolocation.isChecked());
        attendee.setEmail(etEmailAddress.getText().toString());
        FirebaseDB.getInstance().updateUser(attendee);
        Toast.makeText(getContext(), "Profile updated successfully!", Toast.LENGTH_SHORT).show();
        exitEditMode();

    }

    private String generatePathName(String attendeeName){
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        return "profile/" + attendeeName + timeStamp;
    }

    private void enterEditMode() {
        // Make EditTexts editable
        etFullName.setEnabled(true);
        etEmailAddress.setEnabled(true);

        // Show "Done" and "Cancel" buttons
        btnDone.setVisibility(View.VISIBLE);
        btnCancel.setVisibility(View.VISIBLE);

        // Hide "Update Profile" button
        btnUpdateProfile.setVisibility(View.GONE);

        //make profile picture changeable
        imageUpdates = true;
    }

    private void saveChanges() {
        updateUserProfile(this.attendee);
    }





    private void revertChanges() {
        // Reset information to the last saved state
        loadInitialAttendee(this.attendee);

        // Make fields non-editable and update UI back to view mode
        exitEditMode();
    }
    private void exitEditMode() {
        // Make EditTexts non-editable
        etFullName.setEnabled(false);
        etEmailAddress.setEnabled(false);

        // Hide "Done" and "Cancel" buttons
        btnDone.setVisibility(View.GONE);
        btnCancel.setVisibility(View.GONE);

        // Show "Update Profile" button
        btnUpdateProfile.setVisibility(View.VISIBLE);

        //make image not changeable, clear any updates
        imageUpdates = false;
        imageDeleted = false;
        newImageUri = null;
    }

    public static ViewProfileFragment newInstance(Attendee attendee){
        Bundle args = new Bundle();
        args.putSerializable("attendee", attendee);

        ViewProfileFragment fragment = new ViewProfileFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private void restrictEdits(){
        etEmailAddress.setInputType(0);
        etFullName.setInputType(0);
//        etAge.setInputType(0);
        switchGeolocation.setInputType(0);
        btnUpdateProfile.setVisibility(View.GONE);
    }

    /**
     * This function launches the dialog where the user can interact with their profile picture
     */
    private void showProfilePictureOptionsDialog() {
        // Inflate the dialog view
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_profile_picture_options, null);

        AlertDialog dialog = new AlertDialog.Builder(getContext())
                .setView(dialogView)
                .create();

        dialog.show();

        dialogView.findViewById(R.id.ivGallery).setOnClickListener(view -> {
            openGalleryForImage();
            dialog.dismiss();
        });

        dialogView.findViewById(R.id.ivDelete).setOnClickListener(view -> {
            deleteProfileImage();
            dialog.dismiss();
        });
    }


    /**
     * This function launches the activity to select an image
     */
    private void openGalleryForImage() {
        galleryActivityResultLauncher.launch("image/*");
    }


    /**
     * This function updates the state to communicate the user has deleted their profile picture
     */
    private void deleteProfileImage() {
        //we tell the state that the user has deleted their profile picture, so it can delete the old one from firebase if needed
        imageDeleted = true;
        //we remove the user's uploaded profile picture, and change it to the generated one
        Bitmap profileBitmap = InitialsPictureGenerator.createInitialsImage(InitialsPictureGenerator.getInitials(etFullName.getText().toString()));
        imgProfilePicture.setImageBitmap(profileBitmap);
        //we clear any previously uploaded image (during this edit)
        newImageUri = null;
    }
}
