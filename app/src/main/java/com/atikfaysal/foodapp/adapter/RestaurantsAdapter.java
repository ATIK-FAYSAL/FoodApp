package com.atikfaysal.foodapp.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import com.atikfaysal.foodapp.R;
import com.atikfaysal.foodapp.model.RestaurantModel;
import com.bumptech.glide.Glide;


import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class RestaurantsAdapter extends RecyclerView.Adapter<RestaurantsAdapter.ViewHolder>
{

    private List<RestaurantModel> restaurantModels;
    private Context context;
    private LayoutInflater inflater;

    public RestaurantsAdapter(Context context,List<RestaurantModel> models)
    {
        this.context = context;
        this.restaurantModels = models;
        inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = inflater.inflate(R.layout.model_restaurant,viewGroup,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        RestaurantModel model = restaurantModels.get(position);

        viewHolder.setData(model,position);
        viewHolder.setListener();
    }

    @Override
    public int getItemCount() {
        return restaurantModels.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder
    {
        private RestaurantModel model;
        private int position;
        private TextView txtRestName,txtRestAddress,txtRestType;
        private RatingBar ratingBar;

        private CircleImageView imageView;

        //constructor
        ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtRestName = itemView.findViewById(R.id.txtRestName);
            txtRestAddress = itemView.findViewById(R.id.txtRestAddress);
            txtRestType = itemView.findViewById(R.id.txtRestType);
            ratingBar = itemView.findViewById(R.id.ratingBar);
            imageView = itemView.findViewById(R.id.imgRestaurant);
        }

        //set restaurant information
        private void setData(RestaurantModel model,int pos)
        {
            txtRestName.setText(model.getName());
            txtRestAddress.setText(model.getAddress());
            txtRestType.setText(model.getType());
            ratingBar.setNumStars(model.getRating());

            Glide.with(context).
                    load("http://192.168.56.1/foodApp/images/a.jpg").
                    into(imageView);

        }
        private void setListener()
        {

        }

    }
}
