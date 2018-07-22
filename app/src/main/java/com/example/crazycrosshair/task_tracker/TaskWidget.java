package com.example.crazycrosshair.task_tracker;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Implementation of App Widget functionality.
 */
public class TaskWidget extends AppWidgetProvider {


    public static final String WIDGET_IDS_KEY ="mywidgetproviderwidgetids";


    Date current = new Date();
    String names,dates,statuss,notes,ids;


    void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        Intent intent = new Intent(context, Taskmanager.class);
        intent.putExtra("task", names);
        intent.putExtra("date", dates);
        intent.putExtra("status", statuss);
        intent.putExtra("note", notes);
        intent.putExtra("id",ids);


        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        RemoteViews views = new RemoteViews(context.getPackageName(),R.layout.task_widget);
        if (dates.length()!=0) {
            views.setTextViewText(R.id.task_tv, names);
            views.setTextViewText(R.id.date_tv, dates);
            views.setTextViewText(R.id.movebut,"Перейти");
        }
        else {
            views.setTextViewText(R.id.task_tv,"Задач нет");
            views.setTextViewText(R.id.date_tv,"");
            views.setTextViewText(R.id.movebut,"Создать");
        }
        views.setOnClickPendingIntent(R.id.movebut, pendingIntent);
        appWidgetManager.updateAppWidget(appWidgetId, views);


    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        update(context, appWidgetManager, appWidgetIds);
    }


    public void update(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for(int j = 0; j < appWidgetIds.length; j++) {
            int appWidgetId = appWidgetIds[j];
            try {Taskstorage taskstoragew = new Taskstorage(context);
                Map<String, ?> tasks = taskstoragew.getAll();
                if (tasks.size()>1) {
                    Map<String, Date> dateMap = new HashMap<String, Date>();
                    for (Map.Entry entry : tasks.entrySet()) {
                        if (entry.getKey().toString().substring(0, 2).equals("da")) {  // ключ = "date"
                            Date date = MainActivity.DateStringToDate(entry.getValue().toString());
                            dateMap.put(entry.getKey().toString(), date);
                        }

                    }
                    Map<String, Date> sortedmap = SortMap(dateMap, false);
                    for (Map.Entry entry : sortedmap.entrySet()) {
                        if (current.compareTo((Date) entry.getValue()) == -1) {
                            if (!taskstoragew.getbyid("status" + entry.getKey().toString().substring(4)).equals("завершенный")) {
                                names = taskstoragew.getbyid("name" + entry.getKey().toString().substring(4));
                                dates = taskstoragew.getbyid("date" + entry.getKey().toString().substring(4));
                                statuss = taskstoragew.getbyid("status" + entry.getKey().toString().substring(4));
                                notes = taskstoragew.getbyid("note" + entry.getKey().toString().substring(4));
                                ids = entry.getKey().toString().substring(4);
                                break;
                            }
                            else {
                                dates = "";
                            }
                        } else {
                            dates = "";
                        }
                    }
                }
                else {
                    dates = "";
                }
            }
            catch (ActivityNotFoundException e) {
                Toast.makeText(context.getApplicationContext(), "There was a problem loading the application: ", Toast.LENGTH_SHORT).show();
            }
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.hasExtra(WIDGET_IDS_KEY)) {
            int[] ids = intent.getExtras().getIntArray(WIDGET_IDS_KEY);
            this.update(context, AppWidgetManager.getInstance(context), ids);
        } else
            super.onReceive(context, intent);
    }

    public static void updateMyWidgets(Context context) {
        AppWidgetManager man = AppWidgetManager.getInstance(context);
        int[] ids = man.getAppWidgetIds(
                new ComponentName(context,TaskWidget.class));
        Intent updateIntent = new Intent();
        updateIntent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
        updateIntent.putExtra(TaskWidget.WIDGET_IDS_KEY, ids);
        context.sendBroadcast(updateIntent);
    }

    public Map<String, Date> SortMap(Map<String, Date> dateMap, final boolean direct) {
        List<Map.Entry<String, Date>> list =
                new LinkedList<>(dateMap.entrySet());
        Collections.sort(list, new Comparator<Map.Entry<String, Date>>() {
            @Override
            public int compare(Map.Entry<String, Date> a, Map.Entry<String, Date> b) {
                if (direct)
                    return a.getValue().compareTo(b.getValue()) * -1;
                else
                    return a.getValue().compareTo(b.getValue()) ;
            }
        });
        Map<String, Date> result = new LinkedHashMap<>();
        for (Map.Entry<String, Date> entry : list) {
            result.put(entry.getKey(), entry.getValue());
        }
        return result;


    }

}

