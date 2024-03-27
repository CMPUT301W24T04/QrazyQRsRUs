package com.example.qrazyqrsrus;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;
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

    private ActivityResultLauncher<String> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) {
                    Log.d("Notification Permissions", "user accepted notification permissions");
                } else {
                    Log.e("Notification Permissions", "user denied notification permissions");
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

        //we don't need to getToken, this is just for testing
        //FirebaseDB.getToken();
        //we shouldn't subscribe the user here, this is just for testing
        //FirebaseDB.subscribeAttendeeToEventTopic("EVENT");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Event Announcements";
            String description = "Receive push notifications from event organizers";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("EVENTS", name, importance);
            channel.setDescription(description);
            // Register the channel with the system. You can't change the importance
            // or other notification behaviors after this.
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
        //CurrentUser.getInstance().initializeUser(deviceId);

        //Attendee[] user = new Attendee[1];

        FirebaseDB.loginUser(deviceId, new FirebaseDB.GetAttendeeCallBack() {
            @Override
            public void onResult(Attendee attendee) {
                user[0] = attendee;
                ChangeFragment(new HomeEventsFragment());
            }
        });

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU){
            NotificationManager notificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
            if (!notificationManager.areNotificationsEnabled()){
                //THIS NEEDS TESTING, i don't know if it works, because my notifications were enabled already
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS);
            }
        }



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
        //was getting java.lang.IllegalStateException: Can not perform this action after onSaveInstanceState when using .commit()
        //fragmentTransaction.commitAllowingStateLoss();
    }


}