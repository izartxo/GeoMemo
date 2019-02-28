package geomemo.app.code.develop.izartxo.geomemoapp.util;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;

public class BroadcastService extends JobService {

    private static final String LOG_TAG = "*******" + BroadcastService.class.getSimpleName();

    public static int BROADCAST_RECEIVER_ID = 0x10;

    private GeoReceiver geoReceiver;
    private boolean isRunning = false;

    @Override
    public void onCreate() {
        super.onCreate();
        if (!isRunning){
            IntentFilter intentFilter = new IntentFilter(GeoReceiver.GEOFENCE);
            geoReceiver = new GeoReceiver(getApplicationContext());
            registerReceiver(geoReceiver, intentFilter);
            isRunning = true;
        }

        //Log.d(LOG_TAG, "onCreate............service actived: " + isRunning);

    }



    @Override
    public void onDestroy() {
        //Log.d(LOG_TAG, "onCreate............service destroyed: " + isRunning);
        super.onDestroy();
    }

    @Override
    public boolean onStartJob(JobParameters jobParameters) {
        //Log.d(LOG_TAG, "startjob............service actived: " + isRunning);
        if (geoReceiver==null){
            IntentFilter intentFilter = new IntentFilter(GeoReceiver.GEOFENCE);
            geoReceiver = new GeoReceiver(getApplicationContext());
            registerReceiver(geoReceiver, intentFilter);
            isRunning = true;

            Intent intent = new Intent(GeoReceiver.GEOFENCE);
            sendBroadcast(intent);
        }
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters jobParameters) {
        isRunning = false;
        //Log.d(LOG_TAG, "stopjob............service actived: " + isRunning);
        if (geoReceiver!=null)
            unregisterReceiver(geoReceiver);
        return true;
    }


}

