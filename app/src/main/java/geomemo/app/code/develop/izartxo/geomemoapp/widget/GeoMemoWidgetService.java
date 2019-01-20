package geomemo.app.code.develop.izartxo.geomemoapp.widget;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.StaleDataException;
import android.net.Uri;
import android.util.Log;
import android.widget.AdapterView;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import geomemo.app.code.develop.izartxo.geomemoapp.R;

public class GeoMemoWidgetService extends RemoteViewsService {

    private final static int TEXT_SHOW_MAX = 50;

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new WidgetFactory(getApplicationContext(), intent);
    }

    class WidgetFactory implements RemoteViewsService.RemoteViewsFactory{

        private Cursor data = null;
        private Context mContext;
        private int mAppWidgetId;

        public WidgetFactory(Context context, Intent intent){
            mContext = context;
            mAppWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
        }

        @Override
        public void onCreate() {
            // Nothing to do

        }

        @Override
        public void onDataSetChanged() {

            data = GeoMemoAppWidgetProvider.getData();

            if (data == null) {
                return;
            }


        }

        @Override
        public void onDestroy() {
            if (data != null) {
                data.close();
                data = null;
            }
        }

        @Override
        public int getCount() {
            return data == null ? 0 : data.getCount();
        }

        @Override
        public RemoteViews getViewAt(int position) {
            RemoteViews views = null;
            try {
                if (position == AdapterView.INVALID_POSITION ||
                        data == null || !data.moveToPosition(position)) {
                    return null;
                }
                views = new RemoteViews(getPackageName(),
                        R.layout.widget_item);


                for (int u = 0; u < data.getColumnCount(); u++) {

                    String title = data.getString(data.getColumnIndex("geoname")); // + " - " + data.getString(data.getColumnIndex("active"));
                    String memo = data.getString(data.getColumnIndex("geomemo")); // + " - " + data.getString(data.getColumnIndex("active"));

                    views.setTextViewText(R.id.geomemo_widget_item_name, title.toUpperCase());

                    if (memo.length() > TEXT_SHOW_MAX)
                        memo = memo.substring(0,TEXT_SHOW_MAX) + "...";

                    views.setTextViewText(R.id.geomemo_widget_item_memo, memo);
                }



/*                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1) {
                    // setRemoteContentDescription(views, description);
                }*/


                final Intent fillInIntent = new Intent();

                Uri geoMemoUri = GeoMemoProvider.BASE_CONTENT_URI;
                fillInIntent.setData(geoMemoUri);
                views.setOnClickFillInIntent(R.id.geomemo_widget_item_name, fillInIntent);

            } catch (IllegalStateException ie){
                Log.d("Widget", "Error recreating widget");
            } catch (StaleDataException sde){
                Log.d("Widget", "Error recreating widget");
            }
            return views;
        }

        @Override
        public RemoteViews getLoadingView() {
            return new RemoteViews(getPackageName(), R.layout.widget_item);
        }

        @Override
        public int getViewTypeCount() {
            return 1;
        }

        @Override
        public long getItemId(int position) {
            try{
                if (data.moveToPosition(position))
                    return data.getLong(0);
            }catch(IllegalStateException ie){
                Log.d("Widget", "Error recreating widget");
            }catch(StaleDataException ie){
                Log.d("Widget", "Error recreating widget");
            }

            return position;
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }
    };
}
