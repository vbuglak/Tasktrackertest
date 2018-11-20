package com.example.crazycrosshair.task_tracker;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.ToggleButton;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    Taskstorage taskstorage;
    ArrayList<String> idrow; // массив для хранения id строк таблицы. Индекс масива совпадает с номером строки. Строка подразумевает два tablerow.
    TableLayout tableLayout;
    TableRow editrow; // tablerow который вызывает контекстное меню


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        taskstorage = new Taskstorage(this);
        tableLayout = findViewById(R.id.mainactivity_tl);
        inittable();
    }
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        ToggleButton tb_new = findViewById(R.id.mainactivity_tbnew);
        ToggleButton tb_pro = findViewById(R.id.mainactivity_tbproces);
        ToggleButton tb_end = findViewById(R.id.mainactivity_tbend);
        outState.putBoolean("tb_new",tb_new.isChecked());
        outState.putBoolean("tb_pro",tb_pro.isChecked());
        outState.putBoolean("tb_end",tb_end.isChecked());
    }

    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        Boolean tb_new_b = savedInstanceState.getBoolean("tb_new");
        Boolean tb_pro_b = savedInstanceState.getBoolean("tb_pro");
        Boolean tb_end_b = savedInstanceState.getBoolean("tb_end");
        Editvisability("но", tb_new_b);
        Editvisability("в ", tb_pro_b);
        Editvisability("за", tb_end_b);
    }

    @Override
    protected void onStart() {
        super.onStart();
        inittable();
        initfilter();
    }

    private void inittable() {

        Map<String, ?> tasks = taskstorage.getAll();
        Map<String, Date> dateMap = new HashMap<String, Date>();
        for (Map.Entry entry : tasks.entrySet()) {
            if (entry.getKey().toString().substring(0, 2).equals("da")) {  // ключ = "date"
                Date date = DateStringToDate(entry.getValue().toString());
                dateMap.put(entry.getKey().toString(), date); // создаем map только с датами
            }
        }
        Map<String, Date> sortedmap = SortMap(dateMap,true);
        TableLayout tl = (TableLayout) findViewById(R.id.mainactivity_tl);
        tl.removeAllViewsInLayout();
        idrow = new ArrayList<>();
        for (Map.Entry entry : sortedmap.entrySet()) {
            String id = entry.getKey().toString().substring(4);
            idrow.add(id);
            String name = taskstorage.getbyid("name" + id);
            String date = taskstorage.getbyid("date" + id);
            String status = taskstorage.getbyid("status" + id);
            String note = taskstorage.getbyid("note" + id);
            TableRow tbrow = new TableRow(this);
            registerForContextMenu(tbrow);
            TextView tvr0 = new TextView(this);
            tvr0.setText(name);
            tvr0.setTextColor(Color.BLACK);
            tvr0.setTextSize(20);
            tbrow.addView(tvr0);
            TextView tvr1 = new TextView(this);
            tvr1.setText(date);
            tvr1.setTextColor(Color.BLACK);
            tvr1.setTextSize(20);
            tbrow.addView(tvr1);
            TextView tvr2 = new TextView(this);
            tvr2.setText(status);
            tvr2.setTextColor(Color.BLACK);
            tvr2.setTextSize(20);
            tbrow.addView(tvr2);
            tl.addView(tbrow);
            TableRow tbrow1 = new TableRow(this);
            registerForContextMenu(tbrow1);
            TextView tvr3 = new TextView(this);
            tvr3.setText(note);
            tvr3.setTextColor(Color.BLACK);
            tvr3.setTextSize(20);
            tbrow1.addView(tvr3);
            tl.addView(tbrow1);
        }
    }

    private void initfilter() {
        ToggleButton tb_new = findViewById(R.id.mainactivity_tbnew);
        ToggleButton tb_pro = findViewById(R.id.mainactivity_tbproces);
        ToggleButton tb_end = findViewById(R.id.mainactivity_tbend);
        Editvisability("но", tb_new.isChecked());
        Editvisability("в ", tb_pro.isChecked());
        Editvisability("за", tb_end.isChecked());
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

    public static Date DateStringToDate(String s) {
        String[] lines = s.split(" ");
        String mounth = Parsemounth(lines[1]);
        String day = lines[0];
        String year = lines[2];
        String date = day + "." + mounth + "." + year;
        SimpleDateFormat format = new SimpleDateFormat();
        format.applyPattern("dd.MM.yyyy");
        Date docDate = null;
        try {
            docDate = format.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return (docDate);
    }

    private static String Parsemounth(String mounth) {
        if (mounth.equals("января")) {
            mounth = "01";
        } else if (mounth.equals("февраля")) {
            mounth = "02";
        } else if (mounth.equals("марта")) {
            mounth = "03";
        } else if (mounth.equals("апреля")) {
            mounth = "04";
        } else if (mounth.equals("мая")) {
            mounth = "05";
        } else if (mounth.equals("июня")) {
            mounth = "06";
        } else if (mounth.equals("июля")) {
            mounth = "07";
        } else if (mounth.equals("августа")) {
            mounth = "08";
        } else if (mounth.equals("сентября")) {
            mounth = "09";
        } else if (mounth.equals("октября")) {
            mounth = "10";
        } else if (mounth.equals("ноября")) {
            mounth = "11";
        } else if (mounth.equals("декабря")) {
            mounth = "12";
        }

        return (mounth);
    }

    public void Newtask(View view) {
        Intent intent = new Intent(this, Taskmanager.class);
        startActivity(intent);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.add(Menu.NONE, 0, Menu.NONE, "Изменить");
        menu.add(Menu.NONE, 1, Menu.NONE, "Удалить");
        editrow = (TableRow) v;
    }

    public boolean onContextItemSelected(MenuItem item) {

        int id;
        if (tableLayout.indexOfChild(editrow) == 0 || tableLayout.indexOfChild(editrow) == 1) {
            id = 0;
        } else if (editrow.getChildCount() > 1) {
            id = tableLayout.indexOfChild(editrow) / 2;
        } else {
            id = (tableLayout.indexOfChild(editrow) - 1) / 2;
        }
        switch (item.getItemId()) {
            case 0: // изменить в зависимости на какой строке вызвали контекстное меню
                Intent intent = new Intent(this, Taskmanager.class);
                if (editrow.getChildCount() > 1) {
                    TextView tv0 = (TextView) editrow.getChildAt(0);
                    TextView tv1 = (TextView) editrow.getChildAt(1);
                    TextView tv2 = (TextView) editrow.getChildAt(2);
                    TableRow tr = (TableRow) tableLayout.getChildAt(tableLayout.indexOfChild(editrow) + 1);
                    TextView tv3 = (TextView) tr.getChildAt(0);
                    intent.putExtra("task", tv0.getText().toString().replace("  ", ""));
                    intent.putExtra("date", tv1.getText().toString().replace("  ", ""));
                    intent.putExtra("status", tv2.getText().toString().replace("  ", ""));
                    intent.putExtra("note", tv3.getText().toString().replace("  ", ""));
                    intent.putExtra("id", idrow.get(id));
                } else {
                    TableRow tr = (TableRow) tableLayout.getChildAt(tableLayout.indexOfChild(editrow) - 1);
                    TextView tv0 = (TextView) tr.getChildAt(0);
                    TextView tv1 = (TextView) tr.getChildAt(1);
                    TextView tv2 = (TextView) tr.getChildAt(2);
                    TextView tv3 = (TextView) editrow.getChildAt(0);
                    intent.putExtra("task", tv0.getText().toString());
                    intent.putExtra("date", tv1.getText().toString());
                    intent.putExtra("status", tv2.getText().toString());
                    intent.putExtra("note", tv3.getText().toString());
                    intent.putExtra("id", idrow.get(id));
                }
                startActivity(intent);

                break;
            case 1: // удалить в зависимости на какой строке вызвали контекстное меню
                if (editrow.getChildCount() > 1) {
                    tableLayout.removeView(tableLayout.getChildAt(tableLayout.indexOfChild(editrow) + 1));
                    tableLayout.removeView(editrow);
                } else {
                    tableLayout.removeView(tableLayout.getChildAt(tableLayout.indexOfChild(editrow) - 1));
                    tableLayout.removeView(editrow);
                }

                taskstorage.removeTask((idrow.get(id)));
                idrow.remove(id);
                TaskWidget.updateMyWidgets(this);
                break;

        }
        return true;
    }

    public void Tb_clicked(View view) {                     //фильтры
        boolean on = ((ToggleButton) view).isChecked();
        switch (view.getId()){
            case R.id.mainactivity_tbnew:
                Editvisability("но", on);
                break;
            case R.id.mainactivity_tbproces:
                Editvisability("в ", on);
                break;
            case R.id.mainactivity_tbend:
                Editvisability("за", on);
                break;
        }
    }

    private void Editvisability(String status, boolean on) {
        for (int i = 0; i < tableLayout.getChildCount(); i += 2) {
            TableRow tr1 = (TableRow) tableLayout.getChildAt(i);
            TableRow tr2 = (TableRow) tableLayout.getChildAt(i + 1);
            TextView status_tv = (TextView) tr1.getChildAt(2);
            if (status_tv.getText().toString().substring(0, 2).equals(status)) {
                if (!on) {
                    tr1.setVisibility(View.GONE);
                    tr2.setVisibility(View.GONE);
                } else {
                    tr1.setVisibility(View.VISIBLE);
                    tr2.setVisibility(View.VISIBLE);
                }
            }
        }
    }
}