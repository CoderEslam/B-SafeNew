package com.doubleclick.b_safe.Repository;

import androidx.annotation.NonNull;

import com.doubleclick.b_safe.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * Created By Eslam Ghazy on 6/14/2022
 */
public class UserRepository {


    private UserInter userInter;

    public UserRepository(UserInter userInter) {
        this.userInter = userInter;
    }

    public void getUser() {

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        reference.child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid().toString()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                userInter.myUser(user);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public interface UserInter {
        void myUser(User user);
    }

}
