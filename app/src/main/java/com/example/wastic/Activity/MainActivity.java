package com.example.wastic.Activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
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

public class MainActivity extends AppCompatActivity {
    private static final int MY_CAMERA_REQUEST_CODE = 100;
    Button scannerButton;
    Button loginButton;
    Button toProfileButton;
    TextView info;
    EditText searchEditText;
    LinearLayout LL;
    LinearLayout l;
    Button b;
    boolean onBackPressedCalled=false;
    ////////////
    private RequestQueue requestQueue;
String barcode;
 String[]s;
    @Override
    public void onBackPressed() {
        if(onBackPressedCalled) {
            LL.removeAllViews();
            onBackPressedCalled = false;
        }else{
        super.onBackPressed();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.CAMERA}, MY_CAMERA_REQUEST_CODE);
        }
        searchEditText = findViewById(R.id.editTextSearch);
        scannerButton = findViewById(R.id.buttonScanner);
        loginButton = findViewById(R.id.buttonLogin);
        toProfileButton = findViewById(R.id.buttonToProfile);
        info = findViewById(R.id.textViewInfo);
        requestQueue = Volley.newRequestQueue(this);
        LL = findViewById(R.id.LL);

        if (!SharedPrefManager.getInstance(this).isLoggedIn()) {
            info.setVisibility(View.VISIBLE);
            loginButton.setVisibility(View.VISIBLE);
            toProfileButton.setVisibility(View.INVISIBLE);
        }else{
            info.setVisibility(View.INVISIBLE);
            loginButton.setVisibility(View.INVISIBLE);
            toProfileButton.setVisibility(View.VISIBLE);
        }

        scannerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), ScannerActivity.class));
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
            }
        });

        toProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), UserProfileActivity.class));
            }
        });


        searchEditText.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {
            LL.removeAllViews();
            }


            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
                LL.removeAllViews();
                onBackPressedCalled=false;
            }

            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                onBackPressedCalled=true;
                jsonParse();
            }


        });


    }
    private void jsonParse() {
        String url = "https://wasticelo.000webhostapp.com/Search.php?name="+searchEditText.getText().toString() ;

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
if(searchEditText.getText().length()!=0){
                try {
                    JSONArray products = response.getJSONArray("products");

                    LL.removeAllViews();
                    s = new String[7];
                    for(int i = 0; i < (products).length() && i < 7; i++) {
                        JSONObject product = products.getJSONObject(i);
                        l = new LinearLayout(LL.getContext());
                        b = new Button(l.getContext());
                        barcode=product.getString("bar_code");
                        b.setText(product.getString("name")+ "\n" + barcode);
                        b.setBackgroundColor(Color.rgb(240,240,240));
                        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
                        b.setLayoutParams(lp);
                        b.setId(i);

                        final int id=i;
                        s[i] = product.getString("bar_code");

                        b.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent x = new Intent(getApplicationContext() , ProductActivity.class);
                                x.putExtra("code",s[id]);
                                startActivity(x);
                            }

                        });

                        l.addView(b);
                        LL.addView(l);

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }else{ LL.removeAllViews();}
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        requestQueue.add(request);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!SharedPrefManager.getInstance(this).isLoggedIn()) {
            info.setVisibility(View.VISIBLE);
            loginButton.setVisibility(View.VISIBLE);
            toProfileButton.setVisibility(View.INVISIBLE);
        }else{
            info.setVisibility(View.INVISIBLE);
            loginButton.setVisibility(View.INVISIBLE);
            toProfileButton.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_CAMERA_REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "camera permission granted", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "camera permission denied", Toast.LENGTH_LONG).show();
            }
        }
    }
}
