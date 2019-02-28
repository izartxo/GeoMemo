package geomemo.app.code.develop.izartxo.geomemoapp.ui;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import geomemo.app.code.develop.izartxo.geomemoapp.R;
import geomemo.app.code.develop.izartxo.geomemoapp.adapter.ShowGeoMemoAdapter;
import geomemo.app.code.develop.izartxo.geomemoapp.database.AppDatabase;
import geomemo.app.code.develop.izartxo.geomemoapp.database.GMActives;
import geomemo.app.code.develop.izartxo.geomemoapp.database.GeofenceMemo;
import geomemo.app.code.develop.izartxo.geomemoapp.util.GMFactory;

public class ShowActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener{

    private static final String LOG_TAG = "*******" + ShowActivity.class.getSimpleName();

    private AppDatabase mDB;
    private LiveData<List<GeofenceMemo>> geoMemoList;

    private ShowGeoMemoAdapter mShowGeoMemoAdapter;
    private RecyclerView mRecyclerView;
    private ShowViewModel showViewModel;

    SwipeRefreshLayout swipeRefreshLayout;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show);

        swipeRefreshLayout = findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(this);

        mDB = AppDatabase.getInstance(getApplicationContext());

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        toolbar.setTitle(getResources().getString(R.string.toolbar_show_title));

        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
            }
        });




        ButterKnife.bind(this);

        mRecyclerView = (RecyclerView) findViewById(R.id.show_recyclerview);

        OnDeleteListener onDeleteListener = new OnDeleteListener() {
            @Override
            public void OnDelete(String geoname) {
                mDB.gmActivesDao().deleteGMActive(geoname);
            }
        };

        mShowGeoMemoAdapter = new ShowGeoMemoAdapter(this, onDeleteListener);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        dividerItemDecoration.setDrawable(this.getResources().getDrawable(R.drawable.line_divider));
        mRecyclerView.addItemDecoration(dividerItemDecoration);

        mRecyclerView.setAdapter(mShowGeoMemoAdapter);
    }

    @Override
    public void onResume(){
        super.onResume();

        showViewModel();

    }

    @Override
    protected void onDestroy(){
        //Log.d(LOG_TAG, "ONDESTROY");
        super.onDestroy();
    }

    @Override
    public void onRefresh() {
        mRecyclerView.getAdapter().notifyDataSetChanged();
        swipeRefreshLayout.setRefreshing(false);
    }

    public interface OnDeleteListener{
        public void OnDelete(String geoname);
    }

    private void showViewModel(){
        showViewModel = ViewModelProviders.of(this).get(ShowViewModel.class);

        showViewModel.getGeoMemoList().observe(this, new Observer<List<GMActives>>() {
            @Override
            public void onChanged(@Nullable List<GMActives> geofenceMemos) {
                //Log.d(LOG_TAG, "Updating Database changes geomemos.....");
                mShowGeoMemoAdapter.setGeoMemoData(geofenceMemos);
                mShowGeoMemoAdapter.notifyDataSetChanged();
            }
        });


    }
}
