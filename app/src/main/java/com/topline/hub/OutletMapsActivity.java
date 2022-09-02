package com.topline.hub;

import androidx.fragment.app.FragmentActivity;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class OutletMapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private Double latitude,longitude;
    private String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_outlet_maps);
        latitude = getIntent().getDoubleExtra("LATITUDE",0);
        longitude = getIntent().getDoubleExtra("LONGITUDE",0);
        name = getIntent().getStringExtra("NAME");
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

        // Add a marker in Sydney and move the camera
        LatLng sydney = null;
        if(latitude == 0.0 || longitude == 0.0){
            sydney = new LatLng(-1.2921, 36.8219);
        }else {
            sydney = new LatLng(latitude, longitude);
        }
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker of Outlet: " + name));
        float zoom = 10;
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, zoom));
    }
}
