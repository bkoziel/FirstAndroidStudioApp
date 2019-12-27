package com.example.wastic.Activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.wastic.Product;
import com.example.wastic.R;
import com.example.wastic.Requesthandler;
import com.example.wastic.SharedPrefManager;
import com.example.wastic.URLs;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class AddProductActivity extends AppCompatActivity {
    EditText editTextProductName, editTextBarCode, editTextPhotoURL;
    Button buttonAddProduct;
  String code;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addproduct);
        code = getIntent().getStringExtra("code");

        buttonAddProduct = findViewById(R.id.buttonAddProduct);
        editTextProductName = (EditText) findViewById(R.id.editTextProductName);
        editTextBarCode = (EditText) findViewById(R.id.editBarCode);
        editTextPhotoURL = (EditText) findViewById(R.id.editPhotoURL);

    editTextBarCode.setText(code);
        buttonAddProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //if user pressed on button register
                //here we will register the user to server
                productPOST();
            }
        });

    }

    private void productPOST() {
        final String name = editTextProductName.getText().toString().trim();
        final String barcode = editTextBarCode.getText().toString().trim();
        final String photourl = editTextPhotoURL.getText().toString().trim();


        //first we will do the validations
        if (TextUtils.isEmpty(name)) {
            editTextProductName.setError("Wprowadź nazwę produktu");
            editTextProductName.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(barcode)) {
            editTextBarCode.setError("Wprowadź kod produktu");
            editTextBarCode.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(photourl)) {
            editTextPhotoURL.setError("Wprowadź zdjęcie produktu");
            editTextPhotoURL.requestFocus();
            return;
        }

        //if it passes all the validations

        class Products extends AsyncTask<Void, Void, String> {

            private ProgressBar progressBar;

            @Override
            protected String doInBackground(Void... voids) {
                //creating request handler object
                Requesthandler requestHandler = new Requesthandler();

                //creating request parameters
                HashMap<String, String> params = new HashMap<>();
                params.put("name", name);
                params.put("bar_code", barcode);
                params.put("photo", photourl);


                //returing the response
                return requestHandler.sendPostRequest(URLs.URL_PRODUCT, params);
            }

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                //displaying the progress bar while user registers on the server
                progressBar = (ProgressBar) findViewById(R.id.progressBar);
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
                        Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();
                        //getting the user from the response
                        JSONObject userJson = obj.getJSONObject("product");

                        //creating a new user object
                        Product product = new Product(
                                userJson.getInt("id"),
                                userJson.getString("name"),
                                userJson.getString("barcode"),
                                userJson.getString("photoURL")
                        );
                        SharedPrefManager.getInstance(getApplicationContext()).letProduct(product);

                    } else {
                        Toast.makeText(getApplicationContext(), "Taki produkt juz istnieje!", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                finish();
                Intent x = new Intent(getApplicationContext() , ProductActivity.class);
                x.putExtra("code",code);
               // x.putExtra("name",product.getName());
                startActivity(x);
            }
        }

        //executing the async task
        Products ru = new Products();
        ru.execute();
    }


}
