package com.example.wastic;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class ProductActivity extends AppCompatActivity {

    TextView barCodeTextView;
    TextView nameTextView;
    ImageView photoImageView;
    Button addProductButton;
    String code;
    DatabaseHelper db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);
        barCodeTextView = (TextView) findViewById(R.id.textViewCode);
        nameTextView = (TextView) findViewById(R.id.textViewName);
        photoImageView = (ImageView) findViewById(R.id.imageViewPhoto);
        addProductButton = (Button) findViewById(R.id.buttonAddProduct);
        code = getIntent().getStringExtra("code");

        db = new DatabaseHelper(this);

        barCodeTextView.setText(code);

        if(db.productExists(code)){
            nameTextView.setText("Produkt w bazie");
            addProductButton.setVisibility(View.INVISIBLE);
            nameTextView.setText(db.getProductName(code));
        }else{
            nameTextView.setText("Brak produktu w bazie");
            addProductButton.setVisibility(View.VISIBLE);
        }


        addProductButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //startActivity(new Intent(getApplicationContext(), AddProductActivity.class));
               db.writeDataProduct("Żywiec Zdrój", getIntent().getStringExtra("code"));
            }
        });
    }

}
