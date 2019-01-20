package geomemo.app.code.develop.izartxo.geomemoapp.widget;


import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import geomemo.app.code.develop.izartxo.geomemoapp.database.AppDatabase;
import geomemo.app.code.develop.izartxo.geomemoapp.database.GeofenceMemo;

public class GeoMemoProvider extends ContentProvider {

    // My Content Authority
    public static final String AUTHORITY = "geomemo.app.code.develop.izartxo.geomemoapp";

    /* The URI for the Ingredient List Table */
    public static final Uri BASE_CONTENT_URI = Uri.parse(
            "content://" + AUTHORITY + "/" + GeofenceMemo.TABLE_GEOFENCEMEMO);

    private AppDatabase mDatabase;

    // Table access ID's
    public static final int GEOFENCEMEMO_STORAGE = 100;


    // Default is no matches
    private static final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    static{
        // For whole Table access
        // For each value access
        uriMatcher
                .addURI(AUTHORITY, GeofenceMemo.TABLE_GEOFENCEMEMO, GEOFENCEMEMO_STORAGE);
        Uri geoMemoUri = Uri.parse("content://geomemo.app.code.develop.izartxo.geomemoapp/geofencememo");
        Log.d("XXXXXXXXXXXXXXx", "---> " + uriMatcher.match(geoMemoUri));
    }


    @Override
    public boolean onCreate() {
        mDatabase = AppDatabase.getInstance(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri,
                        @Nullable String[] projection,
                        @Nullable String selection,
                        @Nullable String[] selectionArgs, @Nullable String sortOrder) {

        int matcher = uriMatcher.match(uri);
        if(matcher == GEOFENCEMEMO_STORAGE){
            Context context = getContext();
            if(context == null){

                return null;
            }

            final Cursor cursor;
            if(matcher != GEOFENCEMEMO_STORAGE){
                //cursor = mDatabase.geofenceMemoDao().getCursorAll();
                return null; // null because wholesome query is done with LiveData


            }else{

                cursor = mDatabase.geofenceMemoDao().getCursorAll();

                while (!cursor.isLast()){
                    cursor.moveToNext();
                    Log.d("WWWWWWWWWWWWWW", "------> " + cursor.getString(0) + cursor.getString(1));
                }
            }

            cursor.setNotificationUri(context.getContentResolver(), uri);
            return cursor;

        }else{

            throw new IllegalArgumentException("Unknown Uri: "+uri);
        }


    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        int matcher = uriMatcher.match(uri);
        switch (matcher){
            case GEOFENCEMEMO_STORAGE:

                return ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + AUTHORITY + "." + GeofenceMemo.TABLE_GEOFENCEMEMO;

            default:
                throw new IllegalArgumentException(
                        "Can't retrieve database selections: "
                                + uri
                                + " according to matcher, "
                                + matcher);

        }
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        return null;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String s, @Nullable String[] strings) {
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String s, @Nullable String[] strings) {
        return 0;
    }

}


/*public class GeoMemoProvider{} extends ContentProvider {

    private static final String TABLE_NAME = "gmactives";

    *//** The authority of this content provider. *//*
    public static final String AUTHORITY = "geomemo.app.code.develop.izartxo.geomemoapp.provider";

    *//** The URI for the Cheese table. *//*
    public static final Uri URI_GMACTIVES = Uri.parse(
            "content://" + AUTHORITY + "/" + TABLE_NAME);

    *//** The match code for some items in the Cheese table. *//*
    private static final int CODE_CHEESE_DIR = 1;

    *//** The match code for an item in the Cheese table. *//*
    private static final int CODE_CHEESE_ITEM = 2;

    *//** The URI matcher. *//*
    private static final UriMatcher MATCHER = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        MATCHER.addURI(AUTHORITY, TABLE_NAME, CODE_CHEESE_DIR);
        MATCHER.addURI(AUTHORITY, TABLE_NAME + "/*", CODE_CHEESE_ITEM);
    }

    private Context mContext;


    public boolean onCreate(Context context) {
        mContext = context;
        return true;
    }



    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection,
                        @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        final int code = MATCHER.match(uri);
        if (code == CODE_CHEESE_DIR || code == CODE_CHEESE_ITEM) {

            if (mContext == null) {
                return null;
            }
            GMActivesDao gmActivesDao = AppDatabase.getInstance(mContext).gmActivesDao();
            final Cursor cursor;
            //if (code == CODE_CHEESE_DIR) {
                cursor = gmActivesDao.getCursorAll();
            //} else {
            //    cursor = cheese.selectById(ContentUris.parseId(uri));
            //}
            cursor.setNotificationUri(mContext.getContentResolver(), uri);
            return cursor;
        } else {
            throw new IllegalArgumentException("Unknown URI: " + uri);
        }
    }



    public String getType(@NonNull Uri uri) {
        switch (MATCHER.match(uri)) {
            case CODE_CHEESE_DIR:
                return "vnd.android.cursor.dir/" + AUTHORITY + "." + TABLE_NAME;
            case CODE_CHEESE_ITEM:
                return "vnd.android.cursor.item/" + AUTHORITY + "." + TABLE_NAME;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
    }

}
*/