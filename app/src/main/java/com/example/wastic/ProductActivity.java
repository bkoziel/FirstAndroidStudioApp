package com.example.wastic;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

public class ProductActivity extends AppCompatActivity {

    TextView codeTextView;
    TextView nameTextView;
    ImageView photoImageView;

    DatabaseHelper db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);

        codeTextView = (TextView) findViewById(R.id.textViewCode);
        nameTextView = (TextView) findViewById(R.id.textViewName);
        photoImageView = (ImageView) findViewById(R.id.imageViewPhoto);

        db = new DatabaseHelper(this);

        codeTextView.setText(getIntent().getStringExtra("code"));
        if(db.productExists(getIntent().getStringExtra("code"))){
            nameTextView.setText("Produkt w bazie");
        }else{
            nameTextView.setText("Brak produktu w bazie");
        }

    }
}
