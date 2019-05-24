package com.example.nihaal.tracker;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
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

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class FriendRequestAdapter extends FirebaseRecyclerAdapter<FriendRequestConst, FriendRequestAdapter.FriendHolder> {

//    DatabaseReference circleReference,connectedReference, currentRefernce ,reference, databaseReference, data_refernce_to_show;
//    String join_user_id,current_user_id, joined_name, lat, lng, connected_username;
//    FirebaseUser user;
//    FirebaseAuth auth;
//
//    auth = (FirebaseAuth) FirebaseAuth.getInstance(FirebaseApp.initializeApp(this));
//    user = auth.getCurrentUser();
//    current_user_id = user.getUid();
//    currentRefernce = FirebaseDatabase.getInstance().getReference().child("Users").child(user.getUid());
//        currentRefernce.keepSynced(true);
//    reference = FirebaseDatabase.getInstance().getReference().child("Users");
//        reference.keepSynced(true);
//    databaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child(user.getUid());
//        databaseReference.keepSynced(true);
//    data_refernce_to_show = FirebaseDatabase.getInstance().getReference().child("Users")
//                .child(current_user_id).child("GetDataForRequests");
//        data_refernce_to_show.keepSynced(true);
//
//        currentRefernce.addValueEventListener(new ValueEventListener() {
//        @Override
//        public void onDataChange(DataSnapshot dataSnapshot) {
//            Log.d("Naemmmmm",String.valueOf(Objects.requireNonNull(dataSnapshot.getValue(CreateUser.class)).name));
//            joined_name = Objects.requireNonNull(dataSnapshot.getValue(CreateUser.class)).name;
//            lat = Objects.requireNonNull(dataSnapshot.getValue(CreateUser.class)).lat;
//            lng = Objects.requireNonNull(dataSnapshot.getValue(CreateUser.class)).lng;
//        }
//        @Override
//        public void onCancelled(DatabaseError databaseError) {
//
//        }
//    });
//
//

    public FriendRequestAdapter(@NonNull FirebaseRecyclerOptions<FriendRequestConst> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull FriendHolder friendHolder, int i, @NonNull FriendRequestConst friendRequestConst) {
            friendHolder.requestUserName.setText(friendRequestConst.getConnected_name());
    }

    @NonNull
    @Override
    public FriendHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.friend_request_design,
                parent, false);
        return new FriendHolder(v);
    }

    public void deleteItem(int position){
        getSnapshots().getSnapshot(position).getRef().removeValue();
    }

//        public void accept_friend(View v){
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




    class FriendHolder extends RecyclerView.ViewHolder {
        TextView requestUserName;

        public FriendHolder(@NonNull View itemView) {
            super(itemView);
            requestUserName = itemView.findViewById(R.id.Request_name);
        }
    }
}
