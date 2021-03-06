package geomemo.app.code.develop.izartxo.geomemoapp.database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;
import android.database.Cursor;

import java.util.List;

@Dao
public interface GeofenceMemoDao {

    @Query("SELECT * FROM geofencememo")
    LiveData<List<GeofenceMemo>> getAll();

    @Query("SELECT * FROM geofencememo where active=1 order by creation_time DESC")
    Cursor getCursorAll();

    @Query("SELECT * FROM geofencememo WHERE geoName = :memoGeoName")
    List<GeofenceMemo> loadMemoGeofenceByName(String memoGeoName);

    @Query("SELECT * FROM geofencememo WHERE geoName = :memoGeoName")
    LiveData<List<GeofenceMemo>> getMemoGeofenceByName(String memoGeoName);



    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertGeoMemo(GeofenceMemo memo);



    @Query("UPDATE geofencememo SET active=0 WHERE geoname=:geoName")
    void updateGeoMemoState(String geoName);

    @Delete
    void delete(GeofenceMemo memos);

    @Query("DELETE FROM geofencememo")
    void deleteAll();

}
