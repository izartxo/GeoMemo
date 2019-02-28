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
public interface GMActivesDao {

    @Query("SELECT * FROM gmactives order by creation_time desc")
    LiveData<List<GMActives>> getAll();





    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertGeoMemo(GMActives activeMemo);



    @Query("DELETE FROM gmactives WHERE geoname=:geoName")
    void deleteGMActive(String geoName);

    @Delete
    void delete(GMActives gmActives);

    @Query("DELETE FROM gmactives")
    void deleteAll();


}
