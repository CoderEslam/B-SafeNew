package com.doubleclick.b_safe.ui.Login.FrgmentsLogin;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.doubleclick.b_safe.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RegisterFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RegisterFragment extends Fragment {


    private TextView signin;
    private EditText name, password, email, confiremPassword, phone, vehicleModel,friendEmail;
    private FirebaseAuth auth;
    private DatabaseReference reference;

    public RegisterFragment() {
        // Required empty public constructor
    }


    public static RegisterFragment newInstance(String param1, String param2) {
        RegisterFragment fragment = new RegisterFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_register, container, false);
        signin = view.findViewById(R.id.signin);
        name = view.findViewById(R.id.name);
        password = view.findViewById(R.id.password);
        email = view.findViewById(R.id.email);
        confiremPassword = view.findViewById(R.id.confiremPassword);
        phone = view.findViewById(R.id.phone);
        friendEmail = view.findViewById(R.id.friendEmail);
        vehicleModel = view.findViewById(R.id.vehicleModel);
        auth = FirebaseAuth.getInstance();
        reference = FirebaseDatabase.getInstance().getReference().child("Users");
        view.findViewById(R.id.signUp).setOnClickListener(v -> {

            if (Check()) {
                auth.createUserWithEmailAndPassword(email.getText().toString(), password.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        String id = auth.getCurrentUser().getUid().toString();
                        HashMap<String, Object> map = new HashMap<>();
                        map.put("name", name.getText().toString());
                        map.put("email", email.getText().toString());
                        map.put("phone", phone.getText().toString());
                        map.put("vehicleModel", vehicleModel.getText().toString());
                        map.put("password", password.getText().toString());
                        map.put("friendEmail",friendEmail.getText().toString());
                        map.put("id", id);
                        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(new OnCompleteListener<String>() {
                            @Override
                            public void onComplete(@NonNull Task<String> task) {
                                String token = task.getResult().toString();
                                map.put("token", token);
                                reference.child(id).setValue(map);
                            }
                        });
                    }
                });
            }

        });
        signin.setOnClickListener(v -> {

            Navigation.findNavController(v).navigate(RegisterFragmentDirections.actionRegisterFragmentToLoginFragment());

        });

        return view;
    }

    private boolean Check() {
        if (!name.getText().toString().equals("") && !password.getText().toString().equals("") && !confiremPassword.getText().toString().equals("") && !email.getText().toString().equals("") && !phone.getText().toString().equals("") && !vehicleModel.getText().toString().equals("")) {
            return true;
        } else {
            name.setError("check input");
            password.setError("check input");
            confiremPassword.setError("check input");
            email.setError("check input");
            phone.setError("check input");
            vehicleModel.setError("check input");
            return false;
        }

    }
}