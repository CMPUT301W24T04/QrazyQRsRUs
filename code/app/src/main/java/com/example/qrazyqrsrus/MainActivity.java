package com.example.qrazyqrsrus;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.qrazyqrsrus.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        db = FirebaseFirestore.getInstance();

        final CollectionReference usersCollection = db.collection("Users");
        final CollectionReference eventsCollection = db.collection("Events");
        final CollectionReference qrCodesCollection = db.collection("QRCodes"); // Have not completed template yet
        final CollectionReference imagesCollection = db.collection("Images"); // Have not completed template yet
        // At the start we want to be at the Homescreen
        ChangeFragment(new HomeFragment());

        // When the navigation bar is clicked
        binding.BottomNavView.setOnItemSelectedListener(item -> {
            int id = item.getItemId();

            if (id == R.id.home){
                ChangeFragment(new HomeFragment());
            } else if (id == R.id.scan) {
                // TO DO
            } else if (id == R.id.my_events) {
                ChangeFragment(new MyEventsFragment());
            } else if (id == R.id.profile) {
                ChangeFragment(new ProfileFragment());
            }

            return true;
        });

    }

    /**
     * Use this method to display the fragment that is passed
     * as an argument
     *
     * @param fragment The fragment we want to display.
     */
    private void ChangeFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();
    }

    public void eventExample(CollectionReference eventsCollection) {
        Map<String, Object> eventInfo = new HashMap<>();
        LocalDateTime start = LocalDateTime.of(2024, 3, 23, 10, 30);
        LocalDateTime end = LocalDateTime.of(2024, 3, 23, 11, 30);
        String details = "This is a test event. If you're reading this from the app then it worked!";
        List<Integer> signUps = new ArrayList<>();
        signUps.add(11113311);
        signUps.add(22222222);
        signUps.add(33333333);

        // Creating list to hold check-ins
        List<Map<String, Object>> checkIns = new ArrayList<>();
        // Creating first check-in
        Map<String, Object> checkIn1 = new HashMap<>();
        checkIn1.put("attendee_id", 23882139);
        checkIn1.put("times", 2);
        checkIn1.put("location", new Geolocation(40.7128, -74.0060));
        // Creating second check-in
        Map<String, Object> checkIn2 = new HashMap<>();
        checkIn2.put("attendee_id", 66754329);
        checkIn2.put("times", 1);
        checkIn2.put("location", new Geolocation(40.7128, -74.0060));

        // Add check-in to list
        checkIns.add(checkIn1);
        checkIns.add(checkIn2);

        // Now add everything to your document
        eventInfo.put("owner_id", 11111111); // 8 digits long
        eventInfo.put("name", "Event Name");
        eventInfo.put("location", "Event Location");
        eventInfo.put("details", details);
        eventInfo.put("start_date", start.toEpochSecond(java.time.ZoneOffset.UTC)); // Will need to convert it back when you fetch
        eventInfo.put("end_date", end.toEpochSecond(java.time.ZoneOffset.UTC));
        eventInfo.put("geolocation_on", 1); // 1 if true, 0 otherwise
        eventInfo.put("poster_id", 43244444); // 8 digits long
        eventInfo.put("qr_code_id", 55555555); // 8 digits long
        eventInfo.put("promo_qr_code", 88888888); // 8 digits long
        eventInfo.put("sign_ups_id", signUps);
        eventInfo.put("check_ins", checkIns);
        eventsCollection.add(eventInfo); // Add to collection
    }
}