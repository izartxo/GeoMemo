package geomemo.app.code.develop.izartxo.geomemoapp.util;

import android.os.AsyncTask;
import android.util.Log;

import geomemo.app.code.develop.izartxo.geomemoapp.database.AppDatabase;
import geomemo.app.code.develop.izartxo.geomemoapp.database.GeofenceMemo;

public class HistAsyncTask extends AsyncTask<Void, Void, Void> {

    private static final String LOG_TAG = "******" + HistAsyncTask.class.getSimpleName();
    private AppDatabase mDB;


    public HistAsyncTask(AppDatabase db){
        mDB = db;

    }

    @Override
    protected Void doInBackground(Void... url){
        Log.d(LOG_TAG, "-------> DO IN BACKGROUND");

        mDB.gmHistoryDao().deleteAll();


        Log.d(LOG_TAG, "DIB FINISHED");
        return null;
    }

}
