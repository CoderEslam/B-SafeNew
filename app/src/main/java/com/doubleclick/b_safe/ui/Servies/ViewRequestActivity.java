package com.doubleclick.b_safe.ui.Servies;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import com.doubleclick.b_safe.Adapter.ImageAdapter;
import com.doubleclick.b_safe.R;
import com.doubleclick.b_safe.model.Requests;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class ViewRequestActivity extends AppCompatActivity implements OnMapReadyCallback {


    private RecyclerView images;
    private GoogleMap mGoogleMap;
    private LatLng loacation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_request);
        images = findViewById(R.id.images);
        Requests request = (Requests) getIntent().getSerializableExtra("request");
        images.setAdapter(new ImageAdapter("", request.getImages()));
        setUpLocation(new LatLng(Double.parseDouble(request.getLocation().get(0)), Double.parseDouble(request.getLocation().get(1))));
        SupportMapFragment supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        supportMapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(@NonNull GoogleMap googleMap) {
                mGoogleMap = googleMap;
                googleMap.getUiSettings().setMapToolbarEnabled(true);
                googleMap.getUiSettings().setMyLocationButtonEnabled(true);
                if (mGoogleMap != null) {
                    UpdateMapContent();
                }
            }
        });

    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mGoogleMap = googleMap;
        MapsInitializer.initialize(this);
        googleMap.getUiSettings().setMapToolbarEnabled(true);
        googleMap.getUiSettings().setMyLocationButtonEnabled(true);
        if (mGoogleMap != null) {
            UpdateMapContent();
        }
    }

    public void setUpLocation(LatLng latLng) {
        loacation = latLng;
        if (mGoogleMap != null) {
            UpdateMapContent();
        }
    }

    private void UpdateMapContent() {
        mGoogleMap.clear();
        mGoogleMap.addMarker(new MarkerOptions().position(loacation).title("Place of Center Service"));
        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(loacation, 12f));
    }
}