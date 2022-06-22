package com.doubleclick.b_safe.ui.notification;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.doubleclick.b_safe.Adapter.RequestAdapter;
import com.doubleclick.b_safe.R;
import com.doubleclick.b_safe.ViewModel.RequestViewModel;
import com.doubleclick.b_safe.ViewModel.UserViewModel;
import com.doubleclick.b_safe.model.Requests;
import com.doubleclick.b_safe.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;

public class NotificatiionFragment extends Fragment implements RequestAdapter.Check {

    private RecyclerView requestsRecycler;
    private RequestViewModel requestViewModel;
    private RequestAdapter requestAdapter;
    private DatabaseReference reference;
    private UserViewModel userViewModel;
    private User user;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notificatiion, container, false);
        requestsRecycler = view.findViewById(R.id.requests);
        requestAdapter = new RequestAdapter();
        requestAdapter.setCheck(this);
        reference = FirebaseDatabase.getInstance().getReference();
        requestViewModel = new ViewModelProvider(this).get(RequestViewModel.class);
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        requestViewModel.getRequest().observe(getViewLifecycleOwner(), new Observer<ArrayList<Requests>>() {
            @Override
            public void onChanged(ArrayList<Requests> requests) {
                requestAdapter.setRequestsArrayList(requests);
                requestsRecycler.setAdapter(requestAdapter);
            }
        });

        userViewModel.getMyUser().observe(getViewLifecycleOwner(), new Observer<User>() {
            @Override
            public void onChanged(User u) {
                user = u;
            }
        });

        return view;
    }


    @Override
    public void Yes(String id, String custumerId, String email) {
        HashMap<String, Object> map = new HashMap<>();
        map.put(custumerId, true);
        reference.child("CenterServiecAcceptrd").setValue(map);
        reference.child("Requstes").child(id).removeValue();
        Accept(email);
    }

    private void Accept(String email) {
        Intent intent = new Intent(Intent.ACTION_SEND, Uri.parse("mailto:" + user.getEmail()));
        intent.putExtra(Intent.EXTRA_EMAIL, new String[]{email});
        intent.putExtra(Intent.EXTRA_SUBJECT, "B-Safe");
        String message = "you are Accepted";
        intent.putExtra(Intent.EXTRA_TEXT, message);
        intent.setPackage("com.google.android.gm");
        //need this to prompts email client only
        intent.setType("message/rfc822");
        startActivity(Intent.createChooser(intent, "Choose an Email client :"));

    }

    @Override
    public void No(String id, String email) {
        reference.child("Requstes").child(id).removeValue();
        Reject(email);
    }

    private void Reject(String email) {
        Intent intent = new Intent(Intent.ACTION_SEND, Uri.parse("mailto:" + user.getEmail()));
        intent.putExtra(Intent.EXTRA_EMAIL, new String[]{email});
        intent.putExtra(Intent.EXTRA_SUBJECT, "B-safe");
        String message = "you are Rejected";
        intent.putExtra(Intent.EXTRA_TEXT, message);
        intent.setPackage("com.google.android.gm");
        //need this to prompts email client only
        intent.setType("message/rfc822");
        startActivity(Intent.createChooser(intent, "Choose an Email client :"));

    }

}