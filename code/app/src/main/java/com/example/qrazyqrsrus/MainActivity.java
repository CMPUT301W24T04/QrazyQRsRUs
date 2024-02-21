package com.example.qrazyqrsrus;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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

public class MainActivity extends AppCompatActivity {

    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = FirebaseFirestore.getInstance();

        final CollectionReference usersCollection = db.collection("Users");
        final CollectionReference eventsCollection = db.collection("Events");
        final CollectionReference qrCodesCollection = db.collection("QRCodes");
        final CollectionReference imagesCollection = db.collection("Images");
    }

    public void userExample(CollectionReference usersCollection) {
        Map<String, Object> userInfo = new HashMap<>();
        userInfo.put("name", "John Doe");
        userInfo.put("email", "johndoe@example.com"); // Might have to make a check that it's a valid email later
        userInfo.put("profile_picture_id", 00000000); // 8 digits long
        userInfo.put("geolocation_on", 1); // 1 if true, 0 otherwise
        usersCollection.add(userInfo); // Add to Collection
    }

    public void eventExample(CollectionReference eventsCollection) {
        Map<String, Object> eventInfo = new HashMap<>();
        LocalDateTime start = LocalDateTime.of(2024, 3, 23, 10, 30);
        LocalDateTime end = LocalDateTime.of(2024, 3, 23, 11, 30);
        List<Integer> signUps = new ArrayList<>();
        signUps.add(11111111);
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
        checkIn2.put("times", 2);
        checkIn2.put("location", new Geolocation(40.7128, -74.0060));

        // Add check-in to list
        checkIns.add(checkIn1);
        checkIns.add(checkIn2);

        // Now add everything to your document
        eventInfo.put("owner_id", 00000000); // 8 digits long
        eventInfo.put("name", "Event Name");
        eventInfo.put("location", "Event Location");
        eventInfo.put("start_date", start.toEpochSecond(java.time.ZoneOffset.UTC)); // Will need to convert it back when you fetch
        eventInfo.put("end_date", end.toEpochSecond(java.time.ZoneOffset.UTC));
        eventInfo.put("geolocation_on", 1); // 1 if true, 0 otherwise
        eventInfo.put("poster_id", 00000000); // 8 digits long
        eventInfo.put("qr_code_id", 00000000); // 8 digits long
        eventInfo.put("promo_qr_code", 00000000); // 8 digits long
        eventInfo.put("sign_ups_id", signUps);
        eventInfo.put("check_ins", checkIns);
        eventsCollection.add(eventInfo); // Add to collection
    }
}