package com.fteam.supportapp;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

public class EmergencyActivity extends Activity {
    LocalStorage localStorage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emergency);
        localStorage = new LocalStorage(this);
        ImageButton call = (ImageButton) findViewById(R.id.call);
        Button add = (Button) findViewById(R.id.add);
        Button callList = (Button) findViewById(R.id.callList);
        Prefarence pref = new Prefarence();
        final Intent intent = new Intent();

        call.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Log.d("緊急ボタン", "押されました");
                SmsManager sms = SmsManager.getDefault();

                // sms送信
                 sms.sendTextMessage("09084552142", null, "ComSuppoからの通知" +
                 "\n"
                 + "以下の地点で緊急事態が発生しました。" + "\n" + "至急対応をお願いします。", null,null);
                 sms.sendTextMessage("08088892062", null,
                 "http://maps.google.com/maps?q=" + "経度緯度入力", null, null);

                // タスク
                AsyncTask<String, Void, String> task = new AsyncTask<String, Void, String>() {

                    @Override
                    protected String doInBackground(final String... params) {
                        Log.d("メール", "送信準備中");

                        try {

                            Properties props = new Properties();
                            // ホスト名
                            props.put("mail.host", "smtp.gmail.com");
                            Log.d("ホスト", "ホスト名設定");

                            // SMTPサーバー
                            props.put("mail.smtp.host", "smtp.gmail.com");
                            Log.d("SMTP", "設定");

                            // ポート
                            props.put("mail.smtp.port", "587");
                            Log.d("ポート", "設定");

                            // 認証(SMTP AUTH)
                            props.put("mail.smtp.auth", "true");
                            Log.d("認証", "認証");

                            // 暗号化(STARTTLS)
                            props.put("mail.smtp.starttls.enable", "true");
                            Log.d("暗号化", "暗号化");

                            // 接続タイムアウト(ミリ秒)
                            props.put("mail.smtp.connectiontimeout", "10000");
                            Log.d("接続", "タイムアウト");

                            // 転送タイムアウト(ミリ秒)
                            props.put("mail.smtp.timeout", "10000");

                            // セッション
                            Session session = Session.getInstance(props,
                                    new Authenticator() {

                                        @Override
                                        protected PasswordAuthentication getPasswordAuthentication() {
                                            Log.d("パスワード認証", "認証中");

                                            // 認証
                                            return new PasswordAuthentication(
                                                    params[0], params[1]);
                                        }
                                    });

                            MimeMessage msg = new MimeMessage(session);
                            // 形式
                            msg.setHeader("Content-Type", "text/html");
                            // 送信日
                            msg.setSentDate(new Date());
                            // 件名
                            msg.setSubject("ComSuppoからの通知", "UTF-8");
                            // 送信元
                            msg.setFrom(new InternetAddress(params[0],
                                    params[2], "UTF-8"));
                            // 配信元
                            msg.setSender(new InternetAddress(params[0]));
                            // 宛先(To) 単体
                            InternetAddress[] address = new InternetAddress[] { new InternetAddress(
                                    "skouta310@gmail.com") };
                            msg.setRecipients(Message.RecipientType.TO, address);

                            msg.setRecipients(Message.RecipientType.CC, address);
                            // 宛先(Bcc) なし
                            // address = new InternetAddress[]{};
                            // msg.setRecipients(Message.RecipientType.BCC,
                            // address);
                            // 本文
                            msg.setText("以下の地点で緊急事態が発生しました。\n至急対応をお願いします。\n"
                                    + "http://maps.google.com/maps?q="
                                    + "経度緯度入力", "UTF-8", "plain");

                            // 送信
                            Transport.send(msg);

                            return "メールを送信しました";

                        } catch (MessagingException e) {

                            return "メールの送信に失敗しました（１）";

                        } catch (UnsupportedEncodingException e) {

                            return "メールの送信に失敗しました（２）";
                        }
                    }

                    /**
                     * 完了
                     *
                     * @param message
                     *            メッセージ
                     */
                    @Override
                    protected void onPostExecute(String message) {

                        Toast.makeText(EmergencyActivity.this, "メールを送りました",
                                Toast.LENGTH_LONG).show();
                    }
                };

                // タスクを実行
                task.execute("skouta310@gmail.com", "Skoutarou310", localStorage.getName());
                Log.d("test",localStorage.getName());

            }

        });

        callList.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent mainIntent = new Intent().setClass(EmergencyActivity.this, ConfigActivity.class);
                startActivity(mainIntent);
            }
        });

        add.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent mainIntent = new Intent().setClass(EmergencyActivity.this, AddressList.class);
                startActivity(mainIntent);
            }
        });

    }

}
