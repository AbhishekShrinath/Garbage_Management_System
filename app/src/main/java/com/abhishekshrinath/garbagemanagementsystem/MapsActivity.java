package com.abhishekshrinath.garbagemanagementsystem;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private static final int REQUEST_LOCATION_PERMISSION = 1;
    private GoogleMap mMap;
    private TextView lat_txt, long_txt, id;
    Location currentlocation;
    FusedLocationProviderClient fusedLocationProviderClient;
    private final static int REQUEST_CODE = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        featchlastlocation();

        lat_txt = findViewById(R.id.latitude);
        long_txt = findViewById(R.id.longitude);
        id = findViewById(R.id.Binid1);

        String get_id = getIntent().getExtras().getString("id");


        id.setText(get_id);

    }

    private void featchlastlocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]
                    {Manifest.permission.ACCESS_FINE_LOCATION},REQUEST_LOCATION_PERMISSION);
            return;
        }
        Task<Location> task = fusedLocationProviderClient.getLastLocation();
        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if(location!=null)
                {
                    currentlocation=location;
                    Toast.makeText(MapsActivity.this, currentlocation.getLatitude()+" : "+currentlocation.getLongitude(), Toast.LENGTH_SHORT).show();
                    // Obtain the SupportMapFragment and get notified when the map is ready to be used.
                    SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                            .findFragmentById(R.id.map);
                    mapFragment.getMapAsync(MapsActivity.this);
                }
            }
        });
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
        enableMyLocation();
        LatLng latLng=new LatLng(currentlocation.getLatitude(),currentlocation.getLongitude());
        MarkerOptions markerOptions=new MarkerOptions().position(latLng).title("My Location");
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,100));
        mMap.addMarker(markerOptions);

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {

                String lat=String.valueOf(latLng.latitude);
                String laong=String.valueOf(latLng.longitude);
                MarkerOptions markerOptions=new MarkerOptions();
                enableMyLocation();
                markerOptions.position(latLng);
                markerOptions.title(latLng.latitude+":"+latLng.longitude);
                mMap.clear();
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,100));
                mMap.addMarker(markerOptions);
                lat_txt.setText(lat);
                long_txt.setText(laong);
            }
        });
        // Add a marker in Sydney and move the camera
//        LatLng sydney = new LatLng(-34, 151);
//        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }

    public void setlocation(View view) {
        DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference("Create Bin");
        databaseReference.orderByChild("id").equalTo(id.getText().toString()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                    DataSnapshot nodeDataSnapshot = snapshot.getChildren().iterator().next();
                    String key = nodeDataSnapshot.getKey(); // this key is `K1NRz9l5PU_0CFDtgXz`
                    HashMap<String, Object> result = new HashMap<>();
                    result.put("latitude", lat_txt.getText().toString());
                   result.put("longtiude", long_txt.getText().toString());
                   databaseReference.child(key).updateChildren(result);
                    Toast.makeText(MapsActivity.this, "Map Updated", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(MapsActivity.this, Update_Bin.class);
                    startActivity(intent);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        Toast.makeText(this, "Working", Toast.LENGTH_SHORT).show();
    }
    private void enableMyLocation() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
        } else {
            ActivityCompat.requestPermissions(this, new String[]
                            {Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_LOCATION_PERMISSION);
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode,@NonNull String[] permissions, @NonNull int[] grantResults)
    {
        // Check if location permissions are granted and if so enable the
        // location data layer.
        switch (requestCode) {
            case REQUEST_LOCATION_PERMISSION:
                if (grantResults.length > 0 && grantResults[0]== PackageManager.PERMISSION_GRANTED)
                {
                    featchlastlocation();
                }
                break;
        }
    }

}