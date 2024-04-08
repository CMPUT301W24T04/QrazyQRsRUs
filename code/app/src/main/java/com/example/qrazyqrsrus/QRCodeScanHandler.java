//this class contains the activity that is launched to scan a qr code, and defines an interface for the result to be handled by other classes
//currently, no class properly implements onNoResult if there is an error while QR code scanning
package com.example.qrazyqrsrus;

import static androidx.test.InstrumentationRegistry.getContext;
import static androidx.test.core.app.ApplicationProvider.getApplicationContext;

import android.app.Activity;
import android.content.ContentResolver;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
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
import com.google.zxing.BinaryBitmap;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.NotFoundException;
import com.google.zxing.RGBLuminanceSource;
import com.google.zxing.Result;
import com.google.zxing.common.HybridBinarizer;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanIntentResult;
import com.journeyapps.barcodescanner.ScanOptions;

import java.util.ArrayList;
import java.util.Objects;

/**
 * Executes the actions when a Qr code is scanned
 */
public class QRCodeScanHandler{

    //the error if an error occurs
    //1 = no event has this qr code
    //2 = no qr code successfully scanned
    //3 = more than one event with this qr code as their promo
    //4 = more than one event with this qr code as their checkin
//    private int errorNumber;

    private ActivityResultLauncher<ScanOptions> barcodeLauncher;

    private Attendee user;

    private MultiFormatReader reader = new MultiFormatReader();

    private FirebaseDB firebaseDB;

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

        public void onSpecialResult();
    }

    /**
     * Defines the activity that allows for the scanning of a QR code with the camera,
     * then queries the database to see if the scanned QR code is an event's promotion QR code, or check-in QR code
     * @param activity the activity that launches the scanning activity
     * @param userID the AndroidID of the user that is scanning
     * @param callback the function that will handle the results of the scan
     */
    public QRCodeScanHandler(FirebaseDB instance, AppCompatActivity activity, String userID, ScanCompleteCallback callback) {
        this.firebaseDB = instance;
        barcodeLauncher = activity.registerForActivityResult(new ScanContract(),
                result -> {
                    //this ActivityResultCallback lambda function handles the results of the scanning activity
                    //we check if the user scanned the special QR code that will log them in to the admin screen
                    if (Objects.equals(result.getContents(), "https://www.youtube.com/watch?v=dQw4w9WgXcQ&ab_channel=RickAstley")){
                        callback.onSpecialResult();
                        return;
                    }
                    //we check if there is a result
                    if(result.getContents() == null) {
                        callback.onNoResult(null, 2);
                        return;
                    } else {
                        //first we look to see if the qr code we just scanned is an event's promo qr code
                        if (this.firebaseDB == null){
                            this.firebaseDB = FirebaseDB.getInstance();
                        }
                        this.firebaseDB.findEventWithQR(result.getContents(), 0, new FirebaseDB.MatchingQRCallBack() {
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
                                firebaseDB.findEventWithQR(result.getContents(), 1, new FirebaseDB.MatchingQRCallBack() {
                                    @Override
                                    public void onResult(Event matchingEvent) {
                                        Log.d("findEventWithQR", "callback invoked");
                                        //we check if the user scanning the check-in qr code has signed up to the event
                                        if (matchingEvent.getSignUps().contains(user.getDocumentId())){
                                            //if so, they can check in normally
                                            callback.onCheckInResult(matchingEvent);
                                        } else{
                                            firebaseDB.userCheckedIntoEvent(user, matchingEvent, new FirebaseDB.UniqueCheckCallBack() {
                                                @Override
                                                public void onResult(boolean isUnique) {
                                                    if (isUnique) {
                                                        callback.onCheckInResult(matchingEvent);
                                                    }
                                                    else {
                                                        //otherwise, they should be brought to the event details screen, but shown a dialog saying they cannot check-in without signing up first
                                                        callback.onNoResult(matchingEvent, 5);
                                                    }
                                                }
                                            });

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
    public void launch(String userID){
        FirebaseDB.getInstance().loginUser(userID, new FirebaseDB.GetAttendeeCallBack() {
            @Override
            public void onResult(Attendee attendee) {
                setUser(attendee);
                barcodeLauncher.launch(new ScanOptions());
            }

            @Override
            public void onNoResult() {

            }
        });

    }

    /**
     * This function gets the contents of an uploaded QR code
     * @param cr The content resolver that we will use to get the bitmap of the provided uri on the user's phone.
     * @param uri The Uri of the image the user selected to upload
     * @return The content of the uploaded QR code, null if there was a error.
     */
    public String scanImage(ContentResolver cr, Uri uri){
        Bitmap bitmap;
        String contents = null;
        try{
            bitmap = MediaStore.Images.Media.getBitmap(cr, uri);
        } catch(Exception e){
            Log.d("scanImage", "failed to get image from phone");
            return null;
        }
        //we will convert the bitmap of the uploaded image to an RGB luminance source
        //this sequence was made with the help of ZXing documentation, and https://stackoverflow.com/questions/55427308/scaning-qrcode-from-image-not-from-camera-using-zxing Accessed on Mar. 5th, 2024
        //the post was amde by the user Hugo Allexis Cardona (https://stackoverflow.com/users/1797127/hugo-allexis-cardona) on the post https://stackoverflow.com/a/55427749
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        int[] pixels = new int[width * height];
        bitmap.getPixels(pixels, 0, width, 0, 0, width, height);
        RGBLuminanceSource source = new RGBLuminanceSource(width, height, pixels);
        //we create a new Binarizer that ZXing will use to convert the data from the LuminanceSource into 1D data
        HybridBinarizer binarizer = new HybridBinarizer(source);
        //we create a new BinaryBitmap, the type that a reader in ZXing can actually decode
        BinaryBitmap binaryBitmap = new BinaryBitmap(binarizer);
        try {

            Result result = reader.decode(binaryBitmap);
            contents = result.getText();

        } catch (NotFoundException e) {
            //if a user selects an image that is not qr code, it may fail to be decoded. in that case we prompt the user to select something else, or generate a qr code
            Log.d("scanImage", "failed to identify uploaded image as qr code");
            return null;
        }
        return contents;
    }

    public QRCodeScanHandler(){
    }

    public void setReader(MultiFormatReader reader) {
        this.reader = reader;
    }

    public void setFirebaseDB(FirebaseDB firebaseDB) {
        this.firebaseDB = firebaseDB;
    }

    public void setUser(Attendee attendee){
        this.user = attendee;
    }
}
