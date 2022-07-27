package com.example.simplestore;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.strictmode.WebViewMethodCalledOnWrongThreadViolation;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private final int MY_WRITE_REQUEST_CODE = 1;
    private final int MY_READ_REQUEST_CODE = 2;
    Button installButton;
    private final String myURL = "http://15.235.163.133:8000/camera-app.apk";
    ArrayAdapter<String> myAdapter;
    private ArrayList<String> appName = new ArrayList<>();
    ListView myList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        myList = findViewById(R.id.appList);
        appName.add("Camera");
        appName.add("Wifi");
        appName.add("Bluetooth");
        myAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, appName);
        myList.setAdapter(myAdapter);
        myList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
                Intent intent = new Intent(MainActivity.this, appDescreption.class);
                String message = (String) (myList.getItemAtPosition(position));
                intent.putExtra("App name", message);
                startActivity(intent);
            }
        });
    }
}