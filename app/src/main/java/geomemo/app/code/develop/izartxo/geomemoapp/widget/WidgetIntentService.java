package geomemo.app.code.develop.izartxo.geomemoapp.widget;

import android.app.IntentService;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.IBinder;
import android.support.annotation.Nullable;

import java.util.HashMap;

public class WidgetIntentService extends IntentService {


    public WidgetIntentService() {
        super("GeoMemoWidget");
    }



    @Override
    public void setIntentRedelivery(boolean enabled) {
        super.setIntentRedelivery(enabled);
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public void onStart(@Nullable Intent intent, int startId) {
        super.onStart(intent, startId);
    }

    @Override
    public int onStartCommand(@Nullable Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return super.onBind(intent);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

        String ing = "1";

        GeoMemoWidgetProvider.setRecipeNum();

        if (intent.hasExtra("ing"))
            ing = intent.getStringExtra("ing");


        String columns = "_id = ?";
        String[] values = new String[]{ing};

        Uri ingredientUri = RecipeProvider.Ingredients.INGREDIENTS;
        Cursor cursor = getContentResolver().query(ingredientUri,
                null,
                columns,
                values,
                null);


        Uri recipeUri = RecipeProvider.Recipes.RECIPES;
        Cursor cursor2 = getContentResolver().query(recipeUri,
                null,
                null,
                null,
                null);


        HashMap<String,String> hm = new HashMap<>();


        cursor2.moveToFirst();

        while (!cursor2.isAfterLast())
        {
            Recipe r = new Recipe(cursor2.getString(0), 0, cursor2.getString(1), cursor2.getString(2));

            hm.put(r.getRecipeId(),r.getTitle());

            cursor2.moveToNext();
        }


        GeoMemoWidgetProvider.updateWidget(cursor, getApplicationContext(), hm);
    }
}
