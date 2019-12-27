package de.haepfl.ultimatebeercounter;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.widget.RemoteViews;

import java.util.Calendar;
import java.util.List;

import de.haepfl.ultimatebeercounter.Room.Day;

/**
 * Implementation of App Widget functionality.
 */
public class BeerWidget extends AppWidgetProvider {
    private static final String BTN_NEW_BEER = "CalendarWidgetButtonNewBeer";

    private Context context;
    private AppWidgetManager appWidgetManager;
    private ComponentName appWidget;

    private List<Day> allDays;

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        this.context = context;
        this.appWidgetManager = appWidgetManager;
        this.appWidget = new ComponentName(context, BeerWidget.class);

        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.beer_widget);
        views.setOnClickPendingIntent(R.id.btn_new_beer, getPendingSelfIntent(context, BTN_NEW_BEER));
        appWidgetManager.updateAppWidget(appWidget, views);

        new GetDatabase(this, GetDatabase.INIT).execute(context);
    }


    //++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    // Button Done Event

    protected PendingIntent getPendingSelfIntent(Context context, String action) {
        Intent intent = new Intent(context, getClass());
        intent.setAction(action);
        return PendingIntent.getBroadcast(context, 0, intent, 0);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        if (BTN_NEW_BEER.equals(intent.getAction())) {
            this.context = context;
            this.appWidgetManager = AppWidgetManager.getInstance(context);
            this.appWidget = new ComponentName(context, BeerWidget.class);
            new GetDatabase(this, GetDatabase.ADD).execute(context);
        }
    }

    //++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    //GetDatabase:

    void init(DayRepository repository) {
        allDays = repository.getAllDays();
        refreshCounter();
    }

    void refreshCounter() {
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.beer_widget);
        views.setTextViewText(R.id.tv_beer_count, Integer.toString(allDays.size()));
        appWidgetManager.updateAppWidget(appWidget, views);
    }


    void add(DayRepository repository) {
        allDays = repository.getAllDays();

        Calendar c = Calendar.getInstance();
        Day currentDay = new Day(c.get(Calendar.YEAR), c.get(Calendar.MONTH),
                c.get(Calendar.DAY_OF_MONTH), c.get(Calendar.HOUR), c.get(Calendar.MINUTE),
                c.get(Calendar.SECOND));


        allDays.add(currentDay);
        repository.insert(currentDay);
        refreshCounter();
    }

    private static class GetDatabase extends AsyncTask<Context, Void, DayRepository> {

        static final int INIT = 1;
        static final int ADD = 2;
        private BeerWidget beerWidget;
        private int action;

        private GetDatabase(BeerWidget beerWidget, int action) {
            this.beerWidget = beerWidget;
            this.action = action;
        }

        @Override
        protected DayRepository doInBackground(Context... contexts) {
            return new DayRepository(contexts[0]);
        }

        @Override
        protected void onPostExecute(DayRepository dayRepository) {
            super.onPostExecute(dayRepository);
            switch (action) {
                case INIT:
                    beerWidget.init(dayRepository);
                    break;
                case ADD:
                    beerWidget.add(dayRepository);
                    break;
            }
        }
    }
}

