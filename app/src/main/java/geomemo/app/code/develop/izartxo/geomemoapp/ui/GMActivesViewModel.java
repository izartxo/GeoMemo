package geomemo.app.code.develop.izartxo.geomemoapp.ui;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;
import android.util.Log;

import java.util.List;

import geomemo.app.code.develop.izartxo.geomemoapp.database.AppDatabase;
import geomemo.app.code.develop.izartxo.geomemoapp.database.GMActives;


public class GMActivesViewModel extends AndroidViewModel {
    private static final String LOG_TAG = "*******" + GMActivesViewModel.class.getSimpleName();

    LiveData<List<GMActives>> geoMemoActivesList;

    public GMActivesViewModel(@NonNull Application application) {
        super(application);

        AppDatabase mDB = AppDatabase.getInstance(this.getApplication());
        Log.d(LOG_TAG, "Retrieving data from database");
        geoMemoActivesList = mDB.gmActivesDao().getAll();
    }

    public LiveData<List<GMActives>> getGeoMemoActivesList() {
        return geoMemoActivesList;
    }
}
