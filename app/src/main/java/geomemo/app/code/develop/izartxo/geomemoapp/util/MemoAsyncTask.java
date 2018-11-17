package geomemo.app.code.develop.izartxo.geomemoapp.util;

import android.os.AsyncTask;
import android.util.Log;

import geomemo.app.code.develop.izartxo.geomemoapp.database.AppDatabase;
import geomemo.app.code.develop.izartxo.geomemoapp.database.GeofenceMemo;

public class MemoAsyncTask extends AsyncTask<Void, Void, Void> {

    private static final String LOG_TAG = "******" + MemoAsyncTask.class.getSimpleName();
    private AppDatabase mDB;
    private GeofenceMemo mGeofenceMemo;

    public MemoAsyncTask(AppDatabase db, GeofenceMemo geofenceMemo){
        mDB = db;
        mGeofenceMemo = geofenceMemo;
    }

    @Override
    protected Void doInBackground(Void... url){
        Log.d(LOG_TAG, "-------> DO IN BACKGROUND");


        mDB.geofenceMemoDao().insertGeoMemo(mGeofenceMemo);


        Log.d(LOG_TAG, "DIB FINISHED");
        return null;
    }

}
