package com.example.nihaal.tracker;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class MyCode extends AppCompatActivity {

    DatabaseReference databaseReference;
    String codeRetrieve;
    TextView textView_code;
    FirebaseUser user;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_code);
        textView_code = findViewById(R.id.Codeview);
        auth = (FirebaseAuth) FirebaseAuth.getInstance(FirebaseApp.initializeApp(this));
        user = auth.getCurrentUser();

        ProgressDialog dialog = new ProgressDialog(MyCode.this);
        dialog.setTitle("Processing");
        dialog.setMessage("New Code Getting Generated...");
        dialog.setIndeterminate(true);
        dialog.setCancelable(false);
        dialog.show();
        long delayInMillis = 4000;
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                dialog.dismiss();
            }
        }, delayInMillis);

        databaseReference = FirebaseDatabase.getInstance().getReference().child("Users");
        databaseReference.keepSynced(true);

    }

    public void MyCode_back(View v){
        Intent i = new Intent(MyCode.this, UserLocationMainActivity.class);
        startActivity(i);
    }

    public void generate_Code(View v){

            char[] chars = "1234567890".toCharArray();
            StringBuilder stringBuilder = new StringBuilder();
            Random random = new Random();
            for(int i=0; i<6; i++){
                char c = chars[random.nextInt(chars.length)];
                stringBuilder.append(c);
            }

        Log.d("Yele",stringBuilder.toString());
        databaseReference.child(user.getUid()).child("code").setValue(stringBuilder.toString());
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                codeRetrieve = dataSnapshot.child(user.getUid()).child("code").getValue(String.class);
                Log.d("CODE", codeRetrieve);
                textView_code.setText(codeRetrieve);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
}
