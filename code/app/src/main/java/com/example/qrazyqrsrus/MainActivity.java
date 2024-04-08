package com.example.qrazyqrsrus;

// This fragment holds the logic for QR code scanning and acts as a host for the Notifications

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import android.Manifest;
import android.app.Activity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;

import com.example.qrazyqrsrus.databinding.ActivityMainBinding;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity implements ActivityCompat.OnRequestPermissionsResultCallback{
    private ActivityMainBinding binding;
    private String deviceId;
    private Activity activity = this;

    Attendee[] user = new Attendee[1];
    /**
     * Uses a callback to do actions when user scans a QR code
     */
    private QRCodeScanHandler qrHandler = new QRCodeScanHandler(FirebaseDB.getInstance(), this, deviceId, new QRCodeScanHandler.ScanCompleteCallback() {
        //TODO: these callbacks only work on the first time a QR code is scanned after the app is launched

        /**
         * Holds logic for when a promo QR code is scanned
         * @param matchingEvent the event that has this QR code as it's promotional QR
         */
        @Override
        public void onPromoResult(Event matchingEvent) {
            ChangeFragment(EventDetailsFragment.newInstance(matchingEvent, null, false));

        }

        /**
         * Checks in a user to the event when they scan the check in QR code
         * @param event the event that has this QR code as it's check-in QR
         */
        @Override
        public void onCheckInResult(Event event) {
            FirebaseDB.getInstance().loginUser(deviceId, new FirebaseDB.GetAttendeeCallBack() {
                @Override
                public void onResult(Attendee attendee) {
                    FirebaseDB.getInstance().checkInAlreadyExists(event.getDocumentId(), attendee.getDocumentId(), new FirebaseDB.UniqueCheckInCallBack() {
                        @Override
                        public void onResult(boolean isUnique, CheckIn checkIn) {
                            LocationSingleton.getInstance().getLocation(activity, new LocationSingleton.LongitudeLatitudeCallback() {
                                @Override
                                public void onResult(double longitude, double latitude) {
                                    if (isUnique){
                                        //if the user has not yet chcked into the event, we make a new one
                                        CheckIn newCheckIn = new CheckIn(attendee.getDocumentId(), event.getDocumentId(), longitude, latitude);
                                        FirebaseDB.getInstance().addCheckInToEvent(newCheckIn, event);
                                    } else{
                                        //if the user has already checked into the event, we update their checkin with their latest location, and increment the # of checkins
                                        checkIn.setLongitude(longitude);
                                        checkIn.setLatitude(latitude);
                                        checkIn.incrementCheckIn();
                                        FirebaseDB.getInstance().updateCheckIn(checkIn);
                                    }

                                }
                            });
                        }
                    });
                    ChangeFragment(EventDetailsFragment.newInstance(event, attendee, true));
                }

                @Override
                public void onNoResult() {
                    Log.d("MainActivity", "problem logging in user to create checkin");
                }
            });

        }

        /**
         * Show error when something goes wrong when scanning the QR code
         * @param event the event of the scanned qr code that is throwing an error. this parameter can be null depending on what kind of error there is
         * @param errorNumber the error encountered while trying to scan a QR code. (1: no event has this qr code, 2: no qr code successfully scanned, 3: more than one event has this QR code as their promotional qr code, 4: more than one event has this QR code as their check-in qr code, 5: user scanned check-in QR code but they are not signed up
         */
        @Override
        public void onNoResult(@Nullable Event event, int errorNumber){
            if (event != null){
                ChangeFragment(EventDetailsFragment.newInstance(event, null, false));
                new ErrorDialog(R.string.not_signed_up_error).show(getSupportFragmentManager(), "QR Error Dialog");
            } else{
                new ErrorDialog(R.string.no_args).show(getSupportFragmentManager(), "QR Error Dialog");
            }
        }

        /**
         * Changes fragment to the admin when the user scans the admin QR code
         */
        public void onSpecialResult(){
            ChangeFragment(AdminHost.newInstance());
        }

    });
    /**
     * Ask for permission for the user to recieve push notification
     */
    private ActivityResultLauncher<String> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) {
                    Log.d("Notification Permissions", "user accepted notification permissions");
                } else {
                    Log.e("Notification Permissions", "user denied notification permissions");
                }
            });

    /**
     * Called when the activity is starting. This is where most initialization should go: calling {@code setContentView(int)}
     * to inflate the activity's UI, using {@code findViewById(int)} to programmatically interact with widgets in the UI,
     * setting up any static data in lists, binding data to lists, etc.
     * <p>
     * In this implementation, {@code onCreate} initializes the activity's binding for view interaction without directly referencing
     * the UI components by ID, sets up the device ID for future use, and initializes the {@link QRCodeScanHandler} with appropriate
     * callbacks for handling different outcomes of QR code scanning (promo QR codes, check-in QR codes, and special admin QR codes).
     * This setup facilitates actions based on QR code scanning, such as navigating to different fragments based on the scan results.
     * <p>
     * This method also checks for necessary permissions (if any are needed) and sets up any other initial configurations required for
     * the app's main activity.
     *
     * @param savedInstanceState If the activity is being re-initialized after previously being shut down then this Bundle contains the
     *                           data it most recently supplied in {@link #onSaveInstanceState(Bundle)}. <b>Note:</b> Otherwise it is null.
     */

    /**
     * Manages notifications in the server
     * @param savedInstanceState If the activity is being re-initialized after
     *     previously being shut down then this Bundle contains the data it most
     *     recently supplied in {@link #onSaveInstanceState}.  <b><i>Note: Otherwise it is null.</i></b>
     *
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        deviceId = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);

        //we don't need to getToken, this is just for testing
        //FirebaseDB.getInstance().getToken();
        //we shouldn't subscribe the user here, this is just for testing
        //FirebaseDB.getInstance().subscribeAttendeeToEventTopic("EVENT");
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

        ChangeFragment(new HomeEventsFragment());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU){
            NotificationManager notificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
            if (!notificationManager.areNotificationsEnabled()){
                //THIS NEEDS TESTING, i don't know if it works, because my notifications were enabled already
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS);
            }
        }

        // When the navigation bar is clicked
        binding.BottomNavView.setOnItemSelectedListener(item -> {
            int id = item.getItemId();


            if (id == R.id.home) {
                ChangeFragment(new HomeEventsFragment());
            } else if (id == R.id.scan) {
                qrHandler.launch(deviceId);
            } else if (id == R.id.my_events) {
                ChangeFragment(new MyEventsFragment());
            } else if (id == R.id.profile) {
                //create a new instance of the ViewProfileFragment fragment, with the attendee that was obtained by logging in the user
                ChangeFragment(ViewProfileFragment.newInstance(null));
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

    public void setQrHandler(QRCodeScanHandler qrHandler) {
        this.qrHandler = qrHandler;
    }
}