package geomemo.app.code.develop.izartxo.geomemoapp.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import geomemo.app.code.develop.izartxo.geomemoapp.R;
import geomemo.app.code.develop.izartxo.geomemoapp.database.GMActives;
import geomemo.app.code.develop.izartxo.geomemoapp.database.GMHistory;
import geomemo.app.code.develop.izartxo.geomemoapp.util.GMFactory;

public class HistGeoMemoAdapter extends RecyclerView.Adapter<HistGeoMemoAdapter.HistGeoMemoViewHolder> {

    private final static String LOG_TAG = "*******" + HistGeoMemoAdapter.class.getSimpleName();

    private List<GMHistory> mGeoMemoList;
    private Context mContext;

    public HistGeoMemoAdapter(Context mContext) {
        this.mContext = mContext;

    }

    @NonNull
    @Override
    public HistGeoMemoAdapter.HistGeoMemoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.list_item_hist, parent, false);
        return new HistGeoMemoAdapter.HistGeoMemoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HistGeoMemoAdapter.HistGeoMemoViewHolder holder, int position) {
        GMHistory geofenceMemo = mGeoMemoList.get(position);

        Log.d(LOG_TAG, "GMH: " + geofenceMemo.getGeoMemo() + "//" + geofenceMemo.getGeoLatitude() + "//"
                + geofenceMemo.getGeoTimestamp() + "//"
                + geofenceMemo.getGeoHistoryTime());

        holder.geoNameTextview.setText(geofenceMemo.getGeoName() + " : " + geofenceMemo.getGeoMemo());
        holder.geoLatLonTextview.setText(geofenceMemo.getGeoLatitude() + " // " + geofenceMemo.getGeoLongitude());
        holder.geoCreationTextview.setText(geofenceMemo.getGeoTimestamp());
        holder.geoHistoryTextview.setText(geofenceMemo.getGeoHistoryTime());
    }

    @Override
    public int getItemCount() {
        if (null == mGeoMemoList) return 0;
        return mGeoMemoList.size();
    }


    class HistGeoMemoViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        final TextView geoNameTextview;
        final TextView geoLatLonTextview;
        final TextView geoCreationTextview;
        final TextView geoHistoryTextview;


        public HistGeoMemoViewHolder(View itemView) {
            super(itemView);

            geoNameTextview = (TextView) itemView.findViewById(R.id.hist_geomemo_item_textview);
            geoLatLonTextview = (TextView) itemView.findViewById(R.id.hist_geomemo_latlon_item_textview);
            geoCreationTextview = (TextView) itemView.findViewById(R.id.hist_geomemo_creation_item_textview);
            geoHistoryTextview = (TextView) itemView.findViewById(R.id.hist_geomemo_history_item_textview);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            Log.d(LOG_TAG, "Item clicked " + getAdapterPosition());
        }
    }

    public void setGeoMemoData(List<GMHistory> listGeoMemos){
        mGeoMemoList = listGeoMemos;
    }
}

