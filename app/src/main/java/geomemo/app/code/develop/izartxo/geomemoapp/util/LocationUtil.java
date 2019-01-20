package geomemo.app.code.develop.izartxo.geomemoapp.util;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.widget.LinearLayout;

import geomemo.app.code.develop.izartxo.geomemoapp.R;


public class LocationUtil {

    private static final String LOG_TAG = "*******" + LocationUtil.class.getSimpleName();

    private static Location lastLocation;
    private Context mContext;
    private LocationManager locationManager;
    private LocationListener locationListener;
    private LocationReady mInterface;

    public LocationUtil(Context mContext, LocationReady mInterface) {
        this.mContext = mContext;
        this.mInterface = mInterface;
        configure();
        Log.d(LOG_TAG, "CREATED AND LISTENING");
    }


    private void configure() {

        // Acquire a reference to the system Location Manager
        locationManager = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);

        // Define a listener that responds to location updates
        locationListener = new LocationListener() {
            public void onLocationChanged(Location location) {
                // Called when a new location is found by the network location provider.
                lastLocation = location;
                mInterface.onLocationReady(location);
            }

            public void onStatusChanged(String provider, int status, Bundle extras) {
            }

            public void onProviderEnabled(String provider) {
            }

            public void onProviderDisabled(String provider) {
            }
        };

        // Register the listener with the Location Manager to receive location updates
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);


        lastLocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
    }

    public static Location getLastLocation(){
        return lastLocation;
    }

    public interface LocationReady{
        void onLocationReady(Location location);
    }

    public void requestLocationServices(){
        LocationManager lm = (LocationManager)mContext.getSystemService(Context.LOCATION_SERVICE);
        boolean gps_enabled = false;
        boolean network_enabled = false;

        try {
            gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch(Exception ex) {}

        try {
            network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        } catch(Exception ex) {}

        if(!gps_enabled && !network_enabled) {

            // notify user
            final AlertDialog.Builder dialog = new AlertDialog.Builder(mContext);

            dialog.setMessage("Activar location"); //mContext.getResources().getString(R.string.gps_network_not_enabled));
            dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() { //mContext.getResources().getString(R.string.open_location_settings),
                @Override
                public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                    // TODO Auto-generated method stub
                    Intent myIntent = new Intent( Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    mContext.startActivity(myIntent);
                    //get gps
                }
            });
            dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() { //mContext.getString(R.string.Cancel),

                @Override
                public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                    // TODO Auto-generated method stub

                }
            });
            dialog.show();
        }
    }

}
