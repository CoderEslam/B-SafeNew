package com.doubleclick.b_safe.ui.profile;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.doubleclick.b_safe.JoinUsActivity;
import com.doubleclick.b_safe.R;
import com.doubleclick.b_safe.ViewModel.UserViewModel;
import com.doubleclick.b_safe.model.ServiceCenter;
import com.doubleclick.b_safe.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileFragment extends Fragment {


    private TextView joinUs;
    private CircleImageView imageProfile;
    private TextInputEditText name, phone, email, vehicleModel, friendEmail;
    private Button save;
    private UserViewModel userViewModel;
    private FloatingActionButton changeImage;

    private DatabaseReference reference;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        reference = FirebaseDatabase.getInstance().getReference();
        joinUs = view.findViewById(R.id.joinUs);
        imageProfile = view.findViewById(R.id.image);
        name = view.findViewById(R.id.name);
        phone = view.findViewById(R.id.phone);
        email = view.findViewById(R.id.email);
        friendEmail = view.findViewById(R.id.friendEmail);
        vehicleModel = view.findViewById(R.id.vehicleModel);
        save = view.findViewById(R.id.save);
        changeImage = view.findViewById(R.id.changeImage);
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        userViewModel.getMyUser().observe(getViewLifecycleOwner(), new Observer<User>() {
            @Override
            public void onChanged(User user) {
                name.setText(user.getName());
                phone.setText(user.getPhone());
                email.setText(user.getEmail());
                vehicleModel.setText(user.getVehicleModel());
                friendEmail.setText("" + user.getFriendEmail().toString().replace("[", "").replace("]", ""));
                Glide.with(requireContext()).load(user.getImage()).into(imageProfile);
            }
        });

        changeImage.setOnClickListener(v -> {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "Select Picture"), 100);
        });

        joinUs.setOnClickListener(v -> {
            startActivity(new Intent(requireContext(), JoinUsActivity.class));
        });

        save.setOnClickListener(v -> {
            Map<String, Object> map = new HashMap<>();
            if (!name.getText().toString().equals("")) {
                map.put("name", name.getText().toString());
            }
            if (!phone.getText().toString().equals("")) {
                map.put("phone", phone.getText().toString());
            }
            if (!vehicleModel.getText().toString().equals("")) {
                map.put("vehicleModel", vehicleModel.getText().toString());
            }
            if (!friendEmail.getText().toString().equals("")) {
                map.put("friendEmail", friendEmail.getText().toString());
            }
            reference.child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid().toString()).updateChildren(map);
        });


        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == Activity.RESULT_OK && data != null) {
            Upload(data.getData());
        }
    }

    private void Upload(Uri data) {
        ProgressDialog progressDialog = new ProgressDialog(requireContext());
        progressDialog.show();
        StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("ImagesProfiles");
        storageReference.child(FirebaseAuth.getInstance().getCurrentUser().getUid().toString() + ".jpg").putFile(data).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                uriTask.addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        HashMap<String, Object> map = new HashMap<>();
                        map.put("image", task.getResult().toString());
                        reference.child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid().toString()).updateChildren(map);
                        Toast.makeText(requireContext(), "Image Uploaded", Toast.LENGTH_LONG).show();
                        progressDialog.dismiss();
                    }
                });
            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                double p = 100.0 * snapshot.getBytesTransferred() / snapshot.getTotalByteCount();
                progressDialog.setMessage(p + " % Uploading...");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(requireContext(), e.getMessage().toString(), Toast.LENGTH_LONG).show();
                progressDialog.dismiss();
            }
        });

    }
}
