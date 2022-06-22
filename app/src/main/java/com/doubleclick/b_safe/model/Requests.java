package com.doubleclick.b_safe.model;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

/**
 * Created By Eslam Ghazy on 6/16/2022
 */
public class Requests implements Serializable {

    private String images;
    private String location;
    private String id;
    private String CustumerId;
    private String email;


    public List<String> getImages() {
        List<String> image = Arrays.asList(images.replace("[", "").replace("]", "").replace(" ", "").split(","));
        return image;
    }

    public List<String> getLocation() {
        List<String> location = Arrays.asList(this.location.replace("[", "").replace("]", "").replace(" ", "").split(","));
        return location;
    }

    @Override
    public String toString() {
        return "Requests{" +
                "images='" + images + '\'' +
                ", location='" + location + '\'' +
                '}';
    }

    public String getId() {
        return id;
    }

    public String getCustumerId() {
        return CustumerId;
    }

    public String getEmail() {
        return email;
    }
}
