package com.example.qrazyqrsrus;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
    private String deviceId;

    Attendee[] user = new Attendee[1];
    private QRCodeScanHandler qrHandler = new QRCodeScanHandler(this, deviceId, new QRCodeScanHandler.ScanCompleteCallback() {
        //TODO: these callbacks only work on the first time a QR code is scanned after the app is launched

        @Override
        public void onPromoResult(Event matchingEvent) {
            ChangeFragment(EventDetailsFragment.newInstance(matchingEvent, user[0], false));

        }

        @Override
        public void onCheckInResult(Event event) {
            FirebaseDB.checkInAlreadyExists(event.getDocumentId(), user[0].getDocumentId(), new FirebaseDB.UniqueCheckInCallBack() {
                @Override
                public void onResult(boolean isUnique, CheckIn checkIn) {
                    if (isUnique) {
                        //if there is no existing checkIn with the attendee's document ID and the event's document ID we make a new one
                        CheckIn newCheckIn = new CheckIn(user[0].getDocumentId(), event.getDocumentId());
                        FirebaseDB.addCheckInToEvent(newCheckIn, event);
                    } else{
                        //in this case the event should already have the checkIn in it's checkIn list
                        checkIn.incrementCheckIns();
                        FirebaseDB.updateCheckIn(checkIn);
                    }
                }
            });
            ChangeFragment(EventDetailsFragment.newInstance(event, user[0], true));

        }

        @Override
        public void onNoResult(@Nullable Event event, int errorNumber){
            if (event != null){
                ChangeFragment(EventDetailsFragment.newInstance(event, user[0], false));
                new ErrorDialog(R.string.not_signed_up_error).show(getSupportFragmentManager(), "QR Error Dialog");
            } else{
                new ErrorDialog(R.string.no_args).show(getSupportFragmentManager(), "QR Error Dialog");
            }



        }

    });

//    qrHandler =

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Apparently this is not good practice, but if it works, it works.
        deviceId = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);

        Log.d("test", deviceId);


        //CurrentUser.getInstance().initializeUser(deviceId);

        //Attendee[] user = new Attendee[1];

        FirebaseDB.loginUser(deviceId, new FirebaseDB.GetAttendeeCallBack() {
            @Override
            public void onResult(Attendee attendee) {
                user[0] = attendee;
                ChangeFragment(new HomeEventsFragment());
            }
        });



        if (deviceId == null) {
            Log.d("deviceId", "super badness");
            return;
        }

        // When the navigation bar is clicked
        binding.BottomNavView.setOnItemSelectedListener(item -> {
            int id = item.getItemId();


            if (id == R.id.home) {
                ChangeFragment(new HomeEventsFragment());
            } else if (id == R.id.scan) {
                qrHandler.launch(user[0]);
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