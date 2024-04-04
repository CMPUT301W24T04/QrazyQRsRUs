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
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.mapbox.android.gestures.MoveGestureDetector;
import com.mapbox.geojson.Geometry;
import com.mapbox.geojson.Point;
import com.mapbox.maps.CameraOptions;
import com.mapbox.maps.ImageHolder;
import com.mapbox.maps.MapView;
import com.mapbox.maps.Style;
import com.mapbox.maps.ViewAnnotationOptions;
import com.mapbox.maps.plugin.LocationPuck2D;
import com.mapbox.maps.plugin.annotation.AnnotationConfig;
import com.mapbox.maps.plugin.annotation.AnnotationManager;
import com.mapbox.maps.plugin.annotation.AnnotationPlugin;
import com.mapbox.maps.plugin.annotation.generated.CircleAnnotationManager;
import com.mapbox.maps.plugin.annotation.generated.CircleAnnotationManagerKt;
import com.mapbox.maps.plugin.annotation.generated.CircleAnnotationOptions;
import com.mapbox.maps.plugin.annotation.generated.PointAnnotationManager;
import com.mapbox.maps.plugin.annotation.generated.PointAnnotationOptions;
import com.mapbox.maps.plugin.delegates.MapDelegateProvider;
import com.mapbox.maps.plugin.gestures.OnMoveListener;
import com.mapbox.maps.plugin.locationcomponent.LocationComponentPlugin;
import com.mapbox.maps.plugin.locationcomponent.OnIndicatorBearingChangedListener;
import com.mapbox.maps.plugin.locationcomponent.OnIndicatorPositionChangedListener;
import com.mapbox.maps.plugin.annotation.Annotation;
import com.mapbox.maps.plugin.annotation.AnnotationPlugin;
import com.mapbox.maps.viewannotation.ViewAnnotationManager;
import com.mapbox.maps.viewannotation.ViewAnnotationOptionsKtxKt;

import android.Manifest;

import java.util.ArrayList;

import kotlin.DslMarker;

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
//        mapDelegateProvider = mapLayout.findViewById(R.id.mapView);

        //import android.Manifest; to have ACCESS_FINE_LOCATION work
        if(ActivityCompat.checkSelfPermission(getContext(),Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){

            activityResultLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION);
        }
        // Load the mapbox map
        mapView.getMapboxMap().loadStyle(Style.OUTDOORS, new Style.OnStyleLoaded() {
            @Override
            public void onStyleLoaded(@NonNull Style style) {
                //https://docs.mapbox.com/android/maps/guides/styles/set-a-style/
                mapView.getMapboxMap().setCamera(new CameraOptions.Builder().pitch(0.00).build());

                //https://docs.mapbox.com/android/maps/api/11.2.1/mapbox-maps-android/com.mapbox.maps/-image-holder/
                image.Companion.from(R.drawable.baseline_location_on_24).getBitmap();
                image.Companion.from(R.drawable.baseline_location_on_24).toString();
                //
                //**************************************************************************************************************************************

//                // get the lat-longs of an attendee location
                Bundle bundle = getArguments();
                Event event = (Event) bundle.getSerializable("event");
                // get all checked-in attendees in a list with their geolocation on
//                FirebaseDB.getEventCheckedInUsersGeoLocation(event, attendeeDataList, latitudeList, longitudeList);

                FirebaseDB.getInstance().getGeolocations(event, new FirebaseDB.GetMapMarkersCallback() {
                    @Override
                    public void onResult(ArrayList<CheckIn> checks, ArrayList<String> names) {
                        for(int i = 0; i < checks.size();i++){
                            View marker = inflater.inflate(R.layout.marker_layout,container, false);
                            TextView name = marker.findViewById(R.id.annotation);
                            name.setText(names.get(i).toString());
                            attendee_location = Point.fromLngLat( checks.get(i).getLongitude(),checks.get(i).getLatitude());
                            // https://docs.mapbox.com/android/maps/guides/annotations/view-annotations/
                            ViewAnnotationOptions viewAnnotationOptions = ViewAnnotationOptionsKtxKt.geometry(new ViewAnnotationOptions.Builder(),attendee_location).build();
                            mapView.getViewAnnotationManager().addViewAnnotation(marker,viewAnnotationOptions);
                        }
                    }
                });


                attendee_location = Point.fromLngLat( -90.0000, 53.5281);
                // https://docs.mapbox.com/android/maps/guides/annotations/view-annotations/
                ViewAnnotationOptions viewAnnotationOptions = ViewAnnotationOptionsKtxKt.geometry(new ViewAnnotationOptions.Builder(),attendee_location).build();
                mapView.getViewAnnotationManager().addViewAnnotation(R.layout.marker_layout,viewAnnotationOptions);

            }
        });
        return mapLayout;
    }
}