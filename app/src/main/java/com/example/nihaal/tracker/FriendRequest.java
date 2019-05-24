package com.example.nihaal.tracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
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

public class FriendRequest extends AppCompatActivity {

    RecyclerView recyclerView;
    LinearLayoutManager linearLayoutManager;
    DatabaseReference circleReference,connectedReference, currentRefernce ,reference, databaseReference, data_refernce_to_show;
    String join_user_id,current_user_id, joined_name, lat, lng, connected_username;
    FirebaseUser user;
    FirebaseAuth auth;
    Button decline_button;
    FriendRequestAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_request);

        auth = (FirebaseAuth) FirebaseAuth.getInstance(FirebaseApp.initializeApp(this));
        user = auth.getCurrentUser();
        current_user_id = user.getUid();
        currentRefernce = FirebaseDatabase.getInstance().getReference().child("Users").child(user.getUid());
        currentRefernce.keepSynced(true);
        reference = FirebaseDatabase.getInstance().getReference().child("Users");
        reference.keepSynced(true);
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child(user.getUid());
        databaseReference.keepSynced(true);
        data_refernce_to_show = FirebaseDatabase.getInstance().getReference().child("Users")
                .child(current_user_id).child("GetDataForRequests");
        data_refernce_to_show.keepSynced(true);

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

        setUpRecyclerView();

//        mUserReference = FirebaseDatabase.getInstance().getReference().child("Users");
//        mUserReference.keepSynced(true);
//        mUserReference.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                if (dataSnapshot.exists()) {
//                    CreateUser createUser = null;
//                    for (final DataSnapshot childDss : dataSnapshot.getChildren()) {
//
//                        createUser = childDss.getValue(CreateUser.class);
//                        Friend_Request_user_id = createUser.userid;
//                        Friend_request_connected_username = createUser.name;
//
//                    }
//                }
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });

//        FriendRequestConst friendRequestConst = new FriendRequestConst(Friend_Request_user_id, Friend_request_connected_username);
//
//        data_refernce_to_show.child(Friend_Request_user_id).setValue(friendRequestConst)
//                .addOnCompleteListener(new OnCompleteListener<Void>() {
//                    @Override
//                    public void onComplete(@NonNull Task<Void> task) {
//                        if (task.isSuccessful()) {
//                            Log.d("Voila", "Volila");
////                            Log.d("wait3","wait3");
////                            Toast.makeText(getApplicationContext(), "User Joined Circle Successfully", Toast.LENGTH_SHORT).show();
//
//                        }
//                    }
//                });



    }

    private void setUpRecyclerView() {
        FirebaseRecyclerOptions<FriendRequestConst> options = new FirebaseRecyclerOptions.Builder<FriendRequestConst>()
                .setQuery(data_refernce_to_show, FriendRequestConst.class)
                .build();

        adapter = new FriendRequestAdapter(options);
        RecyclerView recyclerView = findViewById(R.id.Request_list);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT ) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                adapter.deleteItem(viewHolder.getAdapterPosition());
            }
        }).attachToRecyclerView(recyclerView);

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT ) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                adapter.deleteItem(viewHolder.getAdapterPosition());
            }
        }).attachToRecyclerView(recyclerView);

    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    //    public void accept_friend(View v){
//
//
//        reference.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                if (dataSnapshot.exists()) {
//                    CreateUser createUser;
//                    for (final DataSnapshot childDss : dataSnapshot.getChildren()) {
//
//                        createUser = childDss.getValue(CreateUser.class);
//                        join_user_id = createUser.userid;
//                        connected_username = createUser.name;
//
//                        Query query1 = databaseReference.child("MyJoinedUsers").orderByKey().equalTo(join_user_id);
//                        query1.addListenerForSingleValueEvent(new ValueEventListener() {
//                            @Override
//                            public void onDataChange(DataSnapshot dataSnapshot) {
//                                circleReference = FirebaseDatabase.getInstance().getReference().child("Users")
//                                        .child(join_user_id).child("CircleMembers");
//                                circleReference.keepSynced(true);
//
//                                connectedReference = FirebaseDatabase.getInstance().getReference().child("Users")
//                                        .child(current_user_id).child("MyJoinedUsers");
//                                connectedReference.keepSynced(true);
//
//
//                                CircleJoin circleJoin = new CircleJoin(current_user_id, joined_name, lat, lng);
//                                // CircleJoin circleJoin1 = new CircleJoin(join_user_id);
//                                final JoinedCircle joinedCircle = new JoinedCircle(join_user_id, connected_username);
//
//
//                                circleReference.child(current_user_id).setValue(circleJoin)
//                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
//                                            @Override
//                                            public void onComplete(@NonNull Task<Void> task) {
//                                                if (task.isSuccessful()) {
//                                                    Log.d("wait1","wait1");
//                                                    Toast.makeText(getApplicationContext(), "User Joined Circle Successfully", Toast.LENGTH_SHORT).show();
//
//                                                    connectedReference.child(join_user_id).setValue(joinedCircle)
//                                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
//                                                                @Override
//                                                                public void onComplete(@NonNull Task<Void> task) {
//                                                                    if (task.isSuccessful()) {
//                                                                        Log.d("wait3","wait3");
//                                                                        Toast.makeText(getApplicationContext(), "User Joined Circle Successfully", Toast.LENGTH_SHORT).show();
//
//                                                                    }
//                                                                }
//                                                            });
//
//
//                                                }
//
//                                            }
//
//                                        });
//                            }
//
//                            @Override
//                            public void onCancelled(DatabaseError databaseError) {
//
//                            }
//                        });
//
//
//
//                    }
//
//
//                }
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });
//
//
//    }


//    public void decline_request(View v){
//        FriendRequestConst friendRequestConst = ;
//        final String string = friendRequestConst.getConected_id();
//        data_refernce_to_show.child(string).addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                data_refernce_to_show.child(string).removeValue();
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });
//
//    }



//    protected void onStart() {
//        super.onStart();
//        FirebaseRecyclerOptions<FriendRequestConst> options =
//                new FirebaseRecyclerOptions.Builder<FriendRequestConst>()
//                .setQuery(data_refernce_to_show,FriendRequestConst.class)
//                .build();
//        FirebaseRecyclerAdapter<FriendRequestConst, FriendRequest_ViewHolder> adapter =
//                new FirebaseRecyclerAdapter<FriendRequestConst, FriendRequest_ViewHolder>(options) {
//                    @Override
//                    protected void onBindViewHolder(@NonNull FriendRequest_ViewHolder friendRequest_viewHolder, int i, @NonNull FriendRequestConst friendRequestConst) {
//                        friendRequest_viewHolder.username.setText(friendRequestConst.getConnected_name());
//
////                        decline_button.setOnClickListener(new View.OnClickListener() {
////                            @Override
////                            public void onClick(View v) {
////                                final String string = friendRequestConst.getConected_id();
////                                data_refernce_to_show.child(string).addListenerForSingleValueEvent(new ValueEventListener() {
////                                    @Override
////                                    public void onDataChange(DataSnapshot dataSnapshot) {
////                                        data_refernce_to_show.child(string).removeValue();
////                                    }
////
////                                    @Override
////                                    public void onCancelled(DatabaseError databaseError) {
////
////                                    }
////                                });
////                            }
////                        });
//
//                    }
//
//                    @NonNull
//                    @Override
//                    public FriendRequest_ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.friend_request_design, parent, false);
//                        FriendRequest_ViewHolder viewHolder = new FriendRequest_ViewHolder(view);
//                        return viewHolder;
//                    }
//                };
//        recyclerView.setAdapter(adapter);
//        adapter.startListening();
//    }


//    {
//        TextView username;
//        Button accept_button, decline_button;
//        DatabaseReference mReference, mDatabaseReference, mCircleReference, mConnectedReference, mCurrentRefernce;
//        String join_user_id, connected_username, joined_name, lat, lng;
//        FirebaseUser user;
//        FirebaseAuth auth;
//
//
//        public FriendRequest_ViewHolder(@NonNull View itemView) {
//            super(itemView);
//            username = itemView.findViewById(R.id.Request_name);
//            auth = FirebaseAuth.getInstance();
//            user = auth.getCurrentUser();
//            mReference = FirebaseDatabase.getInstance().getReference().child("Users");
//            mReference.keepSynced(true);
//            mDatabaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child(user.getUid());
//            mDatabaseReference.keepSynced(true);
//            mCurrentRefernce = FirebaseDatabase.getInstance().getReference().child("Users").child(user.getUid());
//            mCurrentRefernce.keepSynced(true);
//
//
//
//            mCurrentRefernce.addValueEventListener(new ValueEventListener() {
//                @Override
//                public void onDataChange(DataSnapshot dataSnapshot) {
//                    Log.d("Naemmmmm",String.valueOf(Objects.requireNonNull(dataSnapshot.getValue(CreateUser.class)).name));
//
//                    joined_name = Objects.requireNonNull(dataSnapshot.getValue(CreateUser.class)).name;
//
//                    lat = Objects.requireNonNull(dataSnapshot.getValue(CreateUser.class)).lat;
//                    lng = Objects.requireNonNull(dataSnapshot.getValue(CreateUser.class)).lng;
//
//                }
//
//
//                @Override
//                public void onCancelled(DatabaseError databaseError) {
//
//                }
//            });
//
//
//            accept_button.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    mReference.addListenerForSingleValueEvent(new ValueEventListener() {
//                        @Override
//                        public void onDataChange(DataSnapshot dataSnapshot) {
//                            if (dataSnapshot.exists()) {
//                                CreateUser createUser = null;
//                                for (final DataSnapshot childDss : dataSnapshot.getChildren()) {
//
//                                    createUser = childDss.getValue(CreateUser.class);
//                                    join_user_id = createUser.userid;
//                                    connected_username = createUser.name;
//
//                                    Query query1 = mDatabaseReference.child("MyJoinedUsers").orderByKey().equalTo(join_user_id);
//                                    query1.addListenerForSingleValueEvent(new ValueEventListener() {
//                                        @Override
//                                        public void onDataChange(DataSnapshot dataSnapshot) {
//                                            mCircleReference = FirebaseDatabase.getInstance().getReference().child("Users")
//                                                    .child(join_user_id).child("CircleMembers");
//                                            mCircleReference.keepSynced(true);
//
//                                            mConnectedReference = FirebaseDatabase.getInstance().getReference().child("Users")
//                                                    .child(user.getUid()).child("MyJoinedUsers");
//                                            mConnectedReference.keepSynced(true);
//
//
//                                            CircleJoin circleJoin = new CircleJoin(user.getUid(), joined_name, lat, lng);
//                                            // CircleJoin circleJoin1 = new CircleJoin(join_user_id);
//                                            final JoinedCircle joinedCircle = new JoinedCircle(join_user_id, connected_username);
//
//
//                                            mCircleReference.child(user.getUid()).setValue(circleJoin)
//                                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
//                                                        @Override
//                                                        public void onComplete(@NonNull Task<Void> task) {
//                                                            if (task.isSuccessful()) {
//                                                                Log.d("wait1","wait1");
//                                                                Toast.makeText(getApplicationContext(), "User Joined Circle Successfully", Toast.LENGTH_SHORT).show();
//
//                                                                mConnectedReference.child(join_user_id).setValue(joinedCircle)
//                                                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
//                                                                            @Override
//                                                                            public void onComplete(@NonNull Task<Void> task) {
//                                                                                if (task.isSuccessful()) {
//                                                                                    Log.d("wait3","wait3");
//                                                                                    Toast.makeText(getApplicationContext(), "User Joined Circle Successfully", Toast.LENGTH_SHORT).show();
//
//                                                                                }
//                                                                            }
//                                                                        });
//
//
//                                                            }
//
//                                                        }
//
//                                                    });
//                                        }
//
//                                        @Override
//                                        public void onCancelled(DatabaseError databaseError) {
//
//                                        }
//                                    });
//
//
//
//                                }
//
//
//                            }
//                        }
//
//                        @Override
//                        public void onCancelled(DatabaseError databaseError) {
//
//                        }
//                    });
//
//                }
//            });
//        }
//    }

}
