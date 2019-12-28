package com.example.wastic.Activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
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
    Button toProfileButton,ok;
    TextView info;
    EditText searchEditText;
LinearLayout LL;
    LinearLayout l;
    Button b;
    ////////////
    private RequestQueue requestQueue;
String barcode;
 String[]s;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.CAMERA}, MY_CAMERA_REQUEST_CODE);
        }
        searchEditText = (EditText) findViewById(R.id.editTextSearch);
        scannerButton = (Button) findViewById(R.id.buttonScanner);
        loginButton = (Button) findViewById(R.id.buttonLogin);
        toProfileButton = (Button) findViewById(R.id.buttonToProfile);
        info = (TextView) findViewById(R.id.textViewInfo);
        requestQueue = Volley.newRequestQueue(this);
        ok = (Button) findViewById(R.id.button);
        LL = (LinearLayout) findViewById(R.id.LL);
       // l = new LinearLayout(LL.getContext());
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

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                jsonParse();
            }
        });

        searchEditText.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {
            }

            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                jsonParse();
            }
        });

    }
    private void jsonParse() {
        String url = "https://wasticelo.000webhostapp.com/Search.php?name="+searchEditText.getText().toString();
        //System.out.println(url);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {
                    JSONArray products = response.getJSONArray("products");
                    //l.removeAllViews() ;
                   // l.removeView(b);
                    //l.removeAllViewsInLayout();

///////////////////////////////////////////////

                 /*       Button btn = new Button(this);
                        btn.setId(i);
                        final int id_ = btn.getId();
                       // btn.setText("button " + id_);
                       // btn.setBackgroundColor(Color.rgb(70, 80, 90));
                        linear.addView(btn);
                        btn1 = ((Button) findViewById(id_));
                        btn1.setOnClickListener(new View.OnClickListener() {
                            public void onClick(View view) {
                                Toast.makeText(view.getContext(),
                                        "Button clicked index = " + id_, Toast.LENGTH_SHORT)
                                        .show();
                            }
                        });
                    l = new LinearLayout(LL.getContext());*/
                        ////////////////////////////////////////////
                    LL.removeAllViews();
                    s = new String[7];
                    for(int i = 0; i < ((JSONArray) products).length() && i < 7; i++) {
                        JSONObject product = products.getJSONObject(i);
                        l = new LinearLayout(LL.getContext());
                        b = new Button(l.getContext());
                        barcode=product.getString("bar_code");
                        b.setText(product.getString("name")+ "\n" + barcode);
                        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
                        b.setLayoutParams(lp);
                        b.setId(i);
                        final int id=i;
                        s[i] = product.getString("bar_code");
                        /*barcode=product.getString("bar_code");
                        Button b = new Button(LL.getContext());
                        b.setId(i);
                        final int _id = b.getId();
                        b.setText(/*product.getString("name")+ "\n" +*/ /*barcode);
                        s[i]=barcode;
                        // btn.setText("button " + id_);
                        // btn.setBackgroundColor(Color.rgb(70, 80, 90));
                        l.addView(b);
                        Button bb = ((Button) findViewById(_id));
                        bb.setOnClickListener(new View.OnClickListener() {
                            public void onClick(View view) {
                                Toast.makeText(view.getContext(),
                                        "Button clicked index = " + _id, Toast.LENGTH_SHORT)
                                        .show();

                                Intent i = new Intent(getApplicationContext() , ProductActivity.class);
                                i.putExtra("code",s[_id]);
                                startActivity(i);
                            }
                        });*/
                        //b.setWidth();
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

                         info.append(product.getString("name"));
                         info.append(product.getString("bar_code"));
                         info.append("\n");
                        //photoURL = product.getString("photo");
                        //nameTextView.setText(productName);
                        //barCodeTextView.setText(barcode);
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
