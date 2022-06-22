package com.doubleclick.b_safe.ui.Login.FrgmentsLogin;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.doubleclick.b_safe.MainActivity;
import com.doubleclick.b_safe.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginFragment extends Fragment {

    private TextView signup, forgetpassword;
    private Button signin;
    private TextInputEditText password, email;
    private FirebaseAuth auth;

    public LoginFragment() {
        // Required empty public constructor
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
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        signup = view.findViewById(R.id.signup);
        forgetpassword = view.findViewById(R.id.forgetpassword);
        password = view.findViewById(R.id.password);
        email = view.findViewById(R.id.email);
        signin = view.findViewById(R.id.signin);
        auth = FirebaseAuth.getInstance();
        signup.setOnClickListener(v -> {
            Navigation.findNavController(v).navigate(LoginFragmentDirections.actionLoginFragmentToRegisterFragment());
        });
        forgetpassword.setOnClickListener(v -> {
            Navigation.findNavController(v).navigate(LoginFragmentDirections.actionLoginFragmentToForgetPasswordFragment());
        });
        signin.setOnClickListener(v -> {
            if (Check()) {
                auth.signInWithEmailAndPassword(email.getText().toString(), password.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        startActivity(new Intent(requireContext(), MainActivity.class));
                    }
                });
            }
        });

        return view;
    }


    private boolean Check() {
        if (!password.getText().toString().equals("") && !email.getText().toString().equals("")) {
            return true;
        } else {
            password.setError("check input");
            email.setError("check input");
            return false;
        }
    }
}