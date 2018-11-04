package com.example.anshumanyadav.cyclone;

import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;

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
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class MapsActivity2 extends FragmentActivity implements OnMapReadyCallback,LocationListener {

    private GoogleMap mMap;
    final LatLng[] NEW = new LatLng[1];
    final LatLng[] OLD = new LatLng[1];
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference();
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps2);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        attachLocationListener();
        mMap.setMyLocationEnabled(true);
        OLD[0] = new LatLng(23.212198333333337, 72.88479833333334);
        Map<String, Object> city = new HashMap<>();
        city.put("lat", (OLD[0].latitude));
        city.put("lon", (OLD[0].longitude));
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
                                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(OLD[0], 18.0f));

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
                                        .position(latLng);
                                mMap.addMarker(markerOptions);
                                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(OLD[0], 18.0f));

                            }
                        } else {
                            Log.w("TAG", "Error getting documents.", task.getException());
                        }
                    }
                });
        db.collection("Rangers").document(String.valueOf(OLD[0].latitude) + "  " + String.valueOf(OLD[0].longitude))
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
        Log.e("TAG", "onMapReady: " + myRef.toString());
        MarkerOptions markerOptions = new MarkerOptions()
                .position(OLD[0])
                .title("Starting Point")
                .draggable(true);
//        mMap.addMarker(markerOptions).showInfoWindow();
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(OLD[0], 18.0f));




        //firebase new child
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(OLD[0], 18.0f));
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
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
//        Toast.makeText(this, ""+latLng, Toast.LENGTH_SHORT).show();
        NEW[0] = latLng;
        MarkerOptions markerOptions = new MarkerOptions()
                .position(latLng)
                .title("Hello");
//                .draggable(true);
        //firebase delete
        db.collection("SOS").document(String.valueOf(OLD[0].latitude) + "  " + String.valueOf(OLD[0].longitude)).delete();
        OLD[0] = NEW[0];
        Map<String, Object> city = new HashMap<>();
        city.put("lat", (OLD[0].latitude));
        city.put("lon", (OLD[0].longitude));

        db.collection("Rangers").document(String.valueOf(OLD[0].latitude) + "  " + String.valueOf(OLD[0].longitude))
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
                                mMap.addMarker(markerOptions);
                                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(OLD[0], 18.0f));

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

        //firebase new child
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 18.0f));
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
