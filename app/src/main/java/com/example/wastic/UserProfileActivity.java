package com.example.wastic;

import androidx.appcompat.app.AppCompatActivity;

import android.database.sqlite.SQLiteCursor;
import android.os.Bundle;
import android.widget.TextView;

public class UserProfileActivity extends AppCompatActivity {
TextView records;
    DatabaseHelper db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        records = (TextView) findViewById(R.id.ViewDB);
        db = new DatabaseHelper(this);
    SQLiteCursor cursor = db.readDataUser();
    if(cursor.getCount() > 0){
StringBuffer buff = new StringBuffer();
while(cursor.moveToNext()){
buff.append("ID: " + cursor.getString(0) + "\n");
    buff.append("Login: " + cursor.getString(1) + "\n");
    buff.append("Hasło: " + cursor.getString(2) + "\n");
    buff.append("Email: " + cursor.getString(3) + "\n");
    buff.append("Uprawnienia: " + cursor.getString(4) + "\n");
    buff.append("\n");
}
records.setText(buff.toString());
    }
    }
}
