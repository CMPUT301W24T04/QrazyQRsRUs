package com.example.qrazyqrsrus;

import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.example.qrazyqrsrus.databinding.ActivityMainBinding;
import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;


import java.util.ArrayList;

//temporarily implements NewEventTextFragment.AdddEventListener until we get firestore functionality
public class MainActivity extends AppCompatActivity implements NewEventTextFragment.AddEventListener {
    private ActivityMainBinding binding;

    private FirebaseFirestore db;
    private NavHostFragment navHostFragment;
    private ArrayList<Event> eventList = new ArrayList<Event>();

    //we create an ActivityResultLauncher to use when we want to scan a QR code.
    //this code snippet is from ZXings Android Embedded README, which details how to use the ZXing library.
    //it can be foundhttps://github.com/journeyapps/zxing-android-embedded?tab=readme-ov-file#usage-with-scancontract and was accessed March 3rd, 2024
    private final ActivityResultLauncher<ScanOptions> barcodeLauncher = registerForActivityResult(new ScanContract(),
            result -> {
                //this ActivityResultCallback lambda function handles the results of the scanning activity
                //we check if there is a result
                if(result.getContents() == null) {
                    ((TextView) findViewById(R.id.bar_code_output)).setText("Error! No barcode scanned.");
                } else {
                    //TODO: handle result, i.e., check if this qr code was a check in, or promotion qr code, and go to the corresponding screen
                    ((TextView) findViewById(R.id.bar_code_output)).setText(result.getContents());
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

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

                barcodeLauncher.launch(new ScanOptions());
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


}