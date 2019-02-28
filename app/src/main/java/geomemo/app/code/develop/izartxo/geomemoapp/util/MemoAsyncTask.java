package geomemo.app.code.develop.izartxo.geomemoapp.util;

import android.os.AsyncTask;
import android.util.Log;

import java.util.List;

import geomemo.app.code.develop.izartxo.geomemoapp.database.AppDatabase;
import geomemo.app.code.develop.izartxo.geomemoapp.database.GeofenceMemo;

public class MemoAsyncTask extends AsyncTask<Integer, Void, List<GeofenceMemo>> {

    public final static int INSERT = 0x01;
    public final static int QUERY = 0x02;
    public final static int DELETE = 0x03;
    public final static int UPDATE = 0x04;


    private static final String LOG_TAG = "******" + MemoAsyncTask.class.getSimpleName();
    private AppDatabase mDB;
    private GeofenceMemo mGeofenceMemo;
    private String mGeoName;
    private GMFactory.LoadMemo mloadMemo;

    public MemoAsyncTask(AppDatabase db, GeofenceMemo geofenceMemo){
        mDB = db;
        mGeofenceMemo = geofenceMemo;
    }

    public MemoAsyncTask(AppDatabase db, String geofenceMemoName){
        mDB = db;
        mGeoName = geofenceMemoName;
    }

    public MemoAsyncTask(AppDatabase db, String geofenceMemoName, GMFactory.LoadMemo loadMemo){
        mDB = db;
        mGeoName = geofenceMemoName;
        mloadMemo = loadMemo;
    }



    @Override
    protected  List<GeofenceMemo> doInBackground(Integer... operation){
        //Log.d(LOG_TAG, "-------> DO IN BACKGROUND " + operation[0]);
        List<GeofenceMemo> object = null;
        switch(operation[0]){
            case INSERT:
                    mDB.geofenceMemoDao().insertGeoMemo(mGeofenceMemo);
                break;
            case QUERY:
                    object =  mDB.geofenceMemoDao().loadMemoGeofenceByName(mGeoName);
                 break;
            case DELETE:
                    mDB.geofenceMemoDao().deleteAll();
                break;
            case UPDATE:
                    mDB.geofenceMemoDao().updateGeoMemoState(mGeoName);
                break;
        }

        //Log.d(LOG_TAG, "DIB FINISHED");

        return object;
    }

    protected void  onPostExecute(List<GeofenceMemo> result) {

        try{
        if (result!=null)
            mloadMemo.loaded( result.get(0));
        }catch(Exception e){

        }
    }

}
