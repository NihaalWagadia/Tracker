package com.example.nihaal.tracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import de.hdodenhof.circleimageview.CircleImageView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class JoinedCircleActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    LinearLayoutManager linearLayoutManager;
    FirebaseAuth mAuth;
    FirebaseUser mUser;
    DatabaseReference mReference, mUserReference;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_joined_circle);

        recyclerView = findViewById(R.id.joined_friends);
        mAuth = FirebaseAuth.getInstance(FirebaseApp.initializeApp(this));
        mUser = mAuth.getCurrentUser();

        linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);

        mReference = FirebaseDatabase.getInstance().getReference().child("Users");
        mUserReference = FirebaseDatabase.getInstance().getReference().child("Users").child(mUser.getUid()).child("MyJoinedUsers");

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
                    protected void onBindViewHolder(@NonNull JoinedCirlce_Viewolder joinedCirlce_viewolder, int i, @NonNull JoinedCircle joinedCircle) {
                        joinedCirlce_viewolder.username.setText(joinedCircle.getConnected_name());
                        Picasso.get().load(joinedCircle.getConnected_imageUrl()).into(joinedCirlce_viewolder.profileimage);
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
        CircleImageView profileimage;
        Button button;

        public JoinedCirlce_Viewolder(@NonNull View itemView) {
            super(itemView);
            username = itemView.findViewById(R.id.item_title);
            profileimage = itemView.findViewById(R.id.iv11);
            button = itemView.findViewById(R.id.delete_button);
        }
    }
}
