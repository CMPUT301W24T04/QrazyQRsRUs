package com.example.qrazyqrsrus;



import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class UpdateProfileActivity extends AppCompatActivity {

    private EditText etFullName, etAge, etEmailAddress;
    private Button btnUpdateProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.update_profile); // Use the correct layout file name

        // Initialize views
        etFullName = findViewById(R.id.etFullName);
        etAge = findViewById(R.id.etAge);
        etEmailAddress = findViewById(R.id.etEmailAddress);
        btnUpdateProfile = findViewById(R.id.btnUpdateProfile);

        // Set click listener for the update button
        btnUpdateProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateUserProfile();
            }
        });
    }

    private void updateUserProfile() {
        String fullName = etFullName.getText().toString().trim();
        String age = etAge.getText().toString().trim();
        String emailAddress = etEmailAddress.getText().toString().trim();

        // Here you would normally perform validation and then update the database.
        // Since we're not interacting with a database, we'll just show the collected data.

        // Display the collected data in a Toast message or log it
        Toast.makeText(UpdateProfileActivity.this, "Collected profile data: \nName: " + fullName + "\nAge: " + age + "\nEmail: " + emailAddress, Toast.LENGTH_LONG).show();

        // Log the data (for debugging purposes)
        Log.d("UpdateProfile", "Name: " + fullName + ", Age: " + age + ", Email: " + emailAddress);

        // Here, you could potentially create a UserProfile object or some other data structure
        // with the collected data, if you plan to use it within your app.
    }
}

