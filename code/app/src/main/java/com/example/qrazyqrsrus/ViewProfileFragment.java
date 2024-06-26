package com.example.qrazyqrsrus;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

public class ViewProfileFragment extends Fragment {
    private Attendee attendee;
    private EditText etFullName, etEmailAddress;
    ImageView imgProfilePicture;
    private FirebaseStorage storage;
    private StorageReference storageRef;
    private Button btnUpdateProfile;
    private Switch switchGeolocation;
    private FirebaseFirestore db;
    private boolean isProfileLoaded = false;
    private Button btnDone, btnCancel;
    private Boolean imageUpdates = false;
    Uri newImageUri;
    Boolean imageDeleted = false;

    String userId;

    ActivityResultLauncher<String> galleryActivityResultLauncher;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userId = getUserId(requireContext());
        initializeGalleryLauncher();
    }

    private static String getUserId(Context context) {
        return Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.update_profile, container, false);
        initializeViews(view);
        loadInitialData();
        return view;
    }

    private void initializeGalleryLauncher() {
        galleryActivityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.GetContent(),
                this::onImageSelected
        );
    }

    void onImageSelected(Uri uri) {
        if (uri != null) {
            imgProfilePicture.setImageURI(uri);
            newImageUri = uri;
            imageDeleted = false;
        }
    }

    private void initializeViews(View view) {
        btnDone = view.findViewById(R.id.btnDone);
        btnCancel = view.findViewById(R.id.btnCancel);
        etFullName = view.findViewById(R.id.etFullName);
        etEmailAddress = view.findViewById(R.id.etEmailAddress);
        btnUpdateProfile = view.findViewById(R.id.btnUpdateProfile);
        imgProfilePicture = view.findViewById(R.id.imgProfilePicture);
        switchGeolocation = view.findViewById(R.id.switchGeolocation);

        setInitialViewState();

        imgProfilePicture.setOnClickListener(v -> showProfilePictureOptionsDialog());
        btnUpdateProfile.setOnClickListener(v -> enterEditMode());
        btnDone.setOnClickListener(v -> saveChanges());
        btnCancel.setOnClickListener(v -> revertChanges());
    }

    private void setInitialViewState() {
        btnDone.setVisibility(View.INVISIBLE);
        btnCancel.setVisibility(View.INVISIBLE);
        etFullName.setEnabled(false);
        etEmailAddress.setEnabled(false);
    }

    private void loadInitialData() {
        Bundle args = getArguments();
        if (args != null && args.containsKey("attendee")) {
            attendee = (Attendee) args.getSerializable("attendee");
            if (attendee != null) {
                loadInitialAttendee(attendee);
                if (!Objects.equals(userId, attendee.getId())) {
                    restrictEdits();
                }
            } else {
                Log.e("ViewProfileFragment", "Attendee object not found in arguments.");

                FirebaseDB.getInstance().loginUser(userId, new FirebaseDB.GetAttendeeCallBack() {
                    @Override
                    public void onResult(Attendee attendee) {
                        loadInitialAttendee(attendee);
                        if (!Objects.equals(userId, attendee.getId())) {
                            restrictEdits();
                        }
                    }

                    @Override
                    public void onNoResult() {
                        Log.d("very bad", "badness!");
                    }
                });
            }
        } else {
            FirebaseDB.getInstance().loginUser(userId, new FirebaseDB.GetAttendeeCallBack() {
                    @Override
                    public void onResult(Attendee attendee) {
                        loadInitialAttendee(attendee);
                        if (!Objects.equals(userId, attendee.getId())) {
                            restrictEdits();
                        }
                    }

                    @Override
                    public void onNoResult() {
                        Log.d("very bad", "badness!");
                    }
                });
        }
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
        isProfileLoaded = true;
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
        String pathName = "profile/" + attendeeName + timeStamp;
        return pathName;
    }

    /**
     * Prepares the fragment for profile editing by enabling text fields, making the save and cancel buttons visible,
     * hiding the update profile button, and allowing for profile picture updates.
     */

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

    /**
     * Saves the changes made to the user's profile information to Firebase, including name, email, geolocation preference,
     * and potentially a new profile picture if one has been selected. Exits edit mode upon successful update.
     */

    private void saveChanges() {
        updateUserProfile(this.attendee);
    }




    /**
     * Reverts any changes made in the edit mode by reloading the initial attendee information and resetting the UI
     * to view mode, disabling text fields, and hiding save and cancel buttons.
     */

    private void revertChanges() {
        // Reset information to the last saved state
        loadInitialAttendee(this.attendee);

        // Make fields non-editable and update UI back to view mode
        exitEditMode();
    }

    /**
     * Resets the UI to view mode after editing is complete or cancelled, disabling text fields and making the update profile
     * button visible again. Also resets the flag for profile picture updates and clears any new image URI.
     */

    private void exitEditMode() {
        // Make EditTexts non-editable
        etFullName.setEnabled(false);
        etEmailAddress.setEnabled(false);

        // Hide "Done" and "Cancel" buttons
        btnDone.setVisibility(View.GONE);
        btnCancel.setVisibility(View.GONE);

        // Show "Update Profile" button
        btnUpdateProfile.setVisibility(View.VISIBLE);

        //make image not changable, clear any updates
        imageUpdates = false;
        imageDeleted = false;
        newImageUri = null;
    }

    /**
     * Factory method to create a new instance of ViewProfileFragment with attendee details passed as arguments.
     *
     * @param attendee The attendee whose profile is to be viewed or edited.
     * @return A new instance of ViewProfileFragment with attendee data.
     */

    public static ViewProfileFragment newInstance(Attendee attendee){
        Bundle args = new Bundle();
        args.putSerializable("attendee", attendee);

        ViewProfileFragment fragment = new ViewProfileFragment();
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * Restricts the user from editing their profile by disabling input fields and hiding the update profile button.
     * This method is typically called when viewing another user's profile where edits are not permitted.
     */

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
    void deleteProfileImage() {
        //we tell the state that the user has deleted their profile picture, so it can delete the old one from firebase if needed
        imageDeleted = true;
        //we remove the user's uploaded profile picture, and change it to the generated one
        Bitmap profileBitmap = InitialsPictureGenerator.createInitialsImage(InitialsPictureGenerator.getInitials(etFullName.getText().toString()));
        imgProfilePicture.setImageBitmap(profileBitmap);
        //we clear any previously uploaded image (during this edit)
        newImageUri = null;
    }
}
