package com.fteam.supportapp;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.preference.Preference;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class ConfigActivity extends Activity {

    Intent intent;
    Preference pref ;
    int x = AddressList.x;
    LocalStorage localStorage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config);
        localStorage = new LocalStorage(this);
        final EditText name = (EditText) findViewById(R.id.relation);
        final EditText address = (EditText) findViewById(R.id.address);
        final EditText janle = (EditText) findViewById(R.id.janle);


        Button add = (Button) findViewById(R.id.add);


        add.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                x += 1;
                Prefarence pref = new Prefarence();

                localStorage.insertContact(name.getText().toString(), address.getText().toString()
                        , janle.getText().toString());
                pref.setData(ConfigActivity.this,localStorage.getName(),localStorage.getMail(),localStorage.getPhone());
                intent = new Intent();
                Intent mainIntent = new Intent().setClass(ConfigActivity.this, AddressList.class);
                Log.d("test", localStorage.getName());
                Log.d("test", localStorage.getMail());
                Log.d("test", localStorage.getPhone());
                startActivity(mainIntent);

            }
        });
    }

    @Override
    protected void onStop() {
        super.onDestroy();
        Toast.makeText(this, "連絡先が追加されました", Toast.LENGTH_LONG).show();
    }

}
