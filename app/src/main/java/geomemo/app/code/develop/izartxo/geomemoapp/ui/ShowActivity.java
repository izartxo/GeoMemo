package geomemo.app.code.develop.izartxo.geomemoapp.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import geomemo.app.code.develop.izartxo.geomemoapp.R;
import geomemo.app.code.develop.izartxo.geomemoapp.database.AppDatabase;
import geomemo.app.code.develop.izartxo.geomemoapp.database.GeofenceMemo;

public class ShowActivity extends AppCompatActivity {

    private static final String LOG_TAG = "*******" + ShowActivity.class.getSimpleName();

    private AppDatabase mDB;
    private List<GeofenceMemo> geoMemoList;

    @BindView(R.id.show_geomemo_textview)
    TextView tGeoMemoInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show);

        mDB = AppDatabase.getInstance(getApplicationContext());

        ButterKnife.bind(this);

        geoMemoList =  mDB.geofenceMemoDao().getAll(); // It must be a cursor!!!

        Log.d(LOG_TAG, "Database rows: " + geoMemoList.size());

        for (GeofenceMemo gm : geoMemoList)
            tGeoMemoInfo.setText(tGeoMemoInfo.getText() + "\r\n" + gm.getGeoName() + " - " + gm.getGeoMemo());

    }

}
