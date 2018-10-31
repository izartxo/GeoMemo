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
import geomemo.app.code.develop.izartxo.geomemoapp.database.GeofenceMemo;

public class ShowGeoMemoAdapter extends RecyclerView.Adapter<ShowGeoMemoAdapter.ShowGeoMemoViewHolder> {

    private final static String LOG_TAG = ShowGeoMemoAdapter.class.getSimpleName();

    private List<GeofenceMemo> mGeoMemoList;
    private Context mContext;

    public ShowGeoMemoAdapter(Context mContext) {
        this.mContext = mContext;

    }

    @NonNull
    @Override
    public ShowGeoMemoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.list_item_show, parent, false);
        return new ShowGeoMemoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ShowGeoMemoViewHolder holder, int position) {
        GeofenceMemo geofenceMemo = mGeoMemoList.get(position);

        holder.geoNameTextview.setText(geofenceMemo.getGeoName() + " // " + geofenceMemo.getGeoMemo());
    }

    @Override
    public int getItemCount() {
        if (null == mGeoMemoList) return 0;
        return mGeoMemoList.size();
    }


    class ShowGeoMemoViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        final TextView geoNameTextview;

        public ShowGeoMemoViewHolder(View itemView) {
            super(itemView);

            geoNameTextview = (TextView) itemView.findViewById(R.id.show_geomemo_item_textview);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            Log.d(LOG_TAG, "Item clicked " + getAdapterPosition());
        }
    }

    public void setGeoMemoData(List<GeofenceMemo> listGeoMemos){
        mGeoMemoList = listGeoMemos;
    }
}
