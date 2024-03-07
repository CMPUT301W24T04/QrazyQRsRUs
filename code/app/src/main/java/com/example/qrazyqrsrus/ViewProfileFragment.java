package com.example.qrazyqrsrus;

import static android.content.Context.MODE_PRIVATE;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Bundle;
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

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;

public class ViewProfileFragment extends Fragment {
    private EditText etFullName, etAge, etEmailAddress;
    private ImageView imgProfilePicture;
    private FirebaseStorage storage;
    private StorageReference storageRef;
    private Button btnUpdateProfile;
    private Switch switchGeolocation;
    private FirebaseFirestore db;
    private boolean isProfileLoaded = false;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        return super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.update_profile, container, false);
        ImageView backButton = view.findViewById(R.id.backButton);
        //backButton.setOnClickListener(v -> finish());

        etFullName = view.findViewById(R.id.etFullName);
        etAge = view.findViewById(R.id.etAge);
        etEmailAddress = view.findViewById(R.id.etEmailAddress);
        btnUpdateProfile = view.findViewById(R.id.btnUpdateProfile);
        imgProfilePicture = view.findViewById(R.id.imgProfilePicture);
        switchGeolocation = view.findViewById(R.id.switchGeolocation);
        SharedPreferences preferences = getSharedPreferences("USER_PREF", MODE_PRIVATE);

        // Restore state here
        boolean switchState = preferences.getBoolean("GeolocationSwitchState", false);
        switchGeolocation.setChecked(switchState);
        String userId = "11111111"; // replace with dynamic user ID later
        loadUserProfile(userId);

        btnUpdateProfile.setOnClickListener(v -> updateUserProfile(userId));

        //************************************************************************************
        // Get bundle from attendee click to be displayed on the profile view
//        Bundle bundle = getArguments();
//        Attendee attendee = (Attendee) bundle.getSerializable("current_attendee");
//        etFullName.setText(attendee.getName());
        //*************************************************************************************

        switchGeolocation.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                updateGeolocation(userId, isChecked);
            }
        });

        etFullName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void afterTextChanged(Editable editable) {
                if (isProfileLoaded) {
                    String initials = getInitials(editable.toString());
                    Bitmap bitmap = createInitialsImage(initials);
                    imgProfilePicture.setImageBitmap(bitmap);
                    uploadImageToStorage(bitmap, userId);
                }
            }
        });

        return view;
    }
    private void loadUserProfile(String userId) {
        db.collection("Users").document(userId).get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        UserProfile userProfile = documentSnapshot.toObject(UserProfile.class);
                        if (userProfile != null) {
                            etFullName.setText(userProfile.getName());
                            etAge.setText(userProfile.getAge());
                            etEmailAddress.setText(userProfile.getEmail());
                            switchGeolocation.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                                @Override
                                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                    // Save state here
                                    SharedPreferences preferences = getSharedPreferences("USER_PREF", MODE_PRIVATE);
                                    preferences.edit().putBoolean("GeolocationSwitchState", isChecked).apply();

                                    // Update Firestore
                                    updateGeolocation(userId, isChecked);
                                }
                            });


                            String initials = getInitials(userProfile.getName());
                            Bitmap bitmap = createInitialsImage(initials);
                            imgProfilePicture.setImageBitmap(bitmap);
                            isProfileLoaded = true;
                        } else {
                            Log.e("LoadUser", "UserProfile is null");
                        }
                    } else {
                        Toast.makeText(this, "Profile does not exist.", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e("FirestoreError", "Error loading profile", e);
                    Toast.makeText(this, "Error loading profile.", Toast.LENGTH_SHORT).show();
                });
    }

    private void updateUserProfile(String userId) {
        String fullName = etFullName.getText().toString().trim();
        String age = etAge.getText().toString().trim();
        String emailAddress = etEmailAddress.getText().toString().trim();


        Map<String, Object> userProfileMap = new HashMap<>();
        userProfileMap.put("name", fullName);
        userProfileMap.put("age", age);
        userProfileMap.put("email", emailAddress);

        db.collection("Users").document(userId)
                .update(userProfileMap)
                .addOnSuccessListener(aVoid -> Toast.makeText(this, "Profile Updated Successfully!", Toast.LENGTH_LONG).show())
                .addOnFailureListener(e -> Log.e("FirestoreError", "Failed to update profile", e));
    }

    private void updateGeolocation(String userId, boolean isOn) {
        db.collection("Users").document(userId).update("geolocation_on", isOn)
                .addOnSuccessListener(aVoid -> Log.d("UpdateProfileActivity", "Geolocation updated successfully"))
                .addOnFailureListener(e -> Log.e("UpdateProfileActivity", "Failed to update geolocation", e));
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

    private void uploadImageToStorage(Bitmap bitmap, String userId) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] data = baos.toByteArray();

        StorageReference profileImagesRef = storageRef.child("profileImages/" + userId + ".png");
        profileImagesRef.putBytes(data)
                .addOnSuccessListener(aVoid -> Toast.makeText(UpdateProfileActivity.this, "Profile Image Uploaded Successfully!", Toast.LENGTH_SHORT).show())
                .addOnFailureListener(e -> Log.e("UpdateProfileActivity", "Failed to upload profile image", e));
    }
}
