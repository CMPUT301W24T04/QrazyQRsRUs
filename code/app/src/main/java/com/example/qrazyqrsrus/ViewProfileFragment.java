package com.example.qrazyqrsrus;

import static android.content.Context.MODE_PRIVATE;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

public class ViewProfileFragment extends Fragment {
    private Attendee attendee;
    private EditText etFullName, etAge, etEmailAddress;
    private ImageView imgProfilePicture;
    private FirebaseStorage storage;
    private StorageReference storageRef;
    private Button btnUpdateProfile;
    private Switch switchGeolocation;
    private FirebaseFirestore db;
    private boolean isProfileLoaded = false;

    private Button btnDone, btnCancel;

    String userId;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

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
                if (isProfileLoaded) {
                    String newName = s.toString();
                    if (!newName.isEmpty()) {
                        String initials = getInitials(newName);
                        Bitmap bitmap = createInitialsImage(initials);
                        imgProfilePicture.setImageBitmap(bitmap);
                    }
                }
            }
        });



        userId = Settings.Secure.getString(getContext().getContentResolver(), Settings.Secure.ANDROID_ID);

        Bundle args = getArguments();
        Attendee attendeeClicked;
        Log.d("profile_error", userId);
        Log.d("profile_error", ((Attendee) args.getSerializable("attendee")).getId());
        if (args != null){
            if(!Objects.equals(userId, ((Attendee) args.getSerializable("attendee")).getId())){
                restrictEdits();
            }
        } else{
            new ErrorDialog(R.string.no_args).show(getActivity().getSupportFragmentManager(), "Error Dialog");
        }

        // In onCreateView after initializing views
        btnUpdateProfile.setOnClickListener(v -> enterEditMode());
        Button btnDone = view.findViewById(R.id.btnDone);
        Button btnCancel = view.findViewById(R.id.btnCancel);

        btnDone.setOnClickListener(v -> saveChanges());
        btnCancel.setOnClickListener(v -> revertChanges());


//        if (((String) args.getSerializable("userId")) != null && ((Attendee) args.getSerializable("attendee")) != null){
//            if(userId != ((Attendee) args.getSerializable("attendee")).getId()){
//                restrictEdits();
//            }
//        }
        //loadUserProfile(userId);
        FirebaseDB.loginUser(userId, new FirebaseDB.GetAttendeeCallBack() {
            @Override
            public void onResult(Attendee attendee) {
                loadInitialAttendee(attendee);
            }
        });

//        btnUpdateProfile.setOnClickListener(v -> {
//            updateUserProfile(this.attendee);
//        });

        return view;
    }


    private String getInitials(String name) {
        String[] parts = name.trim().split("\\s+");
        StringBuilder initials = new StringBuilder();
        for (String part : parts) {
            if (!part.isEmpty()) {
                initials.append(part.substring(0, 1).toUpperCase());
            }
        }
        return initials.toString();
    }

    private Bitmap createInitialsImage(String initials) {
        Bitmap image = Bitmap.createBitmap(100, 100, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(image);
        Paint paint = new Paint();
        paint.setColor(Color.BLUE);
        paint.setTextSize(40);
        paint.setAntiAlias(true);
        paint.setTypeface(Typeface.DEFAULT_BOLD);
        canvas.drawColor(Color.WHITE);
        float centerX = (100 - paint.measureText(initials)) / 2;
        float centerY = (100 - ((paint.descent() + paint.ascent()) / 2)) / 2;
        canvas.drawText(initials, centerX, centerY, paint);
        return image;
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
                profileBitmap = createInitialsImage(getInitials(userId));
            } else{
                profileBitmap = createInitialsImage(attendee.getName());
            }
            imgProfilePicture.setImageBitmap(profileBitmap);
            //the idea to get a uri from a bitmap was taken from https://stackoverflow.com/questions/12555420/how-to-get-a-uri-object-from-bitmap Accessed on March 7th, 2024
            //posted by user Ajay (https://stackoverflow.com/users/840802/ajay) in the post https://stackoverflow.com/a/16167993
            String localFilePath = MediaStore.Images.Media.insertImage(getContext().getContentResolver(), profileBitmap, "generatedProfilePicture", "the profile picture we generated");
            if (localFilePath != null) {
                Uri uri = Uri.parse(localFilePath);
                String pathName = generatePathName(attendee.getName());
                FirebaseDB.uploadImage(uri, pathName);
                this.attendee.setProfilePicturePath(pathName);
            }
            else{
                Log.e("ProfilePicture", "Failed to insert image into MediaStore,localFilePath is null");

            }
        } else{
            FirebaseDB.retrieveImage(attendee, new FirebaseDB.GetBitmapCallBack() {
                @Override
                public void onResult(Bitmap bitmap) {
                    imgProfilePicture.setImageBitmap(bitmap);
                }
            });
        }
        isProfileLoaded = true;
    }

    private void updateUserProfile(Attendee attendee){
        Bitmap profileBitmap = createInitialsImage(getInitials(etFullName.getText().toString()));
        imgProfilePicture.setImageBitmap(profileBitmap);
        //we make a local file on the user's device with the new image
        String localFilePath = MediaStore.Images.Media.insertImage(getContext().getContentResolver(), profileBitmap, "generatedProfilePicture", "the profile picture we generated");
        Uri uri = Uri.parse(localFilePath);
        //we generate a unique pathname
        String pathName = generatePathName(etFullName.getText().toString());
        //we upload the new profile picture
        //delete the old image this user had
        FirebaseDB.deleteImage(attendee.getProfilePicturePath());
        FirebaseDB.uploadImage(uri, pathName);
        attendee.setProfilePicturePath(pathName);
        attendee.setName(etFullName.getText().toString());
        attendee.setGeolocationOn(switchGeolocation.isChecked());
        attendee.setEmail(etEmailAddress.getText().toString());
        FirebaseDB.updateUser(attendee);
        Toast.makeText(getContext(), "Profile updated successfully!", Toast.LENGTH_SHORT).show();
        exitEditMode();

    }

    private String generatePathName(String attendeeName){
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String pathName = "profile/" + attendeeName + timeStamp;
        return pathName;
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
        etAge.setInputType(0);
        switchGeolocation.setInputType(0);
        btnUpdateProfile.setVisibility(View.GONE);
    }
}
