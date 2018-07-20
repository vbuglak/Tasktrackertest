package com.example.crazycrosshair.task_tracker;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.Map;

/**
 * Created by crazycrosshair on 18.07.2018.
 */

public class Taskstorage {
    public static final String STORAGE_NAME = "Tasks3";

    private static SharedPreferences tasks = null;
    private static SharedPreferences.Editor editor = null;
    private Context context = null;

    Taskstorage( Context cntxt ){
        context = cntxt;
        tasks = context.getSharedPreferences(STORAGE_NAME, Context.MODE_PRIVATE);
        editor = tasks.edit();
    }


    public void addTask( String name_value, String date_value, int status_value, String note_value ){
        int id=0;
        id=tasks.getInt("Id",0);
        editor.putString( "name"+id, name_value );
        editor.putString( "date"+id, date_value );
        editor.putInt( "status"+id, status_value );
        editor.putString( "note"+id, note_value );
        editor.putInt("Id",++id);
        editor.apply();
    }
    public void editTask( String name_value, String date_value, int status_value, String note_value, String id ){
        editor.putString( "name"+id, name_value );
        editor.putString( "date"+id, date_value );
        editor.putInt( "status"+id, status_value );
        editor.putString( "note"+id, note_value );
        editor.apply();
    }

    public void removeTask (String id){
        editor.remove("date"+id);
        editor.remove("name"+id);
        editor.remove("status"+id);
        editor.remove("note"+id);
        editor.apply();
    }

    public Map getAll(){
        return tasks.getAll();
    }

    public String getbyid(String s){
        if (s.substring(0,2).equals("st")){
            int stasus = tasks.getInt(s,0);
            if (stasus == 0){
                return ("новый");
            }else if (stasus == 1){
                return ("в процессе");
            }else if (stasus == 2){
                return ("завершенный");
            }
        } else {
            return (tasks.getString(s, ""));
        }
        return ("");
    }

}
