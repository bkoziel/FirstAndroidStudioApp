package com.example.wastic;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.RelativeLayout;

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
        RelativeLayout view= new RelativeLayout(this);
        //lContainerLayout.setLayoutParams(new RelativeLayout.LayoutParams( LayoutParams.FILL_PARENT , LayoutParams.FILL_PARENT ));
        ScannerView = new ZXingScannerView(this);
        album = (ImageView) findViewById(R.id.imageAlbum);
        view.addView(ScannerView);
       // view.addView(album);
        setContentView(view);
    }


    @Override
    public void handleResult(Result result) {
        MainActivity.resultTextView.setText(result.getText());
   //     MainActivity.resultTextView2.setText("Kod Produktu:");
        //try {
            //String productId = result.getText().toString();
            //Hashtable<EncodeHintType, ErrorCorrectionLevel> hintMap = new Hashtable<EncodeHintType, ErrorCorrectionLevel>();
            //hintMap.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);
            //Writer codeWriter;
            //codeWriter = new EAN13Writer();
            //BitMatrix byteMatrix = codeWriter.encode(productId, BarcodeFormat.EAN_13,400, 200, hintMap);
            //int width = byteMatrix.getWidth();
            //int height = byteMatrix.getHeight();
            //Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
            //for (int i = 0; i < width; i++) {
            //    for (int j = 0; j < height; j++) {
            //        bitmap.setPixel(i, j, byteMatrix.get(i, j) ? Color.BLACK : Color.WHITE);
            //    }
            //}
            //imageView.setImageBitmap(bitmap);
        //} catch (Exception e) {
        //}

        onBackPressed();}

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
