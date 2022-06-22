package com.doubleclick.b_safe.ui.home;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.doubleclick.b_safe.Adapter.ServiceAdapter;
import com.doubleclick.b_safe.JoinUsActivity;
import com.doubleclick.b_safe.R;
import com.doubleclick.b_safe.ViewModel.ServiesCenterViewModel;
import com.doubleclick.b_safe.ViewModel.UserViewModel;
import com.doubleclick.b_safe.databinding.FragmentHomeBinding;
import com.doubleclick.b_safe.model.Data;
import com.doubleclick.b_safe.model.MyResponse;
import com.doubleclick.b_safe.model.Sender;
import com.doubleclick.b_safe.model.ServiceCenter;
import com.doubleclick.b_safe.model.User;
import com.doubleclick.b_safe.notification.Api.APIService;
import com.doubleclick.b_safe.notification.Client;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment implements OnMapReadyCallback {

    private DatabaseReference reference;
    private ServiceAdapter serviceAdapter;
    private RecyclerView centerService;
    private ServiesCenterViewModel serviesCenterViewModel;
    private Button police, Ambulance, fireFighting;
    private EditText search;
    private UserViewModel userViewModel;
    private User user;
    private FusedLocationProviderClient client;
    private GoogleMap map;
    private LocationRequest locationRequest;
    private int count = 0;
    private String uri;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        reference = FirebaseDatabase.getInstance().getReference();
        centerService = view.findViewById(R.id.centerService);
        police = view.findViewById(R.id.police);
        Ambulance = view.findViewById(R.id.Ambulance);
        fireFighting = view.findViewById(R.id.fireFighting);
        search = view.findViewById(R.id.search);
        client = LocationServices.getFusedLocationProviderClient(requireContext());
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        serviesCenterViewModel = new ViewModelProvider(this).get(ServiesCenterViewModel.class);
        serviesCenterViewModel.Search("");
        serviesCenterViewModel.getServiceCenter().observe(getViewLifecycleOwner(), new Observer<ArrayList<ServiceCenter>>() {
            @Override
            public void onChanged(ArrayList<ServiceCenter> serviceCenters) {
                serviceAdapter = new ServiceAdapter(serviceCenters);
                centerService.setAdapter(serviceAdapter);
            }
        });
        police.setOnClickListener(v -> {
            call("01221930858");
        });
        Ambulance.setOnClickListener(v -> {
            call("");
        });
        fireFighting.setOnClickListener(v -> {
            call("");
        });

        userViewModel.getMyUser().observe(getViewLifecycleOwner(), new Observer<User>() {
            @Override
            public void onChanged(User u) {
                user = u;
            }
        });

        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    count++;
                    if (count == 3) {
                        Intent email = new Intent(Intent.ACTION_SEND, Uri.parse("mailto:" + user.getEmail()));
                        String[] list = new String[user.getFriendEmail().size()];
                        for (int i = 0; i < user.getFriendEmail().size(); i++) {
                            list[i] = user.getFriendEmail().get(i);
                        }
                        email.putExtra(Intent.EXTRA_EMAIL, list);
                        email.putExtra(Intent.EXTRA_SUBJECT, "Help");
                        String message = "plaplaplaplapalpalap  " + " Click me \n" + uri;
                        email.putExtra(Intent.EXTRA_TEXT, message);
                        email.setPackage("com.google.android.gm");

                        //need this to prompts email client only
                        email.setType("message/rfc822");
                        startActivity(Intent.createChooser(email, "Choose an Email client :"));

                        count = 0;
                    }
                }

                return false;
            }
        });

        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                serviesCenterViewModel.Search(charSequence.toString());
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        getMyLocation();
        return view;
    }

    private void call(String num) {
        String uri = "tel:" + num.trim();
        Intent intent = new Intent(Intent.ACTION_CALL);
        intent.setData(Uri.parse(uri));
        startActivity(intent);
    }

    private String getMyLocation() {
        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return null;
        }
        Task<Location> task = client.getLastLocation();
        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                    uri = "https://www.google.com/maps/?q=" + latLng.latitude + "," + latLng.longitude;
                } else {
                    Toast.makeText(requireContext(), "Open your location", Toast.LENGTH_SHORT).show();
                }
            }
        });
        return uri;
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        map = googleMap;
        locationRequest = new LocationRequest();
        locationRequest.setInterval(1000);
        locationRequest.setFastestInterval(1000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

    }
}