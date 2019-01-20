package geomemo.app.code.develop.izartxo.geomemoapp.ui;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;
import android.util.Log;

import java.util.List;

import geomemo.app.code.develop.izartxo.geomemoapp.database.AppDatabase;
import geomemo.app.code.develop.izartxo.geomemoapp.database.GeofenceMemo;


public class GeofenceMemoViewModel extends AndroidViewModel {
    private static final String LOG_TAG = "*******" + GeofenceMemoViewModel.class.getSimpleName();

    LiveData<List<GeofenceMemo>> geoMemoActivesList;


    public GeofenceMemoViewModel(@NonNull Application application) {
        super(application);

        AppDatabase mDB = AppDatabase.getInstance(this.getApplication());
        Log.d(LOG_TAG, "Retrieving data from database");
        geoMemoActivesList = mDB.geofenceMemoDao().getAll();

    }

    public LiveData<List<GeofenceMemo>> getMemoGeofenceByName() {
        return geoMemoActivesList;
    }
}

