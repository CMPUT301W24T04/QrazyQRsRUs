package com.example.qrazyqrsrus;

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
import android.util.Log;

import com.example.qrazyqrsrus.databinding.ActivityMainBinding;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity{
    private ActivityMainBinding binding;

    private NavController navController;
    private ArrayList<Event> eventList = new ArrayList<Event>();

    private String deviceId;
    private QRCodeScanHandler qrHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Apparently this is not good practice, but if it works, it works.
        deviceId = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);

        Attendee[] user = new Attendee[1];
        FirebaseDB.loginUser(deviceId, new FirebaseDB.GetAttendeeCallBack() {
            @Override
            public void onResult(Attendee attendee) {
                user[0] = attendee;
                ChangeFragment(HomeFragment.newInstance(user[0]));
            }
        });

        qrHandler = new QRCodeScanHandler(this, deviceId, new QRCodeScanHandler.ScanCompleteCallback() {
            @Override
            public void onPromoResult(Event matchingEvent) {
                ChangeFragment(EventDetailsFragment.newInstance(matchingEvent, user[0], false));
            }

            @Override
            public void onCheckInResult(Event event) {
                ChangeFragment(EventDetailsFragment.newInstance(event, user[0], true));
            }

            @Override
            public void onNoResult(int errorNumber){
                new ErrorDialog(R.string.no_args).show(getSupportFragmentManager(), "Error Dialog");
            }

        });



        // At the start we want to be at the Home screen




        if (deviceId == null) {
            return;
        }

        // When the navigation bar is clicked
        binding.BottomNavView.setOnItemSelectedListener(item -> {
            int id = item.getItemId();


            if (id == R.id.home) {
                ChangeFragment(HomeFragment.newInstance(user[0]));
            } else if (id == R.id.scan) {
                qrHandler.launch();
//                if (event == null){
//                    //TODO: handle errors, check ints in QRCodeScanHandler
//                    Log.d("testing", "no event from qr code");
//                } else{
//                    Log.d("testing", "no event from qr code");
//                    ChangeFragment(EventDetailsFragment.newInstance(event));
//                }
            } else if (id == R.id.my_events) {
                ChangeFragment(new MyEventsFragment());
            } else if (id == R.id.profile) {
                //create a new instance of the ViewProfileFragment fragment, with the attendee that was obtained by logging in the user
                ChangeFragment(ViewProfileFragment.newInstance(user[0]));
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


}