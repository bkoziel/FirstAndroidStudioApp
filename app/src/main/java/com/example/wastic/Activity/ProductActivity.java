package com.example.wastic.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.wastic.Product;
import com.example.wastic.R;
import com.example.wastic.Requesthandler;
import com.example.wastic.SharedPrefManager;
import com.example.wastic.URLs;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class ProductActivity extends AppCompatActivity {

    TextView barCodeTextView;
    TextView nameTextView;
    ImageView photoImageView;
    Button addProductButton;
    String code;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);
        barCodeTextView = (TextView) findViewById(R.id.textViewCode);
        nameTextView = (TextView) findViewById(R.id.textViewName);
        photoImageView = (ImageView) findViewById(R.id.imageViewPhoto);
        addProductButton = (Button) findViewById(R.id.buttonAddProduct);
        code = getIntent().getStringExtra("code");
        barCodeTextView.setText(code);

        checkCode();
       Product product = SharedPrefManager.getInstance(this).getProduct();
      nameTextView.setText(product.getName());


        addProductButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                Intent i = new Intent(getApplicationContext() , AddProductActivity.class);
                i.putExtra("code",code);
                startActivity(i);

            }
        });

    }

    private void checkCode() {
        final String barcode = code;


        class Products extends AsyncTask<Void, Void, String> {

            private ProgressBar progressBar;

            @Override
            protected String doInBackground(Void... voids) {
                //creating request handler object
                Requesthandler requestHandler = new Requesthandler();

                //creating request parameters
                HashMap<String, String> params = new HashMap<>();
                params.put("bar_code", barcode);

                //returing the response
                return requestHandler.sendPostRequest(URLs.URL_PRODUCT, params);
            }

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                //displaying the progress bar while user registers on the server
                progressBar = (ProgressBar) findViewById(R.id.progressBar2);
                progressBar.setVisibility(View.VISIBLE);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                //hiding the progressbar after completion
                progressBar.setVisibility(View.GONE);

                try {
                    //converting response to json object
                    JSONObject obj = new JSONObject(s);

                    //if no error in response
                    if (!obj.getBoolean("error")) {
                        nameTextView.setText("Brak produktu w bazie");
                        addProductButton.setVisibility(View.VISIBLE);

                    } else {
                        addProductButton.setVisibility(View.INVISIBLE);



                       // JSONObject userJson = obj.getJSONObject("product");

                        //creating a new user object
                       // Product product = new Product(
                                //Json.getInt("id"),
                                //userJson.getString("name"),
                               // userJson.getString("barcode"),
                                //userJson.getString("photoURL")
                       // );
                //nameTextView.setText(product.getName());
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

        //executing the async task
        Products ru = new Products();
        ru.execute();
    }

}



