package com.fteam.supportapp;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import java.util.ArrayList;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class Prefarence {


    ArrayList<String> list = new ArrayList<String>();
    public void setData(Context context, String name, String janle,String address) {
        SharedPreferences pref = context.getSharedPreferences("data",
                Context.MODE_PRIVATE);
        Editor edit = pref.edit();

        edit.putString("name","名前 :" +name);
        edit.putString("janle","メール :" +janle);
        edit.putString("address","連絡先 :"+ address);

        edit.commit();
    }

    public void setMyname(Context context,String myname){
        SharedPreferences pref = context.getSharedPreferences("data",
                Context.MODE_PRIVATE);
        Editor edit = pref.edit();
        edit.putString("myname", myname);

        edit.commit();
    }

    public String getAddress(Context context) {
        SharedPreferences pref = context.getSharedPreferences("data",
                Context.MODE_PRIVATE);

        return pref.getString("address", "");
    }

    public String getJanle(Context context) {
        SharedPreferences pref = context.getSharedPreferences("data",
                Context.MODE_PRIVATE);

        return pref.getString("janle", "");
    }

    public String getName(Context context) {
        SharedPreferences pref = context.getSharedPreferences("data",
                Context.MODE_PRIVATE);

        return pref.getString("name", "");
    }

    public String getMyname(Context context){
        SharedPreferences pref = context.getSharedPreferences("data",
                Context.MODE_PRIVATE);

        return pref.getString("myname", "");

    }

}
