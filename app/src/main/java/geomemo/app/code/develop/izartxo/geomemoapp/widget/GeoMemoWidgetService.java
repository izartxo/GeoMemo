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


            data = GeoMemoWidgetProvider.getData();

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

                    String title = data.getString(data.getColumnIndex(IngredientColumns.INGREDIENT));

                    views.setTextViewText(R.id.geomemo_widget_item, title.toUpperCase());
                }



/*                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1) {
                    // setRemoteContentDescription(views, description);
                }*/


                final Intent fillInIntent = new Intent();

                Uri recipeUri = RecipeProvider.Recipes.RECIPES;
                fillInIntent.setData(recipeUri);
                views.setOnClickFillInIntent(R.id.geomemo_widget_item, fillInIntent);
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