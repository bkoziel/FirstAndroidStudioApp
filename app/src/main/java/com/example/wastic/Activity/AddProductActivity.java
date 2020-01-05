package com.example.wastic.Activity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.wastic.Product;
import com.example.wastic.R;
import com.example.wastic.Requesthandler;
import com.example.wastic.SharedPrefManager;
import com.example.wastic.URLs;
import com.example.wastic.User;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class AddProductActivity extends AppCompatActivity {
    private EditText editTextProductName, editTextBarCode;

    public static final String UPLOAD_URL = "https://wasticelo.000webhostapp.com/upload.php";
    public static final String UPLOAD_KEY = "image";
    public static final String TAG = "MY MESSAGE";
    private ProgressDialog loading;
    private RatingBar ratingBar;
    private float ratedValue;
    private TextView  rateCount;
final int CODE_GALLERY_REQUEST=999;
    private int PICK_IMAGE_REQUEST = 8;
    private int REQUEST_IMAGE_CAPTURE = 1;
private  Bitmap bitmap;
private     String imageData="";
    private  Button buttonAddProduct,editPhotoURL,takePhotoButton;
   private User user = SharedPrefManager.getInstance(this).getUser();
    private ImageView imageView;
    private Calendar calendar;
    private SimpleDateFormat dateFormat;
    private String date;

private String urlUpload="https://wasticelo.000webhostapp.com/upload.php";

  String code;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addproduct);
        code = getIntent().getStringExtra("code");

        buttonAddProduct = findViewById(R.id.buttonAddProduct);
        editTextProductName = (EditText) findViewById(R.id.editTextProductName);
        editTextBarCode = (EditText) findViewById(R.id.editBarCode);
        editPhotoURL = (Button) findViewById(R.id.editPhotoURL);
        imageView = (ImageView)findViewById(R.id.imageViewPhoto);
        takePhotoButton = (Button) findViewById(R.id.buttonTakeFoto);

        takePhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            showCamera();
            }
        });

        editPhotoURL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showFileChooser();

            }
        });

        ratingBar= (RatingBar) findViewById(R.id.RatingBars);
        rateCount = (TextView) findViewById(R.id.textViewRating);


        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                ratedValue = ratingBar.getRating();
                rateCount.setText("Twoja ocena: " + ratedValue + "/5");
            }
        });
    editTextBarCode.setText(code);
        buttonAddProduct.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                //if user pressed on button register
                //here we will register the user to server
                //uploadImage();
                productPOST();
              loading = ProgressDialog.show(AddProductActivity.this, "Uploading Image", "Proszę czekać...",true,true);
                StringRequest stringRequest = new StringRequest(Request.Method.POST, urlUpload, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        loading.dismiss();
                        Toast.makeText(getApplicationContext(),response,Toast.LENGTH_LONG).show();

                        Intent x = new Intent(getApplicationContext() , ProductActivity.class);
                        Bundle extras = new Bundle();
                        extras.putString("code",code);
                        extras.putString("pname",code);
                        x.putExtras(extras);
                        // x.putExtra("name",product.getName());
                        startActivity(x);
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        loading.dismiss();
                        Toast.makeText(getApplicationContext(),"error "+error.toString(),Toast.LENGTH_LONG).show();
                    }
                })
                {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> params = new HashMap<>();

                     String userID= Integer.toString(user.getId());
                     String rating= Float.toString(ratedValue);
                      imageData = imageToString(bitmap);

                        params.put("imagee", imageToString(bitmap));
                        params.put("user_id", userID);
                        params.put("ratingValue",rating);


                        return params;
                    }
                };
                RequestQueue requestQueue = Volley.newRequestQueue(AddProductActivity.this);
    requestQueue.add(stringRequest);

            }

        });

    }




    private void productPOST() {
        final String name = editTextProductName.getText().toString().trim();
        final String barcode = editTextBarCode.getText().toString().trim();
        final String photourl = editPhotoURL.getText().toString().trim();


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
        if(!hasImage(imageView)){
            Toast.makeText(getApplicationContext(),"Wybierz lub zrób zdjęcie", Toast.LENGTH_SHORT).show();
            imageView.requestFocus();
            return;
        }

        //if it passes all the validations

        class Products extends AsyncTask<Void, Void, String> {

            private ProgressBar progressBar;

            @Override
            protected String doInBackground(Void... voids) {
                //creating request handler object
                Requesthandler requestHandler = new Requesthandler();
                calendar = Calendar.getInstance();

                dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                date = dateFormat.format(calendar.getTime());
                String userid=Integer.toString(user.getId());
                //creating request parameters
                HashMap<String, String> params = new HashMap<>();
                params.put("name", name);
                params.put("bar_code", barcode);
                params.put("photo", "photo.jpg");
                params.put("user_id",userid);
                params.put("added_date",date);



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

            }
        }

        //executing the async task
        Products ru = new Products();
        ru.execute();
    }
    private void showFileChooser() {
        ActivityCompat.requestPermissions(AddProductActivity.this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},CODE_GALLERY_REQUEST);

    }
    private void showCamera() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

       if(requestCode==CODE_GALLERY_REQUEST){
           if(grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
               Intent intent = new Intent();
               intent.setType("image/*");
               intent.setAction(Intent.ACTION_GET_CONTENT);
               startActivityForResult(Intent.createChooser(intent, "Wybierz Zdjęcie"), PICK_IMAGE_REQUEST);
           }
           else{
               Toast.makeText(getApplicationContext(),"Nie masz pozwolenia do uzywania Galerii",Toast.LENGTH_LONG).show();
           }
           return;
       }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null ) {
            try {

            Uri filePath = data.getData();

            InputStream inputStream = getContentResolver().openInputStream(filePath);

                bitmap = BitmapFactory.decodeStream(inputStream);
                imageView.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK && data != null ) {

                Bundle extras = data.getExtras();
                Bitmap imageBitmap = (Bitmap) extras.get("data");
                bitmap=imageBitmap;
                imageView.setImageBitmap(imageBitmap);


        }
    }

    private boolean hasImage(@NonNull ImageView view) {
        Drawable drawable = view.getDrawable();
        boolean hasImage = (drawable != null);

        if (hasImage && (drawable instanceof BitmapDrawable)) {
            hasImage = ((BitmapDrawable)drawable).getBitmap() != null;
        }

        return hasImage;
    }

    private String imageToString(Bitmap bitmap){

ByteArrayOutputStream outputStream=new ByteArrayOutputStream();

bitmap.compress(Bitmap.CompressFormat.JPEG,40,outputStream);
byte[] imageBytes=outputStream.toByteArray();
String encodedImage=Base64.encodeToString(imageBytes,Base64.DEFAULT);
return encodedImage;


    }

    private void dismissProgressDialog() {
        if (loading != null && loading.isShowing()) {
            loading.dismiss();
        }
    }

    @Override
    protected void onDestroy() {
        dismissProgressDialog();
        super.onDestroy();
    }

}

