package geomemo.app.code.develop.izartxo.geomemoapp.util;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
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
import geomemo.app.code.develop.izartxo.geomemoapp.ui.GeofenceMemoViewModel;
import geomemo.app.code.develop.izartxo.geomemoapp.ui.MenuMainActivity;
import geomemo.app.code.develop.izartxo.geomemoapp.widget.GeoMemoAppWidgetProvider;
import geomemo.app.code.develop.izartxo.geomemoapp.widget.GeoMemoProvider;

public class GMFactory {

    private final static String LOG_TAG = "*******" + GMFactory.class.getSimpleName();

    private final static  String CHANNEL_ID = "GEOMEMO_CHANNEL";

    private static int GM_DEFAULT_SIZE = 1; // Short size
    private static boolean GM_DEFAULT_ACTIVE = true; // Not active on creation
    public static int GM_TEXT_LENGTH = 150;
    public static String GEOFENCE = "geomemo.app.code.develop.izartxo.geomemoapp.ui.GEOFENCE";


    public static List<GeofenceMemo> lGeoMemo;


    public static void setListGeo(List<GeofenceMemo> gfm){
        lGeoMemo = gfm;
    }

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
        gma.setGeoTimestamp(gm.getGeoTimestamp());


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

        new MemoAsyncTask(mDB, geoMemo).execute(MemoAsyncTask.UPDATE);
        // delete from activates

        new GMActiveAsyncTask(mDB, geoMemo).execute(GMActiveAsyncTask.DELETE);
        // Pass to history
        new MemoAsyncTask(mDB, geoMemo).execute(MemoAsyncTask.QUERY); //mDB.geofenceMemoDao().loadMemoGeofenceByName(geoMemo).get(0);


        new HistAsyncTask(mDB, GMFactory.createGMHistory(mDB.geofenceMemoDao().loadMemoGeofenceByName(geoMemo).get(0))).execute(HistAsyncTask.INSERT);

    }

    public static void sendNotification(Context context, String geoName){

        AppDatabase mDB = AppDatabase.getInstance(context);



        if (lGeoMemo==null){
            //Log.d(LOG_TAG,"NULL Notification!!!!!!");
            return;
        }


        int NOTIFICATION_ID = 234;
        String GROUP_KEY_WORK_EMAIL = "com.android.example.WORK_EMAIL";

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);


        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            CharSequence name = "geomemo_channel";
            String Description = "GeoMemo channel";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel mChannel = new NotificationChannel(CHANNEL_ID, name, importance);
            mChannel.setDescription(Description);
            mChannel.enableLights(true);
            mChannel.setLightColor(Color.MAGENTA);

            mChannel.setShowBadge(false);
            notificationManager.createNotificationChannel(mChannel);
        }

        String n1 = String.valueOf(Math.random());
        String n2 = String.valueOf(Math.random());

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.mipmap.brain)
                .setContentTitle(geoName)
                .setContentText(lGeoMemo.get(0).getGeoMemo())
                .setStyle(new NotificationCompat.BigTextStyle()
                .bigText(lGeoMemo.get(0).getGeoMemo()))
                .setGroup(GROUP_KEY_WORK_EMAIL);



        Notification summaryNotification =
                new NotificationCompat.Builder(context, CHANNEL_ID)
                        .setContentTitle("GeoMemos")

                        .setSmallIcon(R.mipmap.brain)

                        .setGroup(GROUP_KEY_WORK_EMAIL)

                        .setGroupSummary(true)
                        .setNumber(notificationManager.getActiveNotifications().length)
                        .setAutoCancel(true)
                        .build();


        Intent resultIntent = new Intent(context, MenuMainActivity.class);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addParentStack(MenuMainActivity.class);
        stackBuilder.addNextIntent(resultIntent);


        PendingIntent resultPendingIntent =
                PendingIntent.getActivity(context, 0, new Intent(), 0);
        builder.setContentIntent(resultPendingIntent);

        builder.setAutoCancel(true);

        //Log.d(LOG_TAG, "sssssssssssssssss: " + n1 + "//" + n2);

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
                    //Log.d(LOG_TAG, "--removed--> " + geoMemoName.get(0));
                }
            })
            .addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    //Log.d(LOG_TAG, "--error removing--> " + geoMemoName.get(0));
                }
            });

    }

    public interface LoadMemo{
        void loaded(GeofenceMemo g);
    };

    public static void readMemo(final Context context, String geoName){
        AppDatabase mDB = AppDatabase.getInstance(context);
        LoadMemo observing = new LoadMemo() {
            @Override
            public void loaded(GeofenceMemo gm) {
                 readGeoMemo2(context, gm);
            }
        };

        new MemoAsyncTask(mDB, geoName, observing).execute(MemoAsyncTask.QUERY);
    }

    public static void sendMemo(final Context context, final String geoName){
        AppDatabase mDB = AppDatabase.getInstance(context);
        LoadMemo observing = new LoadMemo() {
            @Override
            public void loaded(GeofenceMemo gm) {
                lGeoMemo = new ArrayList<>();

                lGeoMemo.add(gm);

                sendNotification(context, geoName);
            }
        };
        new MemoAsyncTask(mDB, geoName,observing).execute(MemoAsyncTask.QUERY);
    }

    public static void readGeoMemo2(Context context, GeofenceMemo geoMemo){
        AppDatabase mDB = AppDatabase.getInstance(context);


        ArrayList<String> mGeoMemo = new ArrayList<String>();
        mGeoMemo.add(geoMemo.getGeoName());
        removeGeoMemo(context, mGeoMemo);

        // state activate --> deactivate

        new MemoAsyncTask(mDB, geoMemo.getGeoName()).execute(MemoAsyncTask.UPDATE);
        // delete from activates

        new GMActiveAsyncTask(mDB, geoMemo.getGeoName()).execute(GMActiveAsyncTask.DELETE);

        new HistAsyncTask(mDB, GMFactory.createGMHistory(geoMemo)).execute(HistAsyncTask.INSERT);


    }

    public static boolean checkText(String text){

        if ( !text.isEmpty() && text.length() <= GM_TEXT_LENGTH )
            return true;
        else
            return false;
    }


}
