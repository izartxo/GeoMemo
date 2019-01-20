package geomemo.app.code.develop.izartxo.geomemoapp.widget;

import android.annotation.TargetApi;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.RemoteViews;

import java.util.HashMap;

import geomemo.app.code.develop.izartxo.geomemoapp.R;

public class GeoMemoAppWidgetProvider extends AppWidgetProvider {

    public static final String APP_UPD = "geomemo.app.code.develop.izartxo.geomemoapp.widget.APP_UPDATE";

    private static Cursor mData;
    private Context mContext;
    private static HashMap<String, String> hm;

    @Override
    public void onReceive(@NonNull Context context, @NonNull Intent intent) {

        if (intent.getAction().equals(AppWidgetManager.ACTION_APPWIDGET_UPDATE) || intent.getAction().equals(APP_UPD)) {
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
            int[] appWidgetIds = appWidgetManager.getAppWidgetIds(
                    new ComponentName(context, getClass()));
            appWidgetManager.notifyAppWidgetViewDataChanged(intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID), R.id.rvW);
            onUpdate(context,appWidgetManager,appWidgetIds);


        }

        super.onReceive(context, intent);
    }




    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {

        mContext = context;
        for (int appWidgetId : appWidgetIds) {
            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.appwidget_layout);

            Intent intent = new Intent(context, GeoMemoWidgetService.class);

            intent.putExtra(appWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
            intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));

            views.setRemoteAdapter(R.id.rvW,intent);

            views.setEmptyView(R.id.rvW, R.id.empty);

            Intent intentSync = new Intent (mContext, WidgetIntentService.class);

            views.setTextViewText(R.id.header, context.getResources().getString(R.string.widget_data_header));


            PendingIntent pI = PendingIntent.getService(mContext, 0, intentSync, PendingIntent.FLAG_UPDATE_CURRENT);

            views.setOnClickPendingIntent(R.id.header, pI);

            appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetId, R.id.rvW);
            appWidgetManager.updateAppWidget(appWidgetId, views);

        }
        super.onUpdate(context, appWidgetManager, appWidgetIds);
    }

    public static Cursor getData(){
        return mData;
    }

    public static void updateWidget(Cursor cursor, Context context, HashMap lhm){
        mData = cursor;
        hm = lhm;
        Intent intent = new Intent(context, GeoMemoAppWidgetProvider.class);
        intent.setAction(APP_UPD);
        context.sendBroadcast(intent);


    }

    @Override
    public void onEnabled(Context context) {
        super.onEnabled(context);

       /* final Intent fillInIntent = new Intent();
        fillInIntent.setAction(APP_UPD);
        Uri geoMemoUri = GeoMemoProvider.BASE_CONTENT_URI;
        fillInIntent.setData(geoMemoUri);
        context.sendBroadcast(fillInIntent);*/
    }
}
