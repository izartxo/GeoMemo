package geomemo.app.code.develop.izartxo.geomemoapp.ui;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.inputmethodservice.Keyboard;
import android.location.Location;
import android.location.LocationManager;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.accessibility.AccessibilityEvent;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;

import butterknife.BindView;
import butterknife.OnClick;
import geomemo.app.code.develop.izartxo.geomemoapp.R;
import geomemo.app.code.develop.izartxo.geomemoapp.util.GMFactory;
import geomemo.app.code.develop.izartxo.geomemoapp.util.LocationUtil;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {

    private static final String LOG_TAG = "*******" + MapsActivity.class.getSimpleName();
    public static final int DIALOG_FRAGMENT = 0x1;

    private GoogleMap mMap;

    private static LocationUtil locationUtil;
    private LocationUtil.LocationReady mInterface;
    private FusedLocationProviderClient lClient;
    private static LatLng galda;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_maps);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getSupportActionBar().setWindowTitle("Point");

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //What to do on back clicked
                finish();
            }
        });

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        if (locationUtil==null){
            mInterface = new LocationUtil.LocationReady() {
                @Override
                public void onLocationReady(Location location) {
                    galda = new LatLng(location.getLatitude(), location.getLongitude());
                }
            };
            locationUtil = new LocationUtil(getApplicationContext(), mInterface);

        }

        lClient = LocationServices.getFusedLocationProviderClient(this);

        lClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        // Got last known location. In some rare situations this can be null.
                        if (location!=null) {
                            galda = new LatLng(location.getLatitude(), location.getLongitude());
                            mMap.addMarker(new MarkerOptions().position(galda).title("Marker geo"));
                            mMap.moveCamera(CameraUpdateFactory.newLatLng(galda));
                        }
                    }
                });


    }

    @Override
    protected void onResume(){
        super.onResume();

    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        //LatLng sydney = new LatLng(-34, 151);
        //mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        //mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        //LatLng galda = new LatLng(43.2317358,-2.8460554);


        mMap.setMinZoomPreference(15.0f);

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {

                showDialog(latLng,false);
                //galda = latLng;
                //
                // showDialogNew(false);
            }

        });

    }

    private void startGeo(String geoname, String memo, LatLng latLng){
        // Instantiates a new CircleOptions object and defines the center and radius

/*
        mCircles.add(circle);
*/

            Log.d(LOG_TAG, "Location: " + latLng);
            String lat = String.valueOf(latLng.latitude);
            Log.d(LOG_TAG, "Lat: " + lat);
            String lon = String.valueOf(latLng.longitude);
            Log.d(LOG_TAG, "Lat: " + lon);



        Intent intent = new Intent(this, MenuMainActivity.class);
        intent.putExtra("geoname", geoname);
        intent.putExtra("geomemo", memo);
        intent.putExtra("latitude", lat);
        intent.putExtra("longitude", lon);
        setResult(RESULT_OK, intent);
        finish();

    }

    private void showDialog(final LatLng latLng, boolean witherror){

        final CircleOptions circleOptions = new CircleOptions()
                .center(latLng)
                .fillColor(Color.GREEN)
                //.clickable(true)
                .radius(100); // In meters

        // Get back the mutable Circle
        Circle circle = mMap.addCircle(circleOptions);

        final TextView terror = new TextView(MapsActivity.this);
        final EditText input = new EditText(MapsActivity.this);
        final EditText name = new EditText(MapsActivity.this);
        final TextView tname = new TextView(MapsActivity.this);
        tname.setText(getResources().getString(R.string.dialog_geomemo_id_textview));
        final TextView tinput = new TextView(MapsActivity.this);
        tinput.setText(getResources().getString(R.string.dialog_geomemo_memo_textview));

        terror.setTextColor(getResources().getColor(R.color.GMcolorPrimaryDark));
        input.setTextColor(R.color.GMcolorSecondText);
        name.setTextColor(R.color.GMcolorSecondText);

        String errorMessage = String.format(getResources().getString(R.string.dialog_geomemo_length_error), GMFactory.GM_TEXT_LENGTH);
        terror.setText(errorMessage);
        terror.setVisibility(View.INVISIBLE);
        if (witherror)
            terror.setVisibility(View.VISIBLE);

        LinearLayout layout = new LinearLayout(this);

        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT
        );
        layout.setLayoutParams(lp);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.addView(terror);
        layout.addView(tname);
        layout.addView(name);
        layout.addView(tinput);
        layout.addView(input);

        TextView customTitle = new TextView(this);
        customTitle.setText("GeoMemo");
        customTitle.setBackgroundColor(getColor(R.color.GMcolorAccent));
        customTitle.setTextColor(getColor(R.color.GMcolorText));

        AlertDialog.Builder b=  new  AlertDialog.Builder(this)
                .setCustomTitle(customTitle)
                .setView(layout)
                .setPositiveButton("Accept",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                // do something...
                                String tname = name.getText().toString();
                                String ttext = input.getText().toString();
                                if (!(GMFactory.checkText(tname) && GMFactory.checkText(ttext))) {
                                    dialog.cancel();
                                    showDialog(latLng, true);
                                }else{
                                    startGeo(tname, ttext, latLng);
                                }
                            }
                        }
                )
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                dialog.dismiss();
                            }
                        }
                );



        b.show();

    }

    private void showDialogNew(boolean error){
        GeoMemoDialog gd = new GeoMemoDialog();
        gd.setTargetFragment(getFragmentManager().findFragmentById(R.id.map), DIALOG_FRAGMENT);
        gd.show(getFragmentManager(), "geomemo dialog");
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch(requestCode) {
            case DIALOG_FRAGMENT:

                if (resultCode == Activity.RESULT_OK) {
                    // After Ok code.

                    String tname = data.getStringExtra("name");
                    String ttext = data.getStringExtra("input");
                    if (!(GMFactory.checkText(tname) && GMFactory.checkText(ttext))) {
                        showDialogNew(true);
                    }else{
                        startGeo(tname, ttext, galda);
                    }

                } else if (resultCode == Activity.RESULT_CANCELED){
                    // After Cancel code.
                }

                break;
        }
    }



}

