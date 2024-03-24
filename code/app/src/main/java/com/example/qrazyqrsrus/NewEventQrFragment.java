//this fragment is the sixth fragment in the event creation sequence. it allows users to generate or upload a checkin qr code for their event.
package com.example.qrazyqrsrus;


import android.graphics.Bitmap;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

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
import com.google.zxing.qrcode.encoder.QRCode;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * Creates a new QR for the fragment
 */
public class NewEventQrFragment extends Fragment {

    private String checkInQRContent;
    public static NewEventQrFragment newInstance(String param1, String param2) {
        NewEventQrFragment fragment = new NewEventQrFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * creates view
     * @param savedInstanceState If the fragment is being re-created from
     * a previous saved state, this is the state.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }
    /**
     *
     * @param inflater The LayoutInflater object that can be used to inflate
     * any views in the fragment,
     * @param container If non-null, this is the parent view that the fragment's
     * UI should be attached to.  The fragment should not add the view itself,
     * but this can be used to generate the LayoutParams of the view.
     * @param savedInstanceState If non-null, this fragment is being re-constructed
     * from a previous saved state as given here.
     *
     * @return attendeeInfoLayout
     */
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
            tryGenerateNewQR();
        });
        Button uploadQrButton = view.findViewById(R.id.new_event_upload_qr_button);
        uploadQrButton.setOnClickListener(v -> {
            uploadQr(pickMedia);
        });
        FloatingActionButton fab = view.findViewById(R.id.qr_screen_finish_button);
        fab.setOnClickListener(v -> {
            //TODO: store event to firebase before navigating back
            Bundle args = makeNewBundle(getArguments());
//            Event event = modifyEvent(this.checkInQRContent, (Event) args.getSerializable("event"));
//            args.putSerializable("event", event);
            String name = (String) args.getSerializable("name");
            String organizerId = (String) args.getSerializable("organizerId");
            String location = (String) args.getSerializable("location");
            String details = (String) args.getSerializable("details");
            Integer maxAttendees = (Integer) args.getSerializable("max_attendees");
            LocalDateTime startDate = (LocalDateTime) args.getSerializable("startDate");
            LocalDateTime endDate = (LocalDateTime) args.getSerializable("endDate");
            String posterPath = (String) args.getSerializable("posterPath");
            Uri uri = (Uri) args.getParcelable("uri");
            FirebaseDB.uploadImage(uri, posterPath);
            String qrCodePromo = (String) args.getSerializable("qrCodePromo");
            String qrCode = (String) args.getSerializable("qrCode");
            FirebaseDB.uploadImage(uri, posterPath);

            Event event = new Event(name, organizerId, details, location, startDate, endDate, maxAttendees);
            event.setPosterPath(posterPath);
            event.setQrCodePromo(qrCodePromo);
            event.setQrCode(qrCode);
            if (maxAttendees != null){
                event.setAttendeeCount(0);
            }
            FirebaseDB.addEvent(event);
            Navigation.findNavController(view).navigate(R.id.action_newEventQrFragment_to_eventList2, args);
        });
        return view;
    }

    /**
     * This function will try to generate a new QR code, handling the case that the generated qr code is already being used by an event in the database
     *
     */
    private void tryGenerateNewQR(){
        //we generate a timestamp to append to the name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String qrContent = ((String) (getArguments().getSerializable("name"))) + "_" + timeStamp + "_checkin";
        //we check if our qr code is unique
        QRCodeGenerator.checkUnique(qrContent, 1, new QRCodeGenerator.UniqueQRCheckCallBack() {
            @Override
            public void onUnique() {
                generateBitmap(qrContent);
            }

            @Override
            public void onNotUnique() {
                new ErrorDialog(R.string.qr_not_unique).show(getActivity().getSupportFragmentManager(), "Error Dialog");
            }
        });
    }

    /**
     * This function will try to use a user uploaded QR code, handling the case that the uploaded qr code is already being used by an event in the database
     * @param content The content of the uploaded image, that will be checked to see if it is already in use.
     */
    private void tryGenerateNewQR(String content){
        String qrContent = content;
        //we check if our qr code is unique
        QRCodeGenerator.checkUnique(qrContent, 1, new QRCodeGenerator.UniqueQRCheckCallBack() {
            @Override
            public void onUnique() {
                generateBitmap(qrContent);
            }

            @Override
            public void onNotUnique() {
                new ErrorDialog(R.string.qr_not_unique).show(getActivity().getSupportFragmentManager(), "Error Dialog");
            }
        });
    }

    /**
     * This function will try to generate an image bitmap of a qr code that encodes the provided content. It will update the ImageView on screen and set the checkInQRContent to use to build the Event object
     * @param content
     */
    private void generateBitmap(String content){
        Bitmap bitmap = QRCodeGenerator.generateBitmap(content, getActivity());
        if (bitmap != null){
            ((ImageView) getView().findViewById(R.id.new_event_display_qr_code)).setImageBitmap(bitmap);
            checkInQRContent = content;
            saveImage(bitmap);
        } else{
            Log.d("generateBitmap", "error generating the bitmap");
            new ErrorDialog(R.string.qr_generation_failed).show(getActivity().getSupportFragmentManager(), "Error Dialog");
        }
    }

    /**
     * This function saves an image of the QR code they have generated/uploaded to their device.
     * @param bitmap The bitmap of the image to be saved
     */
    private void saveImage(Bitmap bitmap){
        //we generate a timestamp that contains the date and time the image was saved. this allows us to prevent naming our file as something already saved in the phone's gallery
        //this idea for safe filename generation is from https://developer.android.com/media/camera/camera-deprecated/photobasics accessed on Feb. 24, 2024
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        //we save the image using MediaStore
        MediaStore.Images.Media.insertImage(getContext().getContentResolver(), bitmap, imageFileName, "should be qr code");
    }

    /**
     * This function launches the activity for a user to upload their own QR code
     * @param pickMedia The activity to be launched
     */
    private void uploadQr(ActivityResultLauncher<PickVisualMediaRequest> pickMedia){
        pickMedia.launch(new PickVisualMediaRequest.Builder()
                //we only want images
                .setMediaType(ActivityResultContracts.PickVisualMedia.ImageOnly.INSTANCE)
                .build());
    }

    /**
     * This function handles the checking of the uploaded image, making sure that it can be recognized as a qr code.
     * @param uri The uri of the image uploaded
     */
    private void imageUploaded(Uri uri){
        //we call the scanImage function to get the content of the uploaded image
        String content = QRCodeScanHandler.scanImage(getContext().getContentResolver(), uri);
        if (content == null){
            //if the uploaded image failed to be recognized as a qr code
            new ErrorDialog(R.string.qr_generation_failed).show(getActivity().getSupportFragmentManager(), "Error Dialog");
        } else{
            tryGenerateNewQR(content);
        }
    }

    /**
     * This function makes a bundle to pass to the next fragment, allowing it to access the data input by the user while making a new event
     * @param bundle The bundle that holds the event fields so far
     * @return The updated bundle.
     */
    private Bundle makeNewBundle(Bundle bundle){
        bundle.putSerializable("qrCode", this.checkInQRContent);
        return bundle;
    }

}
