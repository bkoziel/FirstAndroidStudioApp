package com.example.wastic.Activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.wastic.R;
import com.example.wastic.Requesthandler;
import com.example.wastic.SharedPrefManager;
import com.example.wastic.URLs;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class ProductActivity extends AppCompatActivity {

    private RatingBar ratingBar;
   private TextView barCodeTextView, rateCount;
    private TextView nameTextView,userTextView,userRating,loginForMore;
   private ImageView photoImageView;
    private Button addProductButton;
    private float ratedValue;
    String code;
    String productName,barcode,photoURL,addedByUser,ratingValue;

    private RequestQueue requestQueue;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);
        barCodeTextView = (TextView) findViewById(R.id.textViewCode);
        nameTextView = (TextView) findViewById(R.id.textViewName);
        photoImageView = (ImageView) findViewById(R.id.imageViewPhoto);
        addProductButton = (Button) findViewById(R.id.buttonAddProduct);
        userTextView=(TextView) findViewById(R.id.textViewUser);
        userRating=(TextView) findViewById(R.id.textViewUserRate);
        loginForMore = (TextView) findViewById(R.id.textViewPleaseLogIn);




        code = getIntent().getStringExtra("code");
        barCodeTextView.setText(code);
        requestQueue = Volley.newRequestQueue(this);
        checkCode();
        jsonParse();
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
    private void jsonParse() {
        String url = "https://wasticelo.000webhostapp.com/testing.php?bar_code="+code;
        System.out.println(url);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {

                    JSONObject product = response.getJSONObject("data");


                        productName = product.getString("name");
                        barcode = product.getString("bar_code");
                        photoURL = product.getString("photo");
                        addedByUser=product.getString("username");
                        ratingValue=product.getString("RatingValue");
                        nameTextView.setText(productName);
                        barCodeTextView.setText(barcode);
                        userTextView.setText("Dodane przez: " +addedByUser);
                        userRating.setText("Ocena: "+ratingValue);
                        Picasso.get().load("https://wasticelo.000webhostapp.com/"+photoURL).into(photoImageView);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        requestQueue.add(request);
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
                        if (!SharedPrefManager.getInstance(ProductActivity.this).isLoggedIn()) {

                            loginForMore.setVisibility(View.VISIBLE);
                                loginForMore.setOnTouchListener(new View.OnTouchListener() {
                                    @Override
                                    public boolean onTouch(View v, MotionEvent event) {

                                        Intent x = new Intent(getApplicationContext() , LoginActivity.class);
                                        startActivity(x);
                                        return false;
                                    }
                                });
                            addProductButton.setVisibility(View.INVISIBLE);
                        }else{
                            addProductButton.setVisibility(View.VISIBLE);
                        }



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



