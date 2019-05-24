package com.example.nihaal.tracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import de.hdodenhof.circleimageview.CircleImageView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
import com.google.firebase.database.ValueEventListener;

public class JoinedCircleActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    LinearLayoutManager linearLayoutManager;
    FirebaseAuth mAuth;
    FirebaseUser mUser;
    DatabaseReference mReference, mUserReference;
    Button button;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_joined_circle);

        recyclerView = findViewById(R.id.joined_friends);
        mAuth = FirebaseAuth.getInstance(FirebaseApp.initializeApp(this));
        mUser = mAuth.getCurrentUser();
        button = findViewById(R.id.delete_joined_user);

        linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);

//        mReference = FirebaseDatabase.getInstance().getReference().child("Users");
//        mReference.keepSynced(true);

        mUserReference = FirebaseDatabase.getInstance().getReference().child("Users").child(mUser.getUid()).child("MyJoinedUsers");
        mUserReference.keepSynced(true);

    }

    public void back_JoinedClick(View v){
        Intent intent = new Intent(JoinedCircleActivity.this, UserLocationMainActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseRecyclerOptions<JoinedCircle> options =
                new FirebaseRecyclerOptions.Builder<JoinedCircle>()
                        .setQuery(mUserReference, JoinedCircle.class)
                        .build();
        FirebaseRecyclerAdapter<JoinedCircle, JoinedCirlce_Viewolder> adapter =
                new FirebaseRecyclerAdapter<JoinedCircle, JoinedCirlce_Viewolder>(options) {


                    @Override
                    protected void onBindViewHolder(@NonNull JoinedCirlce_Viewolder joinedCirlce_viewolder, int i, @NonNull final JoinedCircle joinedCircle) {
                        joinedCirlce_viewolder.username.setText(joinedCircle.getConnected_name());

                        joinedCirlce_viewolder.username.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Log.d("Position1", String.valueOf(joinedCircle.getConected_id()));
                                joinedCircle.getConected_id();
                            }
                        });
                        button.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                final String string = joinedCircle.getConected_id();
                                mUserReference.child(string).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        Log.d("Position123", String.valueOf(joinedCircle.getConected_id()));
                                        mUserReference.child(string).removeValue();
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });
                                Toast.makeText(getApplicationContext(),"DELETED", Toast.LENGTH_SHORT).show();
                            }
                        });

                    }

//                    @Override
//                    protected void onBindViewHolder(@NonNull MyCircleActivity.CommitteeViewolder committeeViewolder, int i, @NonNull Committee committee) {
//
//                        committeeViewolder.username.setText(committee.getJoined_name());
//                        Picasso.get().load(committee.getJoined_imageUrl()).into(committeeViewolder.profileimage);
//
//
//                    }



                    @NonNull
                    @Override
                    public JoinedCirlce_Viewolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_layout, parent, false);
                        JoinedCirlce_Viewolder viewholder = new JoinedCirlce_Viewolder(view);
                        return  viewholder;
                    }
                };
        recyclerView.setAdapter(adapter);
        adapter.startListening();

    }




    public static class JoinedCirlce_Viewolder extends  RecyclerView.ViewHolder
    {
        TextView username;


        public JoinedCirlce_Viewolder(@NonNull View itemView) {
            super(itemView);
            username = itemView.findViewById(R.id.item_title);
        }
    }
}
