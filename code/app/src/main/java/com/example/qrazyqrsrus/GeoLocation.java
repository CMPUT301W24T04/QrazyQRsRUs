// This class displays a map and pins where users are checked in from
// Problems:
//            - If a user has a long name, it may be outside the pin boundaries
//            - Names might overlap when zoomed out since pins change size depending on how far we are zoomed
package com.example.qrazyqrsrus;

// This fragment creates a map and sets markers on that map for where users are checked in from
// Problems:
//          - Users with long names may have their name extend outside of the marker background
//          - User names may overlap when zoomed out since markers changes size based on how zoomed out a person it

import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.mapbox.geojson.Point;
import com.mapbox.maps.CameraOptions;
import com.mapbox.maps.ImageHolder;
import com.mapbox.maps.MapView;
import com.mapbox.maps.Style;
import com.mapbox.maps.ViewAnnotationOptions;
import com.mapbox.maps.viewannotation.ViewAnnotationOptionsKtxKt;

import android.Manifest;

import java.util.ArrayList;

/**
 * Creates the map and adds markers to the map
 */
public class GeoLocation extends Fragment {
    private MapView mapView;
    ArrayList<Attendee> attendeeDataList;
    private  FloatingActionButton floatingActionButton;
    // Initialize the image
    private  ImageHolder image;
    private Point attendee_location;
    private View view;


    /**
     * Used to verify the permissions specified in the manifest file
     * <a href="https://docs.mapbox.com/android/maps/guides/install/">...</a>
     */
    private final ActivityResultLauncher<String> activityResultLauncher = registerForActivityResult(new ActivityResultContracts.RequestPermission(), result -> {
        if(result){
            Toast.makeText(requireContext(), "Permission Granted", Toast.LENGTH_SHORT).show();
        }
        else{
            Toast.makeText(requireContext(), "Permission not Granted", Toast.LENGTH_SHORT).show();
        }
    });

    /**
     * Shows the map and places pins relating to user locations on the map
     * @param inflater The LayoutInflater object that can be used to inflate
     * any views in the fragment,
     * @param container If non-null, this is the parent view that the fragment's
     * UI should be attached to.  The fragment should not add the view itself,
     * but this can be used to generate the LayoutParams of the view.
     * @param savedInstanceState If non-null, this fragment is being re-constructed
     * from a previous saved state as given here.
     *
     * @return mapView
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View mapLayout = inflater.inflate(R.layout.fragment_geo_location, container, false);
        FloatingActionButton backButton = mapLayout.findViewById(R.id.back_button);
        mapView = mapLayout.findViewById(R.id.mapView);

        //import android.Manifest; to have ACCESS_FINE_LOCATION work
        if(ActivityCompat.checkSelfPermission(requireContext(),Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){

            activityResultLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION);
        }

        backButton.setOnClickListener(view -> {
            try{
                Navigation.findNavController(mapLayout).popBackStack();
            } catch (Exception e){
                backButton.setVisibility(View.GONE);
            }
        });
        // Load the mapbox map
        mapView.getMapboxMap().loadStyle(Style.OUTDOORS, style -> {
            //https://docs.mapbox.com/android/maps/guides/styles/set-a-style/
            mapView.getMapboxMap().setCamera(new CameraOptions.Builder().pitch(0.00).build());

            //https://docs.mapbox.com/android/maps/api/11.2.1/mapbox-maps-android/com.mapbox.maps/-image-holder/
            image.Companion.from(R.drawable.baseline_location_on_24).getBitmap();
            image.Companion.from(R.drawable.baseline_location_on_24).toString();
            //
            //**************************************************************************************************************************************

//                // get the lat-longs of an attendee location
            Bundle bundle = getArguments();
            assert bundle != null;
            Event event = (Event) bundle.getSerializable("event");
            // get all checked-in attendees in a list with their geolocation on
//                FirebaseDB.getEventCheckedInUsersGeoLocation(event, attendeeDataList, latitudeList, longitudeList);

            assert event != null;
            FirebaseDB.getInstance().getGeolocations(event, (checks, names) -> {
                for(int i = 0; i < checks.size();i++){
                    View marker = inflater.inflate(R.layout.marker_layout,container, false);
                    TextView name = marker.findViewById(R.id.annotation);
                    name.setText(names.get(i).toString());
                    attendee_location = Point.fromLngLat( checks.get(i).getLongitude(),checks.get(i).getLatitude());
                    // https://docs.mapbox.com/android/maps/guides/annotations/view-annotations/
                    ViewAnnotationOptions viewAnnotationOptions = ViewAnnotationOptionsKtxKt.geometry(new ViewAnnotationOptions.Builder(),attendee_location).build();
                    mapView.getViewAnnotationManager().addViewAnnotation(marker,viewAnnotationOptions);
                }
            });

        });
        return mapLayout;
    }
}