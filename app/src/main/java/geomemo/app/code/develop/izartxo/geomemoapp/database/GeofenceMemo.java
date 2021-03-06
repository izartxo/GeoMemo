package geomemo.app.code.develop.izartxo.geomemoapp.database;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity(tableName = GeofenceMemo.TABLE_GEOFENCEMEMO)
public class GeofenceMemo{

    public static final String TABLE_GEOFENCEMEMO = "geofencememo";

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "geoname")
    private String geoName;

    @ColumnInfo(name = "geomemo")
    private String geoMemo;

    @ColumnInfo(name = "creation_time")
    private String geoTimestamp;

    @ColumnInfo(name = "latitude")
    private String geoLatitude;

    @ColumnInfo(name = "longitude")
    private String geoLongitude;

    @ColumnInfo(name = "active")
    private boolean geoActive;

    @ColumnInfo(name = "size")
    private int geoSize;

    @NonNull
    public String getGeoName() {
        return geoName;
    }
    @NonNull
    public void setGeoName(String geoName) {
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

    public boolean getGeoActive() {
        return geoActive;
    }

    public void setGeoActive(boolean geoActive) {
        this.geoActive = geoActive;
    }

    public int getGeoSize() {
        return geoSize;
    }

    public void setGeoSize(int geoSize) {
        this.geoSize = geoSize;
    }
}

