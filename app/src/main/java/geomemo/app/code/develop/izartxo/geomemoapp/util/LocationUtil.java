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
        //Log.d(LOG_TAG, "CREATED AND LISTENING");
    }


    private void configure() {


        locationManager = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);


        locationListener = new LocationListener() {
            public void onLocationChanged(Location location) {

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


        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);


        lastLocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
    }

    public static Location getLastLocation(){
        return lastLocation;
    }

    public interface LocationReady{
        void onLocationReady(Location location);
    }





}
