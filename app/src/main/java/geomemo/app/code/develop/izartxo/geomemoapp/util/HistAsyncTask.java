package geomemo.app.code.develop.izartxo.geomemoapp.util;

import android.os.AsyncTask;
import android.util.Log;

import java.util.List;

import geomemo.app.code.develop.izartxo.geomemoapp.database.AppDatabase;
import geomemo.app.code.develop.izartxo.geomemoapp.database.GMActives;
import geomemo.app.code.develop.izartxo.geomemoapp.database.GMHistory;
import geomemo.app.code.develop.izartxo.geomemoapp.database.GeofenceMemo;

public class HistAsyncTask extends AsyncTask<Integer, Void, List<GMHistory>> {

    private static final String LOG_TAG = "******" + HistAsyncTask.class.getSimpleName();

    public final static int INSERT = 0x01;
    public final static int QUERY = 0x02;
    public final static int DELETE = 0x03;
    public final static int UPDATE = 0x04;

    private AppDatabase mDB;
    private GMHistory mGMHistory;

    public HistAsyncTask(AppDatabase db, GMHistory gmHistory){
        mDB = db;
        mGMHistory = gmHistory;
    }

    @Override
    protected List<GMHistory> doInBackground(Integer... operation){
        //Log.d(LOG_TAG, "-------> DO IN BACKGROUND " + operation);
        List<GMHistory> object = null;
        switch(operation[0]){
            case INSERT:
                mDB.gmHistoryDao().insertGMHistory(mGMHistory);
                break;
            case QUERY:

                break;
            case DELETE:
                mDB.gmHistoryDao().deleteAll();
                break;
            case UPDATE:

                break;
        }

        //Log.d(LOG_TAG, "DIB FINISHED");

        return object;
    }



}
