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

import java.util.HashMap;
import java.util.Objects;

public class JoinCircleActivity extends AppCompatActivity {

    PinView pinview;
    FirebaseUser user;
    FirebaseAuth auth;
    String current_user_id;
    String join_user_id, joined_name, profile_image, lat ,lng;
    String connected_username, connected_imageUrl;
    DatabaseReference mUserReference, data_refernce_to_show, databaseReference, reference, currentRefernce, Notification;
    boolean user_exists = false;
    String CURRENT_STATE = "NO";
    String NEW_CURRENT_STATE;
    DatabaseReference FriendRequestReference;
    String Friend_Request_user_id, Friend_request_connected_username;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_circle);
        pinview = findViewById(R.id.pinview);
        auth = (FirebaseAuth) FirebaseAuth.getInstance(FirebaseApp.initializeApp(this));
        user = auth.getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference().child("Users");
        reference.keepSynced(true);
        currentRefernce = FirebaseDatabase.getInstance().getReference().child("Users").child(user.getUid());
        currentRefernce.keepSynced(true);
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child(user.getUid());
        databaseReference.keepSynced(true);

        FriendRequestReference = FirebaseDatabase.getInstance().getReference().child("Friend_request");

        NEW_CURRENT_STATE = "not_friends";


        Notification = FirebaseDatabase.getInstance().getReference().child("Notification");
        Notification.keepSynced(true);

        current_user_id = user.getUid();

        //Fetching user name and image
        currentRefernce.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d("Naemmmmm",String.valueOf(Objects.requireNonNull(dataSnapshot.getValue(CreateUser.class)).name));

                joined_name = Objects.requireNonNull(dataSnapshot.getValue(CreateUser.class)).name;

                lat = Objects.requireNonNull(dataSnapshot.getValue(CreateUser.class)).lat;
                lng = Objects.requireNonNull(dataSnapshot.getValue(CreateUser.class)).lng;
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

                        Query query1 = databaseReference.child("MyJoinedUsers").orderByKey().equalTo(join_user_id);
                        query1.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if(dataSnapshot.getValue() == null)
                                {
                                    data_refernce_to_show = FirebaseDatabase.getInstance().getReference().child("Users")
                                            .child(join_user_id).child("GetDataForRequests");
                                    data_refernce_to_show.keepSynced(true);
                                    FriendRequestConst friendRequestConst = new FriendRequestConst(current_user_id, joined_name);

                                    data_refernce_to_show.child(current_user_id).setValue(friendRequestConst)
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    HashMap<String, String> notification = new HashMap<String, String>();
                                                    notification.put("from", current_user_id);
                                                    notification.put("type", "request");
                                                    Notification.child(join_user_id).push().setValue(notification)
                                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                @Override
                                                                public void onComplete(@NonNull Task<Void> task) {
                                                                    if(task.isSuccessful()){
                                                                        Log.d("wait2","wait2");
                                                                        CURRENT_STATE =  "request_sent";
                                                                    }
                                                                }
                                                            });
                                                    if(NEW_CURRENT_STATE == "not_friends") {
                                                        FriendRequestReference.child(current_user_id).child(join_user_id)
                                                                .child("request_type").setValue("sent")
                                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                    @Override
                                                                    public void onComplete(@NonNull Task<Void> task) {
                                                                        if (task.isSuccessful()) {
                                                                            FriendRequestReference.child(join_user_id).child(current_user_id)
                                                                                    .child("request_type").setValue("receiver")
                                                                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                        @Override
                                                                                        public void onComplete(@NonNull Task<Void> task) {
                                                                                            if (task.isSuccessful()) {
                                                                                                NEW_CURRENT_STATE = "request_sent";

                                                                                            }

                                                                                        }
                                                                                    });
                                                                        }
                                                                    }
                                                                });
                                                    }
                                                }
                                            });





//Data to be shown
//                                        mUserReference = FirebaseDatabase.getInstance().getReference().child("Users");
//                                        mUserReference.keepSynced(true);
//                                        mUserReference.addListenerForSingleValueEvent(new ValueEventListener() {
//                                            @Override
//                                            public void onDataChange(DataSnapshot dataSnapshot) {
//                                                if (dataSnapshot.exists()) {
//                                                    CreateUser createUser = null;
//                                                    for (final DataSnapshot childDss : dataSnapshot.getChildren()) {
//
//                                                        createUser = childDss.getValue(CreateUser.class);
//                                                        Friend_Request_user_id = createUser.userid;
//                                                        Friend_request_connected_username = createUser.name;
//
//
//
//                                                    }
//                                                }
//                                            }
//
//                                            @Override
//                                            public void onCancelled(DatabaseError databaseError) {
//
//                                            }
//                                        });
//                                        data_refernce_to_show = FirebaseDatabase.getInstance().getReference().child("Users")
//                                                .child(current_user_id).child("GetDataForRequests");
//                                        data_refernce_to_show.keepSynced(true);
//
//                                        FriendRequestConst friendRequestConst = new FriendRequestConst(Friend_Request_user_id, Friend_request_connected_username);
//
//                                        data_refernce_to_show.child(Friend_Request_user_id).setValue(friendRequestConst)
//                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
//                                                    @Override
//                                                    public void onComplete(@NonNull Task<Void> task) {
//                                                        if (task.isSuccessful()) {
//                                                            Log.d("Voila", "Volila");
////                            Log.d("wait3","wait3");
////                            Toast.makeText(getApplicationContext(), "User Joined Circle Successfully", Toast.LENGTH_SHORT).show();
//
//                                                        }
//                                                    }
//                                                });









                                }else{
                                    Toast.makeText(getApplicationContext(), "User Already Exists", Toast.LENGTH_SHORT).show();
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
