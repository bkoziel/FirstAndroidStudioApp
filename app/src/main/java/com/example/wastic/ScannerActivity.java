package com.example.wastic;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.Result;
import com.google.zxing.Writer;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.oned.Code128Writer;
import com.google.zxing.oned.EAN13Writer;
import com.google.zxing.oned.EAN8Writer;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Hashtable;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

//import static com.example.wastic.MainActivity.imageViewResult;

public class ScannerActivity extends AppCompatActivity implements ZXingScannerView.ResultHandler{

    ZXingScannerView ScannerView;
    ImageView album;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ScannerView = new ZXingScannerView(this);
        album = (ImageView) findViewById(R.id.imageAlbum);


        // view.addView(album);

        setContentView(ScannerView);

    }


    @Override
    public void handleResult(Result result) {
      // Intent i = new Intent(getApplicationContext() , ProductActivity.class);
        Intent x = new Intent(getApplicationContext() , AddProductActivity.class);
        //i.putExtra("code",result.getText());
        x.putExtra("code",result.getText());

        startActivity(x);
        onBackPressed();
    }

    @Override
    protected void onPause() {
        super.onPause();
        ScannerView.stopCamera();
    }

    @Override
    protected void onResume() {
        super.onResume();
        ScannerView.setResultHandler(this);
        ScannerView.startCamera();
    }



}
