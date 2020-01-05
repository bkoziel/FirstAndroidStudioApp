package com.example.wastic.Activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.wastic.R;
import com.example.wastic.Requesthandler;
import com.example.wastic.SharedPrefManager;
import com.example.wastic.URLs;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ProductActivity extends AppCompatActivity {

    private RatingBar ratingBar;
   private TextView barCodeTextView, rateCount;
    private TextView nameTextView,userTextView,userRating,loginForMore,commentTextView,addCommentButton;
   private ImageView photoImageView;
    private Button addProductButton;
    private float ratedValue;
    private String commentExist;
    LinearLayout LL;
    String code;
    String productName,barcode,photoURL,addedByUser,ratingValue;
    static int productID;
   private String addOpinionURL = "https://wasticelo.000webhostapp.com/addOpinion.php";

    private RequestQueue requestQueue;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);
        barCodeTextView = findViewById(R.id.textViewCode);
        nameTextView = findViewById(R.id.textViewName);
        photoImageView = findViewById(R.id.imageViewPhoto);
        addProductButton = findViewById(R.id.buttonAddProduct);
        userTextView= findViewById(R.id.textViewUser);
        userRating= findViewById(R.id.textViewUserRate);
        loginForMore = findViewById(R.id.textViewPleaseLogIn);
        commentTextView = findViewById(R.id.editTextComment);
        addCommentButton = findViewById(R.id.buttonAddComment);
        ratingBar = findViewById(R.id.ratingBars);
        LL = findViewById(R.id.commentsLL);
        code = getIntent().getStringExtra("code");
        barCodeTextView.setText(code);
        requestQueue = Volley.newRequestQueue(this);
        jsonParse();
        checkCode();



        addProductButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                Intent i = new Intent(getApplicationContext() , AddProductActivity.class);
                i.putExtra("code",code);
                startActivity(i);

                ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
                    @Override
                    public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                        if(rating<1.0f)
                            ratingBar.setRating(1.0f);
                    }
                });

            }
        });

        addCommentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
StringRequest request = new StringRequest(Request.Method.POST, addOpinionURL, new Response.Listener<String>() {
    @Override
    public void onResponse(String response) {
        Toast.makeText(getApplicationContext(),response,Toast.LENGTH_LONG).show();
    }
}, new Response.ErrorListener() {
    @Override
    public void onErrorResponse(VolleyError error) {
        Toast.makeText(getApplicationContext(),"error"+error.toString(),Toast.LENGTH_LONG).show();
    }
}){

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        Map<String, String> params = new HashMap<>();

        if(!commentTextView.getText().equals("")) {
            params.put("description", commentTextView.getText().toString());
        }else{
            params.put("description", null);
        }
        params.put("product_id",Integer.toString(productID));
        params.put("user_id", Integer.toString(SharedPrefManager.getInstance(ProductActivity.this).currentUser()));
        params.put("ratingValue",String.valueOf(ratingBar.getRating()));


        return params;
    }
};
requestQueue.add(request);
            }
        });

    }
    private void getRating() {
        String prod_id=Integer.toString(productID);
        String url = "https://wasticelo.000webhostapp.com/averageRating.php?product_id="+prod_id;

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {

                    JSONObject product = response.getJSONObject("data");

                    ratingValue = product.getString("ratingValue");
                    userRating.setText("Ocena: "+ ratingValue);


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
    private void jsonParse() {
        String url = "https://wasticelo.000webhostapp.com/testing.php?bar_code="+code;
        System.out.println(url);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {

                    JSONObject product = response.getJSONObject("data");

                        productID = product.getInt("id");
                        productName = product.getString("name");
                        barcode = product.getString("bar_code");
                        photoURL = product.getString("photo");
                        addedByUser = product.getString("username");
                         getRating();
                        nameTextView.setText(productName);
                        barCodeTextView.setText(barcode);
                        userTextView.setText("Dodane przez: " + addedByUser);
                        seeComments();
                        Picasso.get().load("https://wasticelo.000webhostapp.com/"+ photoURL).into(photoImageView);

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
        checkIfExsistComment();
    }

    private void checkIfExsistComment() {
       String prod_id=Integer.toString(productID);
       String user_id=Integer.toString(SharedPrefManager.getInstance(ProductActivity.this).currentUser());
        System.out.println("user: "+user_id + " product: "+prod_id);
String url="https://wasticelo.000webhostapp.com/checkIfCommentExsist.php?user_id="+user_id+"&product_id="+prod_id;

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {

                    JSONObject comment = response.getJSONObject("data");

                    commentExist=comment.getString("exist");


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

    private void seeComments() {
        String url = "https://wasticelo.000webhostapp.com/addedComments.php?product_id="+productID;
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray comments = response.getJSONArray("comments");

//                    nameTextView.setText(Integer.toString(productID));
                    //        LL.removeAllViews();
                    //final String[] s = new String[((JSONArray) products).length()];
                    for(int i = 0; i < ((JSONArray) comments).length(); i++) {
                        final JSONObject comment = comments.getJSONObject(i);
                        //final String barcode = product.getString("barcode");
                        //l = new LinearLayout(LL.getContext());
                        //LinearLayout l = new LinearLayout(LL.getContext());
                        //l.setOrientation(LinearLayout.HORIZONTAL);
                        TextView tv = new TextView(LL.getContext());
                        tv.setGravity(View.TEXT_ALIGNMENT_CENTER);
                        tv.setTextSize(20);
                        //Space space= new Space(l.getContext());

                        //Button b = new Button(l.getContext());
                        //b.setGravity(View.FOCUS_RIGHT);
                        tv.setText(comment.getString("description") + " Ocena:" + comment.getDouble("ratingValue") + " " + comment.get("comment_date")  );
                        //b.setText(" Przejdź > ");
                        //       b.setText(product.getString("name")+ "\n" + barcode);
                        //b.setBackgroundColor(Color.rgb(0,85,77));
                        //b.setTextColor(Color.rgb(255,255,255));
                        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                        //LinearLayout.LayoutParams lp2 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                        lp.setMargins(10,10,10,10);
                        //lp.setMarginEnd(10);
                        //lp2.setMargins(10,10,10,10);
                        tv.setLayoutParams(lp);
                        //b.setLayoutParams(lp2);
                        //space.setLayoutParams(lp2);
                        //b.setId(i);
                        //final int id=i;
                        //s[i] = product.getString("bar_code");

                        //b.setOnClickListener(new View.OnClickListener() {
                         //   @Override
                         //   public void onClick(View v) {
                         //       Intent x = new Intent(getApplicationContext() , ProductActivity.class);
                         //       x.putExtra("code",s[id]);

                         //       startActivity(x);
                          //  }

                        //});
                        //l.addView(tv);
                       // l.addView(space);
                       // l.addView(b);
                        LL.addView(tv);


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

    private void checkCode() {
        final String barcode = code;


        class Products extends AsyncTask<Void, Void, String> {

            private ProgressBar progressBar;

            @Override
            protected String doInBackground(Void... voids) {
                Requesthandler requestHandler = new Requesthandler();

                HashMap<String, String> params = new HashMap<>();
                params.put("bar_code", barcode);

                return requestHandler.sendPostRequest(URLs.URL_PRODUCT, params);
            }

            @Override
            protected void onPreExecute() {
                super.onPreExecute();

                progressBar = (ProgressBar) findViewById(R.id.progressBar2);
                progressBar.setVisibility(View.VISIBLE);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);

                progressBar.setVisibility(View.GONE);

                try {
                    JSONObject obj = new JSONObject(s);

                    if (!obj.getBoolean("error")) {
                        
                        nameTextView.setText("Brak produktu w bazie");

                            addCommentButton.setVisibility(View.INVISIBLE);
                            ratingBar.setVisibility(View.INVISIBLE);
                            commentTextView.setVisibility(View.INVISIBLE);

                        if (!SharedPrefManager.getInstance(ProductActivity.this).isLoggedIn()) {
                            nameTextView.setText("Brak produktu w bazie");
                            loginForMore.setVisibility(View.VISIBLE);
                            loginForMore.setOnTouchListener(new View.OnTouchListener() {
                                @Override
                                public boolean onTouch(View v, MotionEvent event) {

                                    Intent x = new Intent(getApplicationContext(), LoginActivity.class);
                                    startActivity(x);
                                    return false;
                                }
                            });
                            addProductButton.setVisibility(View.INVISIBLE);
                        } else {
                            addProductButton.setVisibility(View.VISIBLE);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                        addProductButton.setVisibility(View.INVISIBLE);

                        if (!SharedPrefManager.getInstance(ProductActivity.this).isLoggedIn()) {

                            addCommentButton.setVisibility(View.INVISIBLE);
                            ratingBar.setVisibility(View.INVISIBLE);
                            commentTextView.setVisibility(View.INVISIBLE);
                            loginForMore.setVisibility(View.VISIBLE);
                            loginForMore.setOnTouchListener(new View.OnTouchListener() {
                                @Override
                                public boolean onTouch(View v, MotionEvent event) {

                                    Intent x = new Intent(getApplicationContext() , LoginActivity.class);
                                    startActivity(x);
                                    return false;
                                }
                            });
                        }else{
                            System.out.println("co siedzi w commentExist= "+commentExist);
                            if(commentExist=="false") {
                            commentTextView.setVisibility(View.VISIBLE);
                            ratingBar.setVisibility(View.VISIBLE);
                            addCommentButton.setVisibility(View.VISIBLE);
                        }else {
                                addCommentButton.setVisibility(View.INVISIBLE);
                                ratingBar.setVisibility(View.INVISIBLE);
                                commentTextView.setVisibility(View.INVISIBLE);
                            }
                        }
                }
            }
        }
        Products ru = new Products();
        ru.execute();
    }

}



