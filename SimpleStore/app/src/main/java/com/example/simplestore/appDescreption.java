package com.example.simplestore;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

public class appDescreption extends AppCompatActivity {

    private TextView appName;
    private TextView appDescription;
    private Button btnStall;
    private ProgressBar progressBar;
    private final String myURL = "http://15.235.163.133:8000/camera-app.apk";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_descreption);

        appName = findViewById(R.id.currAppName);
        appDescription = findViewById(R.id.appDescription);
        btnStall = findViewById(R.id.btnStall);

        Bundle extra = getIntent().getExtras();
        appName.setText(extra.getString("App name"));
        progressBar = findViewById(R.id.appProgressBar);
        progressBar.setVisibility(View.INVISIBLE);
        progressBar.setIndeterminate(false);
        btnStall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBar.setVisibility(View.VISIBLE);
                btnStall.setEnabled(false);
                downloadFile(myURL);
            }
        });

    }


    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putString("Khoa", "Test");
        // Always call the superclass so it can save the view hierarchy state
        super.onSaveInstanceState(savedInstanceState);
    }

    void updateProcessingBar(ProgressBar myProgressBar, int percent) {
        myProgressBar.setProgress(percent);
    }

    private void downloadFile(String f_url) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL(f_url);
                    URLConnection urlConnection = url.openConnection();
                    urlConnection.connect();
                    int len = urlConnection.getContentLength();
                    byte[] data = new byte[1024];
                    int count;
                    int total = 0;
                    InputStream input = new BufferedInputStream(url.openStream(),8192);
                    OutputStream output = new FileOutputStream(getFilesDir().toString() + "/app.apk");
                    while ((count = input.read(data)) != -1) {
                        total += count;
                        updateProcessingBar(progressBar, (int) (100 * total) / len);
                        output.write(data, 0, count);
                    }

                    output.flush();
                    output.close();
                    input.close();
                    progressBar.setIndeterminate(true);
                    installAPK();
                    progressBar.setVisibility(View.INVISIBLE);
                    btnStall.setEnabled(true);
                    btnStall.setText(getResources().getString(R.string.uninstall));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private boolean installApp() {
//        downloadFile(myURL);
        return true;
    }

    private void installAPK(){
        String PATH = getFilesDir().toString() + "/app.apk";
        File file = new File(PATH);
        if (file.exists()) {
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
        } else {
            Toast.makeText(getApplicationContext(),"installing", Toast.LENGTH_LONG).show();
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

