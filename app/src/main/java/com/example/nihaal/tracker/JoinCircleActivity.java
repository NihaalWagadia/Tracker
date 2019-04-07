package com.example.nihaal.tracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import xyz.farhanfarooqui.pinview.PinView;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class JoinCircleActivity extends AppCompatActivity {

    PinView pinview;
    DatabaseReference reference, currentRefernce;
    FirebaseUser user;
    FirebaseAuth auth;
    String current_user_id;
    String join_user_id, joined_name, profile_image, lat ,lng;
    String connected_username, connected_imageUrl;
    DatabaseReference circleReference, connectedReference, databaseReference;
    boolean user_exists = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_circle);
        pinview = findViewById(R.id.pinview);
        auth = (FirebaseAuth) FirebaseAuth.getInstance(FirebaseApp.initializeApp(this));
        user = auth.getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference().child("Users");
        currentRefernce = FirebaseDatabase.getInstance().getReference().child("Users").child(user.getUid());
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child(user.getUid());


        current_user_id = user.getUid();

        //Fetching user name and image
        currentRefernce.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d("Naemmmmm",String.valueOf(dataSnapshot.getValue(CreateUser.class).name));

                joined_name = dataSnapshot.getValue(CreateUser.class).name;

                lat = dataSnapshot.getValue(CreateUser.class).lat;
                lng = dataSnapshot.getValue(CreateUser.class).lng;
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });




    }

    public void backButtonClick(View v){
        Intent intent = new Intent(JoinCircleActivity.this, UserLocationMainActivity.class);
        startActivity(intent);
    }



    public void submitButtonClick(View v){
// 1) to check if the input code is present ot not in database.
        //2) if code is present, find that user and create a node(Circle members)
        if(join_user_id == null) {
            Log.d("joinuserid", "no user id");
        }
        Query query = reference.orderByChild("code").equalTo(pinview.getPin());




        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull final DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    CreateUser createUser = null;
                    for (final DataSnapshot childDss : dataSnapshot.getChildren()) {

                        createUser = childDss.getValue(CreateUser.class);
                        join_user_id = createUser.userid;
                        connected_username = createUser.name;
//                        reference.addValueEventListener(new ValueEventListener() {
//                            @Override
//                            public void onDataChange(DataSnapshot dataSnapshot) {
//                                connected_username = dataSnapshot.getValue(CreateUser.class).name;
//                                connected_imageUrl = dataSnapshot.getValue(CreateUser.class).imageUrl;
//                            }
//
//                            @Override
//                            public void onCancelled(DatabaseError databaseError) {
//
//                            }
//                        });

//                        if (childDss.child(current_user_id).exists()) {
//                            Toast.makeText(getApplicationContext(), "abcde", Toast.LENGTH_SHORT).show();
//
//                        }
//                        else {


                        Query query1 = databaseReference.child("MyJoinedUsers").orderByKey().equalTo(join_user_id);
                        query1.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if(dataSnapshot.getValue() == null){
                                    circleReference = FirebaseDatabase.getInstance().getReference().child("Users")
                                            .child(join_user_id).child("CircleMembers");

                                    connectedReference = FirebaseDatabase.getInstance().getReference().child("Users")
                                            .child(current_user_id).child("MyJoinedUsers");

                                    CircleJoin circleJoin = new CircleJoin(current_user_id, joined_name, lat, lng);
                                    // CircleJoin circleJoin1 = new CircleJoin(join_user_id);
                                    final JoinedCircle joinedCircle = new JoinedCircle(join_user_id, connected_username);

                                    Log.d("printvalue", databaseReference.toString());


//                        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
//                            @Override
//                            public void onDataChange(DataSnapshot dataSnapshot) {
//                                for(DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){
//                                    if(dataSnapshot1.child(join_user_id).exists()){
//                                        Toast.makeText(getApplicationContext(), "Silly", Toast.LENGTH_SHORT).show();
//                                        Log.d("Mad",join_user_id);
//
//                                    }
//                                }
//                            }
//
//                            @Override
//                            public void onCancelled(DatabaseError databaseError) {
//
//                            }
//                        });
//                        Query query1 = databaseReference.child("MyJoinedUsers").orderByChild(join_user_id).equalTo(join_user_id);
//                        query1.addListenerForSingleValueEvent(new ValueEventListener() {
//                            @Override
//                            public void onDataChange(DataSnapshot dataSnapshot) {
//                                if(!dataSnapshot.exists()){
//                                    Toast.makeText(getApplicationContext(), "User Joined Circle Successfully", Toast.LENGTH_SHORT).show();
//
//                                }
//                                else {
//                                    Toast.makeText(getApplicationContext(), "Silly", Toast.LENGTH_SHORT).show();
//
//                                }
//                            }
//
//                            @Override
//                            public void onCancelled(DatabaseError databaseError) {
//
//                            }
//                        });


                                    //inserting data in circlemembers
                                    Log.d("user ex", user_exists+"");

                                    circleReference.child(user.getUid()).setValue(circleJoin)
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()) {

                                                        Toast.makeText(getApplicationContext(), "User Joined Circle Successfully", Toast.LENGTH_SHORT).show();
//                                            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
//                                                @Override
//                                                public void onDataChange(DataSnapshot dataSnapshot) {
//                                                    for(DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){
//                                                        if(dataSnapshot1.child(join_user_id).exists()){
//                                                            Toast.makeText(getApplicationContext(), "Silly", Toast.LENGTH_SHORT).show();
//                                                            Log.d("Mad",join_user_id);
//                                                            Log.d("Bhos", current_user_id);
//
//                                                        }
//                                                        else {
//
//                                                        }
//                                                    }
//                                                }
//
//                                                @Override
//                                                public void onCancelled(DatabaseError databaseError) {
//
//                                                }
//                                            });
                                                        connectedReference.child(join_user_id).setValue(joinedCircle)
                                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                    @Override
                                                                    public void onComplete(@NonNull Task<Void> task) {
                                                                        if (task.isSuccessful()) {
                                                                            Toast.makeText(getApplicationContext(), "updated", Toast.LENGTH_SHORT).show();

                                                                        }
                                                                    }
                                                                });


                                                    }

                                                }

                                            });
                                }else
                                    {
                                    Toast.makeText(getApplicationContext(), "user exists", Toast.LENGTH_SHORT).show();
                                }
                               // Toast.makeText(getApplicationContext(), dataSnapshot.toString(), Toast.LENGTH_SHORT).show();
                                Log.d("Toasty",dataSnapshot.toString());

                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });




                }
                }
                else{
                    Toast.makeText(getApplicationContext(), "Code Invalid", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });




    }
}
