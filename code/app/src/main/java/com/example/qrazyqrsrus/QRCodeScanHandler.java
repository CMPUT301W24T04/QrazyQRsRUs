//this class contains the activity that is launched to scan a qr code, and defines an interface for the result to be handled by other classes
//currently, no class properly implements onNoResult if there is an error while QR code scanning
package com.example.qrazyqrsrus;

import static com.example.qrazyqrsrus.FirebaseDB.findEventWithQR;

import android.app.Activity;
import android.provider.Settings;
import android.util.Log;
import android.widget.TextView;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
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

    //the event that we obtain
    private Event[] event = new Event[1];
    //the error if an error occurs
    //1 = no event has this qr code
    //2 = no qr code successfully scanned
    //3 = more than one event with this qr code as their promo
    //4 = more than one event with this qr code as their checkin
    private int errorNumber;
    private AppCompatActivity activity;
    private String userID;
    private ActivityResultLauncher<ScanOptions> barcodeLauncher;

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
         * @param errorNumber the error encountered while trying to scan a QR code. (1: no event has this qr code, 2: no qr code successfully scanned, 3: more than one event has this QR code as their promotional qr code, 4: more than one event has this QR code as their check-in qr code
         */
        public void onNoResult(int errorNumber);
    }

    /**
     * Defines the activity that allows for the scanning of a QR code with the camera,
     * then queries the database to see if the scanned QR code is an event's promotion QR code, or check-in QR code
     * @param activity the activity that launches the scanning activity
     * @param userID the AndroidID of the user that is scanning
     * @param callback the function that will handle the results of the scan
     */
    public QRCodeScanHandler(AppCompatActivity activity, String userID, ScanCompleteCallback callback) {
        this.activity = activity;
        this.userID = userID;
        barcodeLauncher = activity.registerForActivityResult(new ScanContract(),
                result -> {
                    //this ActivityResultCallback lambda function handles the results of the scanning activity
                    //we check if there is a result
                    if(result.getContents() == null) {
                        //TODO: invoke callback.onNoResult(2)
                        //((TextView) findViewById(R.id.bar_code_output)).setText("Error! No barcode scanned.");
                    } else {
                        findEventWithQR(result.getContents(), 0, new FirebaseDB.MatchingQRCallBack() {
                            @Override
                            public void onResult(Event matchingEvent) {
                                Log.d("findEventWithQR", "this callback invoked");
                                event[0] = matchingEvent;
                                callback.onPromoResult(matchingEvent);
                                //TODO: the problem is that event[0] takes a while to update (has to query and whatnot), and subsequent lines of code continue to execute.
                                //TODO: this means we could hit an error when a qr code does exist
                                //TODO: or in my specific case, it means event is not getting reset to null
                                //TODO: THE QR SCAN WORKS PERFECTLY FINE EVERY **OTHER** TIME

                            }
                        });
                        if (event[0] == null){
                            Log.d("findEventWithQR", "we get to here");
                            //if qr code was not a promo QR code, check if it was a checkin qr code
                            findEventWithQR(result.getContents(), 1, new FirebaseDB.MatchingQRCallBack() {

                                @Override
                                public void onResult(Event matchingEvent) {
                                    Log.d("findEventWithQR", "callback invoked");
                                    event[0] = matchingEvent;
                                    callback.onCheckInResult(matchingEvent);

                                }
                            });
                            if (event[0] == null){
                                Log.d("reset", "we get to badness");
                                //throw error, qr code does not belong to any event
                                //TODO: invoke callback.onNoResult(1)
                            } else{
                                Log.d("reset", "we reset");
                                //reset event after finding a checkIn QR
                                event[0] = null;
                            }
                        } else{
                            Log.d("reset", "we reset here");
                            //reset event after finding a promo QR
                            event[0] = null;
                        }
                    }
                });
    }

    /**
     * Launches the Activity defined in the constructor
     */
    public void launch(){
        barcodeLauncher.launch(new ScanOptions());
    }

//    public Event getEvent() {
//        return event;
//    }

    public int getErrorNumber() {
        return errorNumber;
    }

    public void reset(){
        this.event[0] = null;
    }

}
