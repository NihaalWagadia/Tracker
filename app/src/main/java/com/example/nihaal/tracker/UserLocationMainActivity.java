package com.example.nihaal.tracker;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.google.android.gms.common.ConnectionResult;
//import com.google.android.gms.common.GoogleApiAvailabilityLight;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import android.util.Log;
import android.view.View;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

public class UserLocationMainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {


    FirebaseAuth auth;
    GoogleMap mMap;
    GoogleApiClient client;
    LocationRequest request;
    LatLng latLng;
    DatabaseReference databaseReference, rootRef, pointingRef, all_circle_members;
    FirebaseUser user;
    String current_user_email;
    String current_user_name;
    String current_user_imageUrl;
    TextView t1_currentName, t2_currentEmail;
    ImageView iv;
    LocationManager manager;
    int a = 0;
    ArrayList<MarkerName> markerNames = new ArrayList<MarkerName>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_location_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Map");
        auth = (FirebaseAuth) FirebaseAuth.getInstance(FirebaseApp.initializeApp(this));
        user = auth.getCurrentUser();

//            if(user.getEmail().isEmpty()){
//                Intent intent = new Intent(UserLocationMainActivity.this, MainActivity.class);
//                startActivity(intent);
//                finish();
//            }
//            else {
                all_circle_members = FirebaseDatabase.getInstance().getReference().child("Users").child(user.getUid());
                all_circle_members.keepSynced(true);
                Query query = all_circle_members.orderByChild("CircleMembers");
                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        CircleJoin circleJoin = null;
                        MarkerName markerName = null;
                        Log.d("XYz", dataSnapshot.child("CircleMembers").getChildrenCount() + "");
                        for (DataSnapshot childDss : dataSnapshot.child("CircleMembers").getChildren()) {
                            circleJoin = childDss.getValue(CircleJoin.class);
                            String latit = circleJoin.lat;
                            String longi = circleJoin.lng;
                            LatLng latLng1 = new LatLng(Double.valueOf(latit), Double.valueOf(longi));
                            markerName = new MarkerName(circleJoin.joined_name, latLng1);
                            markerNames.add(markerNames.size(), markerName);


                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
//            }

            SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.map);
            mapFragment.getMapAsync(this);


            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                    this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
            drawer.addDrawerListener(toggle);
            toggle.syncState();

            NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
            navigationView.setNavigationItemSelectedListener(this);

            View header = navigationView.getHeaderView(0);
            t1_currentName = header.findViewById(R.id.title_text);
            t2_currentEmail = header.findViewById(R.id.email_text);


            databaseReference = FirebaseDatabase.getInstance().getReference().child("Users");
            databaseReference.keepSynced(true);
            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                    current_user_name = dataSnapshot.child(user.getUid()).child("name").getValue(String.class);
                    current_user_email = dataSnapshot.child(user.getUid()).child("email").getValue(String.class);

                    t1_currentName.setText(current_user_name);
                    t2_currentEmail.setText(current_user_email);


//                    Log.d("PROOOOOOOO", current_user_email);

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });



    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        pointingRef = FirebaseDatabase.getInstance().getReference("Users").child(user.getUid());
        pointingRef.keepSynced(true);

        pointingRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Date myDate = new Date();
                SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss a", Locale.getDefault());
                String date = format1.format(myDate);
                pointingRef.child("date").setValue(date);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        // Add a marker in Sydney and move the camera
        client = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();

        client.connect();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.user_location_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_joinCircle) {
            // Handle the camera action
            Intent intent = new Intent(UserLocationMainActivity.this, JoinCircleActivity.class);
            startActivity(intent);

        } else if (id == R.id.nav_joinedCircle) {
            Intent intent = new Intent(UserLocationMainActivity.this, JoinedCircleActivity.class);
            startActivity(intent);

        } else if (id == R.id.nav_shareLoc) {

            Intent i = new Intent(Intent.ACTION_SEND);
            i.setType("text/plain");
            i.putExtra(Intent.EXTRA_TEXT, "My location is : " + "https://www.google.com/maps/@" + latLng.latitude + "," + latLng.longitude + ",17z");
            startActivity(i.createChooser(i, "Share using: "));

        } else if (id == R.id.nav_signOut) {
            FirebaseUser user = auth.getCurrentUser();
            if (user != null) {
                auth.signOut();
                finish();
                Intent myIntent = new Intent(UserLocationMainActivity.this, MainActivity.class);
                startActivity(myIntent);
            }


        } else if (id == R.id.nav_myCircle) {

            Intent intent = new Intent(UserLocationMainActivity.this, MyCircleActivity.class);
            startActivity(intent);

        } else if (id == R.id.nav_code) {
            Intent intent = new Intent(UserLocationMainActivity.this, MyCode.class);
            startActivity(intent);
        } else if (id == R.id.Friend_request) {

        Intent intent = new Intent(UserLocationMainActivity.this, FriendRequest.class);
        startActivity(intent);
        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

        request = new LocationRequest().create();
        request.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        request.setInterval(1000);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    Activity#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for Activity#requestPermissions for more details.
                return;
            }
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(client, request, this);

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(final Location location) {

        try {
            latLng = new LatLng(location.getLatitude(), location.getLongitude());
            MarkerName currMarker = new MarkerName(current_user_name, latLng);
            markerNames.add(markerNames.size(), currMarker);
            MarkerOptions options = null;
            for (MarkerName x : markerNames) {
                options = new MarkerOptions();
                options.position(x.getCurrLatlng());
                options.title(x.getName());

                mMap.addMarker(options);
                if(a == 0 ) {
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 16));
                    mMap.getUiSettings().setZoomControlsEnabled(true);
                    a++;
                }
            }

//                mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
//                mMap.animateCamera(CameraUpdateFactory.zoomTo(12));


            rootRef = FirebaseDatabase.getInstance().getReference("Users").child(user.getUid());
            rootRef.keepSynced(true);
            rootRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    String latitude = String.valueOf(location.getLatitude());
                    String longitude = String.valueOf(location.getLongitude());

                    rootRef.child("lat").setValue(String.valueOf(latitude));
                    rootRef.child("lng").setValue(String.valueOf(longitude));
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });


        } catch (Exception e) {
            Log.d("Exception", String.valueOf(e));
        }


    }

    @Override
    public void onConnectionSuspended(int i) {

    }


}
