package com.example.qrazyqrsrus;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.example.qrazyqrsrus.databinding.ActivityMainBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;


import java.util.ArrayList;

//temporarily implements NewEventTextFragment.AdddEventListener until we get firestore functionality
public class MainActivity extends AppCompatActivity implements NewEventTextFragment.AddEventListener {
    private ActivityMainBinding binding;

    private FirebaseFirestore db;
    private NavHostFragment navHostFragment;
    private ArrayList<Event> eventList = new ArrayList<Event>();

    private QRCodeScanHandler qrHandler;

    //we create an ActivityResultLauncher to use when we want to scan a QR code.
    //this code snippet is from ZXings Android Embedded README, which details how to use the ZXing library.
    //it can be foundhttps://github.com/journeyapps/zxing-android-embedded?tab=readme-ov-file#usage-with-scancontract and was accessed March 3rd, 2024
//    private final ActivityResultLauncher<ScanOptions> barcodeLauncher = registerForActivityResult(new ScanContract(),
//            result -> {
//                //this ActivityResultCallback lambda function handles the results of the scanning activity
//                //we check if there is a result
//                if(result.getContents() == null) {
//                    ((TextView) findViewById(R.id.bar_code_output)).setText("Error! No barcode scanned.");
//                } else {
//                    //TODO: handle result, i.e., check if this qr code was a check in, or promotion qr code, and go to the corresponding screen
//                    Event event = findEventWithPromo(result.getContents());
//                    if (event == null){
//                        //if qr code was not a promo QR code, check if it was a checkin qr code
//                        event = findEventWithCheckin(result.getContents());
//                        if (event == null){
//                            //throw error, qr code does not belong to any event
//                            //TODO: add error bar for scanning qr code that no event has
//                        } else{
//                            //if qr code was a checkin qr code, add the user to the event's checkin list
//                            //event.addToCheckinList(userID);
//                            //call firebase function to update database with event with attendee added to checkin list
//                            //updateEvent(event);
//                            //TODO: if attendee's have a list of events they are checked into, add this event to their check-in list
//                        }
//                    } else{
//                        //go to event details screen
//                        //put event in a bundle to populate event details screen with
//                        //OR: put qrContent in a bundle
//                        //in event details screen, find the Event Document with promo_qr_code field matching qrContent
//                    }
//                    ((TextView) findViewById(R.id.bar_code_output)).setText(result.getContents());
//                }
//            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        qrHandler = new QRCodeScanHandler(this);

        db = FirebaseFirestore.getInstance();

        final CollectionReference usersCollection = db.collection("Users");
        final CollectionReference eventsCollection = db.collection("Events");
        // At the start we want to be at the Home screen
        ChangeFragment(new HomeFragment());

        // When the navigation bar is clicked
        binding.BottomNavView.setOnItemSelectedListener(item -> {
            int id = item.getItemId();


            if (id == R.id.home) {
                ChangeFragment(new HomeFragment());
            } else if (id == R.id.scan) {


//                AppCompatActivity mActivity = this.super
                Event event = qrHandler.launch();
            } else if (id == R.id.my_events) {
                ChangeFragment(new MyEventsFragment());
            } else if (id == R.id.profile) {
                ChangeFragment(new ProfileFragment());
            }

            return true;
        });
    }

    //temporarily have an addEvent method. should eventuall be changed to be an observer and update when model (firestore) is changed.
    public void addEvent(Event event){
        eventList.add(event);
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

//    private Event findEventWithPromo(String qrContent){
//        ArrayList<Event> matchingEvents = new ArrayList<Event>();
//        FirebaseFirestore db = FirebaseFirestore.getInstance();
//        CollectionReference events = db.collection("Events");
//        events
//                .whereEqualTo("promo_qr_code", qrContent)
//                .get()
//                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                    @Override
//                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                        if (task.isSuccessful()){
//                            for (DocumentSnapshot documentSnapshot: task.getResult()) {
//                                matchingEvents.add(documentSnapshot.toObject(Event.class));
//                            }
//
//                        }
//                    }
//                });
//        if (matchingEvents.size() == 1){
//            //if we find an event with the promo QR code matching the scanned QR code content, return it
//            return matchingEvents.get(0);
//        } else{
//            //if we do not find an event with the promo QR code matching the scanned QR code content, return null
//            //this amy also occur if there are more than one event with the same QR promo QR code, but that should never be the case
//            //TODO: add handling for more than one event with the same QR promo code
//            return null;
//        }
//    }
//
//    private Event findEventWithCheckin(String qrContent){
//        ArrayList<Event> matchingEvents = new ArrayList<Event>();
//        FirebaseFirestore db = FirebaseFirestore.getInstance();
//        CollectionReference events = db.collection("Events");
//        events
//                .whereEqualTo("qr_code_id", qrContent)
//                .get()
//                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                    @Override
//                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                        if (task.isSuccessful()){
//                            for (DocumentSnapshot documentSnapshot: task.getResult()) {
//                                matchingEvents.add(documentSnapshot.toObject(Event.class));
//                            }
//
//                        }
//                    }
//                });
//        if (matchingEvents.size() == 1){
//            //if we find an event with the checkin QR code matching the scanned QR code content, return it
//            return matchingEvents.get(0);
//        } else{
//            //if we do not find an event with the promo QR code matching the scanned QR code content, return null
//            //this amy also occur if there are more than one event with the same checkin QR code, but that should never be the case
//            //TODO: add handling for more than one event with the same QR checkin code
//            return null;
//        }
//    }

//    public static void loginUser(String userId) {
//        // Attendee user;
//        usersCollection
//                .whereEqualTo("id", userId)
//                .get()
//                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                    @Override
//                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                        if (task.isSuccessful()) {
//                            int i = 0;
//                            for (DocumentSnapshot documentSnapshot: task.getResult()) {
//                                // user = documentSnapshot.toObject(Attendee.class);
//                                i += 1;
//                            }
//                            if (i == 0) {
//                                // This means that Attendee needs a constructor where it only accepts userId and sets the rest to default
//                                // user = new Attendee(userId);
//                                // addUser(user);
//                            }
//                        }
//                        else {
//                            Log.e("MainActivity", "Error trying to login");
//                        }
//                    }
//                });
//        // return user
//    }


}