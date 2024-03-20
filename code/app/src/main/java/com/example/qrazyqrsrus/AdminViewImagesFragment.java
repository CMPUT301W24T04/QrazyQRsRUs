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
        //replace with a array list of string that will hold all paths
        //TODO: add callback to this firebase function
        FirebaseDB.getPostersPaths(allImagePaths, new FirebaseDB.OnFinishedCallback() {
            @Override
            public void onFinished() {
                updateView();
            }
        });
        //TODO: do the same thing with profile picture paths
        //updateView();

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
        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO: make delete images, and clear fields of document that had that image
//                Log.d("test", allImagePaths.get(currentPosition).substring(1, allImagePaths.get(currentPosition).length() - 4));
//                Log.d("test", allImagePaths.get(currentPosition).substring(1, 6));
                FirebaseDB.deleteImageAdmin(allImagePaths.get(currentPosition), new FirebaseDB.OnFinishedCallback() {
                    @Override
                    public void onFinished() {
                        allImagePaths.clear();
                        //TODO: addd callback and do the same with profile pictures
                        FirebaseDB.getPostersPaths(allImagePaths, new FirebaseDB.OnFinishedCallback() {
                            @Override
                            public void onFinished() {
                                if (currentPosition != 0){
                                    currentPosition -= 1;
                                }
                                updateView();
                            }
                        });
                    }
                });

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



        // Inflate the layout for this fragment
        return rootView;
    }

    public void updateView() {
        String currentImagePath = allImagePaths.get(currentPosition);
        FirebaseDB.adminRetrieveImage(currentImagePath, new FirebaseDB.GetBitmapCallBack() {
            @Override
            public void onResult(Bitmap bitmap) {
                imageView.setImageBitmap(bitmap);
            }
        });
//            String nameString = "Name: "+currentEvent.getName();
//            String organizerString = "Organized by: ";
//            String locationString = "Location: "+currentEvent.getLocation();
//            String descriptionString = "Description: "+currentEvent.getDetails();
//            String startDateString = "Starts: "+currentEvent.getStartDate();
//            String endDateString = "Ends: "+currentEvent.getEndDate();
//            FirebaseDB.getUserName(currentEvent.getOrganizerId(), new FirebaseDB.GetStringCallBack() {
//                @Override
//                public void onResult(String string) {
//                    organizerView.setText(organizerString + string);
//                }
//            });

//            if (currentEvent.getPosterPath() != null) {
//                FirebaseDB.retrieveImage(currentEvent, new FirebaseDB.GetBitmapCallBack() {
//                    @Override
//                    public void onResult(Bitmap bitmap) {
//                        posterView.setImageBitmap(bitmap);
//                    }
//                });
//            }
//            else {
//                posterView.setImageResource(R.drawable.no_image_source);
//            }
    }

    public void changeState() {
        deleteButton.setVisibility(deleteButton.getVisibility() == View.GONE ? View.VISIBLE : View.GONE);
        cancelButton.setVisibility(cancelButton.getVisibility() == View.GONE ? View.VISIBLE : View.GONE);
        confirmButton.setVisibility(confirmButton.getVisibility() == View.GONE ? View.VISIBLE : View.GONE);
        confirmTextView.setVisibility(confirmTextView.getVisibility() == View.GONE ? View.VISIBLE : View.GONE);
    }
}
