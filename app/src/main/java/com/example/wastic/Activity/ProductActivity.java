package com.example.wastic.Activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.wastic.CommentAdapter;
import com.example.wastic.R;
import com.example.wastic.Requesthandler;
import com.example.wastic.SharedPrefManager;
import com.example.wastic.URLs;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProductActivity extends AppCompatActivity {
    private RecyclerView mList;

    private LinearLayoutManager linearLayoutManager;
    private DividerItemDecoration dividerItemDecoration;
    private List<Comments> commentsList;
    private RecyclerView.Adapter adapter;
    private RatingBar ratingBar;
   private TextView barCodeTextView, rateCount;
    private TextView nameTextView,userTextView,userRating,loginForMore,commentTextView,addCommentButton;
   private ImageView photoImageView;
    private Button addProductButton;
    private String commentExist="false";
    LinearLayout LL;
    String code;
    String productName,barcode,photoURL,addedByUser,ratingValue;
    static int productID;
   private String addOpinionURL = "https://wasticelo.000webhostapp.com/addOpinion.php";
    private String addCommentReportURL = "https://wasticelo.000webhostapp.com/addCommentReport.php";
    private String addProductReportURL = "https://wasticelo.000webhostapp.com/addProductReport.php";

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
//        LL = findViewById(R.id.commentsLL);
     rateCount = findViewById(R.id.ratingBarText);
        code = getIntent().getStringExtra("code");
        barCodeTextView.setText(code);
        requestQueue = Volley.newRequestQueue(this);
        mList = findViewById(R.id.RecycleViewComments);

        commentsList = new ArrayList<>();
        adapter = new CommentAdapter(getApplicationContext(), commentsList);

        linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        dividerItemDecoration = new DividerItemDecoration(mList.getContext(), linearLayoutManager.getOrientation());

        mList.setHasFixedSize(true);
        mList.setLayoutManager(linearLayoutManager);
        mList.addItemDecoration(dividerItemDecoration);
        mList.setAdapter(adapter);
        jsonParse();
        checkCode();



        addProductButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                Intent i = new Intent(getApplicationContext(), AddProductActivity.class);
                i.putExtra("code", code);
                startActivity(i);
            }
        });

        findViewById(R.id.buttonReport).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //final String commentText = commentTextView.getText().toString().trim();
                //if(TextUtils.isEmpty(commentText)){

                 //   commentTextView.setError("Wprowadź komentarz");
                 //   commentTextView.requestFocus();
                 //   return;

                //}
                StringRequest request = new StringRequest(Request.Method.POST, addProductReportURL, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(getApplicationContext(),"Wysłano zgłoszenie",Toast.LENGTH_LONG).show();
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(),"Wystąpił błąd",Toast.LENGTH_LONG).show();
                    }
                }){

                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> params = new HashMap<>();

                        //params.put("description", commentTextView.getText().toString());
                        params.put("product_id",Integer.toString(productID));
                        params.put("user_id", Integer.toString(SharedPrefManager.getInstance(ProductActivity.this).currentUser()));
                        //params.put("ratingValue",String.valueOf(ratingBar.getRating()));


                        return params;
                    }
                };
                requestQueue.add(request);
            }
        });

        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
                    @Override
                    public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                        if (rating < 1.0f)
                        {       ratingBar.setRating(1.0f);
                                rating = 1.0f;
                        }
                        rateCount.setText("Twoja ocena: " + rating + "/5");
                    }

                }
                );



        addCommentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String commentText = commentTextView.getText().toString().trim();
                if(TextUtils.isEmpty(commentText)){

                    commentTextView.setError("Wprowadź komentarz");
                    commentTextView.requestFocus();
                    return;

                }
StringRequest request = new StringRequest(Request.Method.POST, addOpinionURL, new Response.Listener<String>() {
    @Override
    public void onResponse(String response) {
        Toast.makeText(getApplicationContext(),"Oceniono Produkt",Toast.LENGTH_LONG).show();
       finish();
            Intent x = new Intent(getApplicationContext() , ProductActivity.class);
            x.putExtra("code",code);
            startActivity(x);

    }
}, new Response.ErrorListener() {
    @Override
    public void onErrorResponse(VolleyError error) {
        Toast.makeText(getApplicationContext(),"Wystąpił błąd",Toast.LENGTH_LONG).show();
    }
}){

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        Map<String, String> params = new HashMap<>();

        params.put("description", commentTextView.getText().toString());
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
    private void getRatings() {
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
                         getRatings();
                        nameTextView.setText(productName.toUpperCase());
                        barCodeTextView.setText(barcode);
                        userTextView.setText("Dodane przez: " + addedByUser);
                        seeComments();
                        checkIfExsistComment();
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

                    if(commentExist.equals("false")) {
                        addCommentButton.setVisibility(View.VISIBLE);
                        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(addCommentButton.getLayoutParams().width, ViewGroup.LayoutParams.WRAP_CONTENT);
                        addCommentButton.setPadding(5,5,5,5);
                        addCommentButton.setLayoutParams(lp);

                        ratingBar.setVisibility(View.VISIBLE);
                        LinearLayout.LayoutParams lp2 = new LinearLayout.LayoutParams(ratingBar.getLayoutParams().width, ViewGroup.LayoutParams.WRAP_CONTENT);
                        lp2.gravity = 1;
                        ratingBar.setLayoutParams(lp2);

                        commentTextView.setVisibility(View.VISIBLE);
                        LinearLayout.LayoutParams lp3 = new LinearLayout.LayoutParams(commentTextView.getLayoutParams().width, ViewGroup.LayoutParams.WRAP_CONTENT);
                        commentTextView.setLayoutParams(lp3);

                        rateCount.setVisibility(View.VISIBLE);
                        LinearLayout.LayoutParams lp4 = new LinearLayout.LayoutParams(rateCount.getLayoutParams().width, ViewGroup.LayoutParams.WRAP_CONTENT);
                        lp4.gravity = 1;
                        rateCount.setLayoutParams(lp4);

                    }else {
                        addCommentButton.setVisibility(View.INVISIBLE);
                        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(addCommentButton.getLayoutParams().width, 0);
                        addCommentButton.setLayoutParams(lp);

                        ratingBar.setVisibility(View.INVISIBLE);
                        LinearLayout.LayoutParams lp2 = new LinearLayout.LayoutParams(ratingBar.getLayoutParams().width, 0);
                        ratingBar.setLayoutParams(lp2);

                        commentTextView.setVisibility(View.INVISIBLE);
                        LinearLayout.LayoutParams lp3 = new LinearLayout.LayoutParams(commentTextView.getLayoutParams().width, 0);
                        commentTextView.setLayoutParams(lp3);

                        rateCount.setVisibility(View.INVISIBLE);
                        LinearLayout.LayoutParams lp4 = new LinearLayout.LayoutParams(rateCount.getLayoutParams().width, 0);
                        rateCount.setLayoutParams(lp4);
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

    private void seeComments() {
        String url = "https://wasticelo.000webhostapp.com/addedComments.php?product_id="+productID;
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray comments = response.getJSONArray("comments");

                    for(int i = 0; i < (comments).length(); i++) {
                        final JSONObject comment = comments.getJSONObject(i);

                        Comments comments1 = new Comments();
                        comments1.setDescription(comment.getString("description"));
                        comments1.setRating("Ocena: " + comment.getString("ratingValue"));
                        comments1.setDate(comment.getString("comment_date"));
                        comments1.setImageUser("https://wasticelo.000webhostapp.com/"+ comment.getString("avatar"));
                        comments1.setUsername(comment.getString("username"));

                        commentsList.add(comments1);

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

                progressBar = findViewById(R.id.progressBar2);
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
                        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(addCommentButton.getLayoutParams().width, 0);
                        lp.setMargins(10,10,10,10);
                        addCommentButton.setLayoutParams(lp);


                        ratingBar.setVisibility(View.INVISIBLE);
                        LinearLayout.LayoutParams lp2 = new LinearLayout.LayoutParams(ratingBar.getLayoutParams().width, 0);
                        lp2.setMargins(10,10,10,10);
                        ratingBar.setLayoutParams(lp2);

                        commentTextView.setVisibility(View.INVISIBLE);
                        LinearLayout.LayoutParams lp3 = new LinearLayout.LayoutParams(commentTextView.getLayoutParams().width, 0);
                        lp3.setMargins(10,10,10,10);
                        commentTextView.setLayoutParams(lp3);

                        rateCount.setVisibility(View.INVISIBLE);
                        LinearLayout.LayoutParams lp4 = new LinearLayout.LayoutParams(rateCount.getLayoutParams().width, 0);
                        lp4.setMargins(10,10,10,10);
                        rateCount.setLayoutParams(lp4);


                        if (!SharedPrefManager.getInstance(ProductActivity.this).isLoggedIn()) {
                            nameTextView.setText("Brak produktu w bazie");
                            loginForMore.setVisibility(View.VISIBLE);
                            LinearLayout.LayoutParams lp5 = new LinearLayout.LayoutParams(loginForMore.getLayoutParams().width, ViewGroup.LayoutParams.WRAP_CONTENT);
                            loginForMore.setLayoutParams(lp5);
                            loginForMore.setOnTouchListener(new View.OnTouchListener() {
                                @Override
                                public boolean onTouch(View v, MotionEvent event) {

                                    Intent x = new Intent(getApplicationContext(), LoginActivity.class);
                                    startActivity(x);
                                    return false;
                                }
                            });
                            addProductButton.setVisibility(View.INVISIBLE);
                            LinearLayout.LayoutParams lp6 = new LinearLayout.LayoutParams(addProductButton.getLayoutParams().width, 0);
                            lp6.setMargins(10,10,10,10);
                            addProductButton.setLayoutParams(lp6);
                        } else {
                            addProductButton.setVisibility(View.VISIBLE);
                            LinearLayout.LayoutParams lp6 = new LinearLayout.LayoutParams(addProductButton.getLayoutParams().width, ViewGroup.LayoutParams.WRAP_CONTENT);
                            lp6.setMargins(10,10,10,10);
                            addProductButton.setLayoutParams(lp6);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                        addProductButton.setVisibility(View.INVISIBLE);
                    LinearLayout.LayoutParams lp6 = new LinearLayout.LayoutParams(addProductButton.getLayoutParams().width, 0);
                    lp6.setMargins(10,10,10,10);
                    addProductButton.setLayoutParams(lp6);

                    if (!SharedPrefManager.getInstance(ProductActivity.this).isLoggedIn()) {
                            addCommentButton.setVisibility(View.INVISIBLE);
                            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(addCommentButton.getLayoutParams().width, 0);
                        lp.setMargins(10,10,10,10);
                            addCommentButton.setLayoutParams(lp);

                            ratingBar.setVisibility(View.INVISIBLE);
                            LinearLayout.LayoutParams lp2 = new LinearLayout.LayoutParams(ratingBar.getLayoutParams().width, 0);
                        lp2.setMargins(10,10,10,10);
                            ratingBar.setLayoutParams(lp2);

                            commentTextView.setVisibility(View.INVISIBLE);
                            LinearLayout.LayoutParams lp3 = new LinearLayout.LayoutParams(commentTextView.getLayoutParams().width, 0);
                        lp3.setMargins(10,10,10,10);
                            commentTextView.setLayoutParams(lp3);

                            rateCount.setVisibility(View.INVISIBLE);
                            LinearLayout.LayoutParams lp4 = new LinearLayout.LayoutParams(rateCount.getLayoutParams().width, 0);
                        lp4.setMargins(10,10,10,10);
                            rateCount.setLayoutParams(lp4);

                            loginForMore.setVisibility(View.VISIBLE);
                            LinearLayout.LayoutParams lp5 = new LinearLayout.LayoutParams(loginForMore.getLayoutParams().width, ViewGroup.LayoutParams.WRAP_CONTENT);
                        lp5.setMargins(10,10,10,10);
                            loginForMore.setLayoutParams(lp5);

                            loginForMore.setOnTouchListener(new View.OnTouchListener() {
                                @Override
                                public boolean onTouch(View v, MotionEvent event) {

                                    Intent x = new Intent(getApplicationContext() , LoginActivity.class);
                                    startActivity(x);
                                    return false;
                                }
                            });
                        }
                }
            }
        }
        Products ru = new Products();
        ru.execute();
    }

}



