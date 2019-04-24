package com.example.nihaal.tracker;

import androidx.appcompat.app.AppCompatActivity;

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


        databaseReference = FirebaseDatabase.getInstance().getReference().child("Users");
        databaseReference.keepSynced(true);

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

    public void MyCode_back(View v){
        Intent i = new Intent(MyCode.this, UserLocationMainActivity.class);
        startActivity(i);
    }
}
