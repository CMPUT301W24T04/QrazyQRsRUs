package com.example.qrazyqrsrus;

import android.graphics.Bitmap;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.PickVisualMediaRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.NotFoundException;
import com.google.zxing.RGBLuminanceSource;
import com.google.zxing.Result;
import com.google.zxing.common.HybridBinarizer;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class NewEventQrFragment extends Fragment {
    public static NewEventQrFragment newInstance(String param1, String param2) {
        NewEventQrFragment fragment = new NewEventQrFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.new_event_qr_fragment, container, false);

        //we define the activity to launch where the user will select a qr code from their gallery
        ActivityResultLauncher<PickVisualMediaRequest> pickMedia =
                registerForActivityResult(new ActivityResultContracts.PickVisualMedia(), uri -> {
                    // Callback is invoked after the user selects a media item or closes the
                    // photo picker.
                    if (uri != null) {
//                        Log.d("PhotoPicker", "Selected URI: " + uri);
                        //as long as the user selected an image, we invoke our function to update the imageView to display the uploaded poster, and save the event's poster
                        //imageView = (ImageView) view.findViewById(R.id.new_event_display_qr_code);
                        imageUploaded(uri);
                    } else {
//                        Log.d("PhotoPicker", "No media selected");
                    }
                });

        Button button = view.findViewById(R.id.new_event_generate_qr_button);
        button.setOnClickListener(v -> {
            generateNewQR();
        });
        Button uploadQrButton = view.findViewById(R.id.new_event_upload_qr_button);
        uploadQrButton.setOnClickListener(v -> {
            uploadQr(pickMedia);
        });
        FloatingActionButton fab = view.findViewById(R.id.qr_screen_finish_button);
        fab.setOnClickListener(v -> {
            //TODO: store event to firebase before navigating back
            Navigation.findNavController(view).navigate(R.id.action_newEventQrFragment_to_newEventFragment, getArguments());
        });
        return view;
    }

    private void generateNewQR(){
        try {
            //we generate a timestamp that contains the date and time the qr was generated. this allows us to prevent naming our qrcode as something already saved in the database
            //this idea for safe name generation is from https://developer.android.com/media/camera/camera-deprecated/photobasics accessed on Feb. 24, 2024
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
            //content is a string that should tie the qr code to the event.
            //when we scan the qr code, we can easily get content, and navigate an event details screen that displays the corresponding event
            Bitmap bitmap = barcodeEncoder.encodeBitmap(((Event) (getArguments().getSerializable("event"))).getEventName() + "_" + timeStamp, BarcodeFormat.QR_CODE, 400, 400);
            //getView() might be null here?
            ImageView imageViewQrCode = (ImageView) getView().findViewById(R.id.new_event_display_qr_code);
            imageViewQrCode.setImageBitmap(bitmap);
            saveImage(bitmap);
        } catch(Exception e) {

        }
    }

    //this function generates a qr code from the user's uploaded qr code after scanning it to verify it's contents
    private void generateNewQR(Result result){
        try {

            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
            //content is a string that should tie the qr code to the event.
            //when we scan the qr code, we can easily get content, and navigate an event details screen that displays the corresponding event
            Bitmap bitmap = barcodeEncoder.encodeBitmap(result.getText(), BarcodeFormat.QR_CODE, 400, 400);
            //getView() might be null here?
            ImageView imageViewQrCode = (ImageView) getView().findViewById(R.id.new_event_display_qr_code);
            imageViewQrCode.setImageBitmap(bitmap);
            saveImage(bitmap);
        } catch(Exception e) {
            //it would be unexpected that qr generation fails
        }
    }

    //we save the image upon button press
    private void saveImage(Bitmap bitmap){
        //we generate a timestamp that contains the date and time the image was saved. this allows us to prevent naming our file as something already saved in the phone's gallery
        //this idea for safe filename generation is from https://developer.android.com/media/camera/camera-deprecated/photobasics accessed on Feb. 24, 2024
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        //we save the image using MediaStore
        MediaStore.Images.Media.insertImage(getContext().getContentResolver(), bitmap, imageFileName, "should be qr code");
    }

    //this function launches the activity to upload a QR code
    private void uploadQr(ActivityResultLauncher<PickVisualMediaRequest> pickMedia){
        pickMedia.launch(new PickVisualMediaRequest.Builder()
                //we only want images
                .setMediaType(ActivityResultContracts.PickVisualMedia.ImageOnly.INSTANCE)
                .build());
    }

    //this function gets the image uploaded by the user, and scans it using ZXing to verify they uploaded a QR code
    private void imageUploaded(Uri uri){
        //we get the bitmap from the uri that is returned by the imagePicker activity
        //we upload this bitmap to the database, and display it on-screen
        ImageDecoder.Source imageSource;
        Bitmap bitmap = null;
        try {
            //this requires API 28; if this is a problem, we will have to use a bitmap
            //if we use a Source instead of a bitmap, it allows us to use the same source to display the photo in multiple sizes/orientations
            //imageSource = createSource(getContext().getContentResolver(), uri);
            //imageView.setImageBitmap(decodeBitmap(imageSource, this));
            //deprecated, see https://developer.android.com/reference/android/provider/MediaStore.Images.Media#getBitmap(android.content.ContentResolver,%20android.net.Uri)
            //we try to get the bitmap of the image uploaded by the user
            bitmap = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), uri);
        } catch (IOException e) {
            throw new RuntimeException(e);
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
        //finally, we create a MultiFormatReader, that attempts to find any kind of barcode from an image
        MultiFormatReader reader = new MultiFormatReader();
        try {

            Result result = reader.decode(binaryBitmap);
            generateNewQR(result);

        } catch (NotFoundException e) {
            //TODO: handle case when user selects an image that is not a QR code
            throw new RuntimeException(e);
        }
    }

    /**\
     * Return a boolean that indicates whether or not the content of a QR code is already in use by
     * an event created in the app.
     * @param qrContent     a string that represents the content field of the QR code we are checking
     * @return              a boolean value, true of the QR code is not already in use, false otherwise
     */
    private boolean checkUnique(String qrContent){
        boolean isUnique = false;
        //FirebaseFirestore db = FirebaseFirestore.getInstance();
        //CollectionReference events = db.collection("Events");
        //events
        //      .whereEqualTo("promo_qr_code", qrContent)
        //      .get()
        //      .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
        //            @Override
        //            public void onComplete(@NonNull Task<QuerySnapshot> task) {
        //                if (task.isSuccessful()) {
        //                    int i = 0;
        //                    for (DocumentSnapshot documentSnapshot: task.getResult()) {
        //                        i += 1;
        //                    }
        //                    if (i == 0) {
        //                        // This means that no Event in the database has the same promo qr code as the qr code we have selected
        //                        isUnique = true;
        //                    }
        //                }
        //                else {
        //                    Log.e("MainActivity", "Error checking existing Event QR codes");
        //                }
        //            }
        //        })
        return isUnique;
    }

}