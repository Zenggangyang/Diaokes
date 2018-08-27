package com.example.administrator.diaokes.services;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.util.Log;

import com.example.administrator.diaokes.OkHttp.HttpUtil;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class AutoUpdateService extends Service {
    public AutoUpdateService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        updateInfo();
        updateimg();
        AlarmManager manager = (AlarmManager)getSystemService(ALARM_SERVICE);
        int anhour = 8*60*60*1000;
        long triggerAtTime = SystemClock.elapsedRealtime() + anhour;
        Intent i = new Intent(this,AutoUpdateService.class);
        PendingIntent pi = PendingIntent.getService(this,0,i,0);
        manager.cancel(pi);
        manager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP,triggerAtTime,pi);
        return super.onStartCommand(intent,flags,startId);
    }

    private void updateInfo(){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        String date = preferences.getString("date",null);
        String url = "http://192.168.1.103:8080/tomcats/notify";
        //if(date != null){
        HttpUtil.sendOkHttpRequest(url, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String date = response.body().string();
                SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(AutoUpdateService.this).edit();
                editor.putString("date",date);
                editor.apply();
            }
        });
        //}
    }

    private void updateimg(){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        String date = preferences.getString("img",null);
        final String url = "http://guolin.tech/api/bing_pic";
        //if(date != null) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    OkHttpClient client1 = new OkHttpClient();
                    Request request1 = new Request.Builder()
                            .url(url)
                            .build();
                    Response response1 = client1.newCall(request1).execute();
                    SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(AutoUpdateService.this).edit();
                    editor.putString("img",response1.body().string());
                    editor.apply();
                }catch (Exception e){
                    Log.e("error",e.getMessage());
                }
            }
        }).start();
        //}
    }
}
