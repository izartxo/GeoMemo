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
public interface GMHistoryDao {

    @Query("SELECT * FROM gmhistory order by history_time desc")
    LiveData<List<GMHistory>> getAll();

    @Query("SELECT * FROM gmhistory")
    Cursor getCursorAll();

    @Query("SELECT * FROM gmhistory WHERE geoName = :geoName")
    List<GMHistory> loadGMHistoryByName(String geoName);

    @Insert
    void insertAll(GMHistory... gmHistory);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertGMHistory(GMHistory memo);


    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateGMHistory(GMHistory gmHistory);

    @Delete
    void delete(GMHistory gmHistory);

    @Query("DELETE FROM gmhistory")
    void deleteAll();



}
