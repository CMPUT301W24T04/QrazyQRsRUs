package com.example.qrazyqrsrus;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Find the button by its ID
        Button btnEditProfile = findViewById(R.id.btnEditProfile);

        // Set an OnClickListener on the button
        btnEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Create an Intent to start UpdateProfileActivity
                Intent intent = new Intent(MainActivity.this, UpdateProfileActivity.class);
                startActivity(intent); // Start the new activity
            }
        });
    }
}