package com.example.nihaal.tracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.goodiebag.pinview.Pinview;
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

public class JoinCircleActivity extends AppCompatActivity {

    Pinview pinview;
    DatabaseReference reference, currentRefernce;
    FirebaseUser user;
    FirebaseAuth auth;
    String current_user_id;
    String join_user_id, joined_name, profile_image, lat ,lng;
    String connected_username, connected_imageUrl;
    DatabaseReference circleReference, connectedReference, circleReference2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_circle);
        pinview = findViewById(R.id.pinview);
        auth = (FirebaseAuth) FirebaseAuth.getInstance(FirebaseApp.initializeApp(this));
        user = auth.getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference().child("Users");
        currentRefernce = FirebaseDatabase.getInstance().getReference().child("Users").child(user.getUid());


        current_user_id = user.getUid();

        //Fetching user name and image
        currentRefernce.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d("Naemmmmm",String.valueOf(dataSnapshot.getValue(CreateUser.class).name));

                joined_name = dataSnapshot.getValue(CreateUser.class).name;

                profile_image = dataSnapshot.getValue(CreateUser.class).imageUrl;
                lat = dataSnapshot.getValue(CreateUser.class).lat;
                lng = dataSnapshot.getValue(CreateUser.class).lng;
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });




    }

    public void submitButtonClick(View v){
// 1) to check if the input code is present ot not in database.
        //2) if code is present, find that user and create a node(Circle members)
        Query query = reference.orderByChild("code").equalTo(pinview.getValue());
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    CreateUser createUser = null;
                    for(DataSnapshot childDss : dataSnapshot.getChildren()){
                        createUser = childDss.getValue(CreateUser.class);
                        join_user_id = createUser.userid;
                        connected_username = createUser.name;
                        connected_imageUrl = createUser.imageUrl;
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


                        circleReference = FirebaseDatabase.getInstance().getReference().child("Users")
                                .child(join_user_id).child("CircleMembers");

                        connectedReference = FirebaseDatabase.getInstance().getReference().child("Users")
                                .child(current_user_id).child("MyJoinedUsers");

                        CircleJoin circleJoin = new CircleJoin(current_user_id, joined_name, profile_image, lat,lng);
                       // CircleJoin circleJoin1 = new CircleJoin(join_user_id);
                        final JoinedCircle joinedCircle = new JoinedCircle(join_user_id, connected_username, connected_imageUrl );


                        circleReference.child(user.getUid()).setValue(circleJoin)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if(task.isSuccessful()){
                                          Toast.makeText(getApplicationContext(),"User Joined Circle Successfully", Toast.LENGTH_SHORT).show();

                                          connectedReference.child(user.getUid()).setValue(joinedCircle)
                                                  .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                      @Override
                                                      public void onComplete(@NonNull Task<Void> task) {
                                                        if(task.isSuccessful()){
                                                            Toast.makeText(getApplicationContext(),"updated", Toast.LENGTH_SHORT).show();

                                                        }
                                                      }
                                                  });




                                        }
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
