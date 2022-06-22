package com.doubleclick.b_safe;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import com.doubleclick.b_safe.Adapter.ImageAdapter;
import com.doubleclick.b_safe.ViewModel.UserViewModel;
import com.doubleclick.b_safe.model.Data;
import com.doubleclick.b_safe.model.MyResponse;
import com.doubleclick.b_safe.model.Sender;
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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class JoinUsActivity extends AppCompatActivity implements OnMapReadyCallback {


    private RecyclerView images;
    private FloatingActionButton pickup;
    private Button getLocation;
    private ArrayList<Uri> uriArrayList = new ArrayList<>();
    private GoogleMap map;
    private LocationRequest locationRequest;
    private DatabaseReference reference;
    private ArrayList<String> urlImages = new ArrayList<>();
    private FusedLocationProviderClient client;
    private APIService apiService;
    private UserViewModel userViewModel;
    private User user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_us);
        images = findViewById(R.id.images);
        pickup = findViewById(R.id.pickup);
        getLocation = findViewById(R.id.location);
        client = LocationServices.getFusedLocationProviderClient(this);
        reference = FirebaseDatabase.getInstance().getReference();
        apiService = Client.getClient("https://fcm.googleapis.com/").create(APIService.class);
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        userViewModel.getMyUser().observe(this, new Observer<User>() {
            @Override
            public void onChanged(User u) {
                user = u;
            }
        });

        pickup.setOnClickListener(v -> {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
            startActivityForResult(intent, 100);
        });

        getLocation.setOnClickListener(view -> {
            getMyLocation();
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == Activity.RESULT_OK && data != null) {
            ClipData clipData = data.getClipData();
            if (clipData != null) {
                for (int i = 0; i < clipData.getItemCount(); i++) {
                    uriArrayList.add(clipData.getItemAt(i).getUri());
                }
                images.setAdapter(new ImageAdapter(uriArrayList, "uri"));
            }
        }

    }


    private void getMyLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Task<Location> task = client.getLastLocation();
        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                    Upload(latLng);
                    sendNotifiaction("ssBSL1ScVeVSNmNtjzrrT29uDoT2");
                    Log.e("LOCATIONJOINUS", latLng.toString());
                } else {
                    Toast.makeText(JoinUsActivity.this, "Open your location", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void Upload(LatLng latLng) {
        if (uriArrayList.size() != 0) {
            ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.show();
            StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("Requests");
            for (Uri uri : uriArrayList) {
                storageReference.child("" + System.currentTimeMillis() + ".jpg").putFile(uri).addOnSuccessListener(taskSnapshot -> {
                    Task<Uri> task = taskSnapshot.getStorage().getDownloadUrl();
                    task.addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task) {
                            if (task.isSuccessful()) {
                                Log.e("URLLLLLL", task.getResult().toString());
                                urlImages.add(task.getResult().toString());
                                if (uriArrayList.size() == urlImages.size()) {
                                    String id = reference.push().getKey().toString();
                                    HashMap<String, Object> map = new HashMap<>();
                                    map.put("images", urlImages.toString());
                                    map.put("location", "[" + latLng.latitude + "," + latLng.longitude + "]");
                                    map.put("id", id);
                                    map.put("CustumerId", user.getId());
                                    map.put("email", user.getEmail());
                                    reference.child("Requstes").child(id).setValue(map);
                                    progressDialog.dismiss();
                                }
                            }
                        }
                    });

                }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                        double p = 100.0 * snapshot.getBytesTransferred() / snapshot.getTotalByteCount();
                        progressDialog.setMessage(p + " % Uploading...");
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(JoinUsActivity.this, e.getMessage().toString(), Toast.LENGTH_LONG).show();
                        progressDialog.dismiss();
                    }
                });
            }


        }

    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        map = googleMap;
        locationRequest = new LocationRequest();
        locationRequest.setInterval(1000);
        locationRequest.setFastestInterval(1000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    public void sendNotifiaction(String receiver) {
        reference.child("Users").child(receiver).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                DataSnapshot dataSnapshot = task.getResult();
                User user = dataSnapshot.getValue(User.class);
                Data data = new Data(FirebaseAuth.getInstance().getCurrentUser().getUid().toString(), R.drawable.email, "Hello", "B-Safe", receiver);
                Sender sender = new Sender(data, user.getToken());
                apiService.sendNotification(sender)
                        .enqueue(new Callback<MyResponse>() {
                            @Override
                            public void onResponse(Call<MyResponse> call, Response<MyResponse> response) {
                                if (response.code() == 200) {
                                    if (response.body().success != 1) {
                                        Toast.makeText(JoinUsActivity.this, "Failed!", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }

                            @Override
                            public void onFailure(Call<MyResponse> call, Throwable t) {

                            }
                        });
            }

        });
    }

}