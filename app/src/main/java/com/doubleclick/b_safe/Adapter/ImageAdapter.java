package com.doubleclick.b_safe.Adapter;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.doubleclick.b_safe.R;
import com.doubleclick.b_safe.Views.PhotoView.PhotoView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created By Eslam Ghazy on 6/14/2022
 */
public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ImageViewHolder> {

    ArrayList<Uri> uriArrayList = new ArrayList<>();
    List<String> images = new ArrayList<>();
    String type;

    public ImageAdapter(ArrayList<Uri> uriArrayList, String type) {
        this.uriArrayList = uriArrayList;
        this.type = type;
    }

    public ImageAdapter(String type, List<String> images) {
        this.images = images;
        this.type = type;
    }

    @NonNull
    @Override
    public ImageAdapter.ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ImageViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.image, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ImageAdapter.ImageViewHolder holder, int position) {
        if (type.equals("uri")) {
            Glide.with(holder.itemView.getContext()).load(uriArrayList.get(position)).into(holder.image);
        } else {
            Glide.with(holder.itemView.getContext()).load(images.get(position)).into(holder.image);
        }
    }

    @Override
    public int getItemCount() {
        if (type.equals("uri")) {
            return uriArrayList.size();
        } else {
            return images.size();
        }
    }

    public class ImageViewHolder extends RecyclerView.ViewHolder {
        private PhotoView image;

        public ImageViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.image);
        }
    }
}
