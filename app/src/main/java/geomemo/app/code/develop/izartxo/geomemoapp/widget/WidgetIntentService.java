package geomemo.app.code.develop.izartxo.geomemoapp.widget;

import android.app.IntentService;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.HashMap;

import geomemo.app.code.develop.izartxo.geomemoapp.database.GeofenceMemo;

public class WidgetIntentService extends IntentService {


    public WidgetIntentService() {
        super("GeoMemoAppWidget");
    }



    @Override
    public void setIntentRedelivery(boolean enabled) {
        super.setIntentRedelivery(enabled);
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public void onStart(@Nullable Intent intent, int startId) {
        super.onStart(intent, startId);
    }

    @Override
    public int onStartCommand(@Nullable Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return super.onBind(intent);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

        Uri geoMemoUri = GeoMemoProvider.BASE_CONTENT_URI;
        Log.d("WIDGENT", "---> " + geoMemoUri);
        Cursor cursor = getContentResolver().query(geoMemoUri,
                null,
                null,
                null,
                null);



        HashMap<String,String> hm = new HashMap<>();


        cursor.moveToFirst();

        while (!cursor.isAfterLast())
        {


            hm.put("test 1","test 2");

            cursor.moveToNext();
        }


        GeoMemoAppWidgetProvider.updateWidget(cursor, getApplicationContext(), hm);
    }
}

