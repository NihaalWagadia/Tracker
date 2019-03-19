package com.example.nihaal.tracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import de.hdodenhof.circleimageview.CircleImageView;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
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
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MyCircleActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    LinearLayoutManager linearLayoutManager;

    FirebaseAuth auth;
    FirebaseUser user;
    CreateUser createUser;
    ArrayList<CreateUser> namelist;
    DatabaseReference reference, userReference;
    String circlememberid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_circle);
        recyclerView = findViewById(R.id.all_friends);
        auth = (FirebaseAuth) FirebaseAuth.getInstance(FirebaseApp.initializeApp(this));
        user = auth.getCurrentUser();
        namelist = new ArrayList<>();

        //*today

        linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);



        //today*

        reference = FirebaseDatabase.getInstance().getReference().child("Users");
        userReference = FirebaseDatabase.getInstance().getReference().child("Users").child(user.getUid()).child("CircleMembers");

//        reference.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                namelist.clear();
//
//                if(dataSnapshot.exists()){
//                    for(DataSnapshot dss: dataSnapshot.getChildren()){
//                    circlememberid = dss.child("circlememberid").getValue(String.class);
//                        //circlememberid =  "2fTH0UfVAlYnQ6nkREefEosq5vj2";
//
//
//                      //  Toast.makeText(getApplicationContext(), "XCVBNM: " +circlememberid , Toast.LENGTH_SHORT).show();
//
//                        //fetch circlmemberid
//                        if(circlememberid!= null) {
//                            userReference.child(circlememberid)
//                                    .addListenerForSingleValueEvent(new ValueEventListener() {
//                                        @Override
//                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                                            //fetch object from circle members nide
//                                            createUser = dataSnapshot.getValue(CreateUser.class);
//                                            namelist.add(createUser);
//                                            Log.d("Number","qwertui" + namelist.toString() );
//                                            adapter.notifyDataSetChanged();
//
//                                        }
//
//                                        @Override
//                                        public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                                            Toast.makeText(getApplicationContext(), databaseError.getMessage(), Toast.LENGTH_SHORT).show();
//                                        }
//                                    });
//                        }
//                    }
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//                Toast.makeText(getApplicationContext(), databaseError.getMessage(), Toast.LENGTH_SHORT).show();
//
//            }
//        }

//        adapter = new MembersAdapter(namelist, getApplicationContext());
//        recyclerView.setAdapter(adapter);
//        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<Committee> options =
                new FirebaseRecyclerOptions.Builder<Committee>()
                .setQuery(userReference, Committee.class)
                .build();
        FirebaseRecyclerAdapter<Committee,CommitteeViewolder> adapter =
                new FirebaseRecyclerAdapter<Committee, CommitteeViewolder>(options) {




            @Override
            protected void onBindViewHolder(@NonNull CommitteeViewolder committeeViewolder, int i, @NonNull Committee committee) {
                committeeViewolder.username.setText(committee.getJoined_name());
                Picasso.get().load(committee.getJoined_imageUrl()).into(committeeViewolder.profileimage);
                    }




                    @NonNull
                    @Override
                    public CommitteeViewolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_layout, parent, false);
                        CommitteeViewolder viewholder = new CommitteeViewolder(view);
                        return  viewholder;
                     }




                };
        recyclerView.setAdapter(adapter);
        adapter.startListening();


    }

    public static class CommitteeViewolder extends  RecyclerView.ViewHolder
        {
            TextView username;
            CircleImageView profileimage;
            Button button;

            public CommitteeViewolder(@NonNull View itemView) {
                super(itemView);
                username = itemView.findViewById(R.id.item_title);
                profileimage = itemView.findViewById(R.id.iv11);
                button = itemView.findViewById(R.id.delete_button);

            }
        }




}
