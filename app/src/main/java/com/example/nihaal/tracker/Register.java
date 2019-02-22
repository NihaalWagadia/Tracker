package com.example.nihaal.tracker;

import android.app.ProgressDialog;
import android.content.Intent;
//import android.support.annotation.NonNull;
import androidx.annotation.NonNull;
//import androidx.annotation:annotation:1.0;
//import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.ProviderQueryResult;

import androidx.appcompat.app.AppCompatActivity;

public class Register extends AppCompatActivity {
        EditText e4_email;
        FirebaseAuth auth;
        ProgressDialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        e4_email = findViewById(R.id.editText4);
        auth = FirebaseAuth.getInstance(FirebaseApp.initializeApp(this));
        dialog = new ProgressDialog(this);
    }

    public void goToPasswordActivity(View v){

        dialog.setMessage("Checking email address.");
        dialog.show();
        //check if the email is already registered or not
        auth.fetchProvidersForEmail(e4_email.getText().toString())
                .addOnCompleteListener(new OnCompleteListener<ProviderQueryResult>() {
                    @Override
                    public void onComplete(@NonNull Task<ProviderQueryResult> task) {
                        if(task.isSuccessful()){
                            dialog.dismiss();
                            boolean check = !task.getResult().getProviders().isEmpty();

                            if(!check){
                        // email does not exist, so we create this email with user
                                Intent myIntent = new Intent(Register.this, Password.class);
                                myIntent.putExtra("email", e4_email.getText().toString());
                                startActivity(myIntent);
                                finish();
                            }
                            else{
                                dialog.dismiss();
                                Toast.makeText(getApplicationContext(),"Already Exist", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
    }
}
