package com.abhishekshrinath.garbagemanagementsystem;

import androidx.fragment.app.FragmentActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class View_Map extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private Double sd,md;
    private TextView txt1,txt2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_map);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map1);
        mapFragment.getMapAsync(this);

        txt1=findViewById(R.id.txt22);
        txt2=findViewById(R.id.txt23);

        String get_latitude = getIntent().getExtras().getString("lat");
        String get_longitude = getIntent().getExtras().getString("long");
        txt1.setText(get_latitude);
        txt2.setText(get_longitude);

        sd= Double.valueOf(txt1.getText().toString());
        md= Double.valueOf(txt2.getText().toString());
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        LatLng latLng=new LatLng(sd,md);
        MarkerOptions markerOptions=new MarkerOptions().position(latLng).title("Location");
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,50));
        mMap.addMarker(markerOptions);

//        // Add a marker in Sydney and move the camera
//        LatLng sydney = new LatLng(sd,md);
//        mMap.addMarker(new MarkerOptions().position(sydney).title("location"));
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }
}