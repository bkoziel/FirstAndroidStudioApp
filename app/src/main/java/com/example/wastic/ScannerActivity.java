package com.example.wastic;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.ImageView;
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
<<<<<<< HEAD
        view.addView(ScannerView);
        // view.addView(album);


        setContentView(view);






=======
        setContentView(ScannerView);
>>>>>>> 41b693e568f07b3078e9eac99da87ca0ae08ef25
    }


    @Override
    public void handleResult(Result result) {
        Intent i = new Intent(getApplicationContext() , ProductActivity.class);
        i.putExtra("code",result.getText());
        startActivity(i);
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
