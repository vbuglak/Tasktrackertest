package com.example.crazycrosshair.task_tracker;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

public class Taskmanager extends AppCompatActivity {
    Taskstorage taskstorage;
    TextView Date_tv;
    Calendar Datecal = Calendar.getInstance();
    boolean edit;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_taskmanager);
        taskstorage = new Taskstorage(this);

    }
    @Override
    protected void onStart() {
        super.onStart();
        intent = getIntent();
        Date_tv = (TextView) findViewById(R.id.taskmanager_date_tv);
        EditText Task_ed = findViewById(R.id.taskmanager_task_et);
        EditText Note_ed = findViewById(R.id.taskmanager_note_et);
        Button addedit_but = findViewById(R.id.taskmanager_add_edit_but);
        CheckBox new_checkbox = findViewById(R.id.taskmanager_status_new_cb);
        CheckBox pro_checkbox = findViewById(R.id.taskmanager_status_pro_cb);
        CheckBox end_checkbox = findViewById(R.id.taskmanager_status_end_cb);
        Task_ed.setText("");                                        // возвращаем исходное состояние
        Note_ed.setText("");
        addedit_but.setText("Добавить");
        new_checkbox.setClickable(false);
        new_checkbox.setChecked(true);
        pro_checkbox.setClickable(true);
        pro_checkbox.setChecked(false);
        end_checkbox.setClickable(true);
        end_checkbox.setChecked(false);
        if (intent.getStringExtra("task") == null){
            setInitialDateTime();
            new_checkbox.setClickable(false);
            edit = false;
        } else {
            edit = true;
            addedit_but.setText("Изменить");
            Task_ed.setText(intent.getStringExtra("task"));
            Note_ed.setText(intent.getStringExtra("note"));
            Date_tv.setText(intent.getStringExtra("date"));
            if (intent.getStringExtra("status").equals("новый")){
                new_checkbox.setClickable(false);
                new_checkbox.setChecked(true);
            } else if(intent.getStringExtra("status").equals("в процессе")){
                new_checkbox.setClickable(true);
                new_checkbox.setChecked(false);
                pro_checkbox.setClickable(false);
                pro_checkbox.setChecked(true);
            } else if(intent.getStringExtra("status").equals("завершенный")){
                new_checkbox.setClickable(true);
                new_checkbox.setChecked(false);
                end_checkbox.setClickable(false);
                end_checkbox.setChecked(true);
            }

        }
    }
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        EditText Task_ed = findViewById(R.id.taskmanager_task_et);
        EditText Note_ed = findViewById(R.id.taskmanager_note_et);
        CheckBox new_checkbox = findViewById(R.id.taskmanager_status_new_cb);
        CheckBox pro_checkbox = findViewById(R.id.taskmanager_status_pro_cb);
        CheckBox end_checkbox = findViewById(R.id.taskmanager_status_end_cb);
        if (new_checkbox.isChecked()){
            outState.putString("cb_checked","0");
        } else if (pro_checkbox.isChecked()) {
            outState.putString("cb_checked", "1");
        } else if (end_checkbox.isChecked()) {
            outState.putString("cb_checked", "2");
        }
        outState.putString("task",Task_ed.getText().toString());
        outState.putString("note",Note_ed.getText().toString());



    }

    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        EditText Task_ed = findViewById(R.id.taskmanager_task_et);
        EditText Note_ed = findViewById(R.id.taskmanager_note_et);
        CheckBox new_checkbox = findViewById(R.id.taskmanager_status_new_cb);
        CheckBox pro_checkbox = findViewById(R.id.taskmanager_status_pro_cb);
        CheckBox end_checkbox = findViewById(R.id.taskmanager_status_end_cb);
        Task_ed.setText(savedInstanceState.getString("task"));
        Note_ed.setText(savedInstanceState.getString("note"));
        switch (savedInstanceState.getString("cb_checked")){
            case "0":
                new_checkbox.setChecked(true);
                break;
            case "1":
                pro_checkbox.setChecked(true);
                pro_checkbox.setClickable(false);
                new_checkbox.setClickable(true);
                break;
            case "2":
                end_checkbox.setChecked(true);
                end_checkbox.setClickable(false);
                new_checkbox.setClickable(true);
                break;
        }
    }



    private void setInitialDateTime() {

        Date_tv.setText(DateUtils.formatDateTime(this,
                Datecal.getTimeInMillis(),
                DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_SHOW_YEAR
        ));
    }
    public void datetime_clicked(View view) {
        new DatePickerDialog(this, d,
                Datecal.get(Calendar.YEAR),
                Datecal.get(Calendar.MONTH),
                Datecal.get(Calendar.DAY_OF_MONTH))
                .show();
    }

    DatePickerDialog.OnDateSetListener d=new DatePickerDialog.OnDateSetListener() {
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            Datecal.set(Calendar.YEAR, year);
            Datecal.set(Calendar.MONTH, monthOfYear);
            Datecal.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            setInitialDateTime();
        }
    };

    public void Checkbox_clicked(View view) {
        CheckBox clicked_checkbox = (CheckBox) view;
        CheckBox new_checkbox = findViewById(R.id.taskmanager_status_new_cb);
        CheckBox pro_checkbox = findViewById(R.id.taskmanager_status_pro_cb);
        CheckBox end_checkbox = findViewById(R.id.taskmanager_status_end_cb);
        boolean checked = clicked_checkbox.isChecked();
        switch(view.getId()) {
            case R.id.taskmanager_status_new_cb:
                if (checked)
                {
                    clicked_checkbox.setClickable(false);
                    pro_checkbox.setClickable(true);
                    end_checkbox.setClickable(true);
                    pro_checkbox.setChecked(false);
                    end_checkbox.setChecked(false);
                }

                break;
            case R.id.taskmanager_status_pro_cb:
                if (checked)
                {
                    clicked_checkbox.setClickable(false);
                    new_checkbox.setClickable(true);
                    end_checkbox.setClickable(true);
                    new_checkbox.setChecked(false);
                    end_checkbox.setChecked(false);
                }
                break;
            case R.id.taskmanager_status_end_cb:
                if (checked)
                {
                    clicked_checkbox.setClickable(false);
                    pro_checkbox.setClickable(true);
                    new_checkbox.setClickable(true);
                    pro_checkbox.setChecked(false);
                    new_checkbox.setChecked(false);
                }
                break;

        }
    }

    public void addedit_but_clicked(View view) {
        LinearLayout tasklayout = findViewById(R.id.taskmanager_ll);
        EditText task_ed = (EditText)tasklayout.getChildAt(0);
        TextView date_tv = (TextView)tasklayout.getChildAt(1);
        CheckBox status_new_cb = (CheckBox)tasklayout.getChildAt(2);
        CheckBox status_pro_cb = (CheckBox)tasklayout.getChildAt(3);
        CheckBox status_end_cb = (CheckBox)tasklayout.getChildAt(4);
        EditText note_ed = (EditText)tasklayout.getChildAt(5);
        int statusint = 0;
        if (status_new_cb.isChecked())
        {
            statusint = 0; // 0 - статус "новый"
        }
        else if (status_pro_cb.isChecked())
        {
            statusint = 1; // 1 - статус "в процессе"
        }
        else if (status_end_cb.isChecked())
        {
            statusint = 2; // 2 - статус "завершенный"
        }
        if (task_ed.getText().length()==0)
        {
            Toast toastcheck = Toast.makeText(getApplicationContext(),
                    "Поля не заполнены", Toast.LENGTH_SHORT);
            toastcheck.show();
        }
        else
        {
            if (edit) {
                taskstorage.editTask(task_ed.getText().toString(), date_tv.getText().toString(), statusint, note_ed.getText().toString(),intent.getStringExtra("id"));
                super.onBackPressed();
            } else {
                taskstorage.addTask(task_ed.getText().toString(), date_tv.getText().toString(), statusint, note_ed.getText().toString());
                super.onBackPressed();
            }
            TaskWidget.updateMyWidgets(this);
        }


    }
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
    }

}