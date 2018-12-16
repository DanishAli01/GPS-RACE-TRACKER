package com.example.danishali2875170.GPSTracker;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.List;

import static android.content.Context.LOCATION_SERVICE;



public class GPSTracker implements LocationListener {

    Context context;
    private MainActivity mainActivity;
    List<Location> locationlist;
    LocationManager lm;

    public GPSTracker(Context context, MainActivity mainActivity) {
        super();
        this.context = context;
        locationlist = new ArrayList<Location>();
        this.mainActivity = mainActivity;
    }

    /**
     * GetSystem Service <em>(Location)</em>
     * <p>Permission Check, LocationManager gets location provided GPS_PROVIDER</p>
     */

    public void getLocation() {

        if (ContextCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(mainActivity, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            Toast.makeText(mainActivity, "Press Start to Continue", Toast.LENGTH_SHORT).show();

        }
                else {
        try {
            lm = (LocationManager) context.getSystemService(LOCATION_SERVICE);

            boolean isGPSEnabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);

            if (isGPSEnabled) {

                lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 5, this);

                Location loc = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);

            } else {
                Log.e("sec", "errpr");
            }
        } catch (Exception e) {
            e.printStackTrace();
          }
        }
    }

    /**
     * In event of <bold>location change</bold>, each new location will be added to <em>locationlist</em>
     * @param location
     */

    @Override
    public void onLocationChanged(Location location) {
        locationlist.add(location);
        Toast.makeText(context, "lat : "+location.getLatitude() + "\nlong : "+location.getLongitude(), Toast.LENGTH_SHORT).show();


    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
    }

    @Override
    public void onProviderEnabled(String provider) {
    }

    @Override
    public void onProviderDisabled(String provider) {
        showSettingsAlert();
    }

    /**
     * <bold>List of recorded locations.</bold>
     * <p>Gets all locations in list stored while location change is called.</p>
     * @return <em>List<Location></em>
     */

    public List<Location> getlocationlist(){
        lm.removeUpdates(this);

        return locationlist;
    }

    /**
     *Setting Dialogue Box
     * <p> Inbuilt AlertDialog box to request location premissions.</p>
     */
    public void showSettingsAlert() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);

        // Setting Dialog Title

        alertDialog.setTitle("GPS Premissions");

        // Setting Dialog Message

        alertDialog.setMessage("GPS is not enabled. This app requires GPS permissions to function. \nDo you want to go to settings menu and enable it?");

        // On pressing the Settings button.

        alertDialog.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                context.startActivity(intent);
            }
        });

        // On pressing the cancel button

        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        // Showing Alert Message

        alertDialog.show();
    }
}

