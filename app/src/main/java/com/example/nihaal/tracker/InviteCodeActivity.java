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
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

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
    StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invite_code);
        t1 = findViewById(R.id.textView);
        auth = FirebaseAuth.getInstance(FirebaseApp.initializeApp(this));
        progressDialog = new ProgressDialog(this);
        Intent myIntent = getIntent();
        reference = FirebaseDatabase.getInstance().getReference().child("Users");
        storageReference = FirebaseStorage.getInstance().getReference().child("User_images");

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
                            user = auth.getCurrentUser();

                            CreateUser createUser = new CreateUser(name, email, password, code, "false", "na", "na", "na", user.getUid());

                            user = auth.getCurrentUser();
                            userId = user.getUid();

                            reference.child(userId).setValue(createUser)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                //Save image to firebase storage
                                                StorageReference sr = storageReference.child(user.getUid() + ".jpg");
                                                sr.putFile(imageUri)
                                                        .addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                                                                if (task.isSuccessful()) {
                                                                    String download_image_path = task.getResult().toString();
                                                                    reference.child(user.getUid()).child("imageUrl").setValue(download_image_path)
                                                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                @Override
                                                                                public void onComplete(@NonNull Task<Void> task) {
                                                                                    if (task.isSuccessful()) {
                                                                                        progressDialog.dismiss();
                                                                                    //    Toast.makeText(getApplicationContext(), "Succcccccccc", Toast.LENGTH_SHORT).show();
                                                                                        sendVerificationEmail();
                                                                                        Intent i = new Intent(InviteCodeActivity.this, MainActivity.class);
                                                                                        startActivity(i);

                                                                                    } else {
                                                                                        progressDialog.dismiss();
                                                                                        Toast.makeText(getApplicationContext(), "Error occured", Toast.LENGTH_SHORT).show();
                                                                                    }
                                                                                }
                                                                            });


                                                                }
                                                            }
                                                        });


                                            } else {
                                                progressDialog.dismiss();
                                                Toast.makeText(getApplicationContext(), "Kat gaya tera", Toast.LENGTH_SHORT).show();

                                            }
                                        }
                                    });

                        }
                    }
                });
    }

    public void sendVerificationEmail() {

        user.sendEmailVerification()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(getApplicationContext(), "Email sent for verification", Toast.LENGTH_SHORT).show();
                            finish();
                            auth.signOut();
                        }
                        else {
                            Toast.makeText(getApplicationContext(),"Could not send email",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}




