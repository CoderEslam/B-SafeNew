package com.doubleclick.b_safe.model;

import androidx.annotation.NonNull;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

/**
 * Created By Eslam Ghazy on 6/14/2022
 */
public class ServiceCenter implements Serializable {

    @NonNull
    private String name;
    @NonNull
    private String address;
    private float rate;
    @NonNull
    private String images;
    @NonNull
    private String phone;
    private String ServiceOwner;
    private String location;
    private String id;

    public ServiceCenter() {
        name = "";
        address = "";
        rate = 0.0f;
        images = "";
        phone = "";
    }


    @NonNull
    public String getName() {
        return name;
    }

    @NonNull
    public String getAddress() {
        return address;
    }

    public float getRate() {
        return rate;
    }

    @NonNull
    public List<String> getImages() {
        List<String> image = Arrays.asList(images.replace("[", "").replace("]", "").replace(" ", "").split(","));
        return image;
    }

    public String getOnlyImage() {
        List<String> image = Arrays.asList(images.replace("[", "").replace("]", "").replace(" ", "").split(","));
        return image.get(0);
    }

    @NonNull
    public String getPhone() {
        return phone;
    }

    public String getServiceOwner() {
        return ServiceOwner;
    }

    public List<String> getLocation() {
        List<String> location = Arrays.asList(this.location.replace("[", "").replace("]", "").replace(" ", "").split(","));
        return location;
    }

    public String getId() {
        return id;
    }
}
