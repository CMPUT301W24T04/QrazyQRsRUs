//this class contains the activity that is launched to scan a qr code, and defines an interface for the result to be handled by other classes
//currently, no class properly implements onNoResult if there is an error while QR code scanning
package com.example.qrazyqrsrus;

//import static com.example.qrazyqrsrus.FirebaseDB.findEventWithQR;

import android.app.Activity;
import android.provider.Settings;
import android.util.Log;
import android.widget.TextView;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.zxing.Result;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanIntentResult;
import com.journeyapps.barcodescanner.ScanOptions;

import java.util.ArrayList;

public class QRCodeScanHandler{

    //the error if an error occurs
    //1 = no event has this qr code
    //2 = no qr code successfully scanned
    //3 = more than one event with this qr code as their promo
    //4 = more than one event with this qr code as their checkin
//    private int errorNumber;

    private ActivityResultLauncher<ScanOptions> barcodeLauncher;

    private Attendee user;

    //other classes (main activity) can invoke QR scan handler with a lambda function that implements this interface
    public interface ScanCompleteCallback{

        /**
         * A function for the callback to handle the result of a successful promotional QR code scan
         * @param event the event that has this QR code as it's promotional QR
         */
        public void onPromoResult(Event event);
        /**
         * A function for the callback to handle the result of a successful check-in QR code scan
         * @param event the event that has this QR code as it's check-in QR
         */
        public void onCheckInResult(Event event);
        /**
         * A function for the callback to handle the result of a unsuccessful QR code scan
         * @param errorNumber the error encountered while trying to scan a QR code. (1: no event has this qr code, 2: no qr code successfully scanned, 3: more than one event has this QR code as their promotional qr code, 4: more than one event has this QR code as their check-in qr code, 5: user scanned check-in QR code but they are not signed up
         * @param event the event of the scanned qr code that is throwing an error. this parameter can be null depending on what kind of error there is
         */
        public void onNoResult(@Nullable Event event, int errorNumber);
    }

    /**
     * Defines the activity that allows for the scanning of a QR code with the camera,
     * then queries the database to see if the scanned QR code is an event's promotion QR code, or check-in QR code
     * @param activity the activity that launches the scanning activity
     * @param userID the AndroidID of the user that is scanning
     * @param callback the function that will handle the results of the scan
     */
    public QRCodeScanHandler(AppCompatActivity activity, String userID, ScanCompleteCallback callback) {
        barcodeLauncher = activity.registerForActivityResult(new ScanContract(),
                result -> {
                    //this ActivityResultCallback lambda function handles the results of the scanning activity
                    //we check if there is a result
                    if(result.getContents() == null) {
                        callback.onNoResult(null, 2);
                    } else {
                        //first we look to see if the qr code we just scanned is an event's promo qr code
                        FirebaseDB.findEventWithQR(result.getContents(), 0, new FirebaseDB.MatchingQRCallBack() {
                            @Override
                            public void onResult(Event matchingEvent) {
                                //if a promo QR code is successfully found
                                Log.d("findEventWithQR", "this callback invoked");
                                //event[0] = matchingEvent;
                                callback.onPromoResult(matchingEvent);
                            }

                            //if there we do not find an event in the DB with a matching promo qr content, we look for a matching check-in qr code
                            @Override
                            public void onNoResult() {
                                FirebaseDB.findEventWithQR(result.getContents(), 1, new FirebaseDB.MatchingQRCallBack() {
                                    @Override
                                    public void onResult(Event matchingEvent) {
                                        Log.d("findEventWithQR", "callback invoked");
                                        //we check if the user scanning the check-in qr code has signed up to the event
                                        if (matchingEvent.getSignUps().contains(user.getDocumentId())){
                                            //if so, they can check in normally
                                            callback.onCheckInResult(matchingEvent);
                                        } else{
                                            //otherwise, they should be brought to the event details screen, but shown a dialog saying they cannot check-in without signing up first
                                            callback.onNoResult(matchingEvent, 5);
                                        }

                                    }
                                    //if the qr code just scanned is not any event's promo or check-in qr code, we tell the callback
                                    @Override
                                    public void onNoResult() {
                                        //we tell the callback that we found no matching qr code
                                        callback.onNoResult(null, 1);
                                    }
                                });
                            }
                        });
                    }
                });
    }

    /**
     * Launches the Activity defined in the constructor
     */
    public void launch(Attendee user){
        if (user == null){
            //TODO: use singleton
            Log.d("QRCodeScanHandler Launch", "user was null");
        }
        this.user = user;
        barcodeLauncher.launch(new ScanOptions());
    }

}
