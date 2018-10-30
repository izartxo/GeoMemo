package geomemo.app.code.develop.izartxo.geomemoapp.database;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity
public class GMHistory {

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "geoname")
    private String geoName;

    @ColumnInfo(name = "geomemo")
    private String geoMemo;

    @ColumnInfo(name = "creation_time")
    private String geoTimestamp;

    @ColumnInfo(name = "history_time")
    private String geoHistoryTime;

    @ColumnInfo(name = "latitude")
    private String geoLatitude;

    @ColumnInfo(name = "longitude")
    private String geoLongitude;

    @NonNull
    public String getGeoName() {
        return geoName;
    }

    public void setGeoName(@NonNull String geoName) {
        this.geoName = geoName;
    }

    public String getGeoMemo() {
        return geoMemo;
    }

    public void setGeoMemo(String geoMemo) {
        this.geoMemo = geoMemo;
    }

    public String getGeoTimestamp() {
        return geoTimestamp;
    }

    public void setGeoTimestamp(String geoTimestamp) {
        this.geoTimestamp = geoTimestamp;
    }

    public String getGeoHistoryTime() {
        return geoHistoryTime;
    }

    public void setGeoHistoryTime(String geoHistoryTime) {
        this.geoHistoryTime = geoHistoryTime;
    }

    public String getGeoLatitude() {
        return geoLatitude;
    }

    public void setGeoLatitude(String geoLatitude) {
        this.geoLatitude = geoLatitude;
    }

    public String getGeoLongitude() {
        return geoLongitude;
    }

    public void setGeoLongitude(String geoLongitude) {
        this.geoLongitude = geoLongitude;
    }
}
