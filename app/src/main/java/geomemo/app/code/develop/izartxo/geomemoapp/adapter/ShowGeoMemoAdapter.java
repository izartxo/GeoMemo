package geomemo.app.code.develop.izartxo.geomemoapp.adapter;

import android.arch.persistence.room.util.StringUtil;
import android.content.Context;
import android.media.Image;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import geomemo.app.code.develop.izartxo.geomemoapp.R;
import geomemo.app.code.develop.izartxo.geomemoapp.database.GMActives;
import geomemo.app.code.develop.izartxo.geomemoapp.database.GeofenceMemo;
import geomemo.app.code.develop.izartxo.geomemoapp.ui.ShowActivity;
import geomemo.app.code.develop.izartxo.geomemoapp.util.GMFactory;

public class ShowGeoMemoAdapter extends RecyclerView.Adapter<ShowGeoMemoAdapter.ShowGeoMemoViewHolder> {

    private final static String LOG_TAG = "*******" + ShowGeoMemoAdapter.class.getSimpleName();

    private List<GMActives> mGeoMemoList;
    private Context mContext;
    private ShowActivity.OnDeleteListener mOnDelete;

    public ShowGeoMemoAdapter(Context mContext, ShowActivity.OnDeleteListener OnDelete) {
        this.mContext = mContext;
        mOnDelete = OnDelete;
    }

    @NonNull
    @Override
    public ShowGeoMemoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.list_item_show, parent, false);
        return new ShowGeoMemoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ShowGeoMemoViewHolder holder, final int position) {
        final GMActives geofenceMemo = mGeoMemoList.get(position);

        holder.geoNameTextview.setText(geofenceMemo.getGeoName() + System.lineSeparator() + geofenceMemo.getGeoMemo());
        holder.geoLatLonTextview.setText(geofenceMemo.getGeoLatitude() + " // " + geofenceMemo.getGeoLongitude());
        holder.geoCreatedTextview.setText(geofenceMemo.getGeoTimestamp());
        holder.bDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(LOG_TAG, "---" + position);
                final String geoname = mGeoMemoList.get(position).getGeoName();
                mGeoMemoList.remove(position);
                //mOnDelete.OnDelete(geoname);
                //GMFactory.readGeoMemo(mContext, geoname);
                GMFactory.readMemo(mContext, geoname);
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        if (null == mGeoMemoList) return 0;
        return mGeoMemoList.size();
    }


    class ShowGeoMemoViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        final TextView geoNameTextview;
        final TextView geoLatLonTextview;
        final TextView geoCreatedTextview;
        final ImageButton bDelete;

        // Button to delete active GeoMemo
        //@BindView(R.id.show_geomemo_item_imagebutton)
        //ImageButton bDelete;
        //@OnClick(R.id.show_geomemo_item_imagebutton)
        //public void deleteGeoMemo(){
        //    mGeoMemoList.remove(getAdapterPosition());
        //}
        /////

        public ShowGeoMemoViewHolder(View itemView) {
            super(itemView);

            geoNameTextview = (TextView) itemView.findViewById(R.id.show_geomemo_item_textview);
            geoLatLonTextview= (TextView) itemView.findViewById(R.id.show_geomemo_latlon_item_textview);
            geoCreatedTextview= (TextView) itemView.findViewById(R.id.show_geomemo_creation_item_textview);
            //
            bDelete = (ImageButton) itemView.findViewById(R.id.show_geomemo_item_imagebutton);

            //
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            Log.d(LOG_TAG, "Item clicked " + getAdapterPosition());
        }
    }

    public void setGeoMemoData(List<GMActives> listGeoMemos){
        mGeoMemoList = listGeoMemos;
    }
}
