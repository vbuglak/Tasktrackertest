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
    static Taskstorage taskstorage;
    ArrayList<String> idrow;
    private TableRow editrow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        taskstorage = new Taskstorage(this);
        inittable();

    }

    @Override
    protected void onStart() {
        super.onStart();
        inittable();
    }
    private void inittable() {

        Map<String, ?> tasks = taskstorage.getAll();
        Map<String, Date> dateMap = new HashMap<String, Date>();
        for (Map.Entry entry : tasks.entrySet()) {
            if (entry.getKey().toString().substring(0, 2).equals("da")) {  // ключ = "date"
                Date date = DateStringToDate(entry.getValue().toString());
                dateMap.put(entry.getKey().toString(),date);
            }

        }
        Map<String,Date> sortedmap = SortMap(dateMap);
        int a = 10;
        // заполнить таблицу с помощью tasks sortedmap
        TableLayout tl = (TableLayout) findViewById(R.id.mainactivity_tl);
        tl.removeAllViewsInLayout();
        idrow = new ArrayList<>();
        for (Map.Entry entry : sortedmap.entrySet()){
            String id = entry.getKey().toString().substring(4);
            idrow.add(id);
            String name = taskstorage.getbyid("name"+id);
            String date = taskstorage.getbyid("date"+id);
            String status =taskstorage.getbyid("status"+id);
            String note =taskstorage.getbyid("note"+id);
            TableRow tbrow = new TableRow(this);
            registerForContextMenu(tbrow);
            TextView tvr0 = new TextView(this);
            tvr0.setText("  " + name + "  ");
            tvr0.setTextColor(Color.BLACK);
            tvr0.setTextSize(20);
            tbrow.addView(tvr0);
            TextView tvr1 = new TextView(this);
            tvr1.setText("  " + date + "  ");
            tvr1.setTextColor(Color.BLACK);
            tvr1.setTextSize(20);
            tbrow.addView(tvr1);
            TextView tvr2 = new TextView(this);
            tvr2.setText("  " + status + "  ");
            tvr2.setTextColor(Color.BLACK);
            tvr2.setTextSize(20);
            tbrow.addView(tvr2);
            tl.addView(tbrow);
            TableRow tbrow1 = new TableRow(this);
            registerForContextMenu(tbrow1);
            TextView tvr3 = new TextView(this);
            tvr3.setText("  " + note + "  ");
            tvr3.setTextColor(Color.BLACK);
            tvr3.setTextSize(20);
            tbrow1.addView(tvr3);
            tl.addView(tbrow1);

        }

    }

    private Map<String,Date> SortMap(Map<String, Date> dateMap) {
        List<Map.Entry<String, Date>> list =
                new LinkedList<>(dateMap.entrySet());
        Collections.sort(list, new Comparator<Map.Entry<String, Date>>() {
            @Override
            public int compare(Map.Entry<String, Date> a, Map.Entry<String, Date> b) {
                return a.getValue().compareTo(b.getValue())*-1;
            }
        });
        Map<String, Date> result = new LinkedHashMap<>();
        for (Map.Entry<String,Date> entry : list)
        {
            result.put(entry.getKey(), entry.getValue());
        }
        return result;


    }

    private Date DateStringToDate(String s) {
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

    private String Parsemounth(String mounth) {
        if (mounth.equals("января")){
            mounth = "01";
        }
        else if (mounth.equals("февраля")){
            mounth = "02";
        }
        else if (mounth.equals("марта")){
            mounth = "03";
        }
        else if (mounth.equals("апреля")){
            mounth = "04";
        }
        else if (mounth.equals("мая")){
            mounth = "05";
        }
        else if (mounth.equals("июня")){
            mounth = "06";
        }
        else if (mounth.equals("июля")){
            mounth = "07";
        }
        else if (mounth.equals("августа")){
            mounth = "08";
        }
        else if (mounth.equals("сентября")){
            mounth = "09";
        }
        else if (mounth.equals("октября")){
            mounth = "10";
        }
        else if (mounth.equals("ноября")){
            mounth = "11";
        }
        else if (mounth.equals("декабря")){
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
                                    ContextMenu.ContextMenuInfo menuInfo)
    {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.add(Menu.NONE, 0, Menu.NONE, "Изменить");
        menu.add(Menu.NONE, 1, Menu.NONE, "Удалить");
        editrow = (TableRow) v;
    }
    public boolean onContextItemSelected(MenuItem item) {
        TableLayout tableLayout = findViewById(R.id.mainactivity_tl);
        int id;
        if (tableLayout.indexOfChild(editrow) == 0 || tableLayout.indexOfChild(editrow) == 1){
             id = 0;
        } else if (editrow.getChildCount()>1) {
             id = tableLayout.indexOfChild(editrow)/2;
        } else {
             id = (tableLayout.indexOfChild(editrow)-1)/2;
        }
        switch (item.getItemId()) {
            case 0:
                Intent intent = new Intent(this,Taskmanager.class);
                if (editrow.getChildCount()>1) {
                    TextView tv0 = (TextView) editrow.getChildAt(0);
                    TextView tv1 = (TextView) editrow.getChildAt(1);
                    TextView tv2 = (TextView) editrow.getChildAt(2);
                    TableRow tr = (TableRow) tableLayout.getChildAt(tableLayout.indexOfChild(editrow) + 1);
                    TextView tv3 = (TextView) tr.getChildAt(0);
                    intent.putExtra("task", tv0.getText().toString().replace("  ",""));
                    intent.putExtra("date", tv1.getText().toString().replace("  ",""));
                    intent.putExtra("status", tv2.getText().toString().replace("  ",""));
                    intent.putExtra("note", tv3.getText().toString().replace("  ",""));
                    intent.putExtra("id", Integer.toString(id));
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
                    intent.putExtra("id", Integer.toString(id));
                }
                startActivity(intent);

                break;
            case 1:
                if (editrow.getChildCount()>1) {
                    tableLayout.removeView(tableLayout.getChildAt(tableLayout.indexOfChild(editrow)+1));
                    tableLayout.removeView(editrow);
                }
                else {
                    tableLayout.removeView(tableLayout.getChildAt(tableLayout.indexOfChild(editrow)-1));
                    tableLayout.removeView(editrow);
                }

                taskstorage.removeTask((idrow.get(id)));
                idrow.remove(id);
                break;

        }
        return true;
    }

}