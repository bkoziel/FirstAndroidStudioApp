package com.example.wastic.Activity;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.SparseArray;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.wastic.R;
import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;
import com.google.zxing.Result;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

//import static com.example.wastic.Activity.MainActivity.imageViewResult;

public class ScannerActivity extends AppCompatActivity implements ZXingScannerView.ResultHandler{
    private static final int MY_CAMERA_REQUEST_CODE = 100;
    ZXingScannerView ScannerView;
    ImageView album;
    private static final int RESULT_LOAD_IMAGE = 0;
    BarcodeDetector detector;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        detector = new BarcodeDetector.Builder(getApplicationContext())
                .setBarcodeFormats(Barcode.EAN_13 | Barcode.EAN_8)
                .build();
        if(!detector.isOperational()){
            Toast.makeText(getApplicationContext(), "Could not read the code!", Toast.LENGTH_SHORT).show();
        }
        RelativeLayout layout = new RelativeLayout(this);
        layout.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));

        ScannerView = new ZXingScannerView(this);

        album = new ImageView(this);
        album.setImageResource(R.drawable.ic_action_album);
        album.setId(R.id.albumImageView);


        layout.addView(ScannerView);


        layout.addView(album);


        setContentView(layout);
        // view.addView(album);

        //setContentView(ScannerView);
        album.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivityForResult(new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI),RESULT_LOAD_IMAGE);

            }
        });
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


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};
            if (selectedImage != null) {
                Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
                if (cursor != null) {
                    cursor.moveToFirst();
                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    String picturePath = cursor.getString(columnIndex);
                    //imageView.setImageBitmap(BitmapFactory.decodeFile(picturePath));

                    try {
                        processData(BitmapFactory.decodeFile(picturePath));
                    }catch(Exception e){
                        Toast.makeText(getApplicationContext(), "Could not read the code!", Toast.LENGTH_SHORT).show();
                        //resultTextView.setText("Nie można odczytać kodu");
                    }

                    cursor.close();

                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void processData(Bitmap myBitmap) throws Exception {
        Frame frame = new Frame.Builder().setBitmap(myBitmap).build();
        SparseArray<Barcode> barCodes = detector.detect(frame);

        Intent i = new Intent(getApplicationContext() , ProductActivity.class);
        i.putExtra("code",barCodes.valueAt(0).rawValue);
        startActivity(i);

    }

}
