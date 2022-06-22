package com.doubleclick.b_safe.Repository;

import androidx.annotation.NonNull;

import com.doubleclick.b_safe.model.ServiceCenter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * Created By Eslam Ghazy on 6/15/2022
 */
public class ServiesCenterRepository {

    ArrayList<ServiceCenter> centers = new ArrayList<>();
    private getCenters getCenters;

    public ServiesCenterRepository(getCenters getCenters) {
        this.getCenters = getCenters;
    }

    public void getServiceCenter(String name) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("CenterServies");
        reference.orderByChild("name").startAt(name).endAt(name + "\uf8ff").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                centers.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    ServiceCenter center = dataSnapshot.getValue(ServiceCenter.class);
                    centers.add(center);
                }
                getCenters.getServiesCenter(centers);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public interface getCenters {
        void getServiesCenter(ArrayList<ServiceCenter> serviceCenters);
    }

}
