package com.example.anshumanyadav.cyclone;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,OnMapReadyCallback,LocationListener {

    private GoogleMap mMap;
    boolean f = false;
    final LatLng[] NEW = new LatLng[1];
    final LatLng[] OLD = new LatLng[1];
    final String[] key = new String[1];
    final ArrayList<String> array = new ArrayList<>();
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference();
    FirebaseFirestore db = FirebaseFirestore.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        OLD[0]= new LatLng(23.212198333333337, 72.88479833333334);
        final Map<String, Object> city2 = new HashMap<>();
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
        city2.put("lat",OLD[0].latitude );
        city2.put("lon", OLD[0].longitude);
//        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.floatingActionButton3);
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;
        int height = dm.heightPixels;
        fab.setX(width-150);
        fab.setY(height-200);
        fab.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + "1916"));
                getApplicationContext().startActivity(intent);

                db.collection("SOS").document(String.valueOf(OLD[0].latitude) + "  " + String.valueOf(OLD[0].longitude))
                        .set(city2)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Log.d("TAG", "DocumentSnapshot successfully written!");
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.w("TAG", "Error writing document", e);
                            }
                        });
                f = true;
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
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
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
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

        if (id == R.id.rangers) {
            Intent intent = new Intent(MainActivity.this,LoginActivity.class);
            startActivity(intent);
            // Handle the camera action
        } else if (id == R.id.survival) {
            Intent intent = new Intent(MainActivity.this,Survial.class);
            startActivity(intent);

        } else if (id == R.id.contact) {
            Intent intent = new Intent(MainActivity.this,Contact.class);
            startActivity(intent);

        } else if (id == R.id.donation) {
            Intent i = new Intent(Intent.ACTION_VIEW,
                    Uri.parse("http://apcmrf.ap.gov.in/#Donate"));
            startActivity(i);
        } else if (id == R.id.times) {

            Intent intent = new Intent(MainActivity.this,Timeline.class);
            startActivity(intent);
        } else if (id == R.id.Hospit){
            Intent intent = new Intent(MainActivity.this,HospitalActivity.class);
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        attachLocationListener();
        mMap.setMyLocationEnabled(true);
        OLD[0]=new LatLng(23.212198333333337, 72.88479833333334);

        db.collection("SOS")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (DocumentSnapshot document : task.getResult()) {
                                LatLng latLng = new LatLng(document.getDouble("lat"),
                                        document.getDouble("lon"));

                                MarkerOptions markerOptions = new MarkerOptions()
                                        .position(latLng);
//                                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_add_location_black_24dp));
                                mMap.addMarker(markerOptions);
                                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(OLD[0],18.0f));

                            }
                        } else {
                            Log.w("TAG", "Error getting documents.", task.getException());
                        }
                    }
                });

        db.collection("SafeHouse")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (DocumentSnapshot document : task.getResult()) {
                                LatLng latLng = new LatLng(document.getDouble("lat"),
                                        document.getDouble("lon"));

                                MarkerOptions markerOptions = new MarkerOptions()
                                        .position(latLng)
                                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.safe));
                                mMap.addMarker(markerOptions);
                                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(OLD[0], 18.0f));

                            }
                        } else {
                            Log.w("TAG", "Error getting documents.", task.getException());
                        }
                    }
                });
        db.collection("Rangers")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (DocumentSnapshot document : task.getResult()) {
                                LatLng latLng = new LatLng(document.getDouble("lat"),
                                        document.getDouble("lon"));

                                MarkerOptions markerOptions = new MarkerOptions()
                                        .position(latLng)
                                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.rf));
                                mMap.addMarker(markerOptions);
                                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(OLD[0], 18.0f));

                            }
                        } else {
                            Log.w("TAG", "Error getting documents.", task.getException());
                        }
                    }
                });

        Log.e("TAG", "onMapReady: " + myRef.toString() );
        MarkerOptions markerOptions = new MarkerOptions()
                .position(OLD[0])
                .title("Starting Point")
                .draggable(true);
//        mMap.addMarker(markerOptions).showInfoWindow();
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(OLD[0],18.0f));

        Map<String, Object> city = new HashMap<>();
        city.put("lat", String.valueOf(OLD[0].latitude));
        city.put("lon", String.valueOf(OLD[0].longitude));


        //firebase new child
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(OLD[0],18.0f));
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

            ;

        });
        // Add a marker in Sydney and move the camera
//        LatLng sydney = new LatLng(-34, 151);
//        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }

    private void attachLocationListener() {
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                1000,
                25,
                this);
    }

    @Override
    public void onLocationChanged(Location location) {
        mMap.clear();
        LatLng latLng = new LatLng(location.getLatitude(),location.getLongitude());
//        Toast.makeText(this, ""+latLng, Toast.LENGTH_SHORT).show();
        NEW[0] = latLng;
        MarkerOptions markerOptions = new MarkerOptions()
                .position(latLng)
                .title("Hello");
//                .draggable(true);
        //firebase delete
        db.collection("SOS").document(String.valueOf(OLD[0].latitude)+"  " +String.valueOf(OLD[0].longitude)).delete();
        OLD[0] = NEW[0];
        Map<String, Object> city = new HashMap<>();
        city.put("lat", (OLD[0].latitude));
        city.put("lon", (OLD[0].longitude));

            db.collection("SOS").document(String.valueOf(OLD[0].latitude) + "  " + String.valueOf(OLD[0].longitude))
                    .set(city)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.d("TAG", "DocumentSnapshot successfully written!");
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.w("TAG", "Error writing document", e);
                        }
                    });

        db.collection("SOS")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (DocumentSnapshot document : task.getResult()) {
                                LatLng latLng = new LatLng(document.getDouble("lat"),
                                        document.getDouble("lon"));

                                MarkerOptions markerOptions = new MarkerOptions()
                                        .position(latLng);
//                                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_add_location_black_24dp));
                                mMap.addMarker(markerOptions);
                                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(OLD[0],18.0f));

                            }
                        } else {
                            Log.w("TAG", "Error getting documents.", task.getException());
                        }
                    }
                });

        db.collection("SafeHouse")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (DocumentSnapshot document : task.getResult()) {
                                LatLng latLng = new LatLng(document.getDouble("lat"),
                                        document.getDouble("lon"));

                                MarkerOptions markerOptions = new MarkerOptions()
                                        .position(latLng)
                                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.safe));
                                mMap.addMarker(markerOptions);
                                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(OLD[0], 18.0f));

                            }
                        } else {
                            Log.w("TAG", "Error getting documents.", task.getException());
                        }
                    }
                });
        db.collection("Rangers")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (DocumentSnapshot document : task.getResult()) {
                                LatLng latLng = new LatLng(document.getDouble("lat"),
                                        document.getDouble("lon"));

                                MarkerOptions markerOptions = new MarkerOptions()
                                        .position(latLng)
                                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.rf));
                                mMap.addMarker(markerOptions);
                                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(OLD[0], 18.0f));

                            }
                        } else {
                            Log.w("TAG", "Error getting documents.", task.getException());
                        }
                    }
                });


        key[0] = myRef.push().getKey();
        myRef.child(key[0]).setValue(OLD[0]);

        //firebase new child
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,18.0f));myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

            ;

        });

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

}
