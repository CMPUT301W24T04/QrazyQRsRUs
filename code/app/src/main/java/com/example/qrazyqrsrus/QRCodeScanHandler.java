package com.example.qrazyqrsrus;

import static com.example.qrazyqrsrus.FirebaseDB.findEventWithQR;

import android.app.Activity;
import android.provider.Settings;
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
        Event event;
    //the error if an error occurs
    //1 = no event has this qr code
    //2 = no qr code successfully scanned
    //3 = more than one event with this qr code as their promo
    //4 = more than one event with this qr code as their checkin
    private int errorNumber;
    private AppCompatActivity activity;
    private String userID;
    private Boolean isPromo = false;
    private ActivityResultLauncher<ScanOptions> barcodeLauncher;

    public QRCodeScanHandler(AppCompatActivity activity, String userID) {
        this.activity = activity;
        this.userID = userID;
        barcodeLauncher = activity.registerForActivityResult(new ScanContract(),
                result -> {
                    //this ActivityResultCallback lambda function handles the results of the scanning activity
                    //we check if there is a result
                    if(result.getContents() == null) {
                        //((TextView) findViewById(R.id.bar_code_output)).setText("Error! No barcode scanned.");
                    } else {
                        //TODO: handle result, i.e., check if this qr code was a check in, or promotion qr code, and go to the corresponding screen
//                        Event event;
                        findEventWithQR(result.getContents(), 0, new FirebaseDB.MatchingQRCallBack() {
                            @Override
                            public void onResult(Event matchingEvent) {
                                event = matchingEvent;
                                isPromo = true;
                            }
                        });
                        if (event == null){
                            //if qr code was not a promo QR code, check if it was a checkin qr code
                            findEventWithQR(result.getContents(), 1, new FirebaseDB.MatchingQRCallBack() {

                                @Override
                                public void onResult(Event matchingEvent) {
                                    event = matchingEvent;
                                }
                            });
                            if (event == null){
                                //throw error, qr code does not belong to any event
                                //TODO: add error bar for scanning qr code that no event has
                            } else{
                                //if qr code was a checkin qr code, add the user to the event's checkin list
                                //event.addToCheckinList(userID);
                                //getAttende(UserID).
                                //call firebase function to update database with event with attendee added to checkin list
                                //updateEvent(event);
                                //TODO: if attendee's have a list of events they are checked into, add this event to their check-in list
                                event.addCheckIn(userID);
                            }
                        } else{
                            //go to event details screen
                            //put event in a bundle to populate event details screen with

                            //OR: put qrContent in a bundle
                            //in event details screen, find the Event Document with promo_qr_code field matching qrContent
                        }
                        //((TextView) findViewById(R.id.bar_code_output)).setText(result.getContents());
                    }
                });
    }
    public Event launch(){
        barcodeLauncher.launch(new ScanOptions());
        return this.event;
    }

    public Event getEvent() {
        return event;
    }

    public int getErrorNumber() {
        return errorNumber;
    }

    public Boolean getPromo() {
        return isPromo;
    }

    //    private final ActivityResultLauncher<ScanOptions> barcodeLauncher = activity.registerForActivityResult(new ScanContract(),
//        result -> {
//            //this ActivityResultCallback lambda function handles the results of the scanning activity
//            //we check if there is a result
//            if(result.getContents() == null) {
//                //((TextView) findViewById(R.id.bar_code_output)).setText("Error! No barcode scanned.");
//            } else {
//                //TODO: handle result, i.e., check if this qr code was a check in, or promotion qr code, and go to the corresponding screen
//                Event event = findEventWithPromo(result.getContents());
//                if (event == null){
//                    //if qr code was not a promo QR code, check if it was a checkin qr code
//                    event = findEventWithCheckin(result.getContents());
//                    if (event == null){
//                        //throw error, qr code does not belong to any event
//                        //TODO: add error bar for scanning qr code that no event has
//                    } else{
//                        //if qr code was a checkin qr code, add the user to the event's checkin list
//                        //event.addToCheckinList(userID);
//                        //call firebase function to update database with event with attendee added to checkin list
//                        //updateEvent(event);
//                        //TODO: if attendee's have a list of events they are checked into, add this event to their check-in list
//                    }
//                } else{
//                    //go to event details screen
//                    //put event in a bundle to populate event details screen with
//                    //OR: put qrContent in a bundle
//                    //in event details screen, find the Event Document with promo_qr_code field matching qrContent
//                }
//                //((TextView) findViewById(R.id.bar_code_output)).setText(result.getContents());
//            }
//        });
//    @Override
//    public void onActivityResult(ScanIntentResult result) {
//        //this ActivityResultCallback lambda function handles the results of the scanning activity
//        //we check if there is a result
//
//        if(result.getContents() == null) {
//            //((TextView) findViewById(R.id.bar_code_output)).setText("Error! No barcode scanned.");
//            this.errorNumber = 2;
//        } else {
//            //TODO: handle result, i.e., check if this qr code was a check in, or promotion qr code, and go to the corresponding screen
//            Event event = findEventWithPromo(result.getContents());
//            if (event == null){
//                //if qr code was not a promo QR code, check if it was a checkin qr code
//                event = findEventWithCheckin(result.getContents());
//                if (event == null){
//                    //throw error, qr code does not belong to any event
//                    //TODO: add error bar for scanning qr code that no event has
//                    this.errorNumber = 1;
//                } else{
//                    //if qr code was a checkin qr code, add the user to the event's checkin list
//                    //event.addToCheckinList(userID);
//                    //call firebase function to update database with event with attendee added to checkin list
//                    //updateEvent(event);
//                    //TODO: if attendee's have a list of events they are checked into, add this event to their check-in list
//                }
//            } else{
//                //go to event details screen
//                //put event in a bundle to populate event details screen with
//                //OR: put qrContent in a bundle
//                //in event details screen, find the Event Document with promo_qr_code field matching qrContent
//                this.event = event;
//            }
//
//            //((TextView) findViewById(R.id.bar_code_output)).setText(result.getContents());
//        }
//
//
//    }
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
//            this.errorNumber = 3;
//            return null;
//        }
//    }
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
//            this.errorNumber = 4;
//            return null;
//        }
//    }
}
