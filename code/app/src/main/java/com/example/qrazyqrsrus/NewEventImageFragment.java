//this fragment is the fourth fragment in the event creation sequence. it allows users to upload an image.

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
import android.widget.DatePicker;
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

/**
 * allows the user to choose the image of the event
 */
public class NewEventImageFragment extends Fragment implements Toolbar.OnMenuItemClickListener, ImageDecoder.OnHeaderDecodedListener {

    private ImageView imageView;
    private Toolbar toolbar;

    private Uri uri;
    private String initialPath;

    private String path;

    private FirebaseDB firebaseDB;

    /**
     * save the image as an instance
     * @param param1
     * @param param2
     * @return fragment
     */
    public static NewEventImageFragment newInstance(String param1, String param2) {
        NewEventImageFragment fragment = new NewEventImageFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * creates the view
     * @param savedInstanceState If the fragment is being re-created from
     * a previous saved state, this is the state.
     */
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

    /**
     * when view is creates, show the image selection screen
     * @param inflater The LayoutInflater object that can be used to inflate
     * any views in the fragment,
     * @param container If non-null, this is the parent view that the fragment's
     * UI should be attached to.  The fragment should not add the view itself,
     * but this can be used to generate the LayoutParams of the view.
     * @param savedInstanceState If non-null, this fragment is being re-constructed
     * from a previous saved state as given here.
     *
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.new_event_image_fragment, container, false);
        Bundle bundle = getArguments();
        handleArguments(bundle, view);
        createToolbar(view);
        //temporarily display event name here
//        ((TextView) view.findViewById(R.id.new_event_image_text)).setText(((Event) getArguments().getSerializable("event")).getName());
        FloatingActionButton fab = view.findViewById(R.id.image_screen_next_screen_button);
        //pass bundle ahead to qr code
        fab.setOnClickListener(v -> {
            Bundle args = makeNewBundle(getArguments());
            //Event event = saveImageToFirebase(this.uri, ((Event) args.getSerializable("event")));
            //args.putSerializable("event", event);
            Navigation.findNavController(view).navigate(R.id.action_newEventImageFragment_to_newEventQrFragment, args);
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
                        this.uri = uri;
                        this.path = generateUniquePathName(((Event.EventBuilder) bundle.getSerializable("builder")).getName());
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

        if (this.firebaseDB == null){
            setFirebaseDB(FirebaseDB.getInstance());
        }



        return view;
    }

    /**
     * Initializes and sets up the toolbar for the fragment. This method inflates the toolbar menu and sets a listener for menu item clicks.
     * The toolbar provides navigation options such as going back to the previous screen or canceling the event creation process.
     *
     * @param view The root view of the fragment where the toolbar is located.
     */
    private void createToolbar(View view){
        //once we have made the view, we create the toolbar and inflate it's menu, in order to set and onclicklistener from the fragment
        //the idea to access the toolbar by using the Fragment's host View was taken from https://stackoverflow.com/questions/29020935/using-toolbar-with-fragments on February 21st, 2024
        //it was posted by the user Faisal Naseer (https://stackoverflow.com/users/2641848/faisal-naseer) in the post https://stackoverflow.com/a/45653449
        toolbar = (Toolbar) view.findViewById(R.id.image_screen_toolbar);
        toolbar.inflateMenu(R.menu.menu_with_back_button);
        //the fragment implements the Toolbar.OnMenuItemClick interface, pass itself.
        toolbar.setOnMenuItemClickListener(this);
    }

    /**
     * Handles menu item clicks in the toolbar. Depending on the item clicked, this method navigates to the previous fragment in the event creation sequence,
     * or exits the event creation flow altogether.
     *
     * @param item The menu item that was clicked.
     * @return true if the menu item click was handled, false otherwise.
     */
    @Override
    public boolean onMenuItemClick(MenuItem item) {
        int id = item.getItemId();
        //we check which item id was clicked on, navigating accordingly
        if (id == R.id.back_button){
            //go to previous screen
            Bundle args = getArguments();
            args = makeNewBundle(args);
            Navigation.findNavController(getView()).navigate(R.id.action_newEventImageFragment_to_newEventTextFragment, args);
            return true;
        }
        else if (id == R.id.cancel_button){
            //leave entire new event sequence
            Navigation.findNavController(getView()).navigate(R.id.action_newEventImageFragment_to_newEventFragment);
            return true;
        }
        return false;
    }

    /**
     * Updates the ImageView with the image selected by the user. This method retrieves a bitmap image from the provided URI
     * and sets it as the content of the ImageView designated for displaying the event's poster.
     *
     * @param uri The URI of the image selected by the user, typically obtained through an image picker.
     */
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

    /**
     * Callback method invoked by {@link ImageDecoder} when the header of an image has been decoded, but before the entire image is decoded.
     * This method allows for adjusting the decoding process, such as setting the target size of the decoded image.
     *
     * @param decoder The ImageDecoder that is decoding the image.
     * @param info Information about the image being decoded.
     * @param source The source from which the image is being decoded.
     */
    @Override
    public void onHeaderDecoded(@NonNull ImageDecoder decoder, @NonNull ImageDecoder.ImageInfo info, @NonNull ImageDecoder.Source source) {
        decoder.setTargetSize(800, 400);


    }

//    private Event saveImageToFirebase(Uri uri, Event event){
//        String pathname = generateUniquePathName(event);
//        event.setPosterPath(pathname);
//        FirebaseDB.getInstance().uploadImage(uri, pathname);
//        return event;
//    }

    /**
     * Generates a unique file path name for storing the event's poster image. This method constructs a path name using the event name and a timestamp
     * to ensure uniqueness, avoiding conflicts with existing files in the storage location.
     *
     * @param name The name of the event, used as part of the path name to provide context.
     * @return A unique string path name for storing the image file.
     */
    private String generateUniquePathName(String name){
        //we generate a timestamp that contains the date and time the qr was generated. this allows us to prevent naming our qrcode as something already saved in the database
        //this idea for safe name generation is from https://developer.android.com/media/camera/camera-deprecated/photobasics accessed on Feb. 24, 2024
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String pathName = "poster/" + name + timeStamp;
        return pathName;
    }

    /**
     * Prepares a new Bundle with updated event creation details, including the URI of the uploaded image if available.
     * This method is called before navigating to the next fragment in the event creation process to ensure the state is passed forward.
     *
     * @param bundle The current Bundle containing the event's details.
     * @return The updated Bundle with the newly added or modified image URI and path.
     */
    private Bundle makeNewBundle(Bundle bundle){
        Event.EventBuilder builder = (Event.EventBuilder) bundle.getSerializable("builder");
        if (this.uri == null){
            //if this.uri is null, the user either 1) did not upload an image or 2) had uploaded an image before, but returned to this screen and deleted the image
            if (this.initialPath != null){
                FirebaseDB.getInstance().deleteImage(this.initialPath);
                builder.setUri(null);
                builder.setPosterPath(null);
            }
            return bundle;
        } else{
            //if this.uri is not null, the user either 1) uploaded an image for the first time or changed the image or 2) did not change the image they initially uploaded
            if (this.initialPath == null || this.initialPath != this.path){
                if (this.initialPath != null){
                    FirebaseDB.getInstance().deleteImage(this.initialPath);
                }
                FirebaseDB.getInstance().uploadImage(this.uri, this.path);
                builder.setUri(this.uri);
                builder.setPosterPath(this.path);
            }

        }

        bundle.putSerializable("builder", builder);
        return bundle;
    }

    /**
     * Handles the arguments passed to the fragment, specifically looking for an image URI if one has been previously selected.
     * If an image has already been chosen during the event creation process, this method retrieves it and updates the ImageView to display it.
     *
     * @param args The Bundle containing the event creation arguments and potentially an image URI.
     * @param view The root view of the fragment where the ImageView is located.
     */
    private void handleArguments(Bundle args, View view){
        Event.EventBuilder builder = (Event.EventBuilder) args.getSerializable("builder");
        //if builder.getPosterPath != null, the user has already been to this screen so we restore their state
        if (builder.getPosterPath() != null){
            this.uri = builder.getUri();
            this.initialPath = builder.getPosterPath();
            this.path = this.initialPath;
            FirebaseDB.getInstance().retrieveImage(builder.getPosterPath(), new FirebaseDB.GetBitmapCallBack() {
                @Override
                public void onResult(Bitmap bitmap) {
                    ((ImageView) view.findViewById(R.id.new_event_display_event_poster)).setImageBitmap(bitmap);
                }
            });
        } else{
            this.uri = null;
            this.initialPath = null;
            this.path = null;
        }
    }

    public void setFirebaseDB(FirebaseDB instance){
        this.firebaseDB = instance;
    }
}
