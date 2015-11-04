package com.fteam.supportapp;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

public class AddressList extends Activity {

    ArrayAdapter<String> list;
    Intent intent;
    Prefarence pref = new Prefarence();
    static int x = 0;

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address_list);

        ListView lv = (ListView) findViewById(R.id.list);
        Button addbtn = (Button) findViewById(R.id.add);
        list = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1);
        String[] data = new String[3];
        data[0] = pref.getName(this);
        data[1] = pref.getJanle(this);
        data[2] = pref.getAddress(this);

        Log.d("arrayAdapter", "追加");

        if (list != null) {
            list.addAll(data);
            lv.setAdapter(list);
        } else {
            lv.setAdapter(list);
        }

        Log.d("arrayAdapter", "ListViewセット");

        addbtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                x += 1;
                intent = new Intent();
                Intent mainIntent = new Intent().setClass(AddressList.this, ConfigActivity.class);
                startActivity(mainIntent);

            }
        });
    }

}
