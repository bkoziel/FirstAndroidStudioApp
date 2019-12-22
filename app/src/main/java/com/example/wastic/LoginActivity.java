package com.example.wastic;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


public class LoginActivity extends AppCompatActivity {
    private TextView registerButton;
    private TextView info;
    private CardView loginButton;
    private EditText login;
    private EditText password;
    private EditText email;
    private TextView buttonLabel;

    DatabaseHelper db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        db = new DatabaseHelper(this);
        registerButton = (TextView) findViewById(R.id.textViewRegister);
        email = (EditText) findViewById(R.id.editTextEmail);
        login = (EditText) findViewById(R.id.editTextLogin);
        password = (EditText) findViewById(R.id.editTextPassword);
        loginButton = (CardView) findViewById(R.id.buttonLogin);
        info = (TextView) findViewById(R.id.textInfo);
        buttonLabel = (TextView) findViewById(R.id.labelButton);


        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //  startActivity(new Intent(getApplicationContext(), MainActivity.class));
                email.setVisibility(View.VISIBLE);
                buttonLabel.setText("Zarejestruj");
                registerButton.setVisibility(View.INVISIBLE);
                info.setText("");
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (email.getVisibility()==View.INVISIBLE) {
                    validate(login.getText().toString().toLowerCase(), password.getText().toString());
                } else {
                    boolean makethis;
                    makethis = db.writeDataUser(login.getText().toString(),
                            password.getText().toString(),
                            email.getText().toString());
                    if (makethis) {
                        Toast.makeText(LoginActivity.this, "Zarejestrowano pomyślnie", Toast.LENGTH_LONG).show();
                        email.setVisibility(View.INVISIBLE);
                        buttonLabel.setText("Zaloguj się");
                        registerButton.setVisibility(View.VISIBLE);
                        info.setText("Teraz możesz się zalogować");

                    } else {
                        Toast.makeText(LoginActivity.this, "Spróbuj jeszcze raz", Toast.LENGTH_LONG).show();
                    }
                }

            }
        });

    }

    private void validate(String login, String password){

        if(login.equals("admin")&& password.equals("1234")){
        startActivity(new Intent(getApplicationContext(), UserProfileActivity.class));
        }
        else{
        info.setText("Nieprawidłowy login lub hasło");
         }
    }
}
