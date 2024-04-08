//this fragment is the sixth fragment in the event creation sequence. it allows users to generate or upload a check in qr code for their event.
package com.example.qrazyqrsrus;


import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.PickVisualMediaRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

/**
 * Creates a new QR for the fragment
 */
public class NewEventQrFragment extends Fragment implements Toolbar.OnMenuItemClickListener {

    private String checkInQRContent;
    private QRCodeGenerator qrCodeGenerator;
    private QRCodeScanHandler qrCodeScanHandler;

    /**
     * creates view
     * @param savedInstanceState If the fragment is being re-created from
     * a previous saved state, this is the state.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (qrCodeGenerator == null) {
            qrCodeGenerator = new QRCodeGenerator();
        }
        if (qrCodeScanHandler == null) {
            qrCodeScanHandler = new QRCodeScanHandler();
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
                    if (uri != null) {
                        //as long as the user selected an image, we invoke our function to update the imageView to display the uploaded poster, and save the event's poster
                        imageUploaded(uri);
                    }
                });

        Button button = view.findViewById(R.id.new_event_generate_qr_button);
        button.setOnClickListener(v -> tryGenerateNewQR());
        Button uploadQrButton = view.findViewById(R.id.new_event_upload_qr_button);
        uploadQrButton.setOnClickListener(v -> uploadQr(pickMedia));
        FloatingActionButton fab = view.findViewById(R.id.qr_screen_finish_button);
        fab.setOnClickListener(v -> {
            Bundle args;
            try {
                assert getArguments() != null;
                args = makeNewBundle(getArguments());
                Event event = ((Event.EventBuilder) Objects.requireNonNull(args.getSerializable("builder"))).build();
                FirebaseDB.getInstance().addEvent(event);
                Navigation.findNavController(view).navigate(R.id.action_newEventQrFragment_to_eventList2, args);
            } catch (Exception e) {
                new ErrorDialog(R.string.no_qr_code).show(requireActivity().getSupportFragmentManager(), "Error Dialog");
            }
        });
        Bundle bundle = getArguments();
        assert bundle != null;
        handleArguments(bundle, view);
        createToolbar(view);
        return view;
    }

    private void createToolbar(View view){
        //once we have made the view, we create the toolbar and inflate it's menu, in order to set and onclick listener from the fragment
        //the idea to access the toolbar by using the Fragment's host View was taken from https://stackoverflow.com/questions/29020935/using-toolbar-with-fragments on February 21st, 2024
        //it was posted by the user Faisal Naseer (https://stackoverflow.com/users/2641848/faisal-naseer) in the post https://stackoverflow.com/a/45653449
        Toolbar toolbar = view.findViewById(R.id.checkin_screen_toolbar);
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
            Bundle args = getArguments();
            try{
                assert args != null;
                args = makeNewBundle(args);
            } catch (Exception e){
                //this is allowed
            }
            Navigation.findNavController(requireView()).navigate(R.id.action_newEventQrFragment_to_newEventPromoQrFragment, args);
            return true;
        }
        else if (id == R.id.cancel_button){
            //leave entire new event sequence
            Navigation.findNavController(requireView()).navigate(R.id.action_newEventQrFragment_to_eventList2);
            return true;
        }
        return false;
    }

    /**
     * This function will try to generate a new QR code, handling the case that the generated qr code is already being used by an event in the database
     *
     */
    private void tryGenerateNewQR(){
        //we generate a timestamp to append to the name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        assert getArguments() != null;
        String qrContent = ((Event.EventBuilder) (getArguments().getSerializable("builder"))).getName() + "_" + timeStamp + "_checkin";
        //we check if our qr code is unique
        qrCodeGenerator.checkUnique(qrContent, 1, new QRCodeGenerator.UniqueQRCheckCallBack() {
            @Override
            public void onUnique() {
                generateBitmap(qrContent, getView());
            }
            @Override
            public void onNotUnique() {
                new ErrorDialog(R.string.qr_not_unique).show(requireActivity().getSupportFragmentManager(), "Error Dialog");
            }
        });
    }

    /**
     * This function will try to use a user uploaded QR code, handling the case that the uploaded qr code is already being used by an event in the database
     * @param content The content of the uploaded image, that will be checked to see if it is already in use.
     */
    private void tryGenerateNewQR(String content){
        //we check if our qr code is unique
        qrCodeGenerator.checkUnique(content, 0, new QRCodeGenerator.UniqueQRCheckCallBack() {
            @Override
            public void onUnique() {
                qrCodeGenerator.checkUnique(content, 1, new QRCodeGenerator.UniqueQRCheckCallBack() {
                    @Override
                    public void onUnique() {
                        generateBitmap(content, getView());
                    }
                    @Override
                    public void onNotUnique() {
                        new ErrorDialog(R.string.qr_not_unique).show(requireActivity().getSupportFragmentManager(), "Error Dialog");
                    }
                });
            }
            @Override
            public void onNotUnique() {
                new ErrorDialog(R.string.qr_not_unique).show(requireActivity().getSupportFragmentManager(), "Error Dialog");
            }
        });
    }

    /**
     * This function will try to generate an image bitmap of a qr code that encodes the provided content. It will update the ImageView on screen and set the checkInQRContent to use to build the Event object
     */
    private void generateBitmap(String content, View view){
        Bitmap bitmap = qrCodeGenerator.generateBitmap(content);
        if (bitmap != null){
            ((ImageView) view.findViewById(R.id.new_event_display_qr_code)).setImageBitmap(bitmap);
            checkInQRContent = content;
        } else{
            Log.d("generateBitmap", "error generating the bitmap");
            new ErrorDialog(R.string.qr_generation_failed).show(requireActivity().getSupportFragmentManager(), "Error Dialog");
        }
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
        String content = qrCodeScanHandler.scanImage(requireContext().getContentResolver(), uri);
        if (content == null){
            //if the uploaded image failed to be recognized as a qr code
            new ErrorDialog(R.string.qr_generation_failed).show(requireActivity().getSupportFragmentManager(), "Error Dialog");
        } else{
            tryGenerateNewQR(content);
        }
    }

    /**
     * This function makes a bundle to pass to the next fragment, allowing it to access the data input by the user while making a new event
     * @param bundle The bundle that holds the event fields so far
     * @return The updated bundle.
     */


    private Bundle makeNewBundle(Bundle bundle) throws Exception{
        Event.EventBuilder builder = (Event.EventBuilder) bundle.getSerializable("builder");
        if (this.checkInQRContent == null){
            throw new Exception();
        } else{
            assert builder != null;
            builder.setQrCode(this.checkInQRContent);
        }

        bundle.putSerializable("builder", builder);
        return bundle;
    }

    private void handleArguments(Bundle args, View view){
        Event.EventBuilder builder = (Event.EventBuilder) args.getSerializable("builder");
        assert builder != null;
        if (builder.getQrCode() != null){
            generateBitmap(builder.getQrCode(), view);
        }
    }

    public void setQrCodeGenerator(QRCodeGenerator instance){
        this.qrCodeGenerator = instance;
    }
    public void setQrCodeScanHandler(QRCodeScanHandler instance){
        this.qrCodeScanHandler = instance;
    }
}
