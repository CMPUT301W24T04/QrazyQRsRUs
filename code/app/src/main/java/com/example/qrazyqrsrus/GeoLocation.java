package com.example.qrazyqrsrus;

import static com.mapbox.maps.plugin.gestures.GesturesUtils.getGestures;
import static com.mapbox.maps.plugin.locationcomponent.LocationComponentUtils.getLocationComponent;

import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.mapbox.android.gestures.MoveGestureDetector;
import com.mapbox.geojson.Point;
import com.mapbox.maps.CameraOptions;
import com.mapbox.maps.ImageHolder;
import com.mapbox.maps.MapView;
import com.mapbox.maps.Style;
import com.mapbox.maps.plugin.LocationPuck2D;
import com.mapbox.maps.plugin.annotation.AnnotationPlugin;
import com.mapbox.maps.plugin.annotation.generated.PointAnnotationManager;
import com.mapbox.maps.plugin.gestures.OnMoveListener;
import com.mapbox.maps.plugin.locationcomponent.LocationComponentPlugin;
import com.mapbox.maps.plugin.locationcomponent.OnIndicatorBearingChangedListener;
import com.mapbox.maps.plugin.locationcomponent.OnIndicatorPositionChangedListener;

import android.Manifest;

public class GeoLocation extends Fragment {
    private MapView mapView;
    private  FloatingActionButton floatingActionButton;
    // Initialize the image
    private  ImageHolder image;
    private PointAnnotationManager point;
    private Point attendee_location;
//    private Marker

    /**
     * Used to verify the permissions specified in the manifest file
     * https://docs.mapbox.com/android/maps/guides/install/
     */
    private final ActivityResultLauncher<String> activityResultLauncher = registerForActivityResult(new ActivityResultContracts.RequestPermission(), new ActivityResultCallback<Boolean>() {
        @Override
        public void onActivityResult(Boolean result) {
            if(result){
                Toast.makeText(requireContext(), "Permission Granted", Toast.LENGTH_SHORT).show();
            }
            else{
                Toast.makeText(requireContext(), "Permission not Granted", Toast.LENGTH_SHORT).show();
            }
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

        mapView = mapLayout.findViewById(R.id.mapView);
        floatingActionButton = mapLayout.findViewById(R.id.focusLocation);
        floatingActionButton.hide();

        //import android.Manifest; to have ACCESS_FINE_LOCATION work
        if(ActivityCompat.checkSelfPermission(getContext(),Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){

            activityResultLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION);
        }
        // Load the mapbox map
        mapView.getMapboxMap().loadStyle(Style.STANDARD, new Style.OnStyleLoaded() {
            @Override
            public void onStyleLoaded(@NonNull Style style) {
                mapView.getMapboxMap().setCamera(new CameraOptions.Builder().pitch(0.00).build());

                Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.baseline_location_on_24); // turn image to bitmap(might be needed)


                //*************************************************************
                // get the lat-longs of an attendee location
                attendee_location = Point.fromLngLat(53.5281, -113.5265);
                //*************************************************************


            }
        });
        return mapLayout;
    }
}