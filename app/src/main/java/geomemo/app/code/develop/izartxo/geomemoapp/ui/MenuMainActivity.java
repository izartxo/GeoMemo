package geomemo.app.code.develop.izartxo.geomemoapp.ui;

import android.Manifest;
import android.app.PendingIntent;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
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
import android.widget.Toast;


import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingClient;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
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
        Log.d(LOG_TAG, "add geomemo map button");
        view.setEnabled(false);
        startActivityGeoMemo(MapsActivity.class, MAP_ACTIVITY_CODE);
    }

    @BindView(R.id.show_geomemo_button)
    Button bShow;
    @OnClick(R.id.show_geomemo_button)
    public void showGeoMemo(View view){
        Log.d(LOG_TAG, "show geomemo button");
        view.setEnabled(false);
        startActivityGeoMemo(ShowActivity.class, SHOW_ACTIVITY_CODE);
    }

    @BindView(R.id.history_geomemo_button)
    Button bHist;
    @OnClick(R.id.history_geomemo_button)
    public void historyGeoMemo(View view){
        Log.d(LOG_TAG,"hist geomemo button");
        view.setEnabled(false);
        startActivityGeoMemo(HistActivity.class, HIST_ACTIVITY_CODE);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(LOG_TAG, "ONCREATE");
        setContentView(R.layout.activity_main_menu);

        mDB = AppDatabase.getInstance(getApplicationContext());

        //mDB.gmActivesDao().deleteAll();
        //mDB.geofenceMemoDao().deleteAll();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        toolbar.setTitle("GeoMemoApp");

        setSupportActionBar(toolbar);


        ButterKnife.bind(this);

        // !!!! Tranpa dago eginda hemen birpasatu
        if (false && (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)) {
            startForegroundService(new Intent(this,BroadcastService.class));
        }
        else {
            startGeoMemoService();
        }
        configureViewModel();

       // activateSettings();

    }

    private void enableButtons(){
        bAdd.setEnabled(true);
        bShow.setEnabled(true);
        bHist.setEnabled(true);
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
        enableButtons();
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

            // Insert new GeoMemo
            //mDB.geofenceMemoDao().insertGeoMemo(geofenceMemo);
            new MemoAsyncTask(mDB, geofenceMemo).execute(MemoAsyncTask.INSERT);
            // Insert new GeoMemo on GMActives because of is active
            new GMActiveAsyncTask(mDB, GMFactory.createGMActive(geofenceMemo)).execute(GMActiveAsyncTask.INSERT);
            //mDB.gmActivesDao().insertGeoMemo(GMFactory.createGMActive(geofenceMemo));

            // Intent que no afecta al widget...
            //Intent i = new Intent(GeoMemoAppWidgetProvider.APP_UPD);
            //sendBroadcast(i);

            showSnackBar(RESULT_OK);

        }catch(Exception e){
            Log.d(LOG_TAG, "ERROR geoMemo2Room: " + e.getMessage());
            showSnackBar(RESULT_FAILED);
        }
    }
    //

    private void showSnackBar(int result){

        Log.d(LOG_TAG, "SNACKBAR: " + result);
        String message = "";

        switch (result){
            case RESULT_CANCELED:
                message = "Nothing...";
                break;
            case RESULT_OK:
                message = "Data store OK";
                break;
            case RESULT_FAILED:
                message = "Data store fails";
        }
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

    private void configureViewModel(){
        GMActivesViewModel gmActivesViewModel = ViewModelProviders.of(this).get(GMActivesViewModel.class);

        gmActivesViewModel.getGeoMemoActivesList().observe(this, new Observer<List<GMActives>>() {
            @Override
            public void onChanged(@Nullable List<GMActives> activesGeoMemos) {
                Log.d(LOG_TAG, "Updating Database changes activesGeoMemos....." + activesGeoMemos.size());
                registerAllActives(activesGeoMemos);
                showLogGeoMemos(activesGeoMemos);
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
                    // Set the request ID of the geofence. This is a string to identify this
                    // geofence.
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

        Log.d(LOG_TAG, "############################### " + mGeofenceList.size());

        mGeofencingClient.addGeofences(getGeofencingRequest(mGeofenceList), getGeofencePendingIntent())
                .addOnSuccessListener(this, new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // Geofences added
                        // ...
                        Log.d(LOG_TAG, "Geofences added successfully");

                    }
                })
                .addOnFailureListener(this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Failed to add geofences
                       // ...
                        Log.d(LOG_TAG, "ERROR: " + e.getMessage().toString());
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
        // Reuse the PendingIntent if we already have it.
        if (mGeofencePendingIntent != null) {
            return mGeofencePendingIntent;
        }

        //Intent intent = new Intent(this,GeofenceTransitionsIntentService.class);

        //
        Intent intent = new Intent(GeoReceiver.GEOFENCE);

        // We use FLAG_UPDATE_CURRENT so that we get the same pending intent back when
        // calling addGeofences() and removeGeofences().
        /*mGeofencePendingIntent = PendingIntent.getService(this, 0, intent, PendingIntent.
                FLAG_UPDATE_CURRENT);
        */
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        //return mGeofencePendingIntent;
        return pendingIntent;
    }

    private void startGeoMemoService(){
        Log.d(LOG_TAG, "START GEOMEMO SERVICE");
        ComponentName myService = new ComponentName(this, BroadcastService.class);
        JobInfo myJob = new JobInfo.Builder(BroadcastService.BROADCAST_RECEIVER_ID, myService)
                .setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY)
                //.setMinimumLatency(10 * 1000) // 10 sg
                .build();


        JobScheduler jobScheduler = (JobScheduler) getSystemService(Context.JOB_SCHEDULER_SERVICE);
        jobScheduler.schedule(myJob);

    }

    //For log only
    private void showLogGeoMemos(List<GMActives> testList){
        for (GMActives gm : testList){
            Log.d(LOG_TAG, "GEOMEMO ACTIVE: " + gm.getGeoName());
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R., menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == android.R.id.home) {
            Toast.makeText(MenuMainActivity.this, "Action clicked", Toast.LENGTH_LONG).show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void activateSettings(){
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();

        SettingsClient client = LocationServices.getSettingsClient(this);
        Task<LocationSettingsResponse> task = client.checkLocationSettings(builder.build());

        task.addOnSuccessListener(this, new OnSuccessListener<LocationSettingsResponse>() {
            @Override
            public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                // All location settings are satisfied. The client can initialize
                // location requests here.
                // ...
                try {
                    // Show the dialog by calling startResolutionForResult(),
                    // and check the result in onActivityResult().
                    ResolvableApiException resolvable = new ResolvableApiException(Status.RESULT_SUCCESS); //(ResolvableApiException) e;
                    resolvable.startResolutionForResult(MenuMainActivity.this,
                            REQUEST_CHECK_SETTINGS);
                } catch (IntentSender.SendIntentException sendEx) {
                    // Ignore the error.
                }
                //return;
            }
        });

        task.addOnFailureListener(this, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                if (e instanceof ResolvableApiException) {
                    // Location settings are not satisfied, but this can be fixed
                    // by showing the user a dialog.
                    try {
                        // Show the dialog by calling startResolutionForResult(),
                        // and check the result in onActivityResult().
                        ResolvableApiException resolvable = (ResolvableApiException) e;
                        resolvable.startResolutionForResult(MenuMainActivity.this,
                                REQUEST_CHECK_SETTINGS);
                    } catch (IntentSender.SendIntentException sendEx) {
                        // Ignore the error.
                    }
                }
            }
        });

    }

}