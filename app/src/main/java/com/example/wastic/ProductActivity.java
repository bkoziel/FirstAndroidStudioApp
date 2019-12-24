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






        addProductButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext() , AddProductActivity.class);
                i.putExtra("code",code);
                startActivity(i);

            }
        });
    }

    private void checkCode() {
        //first getting the values
        final String username = code;
        class CheckCode extends AsyncTask<Void, Void, String> {

            ProgressBar progressBar;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                progressBar = (ProgressBar) findViewById(R.id.progressBar);
                progressBar.setVisibility(View.VISIBLE);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                progressBar.setVisibility(View.GONE);


                try {
                    //converting response to json object
                    JSONObject obj = new JSONObject(s);

                    //if no error in response
                    if (!obj.getBoolean("error")) {
                        Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();

                        //getting the user from the response
                        JSONObject productJson = obj.getJSONObject("product");

                        //creating a new user object
                        Product product = new Product(
                                productJson.getInt("id"),
                                productJson.getString("name"),
                                productJson.getString("bar_code"),
                                productJson.getString("photo")
                        );

                        //storing the user in shared preferences
                        SharedPrefManager.getInstance(getApplicationContext()).checkCode(product);

                        //starting the profile activity
                        finish();
                      //  startActivity(new Intent(getApplicationContext(), UserProfileActivity.class));
                            nameTextView.setText("Produkt w bazie");
                            addProductButton.setVisibility(View.INVISIBLE);
                            //nameTextView.setText(db.getProductName(code));


                    } else {
                            nameTextView.setText("Brak produktu w bazie");
                            addProductButton.setVisibility(View.VISIBLE);
              //          Toast.makeText(getApplicationContext(), "Zła nazwa użytkownika lub hasło", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            protected String doInBackground(Void... voids) {
                //creating request handler object
                Requesthandler requestHandler = new Requesthandler();

                //creating request parameters
                HashMap<String, String> params = new HashMap<>();
                params.put("bar_code",code);
                //returing the response
                return requestHandler.sendPostRequest(URLs.URL_PRODUCT, params);
            }
        }

        CheckCode ul = new CheckCode();
        ul.execute();
    }
}
