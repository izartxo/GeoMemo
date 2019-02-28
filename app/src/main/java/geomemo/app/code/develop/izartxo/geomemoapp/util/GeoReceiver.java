package geomemo.app.code.develop.izartxo.geomemoapp.util;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;

import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingEvent;

import java.util.List;

import geomemo.app.code.develop.izartxo.geomemoapp.R;
import geomemo.app.code.develop.izartxo.geomemoapp.database.GMActives;
import geomemo.app.code.develop.izartxo.geomemoapp.ui.MenuMainActivity;

public class GeoReceiver extends BroadcastReceiver {

    private static String LOG_TAG = GeoReceiver.class.getSimpleName();
    public static String GEOFENCE = "geomemo.app.code.develop.izartxo.geomemoapp.ui.GEOFENCE";

    private Context mContext;

    public GeoReceiver(Context context) {
        super();
        //Log.d(LOG_TAG,"---- " + "created");
        mContext = context;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        //Log.d(LOG_TAG, "---- GEOFENCE CLOSE  ---> " + intent.getAction());

        GeofencingEvent geofencingEvent = GeofencingEvent.fromIntent(intent);
        if (geofencingEvent.hasError()) {
            String errorMessage = "error";
            Log.e(LOG_TAG, errorMessage);
            return;
        }


        int geofenceTransition = geofencingEvent.getGeofenceTransition();


        if (geofenceTransition == Geofence.GEOFENCE_TRANSITION_ENTER ||
                geofenceTransition == Geofence.GEOFENCE_TRANSITION_EXIT ||
                geofenceTransition == Geofence.GEOFENCE_TRANSITION_DWELL) {


            List<Geofence> triggeringGeofences = geofencingEvent.getTriggeringGeofences();

            //Log.i(LOG_TAG, "vvvv"); //geofenceTransitionDetails);

            String geofenceName = triggeringGeofences.get(0).getRequestId();

            GMFactory.sendMemo(context, geofenceName);

            GMFactory.readMemo(mContext, geofenceName);

        } else {


            //Log.e(LOG_TAG, "bbbbb"/*getString(R.string.geofence_transition_invalid_type, geofenceTransition)*/);
        }
    }

}

