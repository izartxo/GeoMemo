package geomemo.app.code.develop.izartxo.geomemoapp.ui;

import android.Manifest;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingClient;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStates;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import geomemo.app.code.develop.izartxo.geomemoapp.R;
import geomemo.app.code.develop.izartxo.geomemoapp.database.AppDatabase;
import geomemo.app.code.develop.izartxo.geomemoapp.database.GMActives;
import geomemo.app.code.develop.izartxo.geomemoapp.database.GeofenceMemo;
import geomemo.app.code.develop.izartxo.geomemoapp.util.BroadcastService;
import geomemo.app.code.develop.izartxo.geomemoapp.util.GMActiveAsyncTask;
import geomemo.app.code.develop.izartxo.geomemoapp.util.GMFactory;
import geomemo.app.code.develop.izartxo.geomemoapp.util.GeoReceiver;
import geomemo.app.code.develop.izartxo.geomemoapp.util.LocationUtil;
import geomemo.app.code.develop.izartxo.geomemoapp.util.MemoAsyncTask;
import geomemo.app.code.develop.izartxo.geomemoapp.widget.GeoMemoAppWidgetProvider;


public class MenuMainActivity extends AppCompatActivity {

    private static final String LOG_TAG = "*******" + MenuMainActivity.class.getSimpleName();

    private static final int RESULT_FAILED = 2;

    private final static int PERMISSION = 0xFF;
    private static final int MAP_ACTIVITY_CODE = 0x01;
    private static final int SHOW_ACTIVITY_CODE = 0x02;
    private static final int HIST_ACTIVITY_CODE = 0x03;

    private static final int REQUEST_CHECK_SETTINGS = 0xAA;

    public static String GEOFENCE = "geomemo.app.code.develop.izartxo.geomemoapp.ui.GEOFENCE";

    private AppDatabase mDB;
    private static GeofencingClient mGeofencingClient;
    private PendingIntent mGeofencePendingIntent;



    @BindView(R.id.add_geomemo_button)
    Button bAdd;
    @OnClick(R.id.add_geomemo_button)
    public void addGeoMemo(View view){
        //Log.d(LOG_TAG, "add geomemo map button");
        view.setEnabled(false);
        startActivityGeoMemo(MapsActivity.class, MAP_ACTIVITY_CODE);
    }

    @BindView(R.id.show_geomemo_button)
    Button bShow;
    @OnClick(R.id.show_geomemo_button)
    public void showGeoMemo(View view){
        //Log.d(LOG_TAG, "show geomemo button");
        view.setEnabled(false);
        startActivityGeoMemo(ShowActivity.class, SHOW_ACTIVITY_CODE);
    }

    @BindView(R.id.history_geomemo_button)
    Button bHist;
    @OnClick(R.id.history_geomemo_button)
    public void historyGeoMemo(View view){
        //Log.d(LOG_TAG,"hist geomemo button");
        view.setEnabled(false);
        startActivityGeoMemo(HistActivity.class, HIST_ACTIVITY_CODE);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Log.d(LOG_TAG, "ONCREATE");
        setContentView(R.layout.activity_main_menu);

        mDB = AppDatabase.getInstance(getApplicationContext());



        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        toolbar.setTitle(getString(R.string.app_name));


        setSupportActionBar(toolbar);


        ButterKnife.bind(this);



        startGeoMemoService();
        configureViewModel();


    }

    private void enableButtons(){
        bAdd.setEnabled(true);
        bShow.setEnabled(true);
        bHist.setEnabled(true);
    }

    @Override
    protected void onPause(){
        super.onPause();
        //Log.d(LOG_TAG, "ONPAUSE");

    }

    @Override
    protected void onResume(){
        super.onResume();
        //Log.d(LOG_TAG, "ONRESUME");
        enableButtons();

        statusCheck();

    }

    private void startActivityGeoMemo(Class name, int activityCode){

        startActivityForResult(new Intent(this, name), activityCode);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //Log.d(LOG_TAG, "OnActivityResult: " + requestCode + " - " + resultCode);

        switch(requestCode){
            case MAP_ACTIVITY_CODE:
                if (resultCode == RESULT_OK){
                    geoMemo2Room(data);
                }else
                    showSnackBar(resultCode);
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

                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    //Log.d(LOG_TAG, "Permission granted");

                } else {

                    //Log.d(LOG_TAG, "Permission denied");
                }
                return;
            }


        }
    }

    // ROOM operations on back from activities
    private void geoMemo2Room(Intent data){
        //Log.d(LOG_TAG, "Geomemo2Room");
        try{
            GeofenceMemo geofenceMemo = GMFactory.createGeoMemo(data.getStringExtra("geoname"),
                                                                data.getStringExtra("geomemo"),
                                                                data.getStringExtra("latitude"),
                                                                data.getStringExtra("longitude"));

            // Insert new GeoMemo

            new MemoAsyncTask(mDB, geofenceMemo).execute(MemoAsyncTask.INSERT);
            // Insert new GeoMemo on GMActives because of is active
            new GMActiveAsyncTask(mDB, GMFactory.createGMActive(geofenceMemo)).execute(GMActiveAsyncTask.INSERT);


            showSnackBar(RESULT_OK);
            GeoMemoAppWidgetProvider.updateMyWidgets(this);

        }catch(Exception e){
            //Log.d(LOG_TAG, "ERROR geoMemo2Room: " + e.getMessage());
            showSnackBar(RESULT_FAILED);
        }
    }
    //

    private void showSnackBar(int result){

        //Log.d(LOG_TAG, "SNACKBAR: " + result);
        String message = "";

        switch (result){
            case RESULT_CANCELED:
                message = getString(R.string.snackbar_messge_nothing);
                break;
            case RESULT_OK:
                message = getString(R.string.snackbar_messge_ok);
                break;
            case RESULT_FAILED:
                message = getString(R.string.snackbar_messge_notok);
        }
        final Snackbar snackbar = Snackbar.make(getWindow().getDecorView().getRootView(), message, Snackbar.LENGTH_LONG);
        snackbar.show();
    }


    private void checkPermissions(){
        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted
            //Log.d(LOG_TAG, "CHECK PERMISSION - REQUEST ON COURSE");
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    PERMISSION);

        }
        else{
            ; //Log.d(LOG_TAG, "CHECK PERMISSION - OK");

        }
    }

    private void configureViewModel(){
        GMActivesViewModel gmActivesViewModel = ViewModelProviders.of(this).get(GMActivesViewModel.class);

        gmActivesViewModel.getGeoMemoActivesList().observe(this, new Observer<List<GMActives>>() {
            @Override
            public void onChanged(@Nullable List<GMActives> activesGeoMemos) {
                //Log.d(LOG_TAG, "Updating Database changes activesGeoMemos....." + activesGeoMemos.size());
                registerAllActives(activesGeoMemos);

            }
        });
    }

    private void registerAllActives(List<GMActives> actives){

        if (actives==null || actives.size()<=0)
            return;
        List<Geofence> mGeofenceList = new ArrayList<>();

        mGeofencingClient = LocationServices.getGeofencingClient(this);

        for (GMActives gmActives : actives){
            mGeofenceList.add(new Geofence.Builder()

                    .setRequestId(gmActives.getGeoName())

                    .setCircularRegion(
                            Double.valueOf(gmActives.getGeoLatitude()),
                            Double.valueOf(gmActives.getGeoLongitude()),
                            100 // Circular region radius
                    )
                    .setExpirationDuration(-1)
                    .setLoiteringDelay(1)
                    .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_DWELL | Geofence.GEOFENCE_TRANSITION_ENTER |
                            Geofence.GEOFENCE_TRANSITION_EXIT)

                    .build());

        }

        //Log.d(LOG_TAG, "############################### " + mGeofenceList.size());

        mGeofencingClient.addGeofences(getGeofencingRequest(mGeofenceList), getGeofencePendingIntent())
                .addOnSuccessListener(this, new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

          //              Log.d(LOG_TAG, "Geofences added successfully");

                    }
                })
                .addOnFailureListener(this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

            //            Log.d(LOG_TAG, "ERROR: " + e.getMessage().toString());
                    }
                });

    }

    private GeofencingRequest getGeofencingRequest(List<Geofence> mGeofenceList) {


        GeofencingRequest.Builder builder = new GeofencingRequest.Builder();
        builder.setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER);
        builder.addGeofences(mGeofenceList);
        return builder.build();
    }

    private PendingIntent getGeofencePendingIntent() {

        if (mGeofencePendingIntent != null) {
            return mGeofencePendingIntent;
        }


        Intent intent = new Intent(GeoReceiver.GEOFENCE);


        PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        return pendingIntent;
    }

    private void startGeoMemoService(){
        //Log.d(LOG_TAG, "START GEOMEMO SERVICE");
        ComponentName myService = new ComponentName(this, BroadcastService.class);
        JobInfo myJob = new JobInfo.Builder(BroadcastService.BROADCAST_RECEIVER_ID, myService)
                .setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY)

                .build();


        JobScheduler jobScheduler = (JobScheduler) getSystemService(Context.JOB_SCHEDULER_SERVICE);
        jobScheduler.schedule(myJob);

    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();


        if (id == android.R.id.home) {
            Toast.makeText(MenuMainActivity.this, "Action clicked", Toast.LENGTH_LONG).show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }



    public void statusCheck(){
        final LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        final WifiManager wmanager = (WifiManager) getSystemService(Context.WIFI_SERVICE);

        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER) || !wmanager.isWifiEnabled()){
            buildAlertMessageNoGpsWifi(wmanager.isWifiEnabled()?true:false);
        }
    }

    private void buildAlertMessageNoGpsWifi(final boolean wifi){
        TextView customTitle = new TextView(this);
        customTitle.setText(getString(R.string.dialog_permission_title));
        customTitle.setBackgroundColor(getColor(R.color.GMcolorAccent));
        customTitle.setTextColor(getColor(R.color.GMcolorText));
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCustomTitle(customTitle)
                .setCancelable(false)
                .setPositiveButton(getString(R.string.dialog_permission_settings), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (wifi)
                            startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                        else
                            startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
                    }
                })
                .setNegativeButton(getString(R.string.dialog_permission_cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }

}