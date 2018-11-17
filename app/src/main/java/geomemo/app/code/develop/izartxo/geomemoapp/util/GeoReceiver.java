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
        Log.d(LOG_TAG,"---- " + "created");
        mContext = context;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(LOG_TAG, "---- GEOFENCE CLOSE  ---> " + intent.getAction());

        GeofencingEvent geofencingEvent = GeofencingEvent.fromIntent(intent);
        if (geofencingEvent.hasError()) {
            String errorMessage = "error"; //GeofenceErrorMessages.getErrorString(this,                    geofencingEvent.getErrorCode());
            Log.e(LOG_TAG, errorMessage);
            return;
        }

        // Get the transition type.
        int geofenceTransition = geofencingEvent.getGeofenceTransition();

        // Test that the reported transition was of interest.
        if (geofenceTransition == Geofence.GEOFENCE_TRANSITION_ENTER ||
                geofenceTransition == Geofence.GEOFENCE_TRANSITION_EXIT ||
                geofenceTransition == Geofence.GEOFENCE_TRANSITION_DWELL) {

            // Get the geofences that were triggered. A single event can trigger
            // multiple geofences.
            List<Geofence> triggeringGeofences = geofencingEvent.getTriggeringGeofences();

            // Get the transition details as a String.
            /*String geofenceTransitionDetails = getGeofenceTransitionDetails(
                    this,
                    geofenceTransition,
                    triggeringGeofences
            );*/

            // Send notification and log the transition details.
            /* sendNotification(geofenceTransitionDetails);*/


            Log.i(LOG_TAG, "vvvv"); //geofenceTransitionDetails);

            String geofenceName = triggeringGeofences.get(0).getRequestId();

            //notificationGeo2(triggeringGeofences.get(0).getRequestId());

            GMFactory.sendNotification(context, geofenceName);
            // !!!!
            //noti3(geofenceName, "dummy");

            //new MyAsyncTask(triggeringGeofences.get(0).getRequestId());

            GMFactory.readGeoMemo(mContext, geofenceName); // Updates geoMemos active state 1-->0

        } else {
            // Log the error.


            Log.e(LOG_TAG, "bbbbb"/*getString(R.string.geofence_transition_invalid_type,
                    geofenceTransition)*/);
        }
    }

    private void noti3(final String geoName, final String geoMemo){

        int NOTIFICATION_ID = 234;
        String GROUP_KEY_WORK_EMAIL = "com.android.example.WORK_EMAIL";

        NotificationManager notificationManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
        // NotificationManagerCompat notificationManager2 = NotificationManagerCompat.from(mContext);



        String CHANNEL_ID = "my_channel_01";
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {



            CharSequence name = "my_channel";
            String Description = "This is my channel";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel mChannel = new NotificationChannel(CHANNEL_ID, name, importance);
            mChannel.setDescription(Description);
            mChannel.enableLights(true);
            mChannel.setLightColor(Color.BLUE);
            //mChannel.enableVibration(true);
            //mChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
            mChannel.setShowBadge(false);
            notificationManager.createNotificationChannel(mChannel);
        }

        String n1 = String.valueOf(Math.random());
        String n2 = String.valueOf(Math.random());

        NotificationCompat.Builder builder = new NotificationCompat.Builder(mContext, CHANNEL_ID)
                .setSmallIcon(R.mipmap.brain)
                .setContentTitle(geoName)
                .setContentText(geoMemo)
                .setGroup(GROUP_KEY_WORK_EMAIL);

       /* NotificationCompat.Builder builder2 = new NotificationCompat.Builder(mContext, CHANNEL_ID)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(n2)
                .setContentText("message")
                .setGroup(GROUP_KEY_WORK_EMAIL);*/

        Notification summaryNotification =
                new NotificationCompat.Builder(mContext, CHANNEL_ID)
                        .setContentTitle("GeoMemos")
                        //set content text to support devices running API level < 24
                        .setContentText("Two new messages")
                        .setSmallIcon(R.mipmap.brain)
                        //build summary info into InboxStyle template
                        .setStyle(new NotificationCompat.InboxStyle()
                                .addLine("Alex Faarborg  Check this out")
                                .addLine("Jeff Chang    Launch Party")
                                .setBigContentTitle("2 new messages")
                                .setSummaryText("janedoe@example.com"))
                        //specify which group this notification belongs to
                        .setGroup(GROUP_KEY_WORK_EMAIL)
                        //set this notification as the summary for the group
                        .setGroupSummary(true)
                        .setNumber(notificationManager.getActiveNotifications().length)
                        .build();

        Intent resultIntent = new Intent(mContext, MenuMainActivity.class);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(mContext);
        stackBuilder.addParentStack(MenuMainActivity.class);
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        builder.setContentIntent(resultPendingIntent);


        Log.d(LOG_TAG, "sssssssssssssssss: " + n1 + "//" + n2);

        notificationManager.notify(Integer.valueOf(geoName.length()), builder.build());

        notificationManager.notify(0, summaryNotification);

     /*   notificationManager.notify(NOTIFICATION_ID, builder.build());
        notificationManager.notify(0x02, builder2.build());
        notificationManager.notify(0, summaryNotification);*/
    }

   /* class MyAsyncTask extends AsyncTask<Void, Void, Void> {

        public String _name;

        MyAsyncTask(String name){
            _name = name;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            notificationGeo(_name);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Log.d(LOG_TAG, "ONPOSTEXceuted");
        }


        private void notificationGeo(final String geoname) {

            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(mContext, "channel_id")
                    .setSmallIcon(R.drawable.ic_launcher_foreground)
                    .setContentTitle(geoname)
                    .setContentText("content")
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT);


            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                CharSequence name = geoname;
                String description = "description";
                int importance = NotificationManager.IMPORTANCE_DEFAULT;
                NotificationChannel channel = new NotificationChannel("channel_id", name, importance);
                channel.setDescription(description);
                // Register the channel with the system; you can't change the importance
                // or other notification behaviors after this
                NotificationManager notificationManager = mContext.getSystemService(NotificationManager.class);
                notificationManager.createNotificationChannel(channel);
            }

            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(mContext);

            // notificationId is a unique int for each notification that you must define
            notificationManager.notify(0x02, mBuilder.build());

        }
    }*/

    /*private void notificationGeo(final String geoname) {

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(mContext, "channel_id")
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle(geoname)
                .setContentText("content")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = geoname;
            String description = "description";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("channel_id", name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = mContext.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(mContext);

        // notificationId is a unique int for each notification that you must define
        notificationManager.notify(0x02, mBuilder.build());

    }*/

    /*private void notificationGeo2(final String geoname){
        //use constant ID for notification used as group summary
        int SUMMARY_ID = 0;
        String GROUP_KEY_WORK_EMAIL = "com.android.example.WORK_EMAIL";
        try {
            Notification newMessageNotification1 =
                    new NotificationCompat.Builder(mContext, "channel_id")
                            .setSmallIcon(R.drawable.ic_launcher_foreground)
                            .setContentTitle("not1")
                            .setContentText(geoname)
                            .setGroup(GROUP_KEY_WORK_EMAIL)
                            .build();

            Notification newMessageNotification2 =
                    new NotificationCompat.Builder(mContext, "channel_id")
                            .setSmallIcon(R.drawable.ic_launcher_foreground)
                            .setContentTitle("not2")
                            .setContentText(geoname)
                            .setGroup(GROUP_KEY_WORK_EMAIL)
                            .build();

            Notification summaryNotification =
                    new NotificationCompat.Builder(mContext, "channel_id")
                            .setContentTitle("sum1")
                            //set content text to support devices running API level < 24
                            .setContentText("Two new messages")
                            .setSmallIcon(R.drawable.ic_launcher_foreground)
                            //build summary info into InboxStyle template
                            .setStyle(new NotificationCompat.InboxStyle()
                                    .addLine("Alex Faarborg  Check this out")
                                    .addLine("Jeff Chang    Launch Party")
                                    .setBigContentTitle("2 new messages")
                                    .setSummaryText("janedoe@example.com"))
                            //specify which group this notification belongs to
                            .setGroup(GROUP_KEY_WORK_EMAIL)
                            //set this notification as the summary for the group
                            .setGroupSummary(true)
                            .build();

            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(mContext);
            notificationManager.notify(0x01, newMessageNotification1);
            notificationManager.notify(0x02, newMessageNotification2);
            notificationManager.notify(SUMMARY_ID, summaryNotification);
        }catch(Exception e){
            Log.d(LOG_TAG, "ERRROOOOR: " + e.getMessage().toString());
        }


    }*/


}

