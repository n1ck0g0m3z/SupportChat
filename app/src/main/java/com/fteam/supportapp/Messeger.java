package com.fteam.supportapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;
import android.view.View.OnClickListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.util.ArrayList;


public class Messeger extends Activity implements OnClickListener, LocationListener {

    private ArrayList<com.fteam.supportapp.Message> mMessages = new ArrayList<com.fteam.supportapp.Message>();
    private EditText sendMessage;
    boolean isNetworkEnabled = false;
    boolean isGPSenabled = false;
    boolean connection=false;
    Location location;
    double latitude;
    double longitude;
    private static final int MIN_DISTANCE_CHANGE_FOR_UPDATES = 5;
    private static final int MIN_TIME_BTW_UPDATES = 3000;
    protected LocationManager locationManager;
    private Socket mSocket;
    {
        try{
            mSocket = IO.socket("http://157.7.137.182:3000");
        } catch (URISyntaxException e){
            throw new RuntimeException(e);
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messeger);
        getLocation();
        mSocket.connect();
        mSocket.emit("join room", "chatTest");
        mSocket.on("chat message", onNewMessage);
        sendMessage = (EditText) findViewById(R.id.message_input);
        ImageButton button = (ImageButton) findViewById(R.id.send_button);
        button.setOnClickListener(this);
    }

    void peopleIn(String number){
        TextView textView = (TextView) findViewById(R.id.peoNum);
        textView.setText("0");
//        setContentView(textView);
    }

    private void decryptedMsg(String data){
        String category = "";
//        peopleIn("233");
        Log.d("enr","sdsdsd");
        try {
            JSONObject jsonObject = new JSONObject(data);
            category = jsonObject.getString("category");
            if(category.equals("msg")) {
                addMessage(jsonObject.getString("message"), false);
            }else
            if(category.equals("numP")){
                String number = jsonObject.getString("num");
                Log.d("asd",number);
                peopleIn(number);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private Emitter.Listener onNewMessage = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            Messeger.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    String data = (String) args[0];
                    addMessage(data,false);
                }
            });
        }
    };

    public void addMessage(String message, boolean bool) {
        String blank = "";
        if(blank.compareTo(message)!=0) {
            mMessages.add(new Message(message, bool));
            ListView list = (ListView) findViewById(R.id.liveChat);
            list.setAdapter(new ListAdapt(this, R.layout.chat_item, mMessages) {
                @Override
                public void onEntrada(Object entrada, View view) {
                    TextView msg = (TextView) view.findViewById(R.id.tvBody);
                    msg.setText(((Message) entrada).getMessage());
                }
            });
        }
    }

    private void attemptSend(){
        String chatMessage = sendMessage.getText().toString().trim();
        if(TextUtils.isEmpty(chatMessage))return;
        sendMessage.setText("");
        mSocket.emit("chat message",chatMessage);
        addMessage(chatMessage, true);
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.send_button){
            attemptSend();
        }
    }

    public void getLocation(){
        locationManager = (LocationManager) this.getSystemService(LOCATION_SERVICE);
        isGPSenabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        if( !isGPSenabled && !isNetworkEnabled) {

            showDialog(Messeger.this);

            if(!isGPSenabled){
                Log.d("GPS", "gps fail");
            }
            if(!isNetworkEnabled){
                Log.d("Network", "network fail");
            }
        }else{
            connection = true;
            if (isNetworkEnabled) {
                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
                        MIN_TIME_BTW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                Log.d("network", "network ok");
                if (locationManager != null) {
                    location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                    if (location != null) {
                        latitude = location.getLatitude();
                        longitude = location.getLongitude();
                    }
                }
            }
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        //mSocket.emit("chat message","Position" + latitude + "   " + longitude);
        Log.d("Position", latitude + "   " + longitude);
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("name","hola");
            jsonObject.put("x",longitude);
            jsonObject.put("y", latitude);
            mSocket.emit("addInfo", jsonObject.toString());
        }catch (JSONException e){
            Log.e("ERROR",e.toString());
        }
    }

    public void showDialog(Context ctx1){
        final Context ctx = ctx1;
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ctx);
        alertDialogBuilder.setIcon(R.drawable.error);
        alertDialogBuilder.setTitle("位置情報サービスが無効です");
        alertDialogBuilder.setMessage("位置情報サービスをオンにしてください。");
        alertDialogBuilder.setPositiveButton("設定", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog,int which) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                ctx.startActivity(intent);
            }
        });

        // on pressing cancel button
        alertDialogBuilder.setNegativeButton("キャンセル", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        alertDialogBuilder.show();
    }

    @Override
    public void onProviderDisabled(String provider) {
        showDialog(Messeger.this);
        Log.d("DISABLED","disabled");
        getLocation();
    }

    @Override
    public void onProviderEnabled(String provider) {
        Log.d("ENABLED","enabled");
        getLocation();
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {}

}
