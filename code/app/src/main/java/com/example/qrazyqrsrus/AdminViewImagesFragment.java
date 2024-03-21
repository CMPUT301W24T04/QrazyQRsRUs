package com.example.qrazyqrsrus;

import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 *
 */
public class AdminViewImagesFragment extends Fragment {

    //list of poster paths
    //viewing one - get it from the firebase
    //everytime you view a new image: method to retrieve the image based on the path
    //when you delete an image: find event or attendee (we can check folder name) that has this path in their field, set to null, update event or attendee, then call delete image

    private ImageView imageView;
    private Button deleteButton;
    private Button cancelButton;
    private Button confirmButton;
    private TextView confirmTextView;
    private Button nextButton;
    private Button previousButton;



    private ArrayList<String> allImagePaths;
    private Integer currentPosition;

    public AdminViewImagesFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_admin_view_images, container, false);

        imageView = rootView.findViewById(R.id.image_view);
        FloatingActionButton backButton = rootView.findViewById(R.id.back_button);
        deleteButton = rootView.findViewById(R.id.delete_button);
        cancelButton = rootView.findViewById(R.id.cancel_button);
        confirmButton = rootView.findViewById(R.id.confirm_button);
        confirmTextView = rootView.findViewById(R.id.confirm_text_view);
        //TODO: update button colors and images with Mikael's work
        nextButton = rootView.findViewById(R.id.next_event_button);
        previousButton = rootView.findViewById(R.id.previous_event_button);

        currentPosition = 0;
        allImagePaths = new ArrayList<String>();

        //we get the initial list of all picture paths
        FirebaseDB.getAllPicturesPaths(allImagePaths, new FirebaseDB.OnFinishedCallback() {
            @Override
            public void onFinished() {
                updateView();
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{
                    Navigation.findNavController(rootView).popBackStack();
                } catch (Exception e){
                    backButton.setVisibility(View.GONE);
                }
            }
        });

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeState();
            }
        });
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeState();
            }
        });

        //we set an onclicklistener for the confirm button
        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //we will delete the currently viewed image
                FirebaseDB.deleteImageAdmin(allImagePaths.get(currentPosition), new FirebaseDB.OnFinishedCallback() {
                    @Override
                    public void onFinished() {
                        //we clear the list of image paths and get it again
                        allImagePaths.clear();
                        FirebaseDB.getAllPicturesPaths(allImagePaths, new FirebaseDB.OnFinishedCallback() {
                            //when the database finished this operation, we update what is being displayed on screen
                            @Override
                            public void onFinished() {
                                //we view the previous image unless we are viewing the first image in the list
                                if (currentPosition != 0){
                                    currentPosition -= 1;
                                }
                                updateView();
                            }
                        });
                    }
                });
                //we reset the confirm, cancel, and delete button
                changeState();
            }
        });
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentPosition != allImagePaths.size() - 1) {
                    currentPosition += 1;
                    updateView();
                }
            }
        });
        previousButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentPosition != 0) {
                    currentPosition -= 1;
                    updateView();
                }
            }
        });

        return rootView;
    }

    /**
     * This function updates the screen with the new currently viewed image
     */
    public void updateView() {
        Log.d("image test", allImagePaths.get(currentPosition));
        String currentImagePath = allImagePaths.get(currentPosition);
        FirebaseDB.adminRetrieveImage(currentImagePath, new FirebaseDB.GetBitmapCallBack() {
            @Override
            public void onResult(Bitmap bitmap) {
                imageView.setImageBitmap(bitmap);
            }
        });
    }

    /**
     * This function will change the browse screen to and from the mode where the Confirm and Cancel deletion button are shown
     */
    public void changeState() {
        deleteButton.setVisibility(deleteButton.getVisibility() == View.GONE ? View.VISIBLE : View.GONE);
        cancelButton.setVisibility(cancelButton.getVisibility() == View.GONE ? View.VISIBLE : View.GONE);
        confirmButton.setVisibility(confirmButton.getVisibility() == View.GONE ? View.VISIBLE : View.GONE);
        confirmTextView.setVisibility(confirmTextView.getVisibility() == View.GONE ? View.VISIBLE : View.GONE);
    }
}
