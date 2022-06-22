package com.doubleclick.b_safe;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;

import com.doubleclick.b_safe.Adapter.ImageAdapter;
import com.doubleclick.b_safe.model.Rate;
import com.doubleclick.b_safe.model.ServiceCenter;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class ServiceCenterActivity extends AppCompatActivity implements OnMapReadyCallback {


    private ServiceCenter serviceCenter;
    private RecyclerView images;
    private TextView name, phone, address, rating;
    private GoogleMap mGoogleMap;
    private LatLng location;
    private RatingBar rate;
    private DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_center);
        serviceCenter = (ServiceCenter) getIntent().getSerializableExtra("serviceCenter");
        images = findViewById(R.id.images);
        name = findViewById(R.id.name);
        phone = findViewById(R.id.phone);
        address = findViewById(R.id.address);
        rate = findViewById(R.id.rate);
        rating = findViewById(R.id.rating);
        reference = FirebaseDatabase.getInstance().getReference();
        setUpLocation(new LatLng(Double.parseDouble(serviceCenter.getLocation().get(0)), Double.parseDouble(serviceCenter.getLocation().get(1))));
        images.setAdapter(new ImageAdapter("", serviceCenter.getImages()));
        name.setText(serviceCenter.getName());
        phone.setText(serviceCenter.getPhone());
        address.setText(serviceCenter.getAddress());
        SupportMapFragment supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        supportMapFragment.getMapAsync(this);
        rate.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                if (v != 0) {
                    HashMap<String, Object> map = new HashMap<>();
                    map.put("rate", v);
                    map.put("idCustomer", FirebaseAuth.getInstance().getCurrentUser().getUid().toString());
                    reference.child("Rate").child(serviceCenter.getId()).child(FirebaseAuth.getInstance().getCurrentUser().getUid().toString()).setValue(map);
                } else {
                    reference.child("Rate").child(serviceCenter.getId()).child(FirebaseAuth.getInstance().getCurrentUser().getUid().toString()).removeValue();
                }
            }
        });
        reference.child("Rate").child(serviceCenter.getId()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                try {
                    rate.setRating(snapshot.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).getValue(Rate.class).getRate());
                    rating.setText("" + snapshot.getChildrenCount() + "Person Rated");
                } catch (NullPointerException e) {

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

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
        location = latLng;
        if (mGoogleMap != null) {
            UpdateMapContent();
        }
    }

    private void UpdateMapContent() {
        mGoogleMap.clear();
        mGoogleMap.addMarker(new MarkerOptions().position(location).title("Place of Center Service"));
        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 12f));
    }
}