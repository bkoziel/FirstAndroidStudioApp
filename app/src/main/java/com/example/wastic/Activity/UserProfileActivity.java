package com.example.wastic.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.wastic.R;
import com.example.wastic.SharedPrefManager;
import com.example.wastic.User;
import com.squareup.picasso.Picasso;

public class UserProfileActivity extends AppCompatActivity {
    TextView textViewId, textViewUsername, textViewEmail, textViewGender, textViewSince;
    ImageView imageView;
    Button adminPanelButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);


        if (!SharedPrefManager.getInstance(this).isLoggedIn()) {
            finish();
            startActivity(new Intent(this, LoginActivity.class));
        }


        textViewId = findViewById(R.id.textViewId);
        textViewUsername = findViewById(R.id.textViewUsername);
        textViewEmail = findViewById(R.id.textViewEmail);
        textViewGender = findViewById(R.id.textViewGender);
        textViewSince = findViewById(R.id.textViewSince);
        imageView= findViewById(R.id.imageViewAvatar);
        adminPanelButton = findViewById(R.id.buttonAdminPanel);

        //getting the current user
        User user = SharedPrefManager.getInstance(this).getUser();

        //setting the values to the textviews
       // textViewId.setText(String.valueOf(user.getId()));
        textViewUsername.setText(user.getUsername());
        textViewEmail.setText("Email:\n"+user.getEmail());
        textViewGender.setText("Sex:\n" + user.getGender());
        textViewSince.setText("Registered since:\n" + user.getSince());
        Picasso.get().load("https://wasticelo.000webhostapp.com/" + user.getAvatar()).into(imageView);

        //when the user presses logout button/'''
        //calling the logout method
        if(SharedPrefManager.getInstance(this).ifAdmin()==1) {
            adminPanelButton.setVisibility(View.VISIBLE);
            adminPanelButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onPause();
                    startActivity(new Intent(getApplicationContext(), AdminPanelActivity.class));
                }
            });
        }else{
            adminPanelButton.setVisibility(View.INVISIBLE);
        }
        findViewById(R.id.buttonLogout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                SharedPrefManager.getInstance(getApplicationContext()).logout();
            }
        });

        findViewById(R.id.buttonAddedProducts).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onPause();
                startActivity(new Intent(getApplicationContext(), UserProductsActivity.class));
            }
        });
    }
}
