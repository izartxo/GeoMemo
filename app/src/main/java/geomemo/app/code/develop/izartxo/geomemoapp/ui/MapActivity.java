package geomemo.app.code.develop.izartxo.geomemoapp.ui;

import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import geomemo.app.code.develop.izartxo.geomemoapp.R;
import geomemo.app.code.develop.izartxo.geomemoapp.database.AppDatabase;
import geomemo.app.code.develop.izartxo.geomemoapp.database.GeofenceMemo;
import geomemo.app.code.develop.izartxo.geomemoapp.util.GMFactory;
import geomemo.app.code.develop.izartxo.geomemoapp.util.LocationUtil;


public class MapActivity extends AppCompatActivity {


    private static final String LOG_TAG = "*******" + MapActivity.class.getSimpleName();

    private static LocationUtil locationUtil;
    private LocationUtil.LocationReady mInterface;
    private FusedLocationProviderClient lClient;

    List<GeofenceMemo> testGM = new ArrayList<>();


    @BindView(R.id.map_geomemo_textview)
    TextView tLocation;
    @BindView(R.id.map_geomemo_edittext)
    EditText eLocation;

    @BindView(R.id.add_geomemo_map_button)
    Button bAdd;
    @OnClick(R.id.add_geomemo_map_button)
    public void addGeoMemoMap(View view){
        // For test
        //Random r = new Random();
        //int i1 = r.nextInt(3);
        //
        showDialog("test");//testGM.get(i1));
        Log.d(LOG_TAG, "add geofence on map");


    }

    @BindView(R.id.get_location_map_button)
    Button bGet;
    @OnClick(R.id.get_location_map_button)
    public void getLocation(View view){
        // For test
        //Random r = new Random();
        //int i1 = r.nextInt(3);
        //

        Log.d(LOG_TAG, "Get location");

        lClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location!=null)
                    eLocation.setText(location.getLatitude()+","+location.getLongitude());

            }
        });



    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        //mDB = AppDatabase.getInstance(getApplicationContext());

        ButterKnife.bind(this);

        // To test Geofences
        //GeofenceMemo geoMemo1 = GMFactory.createGeoMemo("etxea","animo animo animo1","43.2324067","-2.8427583");
        //GeofenceMemo geoMemo2 = GMFactory.createGeoMemo("eroski","animo animo animo2","43.23044","-2.841632");
        //GeofenceMemo geoMemo3 = GMFactory.createGeoMemo("torrezabal","animo animo animo3","43.2315647","-2.844483");

        //testGM.add(geoMemo1);
        //testGM.add(geoMemo2);
        //testGM.add(geoMemo3);
        //

        if (locationUtil==null){
            mInterface = new LocationUtil.LocationReady() {
                @Override
                public void onLocationReady(Location location) {
                    tLocation.setText(location.toString());
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
                        if (location!=null)
                            tLocation.setText(location.toString());

                    }
                });


    }



    private void showDialog(final String name/*GeofenceMemo geofenceMemo*/){

        final EditText input = new EditText(this);
        final EditText gname = new EditText(this);
        final TextView tname = new TextView(this);
        tname.setText("GeoMemoName:");
        final TextView tinput = new TextView(this);
        tinput.setText("Memo:");
        gname.setText(name/*geofenceMemo.getGeoName()*/);


        LinearLayout layout = new LinearLayout(this);

        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT
        );
        layout.setLayoutParams(lp);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.addView(tname);
        layout.addView(gname);
        layout.addView(tinput);
        layout.addView(input);


        AlertDialog.Builder b=  new  AlertDialog.Builder(this)
                .setTitle("Memo text")
                .setView(layout)
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                // do something...
                                startGeo(gname.getText().toString(), input.getText().toString(),
                                        ""/*geofenceMemo.getGeoLatitude()*/,""/*geofenceMemo.getGeoLongitude()*/);
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

    private void startGeo(String geoname, String memo, String lat, String lon){

        if (!eLocation.equals("loc")){
            String l = eLocation.getEditableText().toString();
            Log.d(LOG_TAG, "Location: " + l);
            lat = l.substring(0, l.indexOf(','));
            Log.d(LOG_TAG, "Lat: " + lat);
            lon = l.substring(l.indexOf(',')+1, l.length());
            Log.d(LOG_TAG, "Lat: " + lon);
        }


        Intent intent = new Intent(this, MenuMainActivity.class);
        intent.putExtra("geoname", geoname);
        intent.putExtra("geomemo", memo);
        intent.putExtra("latitude", lat);
        intent.putExtra("longitude", lon);
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public void onBackPressed() {

        super.onBackPressed();

        setResult(RESULT_CANCELED);
        finish();
    }





}
