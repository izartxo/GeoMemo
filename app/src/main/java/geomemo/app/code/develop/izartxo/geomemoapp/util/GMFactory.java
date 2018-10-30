package geomemo.app.code.develop.izartxo.geomemoapp.util;

import android.os.SystemClock;

import geomemo.app.code.develop.izartxo.geomemoapp.database.GMActives;
import geomemo.app.code.develop.izartxo.geomemoapp.database.GMHistory;
import geomemo.app.code.develop.izartxo.geomemoapp.database.GeofenceMemo;

public class GMFactory {

    private static int GM_DEFAULT_SIZE = 1; // Short size
    private static boolean GM_DEFAULT_ACTIVE = false; // Not active on creation


    /*
    * Creation of GeoMemo from pased data
    *
    *
    * */
    public static GeofenceMemo createGeoMemo(
            String geoName,
            String geoMemo,
            String geoLatitude,
            String geoLongitude)
    {
        GeofenceMemo gm = new GeofenceMemo();
        gm.setGeoName(geoName);
        gm.setGeoMemo(geoMemo);
        gm.setGeoTimestamp(String.valueOf(System.currentTimeMillis()));
        gm.setGeoLatitude(geoLatitude);
        gm.setGeoLongitude(geoLongitude);
        gm.setGeoSize(GM_DEFAULT_SIZE);
        gm.setGeoActive(GM_DEFAULT_ACTIVE);

        return gm;
    }

    /*
    * Creation of GMActive to fill active GeoMemos table
    *
    *
    * */
    public static GMActives createGMActive(
            GeofenceMemo gm)
    {
        GMActives gma = new GMActives();
        gma.setGeoName(gm.getGeoName());
        gma.setGeoMemo(gm.getGeoMemo());
        gma.setGeoLatitude(gm.getGeoLatitude());
        gma.setGeoLongitude(gm.getGeoLongitude());


        return gma;
    }

    /*
    * Creation of GMHistory to store on DB
    *
    *
    * */
    public static GMHistory createGMHistory(
            GeofenceMemo gm)
    {
        GMHistory gmh = new GMHistory();
        gmh.setGeoName(gm.getGeoName());
        gmh.setGeoMemo(gm.getGeoMemo());
        gmh.setGeoTimestamp(gm.getGeoTimestamp());
        gmh.setGeoHistoryTime(String.valueOf(System.currentTimeMillis()));
        gmh.setGeoLatitude(gm.getGeoLatitude());
        gmh.setGeoLongitude(gm.getGeoLongitude());

        return gmh;
    }

}
