package com.example.simplestore;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
    private final int MY_WRITE_REQUEST_CODE = 1;
    private final int MY_READ_REQUEST_CODE = 2;
    Button installButton;
//    private final String myURL = "http://localhost:8080/app-debug.apk";
    private final String myURL = "http://15.235.163.133:8000/app-debug.apk";
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        installButton = findViewById(R.id.btnInstall);
        installButton.setOnClickListener(view -> {
            if (hasWritePermission()) {
                if (installApp()) {
                    installAPK();
                }
            } else {
                requestPermission();
            }
        });
    }

    private boolean hasWritePermission() {
        return ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(this,
                PERMISSIONS_STORAGE,
                MY_WRITE_REQUEST_CODE);
    }

    private String downloadFile(String f_url) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL(f_url);
                    InputStream in = url.openStream();
                    BufferedInputStream bis = new BufferedInputStream(in);
                    FileOutputStream fos = new FileOutputStream(Environment.getExternalStorageDirectory().toString() + "/app.apk");
                    Log.d("download", "download file from " + f_url);
                    byte[] data = new byte[1024];
                    int count;
                    while ((count = bis.read(data, 0, 1024)) != -1) {
                        fos.write(data, 0, count);
                    }
                } catch (Exception e) {
                    Log.d("wrong", "something wrong");
                    e.printStackTrace();
                }
            }
        }).start();
        return null;
    }

    private boolean installApp() {
        downloadFile(myURL);
        return true;
    }

    private void installAPK(){
        String PATH = Environment.getExternalStorageDirectory() + "/" + "app.apk";
        File file = new File(PATH);
        if(file.exists()) {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(uriFromFile(getApplicationContext(), new File(PATH)), "application/vnd.android.package-archive");
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            try {
                getApplicationContext().startActivity(intent);
            } catch (ActivityNotFoundException e) {
                e.printStackTrace();
                Log.e("TAG", "Error in opening the file!");
            }
        }else{
            Toast.makeText(getApplicationContext(),"installing",Toast.LENGTH_LONG).show();
        }
    }
    Uri uriFromFile(Context context, File file) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID + ".provider", file);
        } else {
            return Uri.fromFile(file);
        }
    }

}