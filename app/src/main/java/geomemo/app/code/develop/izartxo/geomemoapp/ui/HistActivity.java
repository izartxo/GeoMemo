package geomemo.app.code.develop.izartxo.geomemoapp.ui;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
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

public class HistActivity extends AppCompatActivity {

    private static final String LOG_TAG = "*******" + HistActivity.class.getSimpleName();

    private AppDatabase mDB;
    private LiveData<List<GMHistory>> geoMemoList;

    private HistGeoMemoAdapter mHistGeoMemoAdapter;
    private RecyclerView mRecyclerView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hist);

        mDB = AppDatabase.getInstance(getApplicationContext());

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        toolbar.setTitle("History");


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
                Log.d(LOG_TAG, "Updating Database changes geomemos.....");
                mHistGeoMemoAdapter.setGeoMemoData(geofenceMemos);
                mHistGeoMemoAdapter.notifyDataSetChanged();

                /*for (GeofenceMemo gm : geofenceMemos)
                    tGeoMemoInfo.setText(tGeoMemoInfo.getText() + "\r\n" + gm.getGeoName() + " - " + gm.getGeoMemo());*/
            }
        });

        mRecyclerView = (RecyclerView) findViewById(R.id.hist_recyclerview);

        mHistGeoMemoAdapter = new HistGeoMemoAdapter(this);

        RecyclerView.ItemDecoration itemDecoration = new RecyclerView.ItemDecoration() {
            @Override
            public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
                super.onDraw(c, parent, state);
            }
        };

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));



        mRecyclerView.setAdapter(mHistGeoMemoAdapter);
        /*mRecyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {

                Paint p = new Paint();
                p.setColor(Color.BLACK);
                c.drawLine(0f,0f,20f,0f, p);
                super.onDraw(c, parent, state);
            }
        });*/
    }


    @Override
    protected void onDestroy(){
        Log.d(LOG_TAG, "ONDESTROY");
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
            // Add a toast just for confirmation
            Toast.makeText(this, "Clearing the data...",
                    Toast.LENGTH_SHORT).show();

            // Delete the existing data
            new HistAsyncTask(mDB, null).execute(HistAsyncTask.DELETE);
            new GMActiveAsyncTask(mDB, "").execute(GMActiveAsyncTask.DELETE_ALL);
            new MemoAsyncTask(mDB, "").execute(MemoAsyncTask.DELETE);


            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}

