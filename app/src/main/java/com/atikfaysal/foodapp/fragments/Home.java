package com.atikfaysal.foodapp.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ViewFlipper;

import com.atikfaysal.foodapp.R;
import com.atikfaysal.foodapp.adapter.RestaurantsAdapter;
import com.atikfaysal.foodapp.interfaces.InitialMethods;
import com.atikfaysal.foodapp.model.RestaurantModel;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class Home extends Fragment implements InitialMethods
{

    private ViewFlipper viewOfferFlipper,viewReviewFlipper;
    private View view;
    private RecyclerView recyclerView;
    private LinearLayoutManager layoutManager;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view =  inflater.inflate(R.layout.home, container, false);
        initComponent();
        viewInformation();
        return view;
    }

    @Override
    public void initComponent() {
        viewOfferFlipper = view.findViewById(R.id.offerViewFlipper);
        viewReviewFlipper = view.findViewById(R.id.reviewViewFlipper);

        recyclerView = view.findViewById(R.id.list);
        layoutManager = new LinearLayoutManager(getContext());

        List<String> imagePath = new ArrayList<>();
        imagePath.add("http://192.168.56.1/foodApp/images/a.jpg");
        imagePath.add("http://192.168.56.1/foodApp/images/b.jpg");
        imagePath.add("http://192.168.56.1/foodApp/images/c.jpg");
        imagePath.add("http://192.168.56.1/foodApp/images/d.jpg");
        imagePath.add("http://192.168.56.1/foodApp/images/e.jpg");
        imagePath.add("http://192.168.56.1/foodApp/images/f.jpg");

        for(String image : imagePath)
        {
            flipperOfferImages(image);
            flipperReviewImages(image);
        }
    }

    @Override
    public void setToolbar() {

    }


    private void flipperOfferImages(String image)
    {
        ImageView imageView = new ImageView(getContext());
        Glide.with(Objects.requireNonNull(getContext())).
                load(image).
                into(imageView);
        viewOfferFlipper.addView(imageView);
        viewOfferFlipper.setFlipInterval(1500);
        viewOfferFlipper.setAutoStart(true);
        viewOfferFlipper.setInAnimation(getContext(),android.R.anim.slide_in_left);
        viewOfferFlipper.setInAnimation(getContext(),android.R.anim.slide_out_right);
    }


    private void flipperReviewImages(String image)
    {
        ImageView imageView = new ImageView(getContext());
        Glide.with(Objects.requireNonNull(getContext())).
                load(image).
                into(imageView);
        viewReviewFlipper.addView(imageView);
        viewReviewFlipper.setFlipInterval(1500);
        viewReviewFlipper.setAutoStart(true);
        viewReviewFlipper.setInAnimation(getContext(),android.R.anim.slide_in_left);
        viewReviewFlipper.setInAnimation(getContext(),android.R.anim.slide_out_right);
    }

    private void viewInformation()
    {

        List<RestaurantModel> models = new ArrayList<>();
        models.add(new RestaurantModel("Star kabab","Dhanmondi 2 no road, dhaka","Kabab & Restaurent",5));
        models.add(new RestaurantModel("Star kabab","Dhanmondi 2 no road, dhaka","Kabab & Restaurent",5));
        models.add(new RestaurantModel("Star kabab","Dhanmondi 2 no road, dhaka","Kabab & Restaurent",5));
        models.add(new RestaurantModel("Star kabab","Dhanmondi 2 no road, dhaka","Kabab & Restaurent",5));
        models.add(new RestaurantModel("Star kabab","Dhanmondi 2 no road, dhaka","Kabab & Restaurent",5));
        models.add(new RestaurantModel("Star kabab","Dhanmondi 2 no road, dhaka","Kabab & Restaurent",5));
        models.add(new RestaurantModel("Star kabab","Dhanmondi 2 no road, dhaka","Kabab & Restaurent",5));
        models.add(new RestaurantModel("Star kabab","Dhanmondi 2 no road, dhaka","Kabab & Restaurent",5));
        models.add(new RestaurantModel("Star kabab","Dhanmondi 2 no road, dhaka","Kabab & Restaurent",5));
        models.add(new RestaurantModel("Star kabab","Dhanmondi 2 no road, dhaka","Kabab & Restaurent",5));
        models.add(new RestaurantModel("Star kabab","Dhanmondi 2 no road, dhaka","Kabab & Restaurent",5));
        models.add(new RestaurantModel("Star kabab","Dhanmondi 2 no road, dhaka","Kabab & Restaurent",5));
        models.add(new RestaurantModel("Star kabab","Dhanmondi 2 no road, dhaka","Kabab & Restaurent",5));

        RestaurantsAdapter adapter = new RestaurantsAdapter(getContext(), models);//create adapter
        recyclerView.setAdapter(adapter);//set adapter in recyler view
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
    }

    @Override
    public void processJsonData(String json) {

    }

    @Override
    public List<?> processJsonValue(String json) {
        return null;
    }
}
