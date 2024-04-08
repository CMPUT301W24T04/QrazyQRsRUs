package com.example.qrazyqrsrus;

// This class generates new QR codes in the event creation sequence
import android.graphics.Bitmap;

import com.google.zxing.BarcodeFormat;
import com.journeyapps.barcodescanner.BarcodeEncoder;

/**
 * Generates a QR code for an event
 */
public class QRCodeGenerator {

    private FirebaseDB firebaseDB;

    private BarcodeEncoder barcodeEncoder = new BarcodeEncoder();

    private static final Bitmap bitmap = null;

    public interface UniqueQRCheckCallBack {
        void onUnique();
        void onNotUnique();
    }

    /**
     * This function will check if a qr code is already in use
     * @param content The content field of the QR code to be generated
     * @param mode The kind of qr code we are checking. 0 - promo   1 - checkin
     * @param callback The UniqueQRCheckCallBack that will handle the two cases: the qr code we are generating is unique (no problem), or the qr code we are generating is already in use (problem)
     */
    public void checkUnique (String content, int mode, UniqueQRCheckCallBack callback){
        if (firebaseDB == null){
            firebaseDB = FirebaseDB.getInstance();
        }
        firebaseDB.checkUnique(content, mode, isUnique -> {
            //if the qr code is not unique,
            if (!isUnique) {
                callback.onNotUnique();
            } else{
                callback.onUnique();
            }
        });
    }

    /**
     * This function will create an image bitmap out of a provided content field
     * @param content The content field of the QR code to be generated
     * @return The bitmap of the QR code image that encodes the content parameter. Null if there was an error generating a QR code.
     */
    public Bitmap generateBitmap(String content){
        Bitmap qrBitmap = null;

        //we try to generate a bitmap that encodes the content field, and catch a possible exception
        try {
            qrBitmap = barcodeEncoder.encodeBitmap(content, BarcodeFormat.QR_CODE, 400, 400);
        } catch(Exception e) {
            //it would be unexpected that qr generation fails, but for now we will display error bar and prompt user to try again

        }
        //then we check if the content field is already in use
        return qrBitmap;
    }

    public QRCodeGenerator(){
    }

    public void setFirebaseDB(FirebaseDB firebaseDB) {
        this.firebaseDB = firebaseDB;
    }

    public void setBarcodeEncoder(BarcodeEncoder barcodeEncoder) {
        this.barcodeEncoder = barcodeEncoder;
    }
}
