package com.doubleclick.b_safe.ui.Login.FrgmentsLogin;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.doubleclick.b_safe.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.UserProfileChangeRequest;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ForgetPasswordFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ForgetPasswordFragment extends Fragment {

    private EditText forgeremail;
    private Button next;

    public ForgetPasswordFragment() {
        // Required empty public constructor
    }


    public static ForgetPasswordFragment newInstance(String param1, String param2) {
        ForgetPasswordFragment fragment = new ForgetPasswordFragment();
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
        View view = inflater.inflate(R.layout.fragment_forget_password, container, false);
        forgeremail = view.findViewById(R.id.forgeremail);
        next = view.findViewById(R.id.next);
        next.setOnClickListener(v -> {
            if (!forgeremail.getText().toString().equals("")) {
                Log.e("EMIALLLLL", forgeremail.getText().toString());
                FirebaseAuth.getInstance().sendPasswordResetEmail(forgeremail.getText().toString().trim()).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(requireContext(), "check your email", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(requireContext(), "Error = " + task.getException(), Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });
        return view;
    }
}