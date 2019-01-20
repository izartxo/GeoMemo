package geomemo.app.code.develop.izartxo.geomemoapp.util;

import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import geomemo.app.code.develop.izartxo.geomemoapp.database.AppDatabase;
import geomemo.app.code.develop.izartxo.geomemoapp.database.GMActives;
import geomemo.app.code.develop.izartxo.geomemoapp.database.GeofenceMemo;

public class GMActiveAsyncTask extends AsyncTask<Integer, Void, List<GMActives>> {

    private static final String LOG_TAG = "******" + GMActiveAsyncTask.class.getSimpleName();

    public final static int INSERT = 0x01;
    public final static int QUERY = 0x02;
    public final static int DELETE = 0x03;
    public final static int UPDATE = 0x04;
    public final static int DELETE_ALL = 0x05;

    private AppDatabase mDB;
    private GMActives mGMActive;
    private String mGeoName;

    public GMActiveAsyncTask(AppDatabase db, GMActives gmActive){
        mDB = db;
        mGMActive = gmActive;
    }

    public GMActiveAsyncTask(AppDatabase db, String geoName){
        mDB = db;
        mGeoName = geoName;
    }


    @Override
    protected List<GMActives> doInBackground(Integer... operation){
        Log.d(LOG_TAG, "-------> DO IN BACKGROUND " + operation);
        List<GMActives> object = null;
        LiveData<List<GMActives>> getData = null;
         switch(operation[0]){
            case INSERT:
                mDB.gmActivesDao().insertGeoMemo(mGMActive);
                break;
            case QUERY:
                getData = mDB.gmActivesDao().getAll();
                for (GMActives g : getData.getValue()) {
                    if (g.getGeoName().equals(mGeoName)) {
                        object = new ArrayList<GMActives>();
                        object.add(g);
                    }


                }
                break;
            case DELETE:
                mDB.gmActivesDao().deleteGMActive(mGeoName);
                break;

                case DELETE_ALL:
                    mDB.gmActivesDao().deleteAll();
                 break;
            case UPDATE:

                break;
        }

        Log.d(LOG_TAG, "DIB FINISHED");

        return object;
    }

}
