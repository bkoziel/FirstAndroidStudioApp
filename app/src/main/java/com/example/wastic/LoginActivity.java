package com.example.wastic;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


public class LoginActivity extends AppCompatActivity {
    TextView registerButton;
    TextView info;
    CardView loginButton;
    private EditText login;
    private EditText password;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        registerButton = (TextView) findViewById(R.id.textViewRegister);
        login = (EditText) findViewById(R.id.editTextLogin);
        password = (EditText) findViewById(R.id.editTextPassword);
        loginButton = (CardView) findViewById(R.id.buttonRegister);
        info = (TextView) findViewById(R.id.textInfo) ;

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            validate(login.getText().toString(),password.getText().toString());
            }
        });

    }

    private void validate(String login, String password){
    if(login.equals("Admin")&& password.equals("1234")){
        startActivity(new Intent(getApplicationContext(), UserProfileActivity.class));
    }else{
        info.setText("Nieprawidłowy login lub hasło");
    }
    }
}
