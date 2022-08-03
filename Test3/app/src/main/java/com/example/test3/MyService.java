package com.example.test3;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;


public class MyService extends Service {
    private IBinder mBinder = new MyBinder();
    private static int counts = 0;
    @Override
    public void onCreate() {
        final Handler handler = new Handler();
        Runnable runnable = (new Runnable() {
            @Override
            public void run() {
//                    ++counts;
//                    Intent intent1 = new Intent();
//                    intent1.setAction("com.example.test3");
//                    intent1.putExtra("DATAPASSED", "Tutorialspoint.com" + String.valueOf(counts));
//                    sendBroadcast(intent1);
            }
        });
        Thread k = new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 100; ++i) {
                    Intent intent1 = new Intent();
                    intent1.setAction("com.example.test3");
                    intent1.putExtra("DATAPASSED", "Tutorialspoint.com" + String.valueOf(i));
                    sendBroadcast(intent1);
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        k.start();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return true;
    }

    @Override
    public void onRebind(Intent intent) {
        super.onRebind(intent);
    }

    public class MyBinder extends Binder {
        MyService getService() {
            return MyService.this;
        }
    }
}