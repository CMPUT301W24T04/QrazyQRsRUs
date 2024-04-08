package com.example.qrazyqrsrus;
// This fragment shows all the images for the users and event posters
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

/**
 * A {@link Fragment} subclass designed for the admin panel to view and manage images.
 * It allows navigation between images, deletion of selected images, and provides
 * an interface for image management tasks within the admin panel.
 *
 * The fragment displays images one at a time with navigation options to cycle through all available
 * images. Administrators can delete images if needed. The UI updates dynamically to reflect the current
 * selection and available actions.
 */
public class AdminViewImagesFragment extends Fragment {
    private ImageView imageView;
    private Button deleteButton;
    private Button cancelButton;
    private Button confirmButton;
    private TextView confirmTextView;


    private ArrayList<String> allImagePaths;
    private Integer currentPosition;

    /**
     * Constructs a new instance of {@link AdminViewImagesFragment}.
     * This is the required empty public constructor.
     */
    public AdminViewImagesFragment() {
        // Required empty public constructor
    }

    /**
     * Called to do the initial creation of the fragment.
     * This method is where you should perform any one-time initializations.
     *
     * @param savedInstanceState If the fragment is being re-created from a previous saved state, this
     *                           is the state. This value may be null.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /**
     * Called to have the fragment instantiate its user interface view.
     * This method inflates the layout for the fragment and initializes the UI components.
     *
     * @param inflater           The LayoutInflater object that can be used to inflate any views in the fragment.
     * @param container          The parent view that the fragment's UI should be attached to.
     * @param savedInstanceState If non-null, this fragment is being re-constructed from a previous saved state.
     * @return The View for the fragment's UI, or null.
     */
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
        Button nextButton = rootView.findViewById(R.id.next_event_button);
        Button previousButton = rootView.findViewById(R.id.previous_event_button);

        currentPosition = 0;
        allImagePaths = new ArrayList<>();

        //we get the initial list of all picture paths
        FirebaseDB.getInstance().getAllPicturesPaths(allImagePaths, () -> updateView());

        backButton.setOnClickListener(view -> {
            try{
                Navigation.findNavController(rootView).popBackStack();
            } catch (Exception e){
                backButton.setVisibility(View.GONE);
            }
        });

        deleteButton.setOnClickListener(v -> changeState());
        cancelButton.setOnClickListener(v -> changeState());

        //we set an onclicklistener for the confirm button
        confirmButton.setOnClickListener(v -> {
            //we will delete the currently viewed image
            FirebaseDB.getInstance().deleteImageAdmin(allImagePaths.get(currentPosition), () -> {
                //we clear the list of image paths and get it again
                allImagePaths.clear();
                //when the database finished this operation, we update what is being displayed on screen
                FirebaseDB.getInstance().getAllPicturesPaths(allImagePaths, () -> {
                    //we view the previous image unless we are viewing the first image in the list
                    if (currentPosition != 0){
                        currentPosition -= 1;
                    }
                    updateView();
                });
            });
            //we reset the confirm, cancel, and delete button
            changeState();
        });
        nextButton.setOnClickListener(v -> {
            if (currentPosition != allImagePaths.size() - 1) {
                currentPosition += 1;
                updateView();
            }
        });
        previousButton.setOnClickListener(v -> {
            if (currentPosition != 0) {
                currentPosition -= 1;
                updateView();
            }
        });

        return rootView;
    }

    /**
     * Updates the display with the currently selected image.
     * It retrieves the image from a list of image paths and sets it to the ImageView.
     */
    public void updateView() {
        Log.d("image test", allImagePaths.get(currentPosition));
        String currentImagePath = allImagePaths.get(currentPosition);
        FirebaseDB.getInstance().adminRetrieveImage(currentImagePath, bitmap -> imageView.setImageBitmap(bitmap));
    }

    /**
     * Toggles the visibility of the confirm, cancel, and delete buttons.
     * This function changes the UI to a state that requires confirmation for image deletion,
     * offering an additional step to prevent accidental deletions.
     */
    public void changeState() {
        deleteButton.setVisibility(deleteButton.getVisibility() == View.GONE ? View.VISIBLE : View.GONE);
        cancelButton.setVisibility(cancelButton.getVisibility() == View.GONE ? View.VISIBLE : View.GONE);
        confirmButton.setVisibility(confirmButton.getVisibility() == View.GONE ? View.VISIBLE : View.GONE);
        confirmTextView.setVisibility(confirmTextView.getVisibility() == View.GONE ? View.VISIBLE : View.GONE);
    }
}
