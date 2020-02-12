package com.example.wastic.Activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Space;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.wastic.R;
import com.example.wastic.SharedPrefManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class UserProductsActivity extends AppCompatActivity {
    LinearLayout LL;
    TextView allProducts;
    RequestQueue requestQueue;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_products);
        allProducts = findViewById(R.id.allProducts);
        LL = findViewById(R.id.LL);
        requestQueue= Volley.newRequestQueue(this);
    jsonParse();
    }


    private void jsonParse() {
        String url = "https://wasticelo.000webhostapp.com/addedProducts.php?user_id="+ SharedPrefManager.getInstance(this).currentUser();
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                    try {
                        JSONArray products = response.getJSONArray("products");
                //        LL.removeAllViews();
                        final String[] s = new String[((JSONArray) products).length()];
                        for(int i = 0; i < ((JSONArray) products).length(); i++) {
                            final JSONObject product = products.getJSONObject(i);
                           //final String barcode = product.getString("barcode");
                            //l = new LinearLayout(LL.getContext());
                            LinearLayout l = new LinearLayout(LL.getContext());
                            l.setOrientation(LinearLayout.HORIZONTAL);
                            TextView tv = new TextView(l.getContext());
                            tv.setGravity(View.TEXT_ALIGNMENT_CENTER);
                            tv.setTextSize(20);
                            Space space= new Space(l.getContext());

                            Button b = new Button(l.getContext());
                            //b.setGravity(View.FOCUS_RIGHT);
                            tv.setText(product.getString("name"));
                            b.setText(" Przejdź > ");
                     //       b.setText(product.getString("name")+ "\n" + barcode);
                            b.setBackgroundColor(Color.rgb(0,85,77));
                            b.setTextColor(Color.rgb(255,255,255));
                            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(800, LinearLayout.LayoutParams.WRAP_CONTENT);
                            LinearLayout.LayoutParams lp2 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                            lp.setMargins(10,10,10,10);
                            //lp.setMarginEnd(10);
                            lp2.setMargins(10,10,10,10);
                            tv.setLayoutParams(lp);
                            b.setLayoutParams(lp2);
                            //space.setLayoutParams(lp2);
                            b.setId(i);
                            final int id=i;
                            s[i] = product.getString("bar_code");

                            b.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent x = new Intent(getApplicationContext() , ProductActivity.class);
                                        x.putExtra("code",s[id]);
                                    finish();
                                    startActivity(x);
                                }

                            });
                            l.addView(tv);
                            l.addView(space);
                           l.addView(b);
                           LL.addView(l);


                        }
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
}


