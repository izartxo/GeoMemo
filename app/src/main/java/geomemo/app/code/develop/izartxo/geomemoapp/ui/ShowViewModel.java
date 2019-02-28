package geomemo.app.code.develop.izartxo.geomemoapp.ui;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;
import android.util.Log;

import java.util.List;

import geomemo.app.code.develop.izartxo.geomemoapp.database.AppDatabase;
import geomemo.app.code.develop.izartxo.geomemoapp.database.GMActives;
import geomemo.app.code.develop.izartxo.geomemoapp.database.GeofenceMemo;


public class ShowViewModel extends AndroidViewModel {


    private static final String LOG_TAG = "*******" + ShowViewModel.class.getSimpleName();

    LiveData<List<GMActives>> geoMemoList;

    public ShowViewModel(@NonNull Application application) {
        super(application);

        AppDatabase mDB = AppDatabase.getInstance(this.getApplication());
        //Log.d(LOG_TAG, "Retrieving data from database");
        geoMemoList = mDB.gmActivesDao().getAll();
    }

    public LiveData<List<GMActives>> getGeoMemoList() {
        return geoMemoList;
    }

}
