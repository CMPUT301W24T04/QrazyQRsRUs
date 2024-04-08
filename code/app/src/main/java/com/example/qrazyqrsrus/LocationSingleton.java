package com.example.qrazyqrsrus;

// This class gets the device location of users who scan check in QR code

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class LocationSingleton {
    private static LocationSingleton instance = null;

    public interface LongitudeLatitudeCallback {
        /**
         * A function for the callback to handle the accessed user location
         * @param longitude the longitude of the user's location
         * @param latitude the latitude of the user's location
         */
        void onResult(double longitude, double latitude);
    }

    /**
     * Singleton constructor
     */
    private LocationSingleton() {

    }

    /**
     * Singleton instance function
     * @return the instance of the singleton
     */
    public static LocationSingleton getInstance() {
        if (instance == null) {
            instance = new LocationSingleton();
        }

        return instance;
    }

    /**
     * This function will asynchronously get the user's location, and invoke a callback to handle it
     * @param activity The activity where we will request permissions in
     * @param callback The callback that will handle the longitude and latitude of the user's location
     */
    public void getLocation(Activity activity, LongitudeLatitudeCallback callback) {
        Context context = activity.getApplicationContext();

        //we get the locationManager that can provide us with the device's location
        //this idea was originally from https://stackoverflow.com/a/2227299, Accessed on Mar. 28th, 2024
        //it was poster by the user David Webb (https://stackoverflow.com/users/3171/david-webb)
        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);


        //we check if we have permissions to access the user's location
        //this implementation fo checking for permissions is from Stack Overflow (https://stackoverflow.com/a/33070595), Accessed Mar. 28th, 2024
        //the post was made by the user keshav.bahadoor (https://stackoverflow.com/users/1535115/keshav-bahadoor)
        if (ContextCompat.checkSelfPermission( context, Manifest.permission.ACCESS_COARSE_LOCATION ) != PackageManager.PERMISSION_GRANTED) {
            //if not, we launch a dialog in main activity that asks the user to grant permissions
            int LOCATION_PERMISSION_GRANTED = 1;
            ActivityCompat.requestPermissions( activity, new String[] {  Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION  },
                    LOCATION_PERMISSION_GRANTED);
        }

        //if users have not granted permissions, we invoke the callback with a default location
        if (ContextCompat.checkSelfPermission( context, Manifest.permission.ACCESS_COARSE_LOCATION ) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission( context, Manifest.permission.ACCESS_FINE_LOCATION ) != PackageManager.PERMISSION_GRANTED){
            //TODO: return a default location in iceland if user won't accept permissions
            return;
        }

        //we try to get the last known location, but this may be null
        Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        //if last known location is null, we invoke the callback with the calculated current location
        if (location == null){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                locationManager.getCurrentLocation(
                        LocationManager.GPS_PROVIDER,
                        null,
                        activity.getMainExecutor(),
                        location1 -> callback.onResult(location1.getLongitude(), location1.getLatitude())
                );
            }
        } else{
            //we invoke the callback with the last known location
            callback.onResult(location.getLongitude(), location.getLatitude());
        }
    }


}
