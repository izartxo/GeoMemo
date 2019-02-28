package geomemo.app.code.develop.izartxo.geomemoapp.ui;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import geomemo.app.code.develop.izartxo.geomemoapp.R;
import geomemo.app.code.develop.izartxo.geomemoapp.adapter.HistGeoMemoAdapter;
import geomemo.app.code.develop.izartxo.geomemoapp.adapter.ShowGeoMemoAdapter;
import geomemo.app.code.develop.izartxo.geomemoapp.database.AppDatabase;
import geomemo.app.code.develop.izartxo.geomemoapp.database.GMActives;
import geomemo.app.code.develop.izartxo.geomemoapp.database.GMHistory;
import geomemo.app.code.develop.izartxo.geomemoapp.database.GeofenceMemo;
import geomemo.app.code.develop.izartxo.geomemoapp.util.GMActiveAsyncTask;
import geomemo.app.code.develop.izartxo.geomemoapp.util.GMFactory;
import geomemo.app.code.develop.izartxo.geomemoapp.util.HistAsyncTask;
import geomemo.app.code.develop.izartxo.geomemoapp.util.MemoAsyncTask;

import static android.support.v7.widget.DividerItemDecoration.HORIZONTAL;

public class HistActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener{

    private static final String LOG_TAG = "*******" + HistActivity.class.getSimpleName();

    private AppDatabase mDB;


    private HistGeoMemoAdapter mHistGeoMemoAdapter;
    private RecyclerView mRecyclerView;

    SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hist);

        swipeRefreshLayout = findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(this);

        mDB = AppDatabase.getInstance(getApplicationContext());

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);


        toolbar.setTitle(getResources().getString(R.string.toolbar_history_title));

        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //What to do on back clicked
                finish();
            }
        });

        ButterKnife.bind(this);

        HistViewModel histViewModel = ViewModelProviders.of(this).get(HistViewModel.class);

        histViewModel.getGeoMemoList().observe(this, new Observer<List<GMHistory>>() {
            @Override
            public void onChanged(@Nullable List<GMHistory> geofenceMemos) {
                //Log.d(LOG_TAG, "Updating Database changes geomemos.....");
                mHistGeoMemoAdapter.setGeoMemoData(geofenceMemos);
                mHistGeoMemoAdapter.notifyDataSetChanged();


            }
        });

        mRecyclerView = (RecyclerView) findViewById(R.id.hist_recyclerview);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        dividerItemDecoration.setDrawable(this.getResources().getDrawable(R.drawable.line_divider));
        mRecyclerView.addItemDecoration(dividerItemDecoration);

        mHistGeoMemoAdapter = new HistGeoMemoAdapter(this);


        mRecyclerView.setAdapter(mHistGeoMemoAdapter);

    }


    @Override
    protected void onDestroy(){
        //Log.d(LOG_TAG, "ONDESTROY");
        super.onDestroy();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_hist, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.clear_data) {

            new HistAsyncTask(mDB, null).execute(HistAsyncTask.DELETE);
            new GMActiveAsyncTask(mDB, "").execute(GMActiveAsyncTask.DELETE_ALL);
            new MemoAsyncTask(mDB, "").execute(MemoAsyncTask.DELETE);

            showSnackBar();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRefresh() {
        mRecyclerView.getAdapter().notifyDataSetChanged();
        swipeRefreshLayout.setRefreshing(false);
    }

    private void showSnackBar(){

        final Snackbar snackbar = Snackbar.make(getWindow().getDecorView().getRootView(), getString(R.string.menu_history_delete_message), Snackbar.LENGTH_LONG);
        snackbar.show();
    }
}

