package com.example.wastic.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wastic.R;
import com.example.wastic.SharedPrefManager;

public class MainActivity extends AppCompatActivity {
    private static final int MY_CAMERA_REQUEST_CODE = 100;
    Button scannerButton;
    Button loginButton;
    Button toProfileButton;
    TextView info;
    ////////////


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.CAMERA}, MY_CAMERA_REQUEST_CODE);
        }

        scannerButton = (Button) findViewById(R.id.buttonScanner);
        loginButton = (Button) findViewById(R.id.buttonLogin);
        toProfileButton = (Button) findViewById(R.id.buttonToProfile);
        info = (TextView) findViewById(R.id.textViewInfo);

        if (!SharedPrefManager.getInstance(this).isLoggedIn()) {
            info.setVisibility(View.VISIBLE);
            loginButton.setVisibility(View.VISIBLE);
            toProfileButton.setVisibility(View.INVISIBLE);
        }else{
            info.setVisibility(View.INVISIBLE);
            loginButton.setVisibility(View.INVISIBLE);
            toProfileButton.setVisibility(View.VISIBLE);
        }

        scannerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), ScannerActivity.class));
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
            }
        });

        toProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), UserProfileActivity.class));
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!SharedPrefManager.getInstance(this).isLoggedIn()) {
            info.setVisibility(View.VISIBLE);
            loginButton.setVisibility(View.VISIBLE);
            toProfileButton.setVisibility(View.INVISIBLE);
        }else{
            info.setVisibility(View.INVISIBLE);
            loginButton.setVisibility(View.INVISIBLE);
            toProfileButton.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_CAMERA_REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "camera permission granted", Toast.LENGTH_LONG).show();

            } else {
                Toast.makeText(this, "camera permission denied", Toast.LENGTH_LONG).show();
            }
        }
    }
}
