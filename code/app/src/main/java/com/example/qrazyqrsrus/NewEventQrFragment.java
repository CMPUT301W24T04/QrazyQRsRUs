package com.example.qrazyqrsrus;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.google.zxing.BarcodeFormat;
import com.journeyapps.barcodescanner.BarcodeEncoder;

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
        Button button = view.findViewById(R.id.new_event_generate_qr_button);
        button.setOnClickListener(v -> {
            generateNewQR();
        });
        return view;
    }

    private void generateNewQR(){
        try {
            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
            //content is a string that should tie the qr code to the event.
            //when we scan the qr code, we can easily get content, and navigate an event details screen that displays the corresponding event
            Bitmap bitmap = barcodeEncoder.encodeBitmap(((Event) (getArguments().getSerializable("event"))).getName(), BarcodeFormat.QR_CODE, 400, 400);
            //getView() might be null here?
            ImageView imageViewQrCode = (ImageView) getView().findViewById(R.id.new_event_display_qr_code);
            imageViewQrCode.setImageBitmap(bitmap);
            saveImage(bitmap);
        } catch(Exception e) {

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

}
