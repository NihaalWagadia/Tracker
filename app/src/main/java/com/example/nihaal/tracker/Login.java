package com.example.nihaal.tracker;

//import android.support.annotation.NonNull;
import androidx.annotation.NonNull;
//import android.support.v7.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import androidx.appcompat.app.AppCompatActivity;

public class Login extends AppCompatActivity {

    FirebaseAuth auth;
    EditText e1, e2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        e1 = findViewById(R.id.emailid);
        e2 = findViewById(R.id.password);
        auth = FirebaseAuth.getInstance(FirebaseApp.initializeApp(this));
    }

    public  void login(View v){
        auth.signInWithEmailAndPassword(e1.getText ().toString(), e2.getText().toString())
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                         //   Toast.makeText(getApplicationContext(), "Successful login", Toast.LENGTH_SHORT).show();

                            FirebaseUser user = auth.getCurrentUser();
//                            if(user.isEmailVerified()){
                                Intent intent = new Intent(Login.this, UserLocationMainActivity.class);
                                startActivity(intent);
                                finish();
//                            }
//                            else {
//                             Toast.makeText(getApplicationContext(),"Email not verified, yet", Toast.LENGTH_SHORT).show();
//                           }

                        }
//                        else{
//                            Toast.makeText(getApplicationContext(),"Incorrect password or email-id", Toast.LENGTH_SHORT).show();
//                        }
                    }
                });
    }
}
