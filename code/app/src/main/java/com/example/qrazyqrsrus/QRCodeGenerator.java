package com.example.qrazyqrsrus;

import android.app.Activity;
import android.graphics.Bitmap;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.Result;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.text.SimpleDateFormat;
import java.util.Date;

public class QRCodeGenerator {

    private static Bitmap bitmap = null;

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
    public static void checkUnique (String content, int mode, UniqueQRCheckCallBack callback){
        FirebaseDB.checkUnique(content, mode, new FirebaseDB.UniqueCheckCallBack() {
            @Override
            public void onResult(boolean isUnique) {
                //if the qr code is not unique,
                if (!isUnique) {
                    callback.onNotUnique();
                } else{
                    callback.onUnique();
                }
            }
        });
    }

    /**
     * This function will create an image bitmap out of a provided content field
     * @param content The content field of the QR code to be generated
     * @param activity The activity that we will create the error dialog if something goes wrong
     * @return The bitmap of the QR code image that encodes the content parameter.
     */
    public static Bitmap generateBitmap(String content, FragmentActivity activity){
        Bitmap qrBitmap = null;
        BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
        //we try to generate a bitmap that encodes the content field, and catch a possible exception
        try {
            qrBitmap = barcodeEncoder.encodeBitmap(content, BarcodeFormat.QR_CODE, 400, 400);
        } catch(Exception e) {
            //it would be unexpected that qr generation fails, but for now we will display error bar and prompt user to try again
            new ErrorDialog(R.string.qr_generation_failed).show(activity.getSupportFragmentManager(), "Error Dialog");
        }
        //then we check if the content field is already in use
        return qrBitmap;
    }
}
