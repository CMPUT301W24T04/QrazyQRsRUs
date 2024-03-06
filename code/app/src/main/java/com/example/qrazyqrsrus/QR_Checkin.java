package com.example.qrazyqrsrus;

import android.util.Log;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;


import android.util.Log;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

public class QR_Checkin {
    /**
     * This is not meant to be run here, just as an example of how promotional QR code scan adds attendees
     * addUser() is relevant here to add a user by their id to the firestore
     */
    final FirebaseFirestore db = FirebaseFirestore.getInstance();
    final CollectionReference attendeesCollection = db.collection("Attendees");

    public void check_event(){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference events = db.collection(("Events"));

    }
    public void addUser(String user_id) {
        /**
         * Takes in an initial user Id from a main function, adds that id and increments it for each user added
         */

        final String TAG = "Sample";
        final CollectionReference collectionReference = db.collection("Attendees");
        HashMap<String, String> data = new HashMap<>();
        collectionReference
                .document(user_id)// "111"
                .set(data)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // These are a method which gets executed when the task is succeeded
                        Log.d(TAG, "Data has been added successfully!");

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // These are a method which gets executed if there’s any problem
                        Log.d(TAG, "Data could not be added!" + e.toString());
                    }
                });


    }
}
//            // CHECK IF QR CODE IS SCANNED
//        // Register the launcher and result handler
//        private final ActivityResultLauncher<ScanOptions> barcodeLauncher = registerForActivityResult(new ScanContract(),
//                result -> {
//                    if(result.getContents() == null) {
//                        Toast.makeText(MyActivity.this, "Cancelled", Toast.LENGTH_LONG).show();
//                    } else {
//                        Toast.makeText(MyActivity.this, "Scanned: " + result.getContents(), Toast.LENGTH_LONG).show();
//                        // add user
//
//                    }
//                });
//
////     Launch
//        public void onButtonClick(View view) {
//            barcodeLauncher.launch(new ScanOptions());
//        }
//}
