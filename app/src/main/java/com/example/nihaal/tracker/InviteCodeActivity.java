package com.example.nihaal.tracker;

//import android.support.v7.app.AppCompatActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class InviteCodeActivity extends AppCompatActivity {
    String name, email, password, date, isSharing, code;
    ProgressDialog progressDialog;
    Uri imageUri;
    TextView t1;
    FirebaseAuth auth;
    FirebaseUser user;
    DatabaseReference reference;
    String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invite_code);
        t1 = findViewById(R.id.textView);
        auth = FirebaseAuth.getInstance(FirebaseApp.initializeApp(this));
        progressDialog = new ProgressDialog(this);
        Intent myIntent = getIntent();
        reference = FirebaseDatabase.getInstance().getReference().child("Users");
        if (myIntent != null) {
            name = myIntent.getStringExtra("name");
            isSharing = myIntent.getStringExtra("isSharing");
            code = myIntent.getStringExtra("code");
            imageUri = myIntent.getParcelableExtra("imageUri");
            email = myIntent.getStringExtra("email");
            password = myIntent.getStringExtra("password");
            date = myIntent.getStringExtra("date");

        }
        t1.setText(code);


    }

    public void registerUser(View v) {
        progressDialog.setMessage("Please Wait, Fucker");
        progressDialog.show();

        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            //insert values in Real time database
                            CreateUser createUser = new CreateUser(name,email,password,code,"false","na","na","na");

                            user = auth.getCurrentUser();
                            userId = user.getUid();

                            reference.child(userId).setValue(createUser)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                progressDialog.dismiss();
                                                Toast.makeText(getApplicationContext(), "Successfully registered", Toast.LENGTH_SHORT).show();
                                                finish();
                                                Intent intent = new Intent(InviteCodeActivity.this, MyNavigationActivity.class);
                                                startActivity(intent);
                                            }
                                            else {
                                                progressDialog.dismiss();
                                                Toast.makeText(getApplicationContext(), "Kat gaya tera", Toast.LENGTH_SHORT).show();

                                            }
                                        }
                                    });

                        }
                    }
                });
    }
}
