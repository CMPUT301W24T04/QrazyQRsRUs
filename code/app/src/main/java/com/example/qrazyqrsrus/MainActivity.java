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
import android.provider.Settings;
import android.widget.TextView;
import android.util.Log;

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

    private String deviceId;
    private QRCodeScanHandler qrHandler;

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

        // Apparently this is not good practice, but if it works, it works.
        deviceId = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);

        if (deviceId == null) {
            return;
        }

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


}