package com.example.qrazyqrsrus;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.google.zxing.BarcodeFormat;
import com.journeyapps.barcodescanner.BarcodeEncoder;

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
            Bitmap bitmap = barcodeEncoder.encodeBitmap("content", BarcodeFormat.QR_CODE, 400, 400);
            //getView() might be null here?
            ImageView imageViewQrCode = (ImageView) getView().findViewById(R.id.new_event_display_qr_code);
            imageViewQrCode.setImageBitmap(bitmap);
        } catch(Exception e) {

        }
    }

}
