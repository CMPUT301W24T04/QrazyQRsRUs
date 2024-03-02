package com.example.qrazyqrsrus;

import static android.graphics.ImageDecoder.createSource;
import static android.graphics.ImageDecoder.decodeBitmap;
import static android.graphics.ImageDecoder.decodeDrawable;
import static android.os.Environment.getExternalStoragePublicDirectory;

import android.graphics.Bitmap;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.PickVisualMediaRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class NewEventImageFragment extends Fragment implements Toolbar.OnMenuItemClickListener, ImageDecoder.OnHeaderDecodedListener {

    private ImageView imageView;
    private Toolbar toolbar;
    public static NewEventImageFragment newInstance(String param1, String param2) {
        NewEventImageFragment fragment = new NewEventImageFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
        //alternative method, giving toolbar to activity rather than fragment
//        toolbar = (Toolbar) getActivity().findViewById(R.id.image_screen_toolbar);
        //we must cast the fragment's activity to an AppCompatActivity in order to invoke setSupportActionBar
        //this idea was found from https://stackoverflow.com/questions/29020935/using-toolbar-with-fragments on February 21st, 2024
        //it was posted by user vinitius (https://stackoverflow.com/users/3112331/vinitius) in the reply https://stackoverflow.com/a/29021287
//        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.new_event_image_fragment, container, false);
        createToolbar(view);
        //temporarily display event name here
        ((TextView) view.findViewById(R.id.new_event_image_text)).setText(((Event) getArguments().getSerializable("event")).getEventName());
        FloatingActionButton fab = view.findViewById(R.id.image_screen_next_screen_button);
        //pass bundle ahead to qr code
        fab.setOnClickListener(v -> {
            Navigation.findNavController(view).navigate(R.id.action_newEventImageFragment_to_newEventQrFragment, getArguments());
        });

        //we make a new activity with the uri result that will provide us with the uri of the uploaded image on the user's system locally
        //https://developer.android.com/training/data-storage/shared/photopicker on February 23rd, 2024
        // Registers a photo picker activity launcher in single-select mode.
        ActivityResultLauncher<PickVisualMediaRequest> pickMedia =
                registerForActivityResult(new ActivityResultContracts.PickVisualMedia(), uri -> {
                    // Callback is invoked after the user selects a media item or closes the
                    // photo picker.
                    if (uri != null) {
//                        Log.d("PhotoPicker", "Selected URI: " + uri);
                        //as long as the user selected an image, we invoke our function to update the imageView to display the uploaded poster, and save the event's poster
                        imageView = (ImageView) view.findViewById(R.id.new_event_display_event_poster);
                        updateImage(uri);
                    } else {
//                        Log.d("PhotoPicker", "No media selected");
                    }
                });

        //we set an onclick listener for the upload image button
        //upon clicking, we launch the pickVisualMediaRequest acitivity
        Button button = view.findViewById(R.id.new_event_image_button);
        button.setOnClickListener(v -> {
            pickMedia.launch(new PickVisualMediaRequest.Builder()
                    //we only want images
                    .setMediaType(ActivityResultContracts.PickVisualMedia.ImageOnly.INSTANCE)
                    .build());
        });

        return view;
    }

    private void createToolbar(View view){
        //once we have made the view, we create the toolbar and inflate it's menu, in order to set and onclicklistener from the fragment
        //the idea to access the toolbar by using the Fragment's host View was taken from https://stackoverflow.com/questions/29020935/using-toolbar-with-fragments on February 21st, 2024
        //it was posted by the user Faisal Naseer (https://stackoverflow.com/users/2641848/faisal-naseer) in the post https://stackoverflow.com/a/45653449
        toolbar = (Toolbar) view.findViewById(R.id.image_screen_toolbar);
        toolbar.inflateMenu(R.menu.menu_with_back_button);
        //the fragment implements the Toolbar.OnMenuItemClick interface, pass itself.
        toolbar.setOnMenuItemClickListener(this);
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        int id = item.getItemId();
        //we check which item id was clicked on, navigating accordingly
        if (id == R.id.back_button){
            //go to previous screen
            Navigation.findNavController(getView()).navigate(R.id.action_newEventImageFragment_to_newEventTextFragment);
            return true;
        }
        else if (id == R.id.cancel_button){
            //leave entire new event sequence
            Navigation.findNavController(getView()).navigate(R.id.action_newEventImageFragment_to_newEventFragment);
            return true;
        }
        return false;
    }

    private void updateImage(Uri uri){
        //we get the bitmap from the uri that is returned by the imagePicker activity
        //we upload this bitmap to the database, and display it on-screen
        ImageDecoder.Source imageSource;
        try {
            //this requires API 28; if this is a problem, we will have to use a bitmap
            //if we use a Source instead of a bitmap, it allows us to use the same source to display the photo in multiple sizes/orientations
            imageSource = createSource(getContext().getContentResolver(), uri);
            imageView.setImageBitmap(decodeBitmap(imageSource, this));
            //deprecated, see https://developer.android.com/reference/android/provider/MediaStore.Images.Media#getBitmap(android.content.ContentResolver,%20android.net.Uri)
//            bitmap = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), uri);
//            imageView.setImageBitmap(bitmap);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onHeaderDecoded(@NonNull ImageDecoder decoder, @NonNull ImageDecoder.ImageInfo info, @NonNull ImageDecoder.Source source) {
        decoder.setTargetSize(800, 400);


    }
}
