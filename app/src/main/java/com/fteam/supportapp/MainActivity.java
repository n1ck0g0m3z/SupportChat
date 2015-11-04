package com.fteam.supportapp;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

public class MainActivity extends Activity {

    Prefarence pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImageButton talk = (ImageButton)findViewById(R.id.talk);
        ImageButton emergency = (ImageButton)findViewById(R.id.emergency);
        final EditText myname = (EditText)findViewById(R.id.name);
        pref = new  Prefarence();
        final Intent intent = new Intent();

        talk.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                pref.setMyname(MainActivity.this, myname.getText().toString());
                Intent mainIntent = new Intent().setClass(MainActivity.this, Messeger.class);
                startActivity(mainIntent);
            }
        });

        emergency.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent mainIntent = new Intent().setClass(MainActivity.this, EmergencyActivity.class);
                startActivity(mainIntent);
            }
        });


    }

}

