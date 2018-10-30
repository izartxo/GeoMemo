package geomemo.app.code.develop.izartxo.geomemoapp.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.util.Log;

@Database(entities = {GeofenceMemo.class, GMActives.class, GMHistory.class}, version = 1, exportSchema = false)

public abstract class AppDatabase extends RoomDatabase {
    private static final String TAG = AppDatabase.class.getSimpleName();
    private static final String DATABSE_NAME = "geomemodb";
    private static final Object LOCK = new Object();
    //private static volatile AppDatabase INSTANCE; // mirar lo de volatile porque en los videos no sale asi.
    private static AppDatabase sInstance;


    //public abstract GeofenceMemoDao memosDao();

    public static AppDatabase getInstance(Context context) {
        if (sInstance == null) {
            //synchronized (AppDatabase.class) {
            synchronized (LOCK) {
                //if (INSTANCE == null) {
                    // Create database here
                    Log.d(TAG, "Creating database instance");
                    sInstance = Room.databaseBuilder(context, AppDatabase.class, AppDatabase.DATABSE_NAME)
                            .allowMainThreadQueries() //WARNING!!!!!
                            .build();
                }
            }
        return sInstance;
    }

    public abstract GeofenceMemoDao geofenceMemoDao();
    public abstract GMActivesDao gmActivesDao();
    public abstract GMHistoryDao gmHistoryDao();

}



