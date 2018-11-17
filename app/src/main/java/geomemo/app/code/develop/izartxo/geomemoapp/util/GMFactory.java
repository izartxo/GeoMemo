package geomemo.app.code.develop.izartxo.geomemoapp.util;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.android.gms.location.GeofencingClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import java.text.DateFormat;
import java.text.FieldPosition;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import geomemo.app.code.develop.izartxo.geomemoapp.R;
import geomemo.app.code.develop.izartxo.geomemoapp.database.AppDatabase;
import geomemo.app.code.develop.izartxo.geomemoapp.database.GMActives;
import geomemo.app.code.develop.izartxo.geomemoapp.database.GMHistory;
import geomemo.app.code.develop.izartxo.geomemoapp.database.GeofenceMemo;
import geomemo.app.code.develop.izartxo.geomemoapp.ui.MenuMainActivity;

public class GMFactory {

    private static String LOG_TAG = "*******" + GMFactory.class.getSimpleName();

    private static int GM_DEFAULT_SIZE = 1; // Short size
    private static boolean GM_DEFAULT_ACTIVE = true; // Not active on creation
    public static String GEOFENCE = "geomemo.app.code.develop.izartxo.geomemoapp.ui.GEOFENCE";

    /*
    * Creation of GeoMemo from pased data
    *
    *
    * */
    public static GeofenceMemo createGeoMemo(
            String geoName,
            String geoMemo,
            String geoLatitude,
            String geoLongitude)
    {
        GeofenceMemo gm = new GeofenceMemo();
        gm.setGeoName(geoName);
        gm.setGeoMemo(geoMemo);
        gm.setGeoTimestamp(getCurrentDateTime(System.currentTimeMillis()));
        gm.setGeoLatitude(geoLatitude);
        gm.setGeoLongitude(geoLongitude);
        gm.setGeoSize(GM_DEFAULT_SIZE);
        gm.setGeoActive(GM_DEFAULT_ACTIVE);

        return gm;
    }

    /*
    * Creation of GMActive to fill active GeoMemos table
    *
    *
    * */
    public static GMActives createGMActive(
            GeofenceMemo gm)
    {
        GMActives gma = new GMActives();
        gma.setGeoName(gm.getGeoName());
        gma.setGeoMemo(gm.getGeoMemo());
        gma.setGeoLatitude(gm.getGeoLatitude());
        gma.setGeoLongitude(gm.getGeoLongitude());


        return gma;
    }

    /*
    * Creation of GMHistory to store on DB
    *
    *
    * */
    public static GMHistory createGMHistory(
            GeofenceMemo gm)
    {
        GMHistory gmh = new GMHistory();
        gmh.setGeoName(gm.getGeoName());
        gmh.setGeoMemo(gm.getGeoMemo());
        gmh.setGeoTimestamp(gm.getGeoTimestamp());
        gmh.setGeoHistoryTime(getCurrentDateTime(System.currentTimeMillis()));
        gmh.setGeoLatitude(gm.getGeoLatitude());
        gmh.setGeoLongitude(gm.getGeoLongitude());

        return gmh;
    }

    public static void readGeoMemo(Context context, String geoMemo){
        AppDatabase mDB = AppDatabase.getInstance(context);

        // unregistering geofence
        ArrayList<String> mGeoMemo = new ArrayList<String>();
        mGeoMemo.add(geoMemo);
        removeGeoMemo(context, mGeoMemo);

        // state activate --> deactivate
        mDB.geofenceMemoDao().updateGeoMemoState(geoMemo);
        // delete from activates
        mDB.gmActivesDao().deleteGMActive(geoMemo);
        // Pass to history
        GeofenceMemo geofenceMemo = mDB.geofenceMemoDao().loadMemoGeofenceByName(geoMemo).get(0);
        mDB.gmHistoryDao().insertGMHistory(GMFactory.createGMHistory(geofenceMemo));

    }

    public static void sendNotification(Context context, String geoName){

        AppDatabase mDB = AppDatabase.getInstance(context);
        List<GeofenceMemo> lGeoMemo = mDB.geofenceMemoDao().loadMemoGeofenceByName(geoName);

        int NOTIFICATION_ID = 234;
        String GROUP_KEY_WORK_EMAIL = "com.android.example.WORK_EMAIL";

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
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

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.mipmap.brain)
                .setContentTitle(geoName)
                .setContentText(lGeoMemo.get(0).getGeoMemo())
                .setGroup(GROUP_KEY_WORK_EMAIL);

       /* NotificationCompat.Builder builder2 = new NotificationCompat.Builder(mContext, CHANNEL_ID)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(n2)
                .setContentText("message")
                .setGroup(GROUP_KEY_WORK_EMAIL);*/

        Notification summaryNotification =
                new NotificationCompat.Builder(context, CHANNEL_ID)
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

        Intent resultIntent = new Intent(context, MenuMainActivity.class);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addParentStack(MenuMainActivity.class);
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        builder.setContentIntent(resultPendingIntent);


        Log.d(LOG_TAG, "sssssssssssssssss: " + n1 + "//" + n2);

        notificationManager.notify(Integer.valueOf(geoName.length()), builder.build());

        notificationManager.notify(0, summaryNotification);
    }

    private static String getCurrentDateTime(long current){
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(current);
        return format.format(calendar.getTime()).toString();
    }

    public static void removeGeoMemo(Context context, final List<String> geoMemoName){
        GeofencingClient geofencingClient = LocationServices.getGeofencingClient(context);
        geofencingClient.removeGeofences(geoMemoName)
            .addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Log.d(LOG_TAG, "--removed--> " + geoMemoName.get(0));
                }
            })
            .addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.d(LOG_TAG, "--error removing--> " + geoMemoName.get(0));
                }
            });

    }

}
