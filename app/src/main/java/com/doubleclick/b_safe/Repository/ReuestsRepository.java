package com.doubleclick.b_safe.Repository;

import androidx.annotation.NonNull;

import com.doubleclick.b_safe.model.Requests;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * Created By Eslam Ghazy on 6/16/2022
 */
public class ReuestsRepository {

    private ArrayList<Requests> requests = new ArrayList<>();
    private RequestInter requestInter;

    public ReuestsRepository(RequestInter requestInter) {
        this.requestInter = requestInter;
    }

    public void getRequests() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        reference.child("Requstes").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Requests request = dataSnapshot.getValue(Requests.class);
                    requests.add(request);
                }
                requestInter.getRequests(requests);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    public interface RequestInter {
        void getRequests(ArrayList<Requests> requests);
    }

}
