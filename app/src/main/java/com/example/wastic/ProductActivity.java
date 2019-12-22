package com.example.wastic;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class ProductActivity extends AppCompatActivity {

    TextView codeTextView;
    TextView nameTextView;
    ImageView photoImageView;
    Button addProductButton;
    String code;
    DatabaseHelper db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);

        codeTextView = (TextView) findViewById(R.id.textViewCode);
        nameTextView = (TextView) findViewById(R.id.textViewName);
        photoImageView = (ImageView) findViewById(R.id.imageViewPhoto);
        addProductButton = (Button) findViewById(R.id.buttonAddProduct);
        code = getIntent().getStringExtra("code");
        db = new DatabaseHelper(this);

        codeTextView.setText(code);

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
                //startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                db.writeDataProduct("Żywiec Zdrój", getIntent().getStringExtra("code"),"Napoje");
            }
        });
    }
}
