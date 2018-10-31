package geomemo.app.code.develop.izartxo.geomemoapp.ui;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import geomemo.app.code.develop.izartxo.geomemoapp.R;
import geomemo.app.code.develop.izartxo.geomemoapp.database.AppDatabase;
import geomemo.app.code.develop.izartxo.geomemoapp.database.GeofenceMemo;
import geomemo.app.code.develop.izartxo.geomemoapp.util.GMFactory;


public class MenuMainActivity extends AppCompatActivity {

    private static final String LOG_TAG = "*******" + MenuMainActivity.class.getSimpleName();

    private final static int PERMISSION = 0xFF;
    private static final int MAP_ACTIVITY_CODE = 0x01;
    private static final int SHOW_ACTIVITY_CODE = 0x02;
    private static final int HIST_ACTIVITY_CODE = 0x03;


    private AppDatabase mDB;


    @BindView(R.id.add_geomemo_button)
    Button bAdd;
    @OnClick(R.id.add_geomemo_button)
    public void addGeoMemo(View view){
        Log.d(LOG_TAG, "add geomemo map button");
        startActivityGeoMemo(MapActivity.class, MAP_ACTIVITY_CODE);
    }

    @BindView(R.id.show_geomemo_button)
    Button bShow;
    @OnClick(R.id.show_geomemo_button)
    public void showGeoMemo(View view){
        Log.d(LOG_TAG, "show geomemo button");
        startActivityGeoMemo(ShowActivity.class, SHOW_ACTIVITY_CODE);
    }

    @BindView(R.id.history_geomemo_button)
    Button bHist;
    @OnClick(R.id.history_geomemo_button)
    public void historyGeoMemo(View view){
        //Log.d(LOG_TAG,"hist" + mDB.geofenceMemoDao().getAll().get(0).getGeoName());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(LOG_TAG, "ONCREATE");
        setContentView(R.layout.activity_main_menu);

        mDB = AppDatabase.getInstance(getApplicationContext());

        ButterKnife.bind(this);



        //GeofenceMemo geoMemo = GMFactory.createGeoMemo("gorka_place","animo animo animo","43.2289146","-2.8491351");

        //mDB.geofenceMemoDao().updateGeoMemo(geoMemo);
    }

    @Override
    protected void onPause(){
        super.onPause();
        Log.d(LOG_TAG, "ONPAUSE");
    }

    @Override
    protected void onResume(){
        super.onResume();
        Log.d(LOG_TAG, "ONRESUME");
        checkPermissions();

    }

    private void startActivityGeoMemo(Class name, int activityCode){

        startActivityForResult(new Intent(this, name), activityCode);



    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Log.d(LOG_TAG, "OnActivityResult: " + requestCode + " - " + resultCode);

        switch(requestCode){
            case MAP_ACTIVITY_CODE:
                if (resultCode == RESULT_OK){
                    geoMemo2Room(data);
                }else
                    showSnackBar(0);
                break;
            case SHOW_ACTIVITY_CODE:
                break;
            case HIST_ACTIVITY_CODE:
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    Log.d(LOG_TAG, "Permission granted");

                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Log.d(LOG_TAG, "Permission denied");
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request.
        }
    }

    // ROOM operations on back from activities
    private void geoMemo2Room(Intent data){
        Log.d(LOG_TAG, "Geomemo2Room");
        try{
            GeofenceMemo geofenceMemo = GMFactory.createGeoMemo(data.getStringExtra("geoname"),
                                                                data.getStringExtra("geomemo"),
                                                                data.getStringExtra("latitude"),
                                                                data.getStringExtra("longitude"));
            mDB.geofenceMemoDao().insertGeoMemo(geofenceMemo);
            showSnackBar(1);

        }catch(Exception e){
            Log.d(LOG_TAG, "ERROR geoMemo2Room: " + e.getMessage());
            showSnackBar(0);
        }
    }
    //

    private void showSnackBar(int result){

        Log.d(LOG_TAG, "SNACKBAR: " + result);
        String message = result == 0 ? "Data store fails" : "Data store OK";
        final Snackbar snackbar = Snackbar.make(getWindow().getDecorView().getRootView(), message, Snackbar.LENGTH_LONG);
        snackbar.show();
    }

    // Begiratu ez dakit ondo dabilen bi baimenak esaktzean bat jarrita aurrera doa...
    private void checkPermissions(){
        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted
            Log.d(LOG_TAG, "CHECK PERMISSION - REQUEST ON COURSE");
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    PERMISSION);

        }
        else{
            Log.d(LOG_TAG, "CHECK PERMISSION - OK");

        }
    }

}