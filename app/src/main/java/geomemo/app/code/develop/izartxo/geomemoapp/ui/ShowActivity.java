package geomemo.app.code.develop.izartxo.geomemoapp.ui;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import geomemo.app.code.develop.izartxo.geomemoapp.R;
import geomemo.app.code.develop.izartxo.geomemoapp.adapter.ShowGeoMemoAdapter;
import geomemo.app.code.develop.izartxo.geomemoapp.database.AppDatabase;
import geomemo.app.code.develop.izartxo.geomemoapp.database.GeofenceMemo;
import geomemo.app.code.develop.izartxo.geomemoapp.util.GMFactory;

public class ShowActivity extends AppCompatActivity {

    private static final String LOG_TAG = "*******" + ShowActivity.class.getSimpleName();

    private AppDatabase mDB;
    private LiveData<List<GeofenceMemo>> geoMemoList;

    private ShowGeoMemoAdapter mShowGeoMemoAdapter;
    private RecyclerView mRecyclerView;

    /*@BindView(R.id.show_geomemo_textview)
    TextView tGeoMemoInfo;
*/
    @BindView(R.id.test_button)
    Button test;
    @OnClick(R.id.test_button)
    public void test(){
        mDB.geofenceMemoDao().insertGeoMemo(GMFactory.createGeoMemo("yyyy","dddd","234234","4234"));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show);

        mDB = AppDatabase.getInstance(getApplicationContext());

        ButterKnife.bind(this);

        ShowViewModel showViewModel = ViewModelProviders.of(this).get(ShowViewModel.class);

        showViewModel.getGeoMemoList().observe(this, new Observer<List<GeofenceMemo>>() {
            @Override
            public void onChanged(@Nullable List<GeofenceMemo> geofenceMemos) {
                Log.d(LOG_TAG, "Updating Database changes geomemos.....");
                mShowGeoMemoAdapter.setGeoMemoData(geofenceMemos);
                mShowGeoMemoAdapter.notifyDataSetChanged();

                /*for (GeofenceMemo gm : geofenceMemos)
                    tGeoMemoInfo.setText(tGeoMemoInfo.getText() + "\r\n" + gm.getGeoName() + " - " + gm.getGeoMemo());*/
            }
        });

        mRecyclerView = (RecyclerView) findViewById(R.id.show_recyclerview);

        mShowGeoMemoAdapter = new ShowGeoMemoAdapter(this);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        mRecyclerView.setAdapter(mShowGeoMemoAdapter);
    }


    @Override
    protected void onDestroy(){
        Log.d(LOG_TAG, "ONDESTROY");
        super.onDestroy();
    }
}
