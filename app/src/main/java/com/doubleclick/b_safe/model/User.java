package com.doubleclick.b_safe.model;

import androidx.annotation.NonNull;

import java.util.Arrays;
import java.util.List;

/**
 * Created By Eslam Ghazy on 6/13/2022
 */
public class User {

    @NonNull
    private String id;
    @NonNull
    private String email;
    @NonNull
    private String name;
    @NonNull
    private String phone;
    @NonNull
    private String token;
    @NonNull
    private String vehicleModel;
    @NonNull
    private String image;
    @NonNull
    private String friendEmail;

    public User() {
        id = "";
        email = "";
        name = "";
        phone = "";
        token = "";
        vehicleModel = "";
        image = "";
        friendEmail = "";
    }


    public String getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

    public String getToken() {
        return token;
    }

    public String getVehicleModel() {
        return vehicleModel;
    }

    public String getImage() {
        return image;
    }

    public List<String>  getFriendEmail() {
        List<String> friend = Arrays.asList(friendEmail.replace("[","").replace("]","").split(" "));
        return friend;
    }
}
