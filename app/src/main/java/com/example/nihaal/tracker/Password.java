package com.example.nihaal.tracker;

import android.content.Intent;
//import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class Password extends AppCompatActivity {

    String email;
    EditText e3_password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password);
        e3_password = findViewById(R.id.editTextpass);

        Intent myIntent = getIntent();
        if(myIntent!= null){
            email = myIntent.getStringExtra("email");
        }
    }

    public void goToNameActivity(View v){
        if(e3_password.getText().toString().length() > 6){
            Intent myIntent = new Intent(Password.this, NameActivity.class);
            myIntent.putExtra("email", email);
            myIntent.putExtra("password", e3_password.getText().toString());
            startActivity(myIntent);
            finish();
        }
        else {

            Toast.makeText(getApplicationContext(), "Password length should be greater than 6 characters", Toast.LENGTH_SHORT).show();
        }
    }
}
