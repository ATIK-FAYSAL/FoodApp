package com.atikfaysal.foodapp.fragments;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.atikfaysal.foodapp.R;
import com.atikfaysal.foodapp.backend.SharedPreferencesData;
import com.atikfaysal.foodapp.interfaces.InitialMethods;
import com.atikfaysal.foodapp.main.SignIn;
import com.atikfaysal.foodapp.others.AdditionalMethods;

import java.util.List;


public class More extends Fragment implements InitialMethods,View.OnClickListener
{
    private View view;
    private SharedPreferencesData sharedPreferencesData;
    private AdditionalMethods additionalMethods;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.more, container, false);

        initComponent();//initialize all component

        return view;
    }

    @Override
    public void onClick(View getView) {
        switch (getView.getId())
        {
            case R.id.txtOptSignOut://user sign out from this device
                userSignOut();
                break;
        }
    }

    //initialize all information
    @Override
    public void initComponent() {
        TextView txtSignOut = view.findViewById(R.id.txtOptSignOut);

        sharedPreferencesData = new SharedPreferencesData(getContext());
        additionalMethods = new AdditionalMethods(getContext());

        txtSignOut.setOnClickListener(More.this);
    }


    @Override
    public void setToolbar() {}

    @Override
    public void processJsonData(String json) { }

    @Override
    public List<?> processJsonValue(String json) { return null; }


    //sing out
    private void userSignOut()
    {
        //set progress bar
        final ProgressDialog progressDialog = ProgressDialog.show(getContext(), "Please wait", "User Sign out...", true);
        progressDialog.setCancelable(true);//set progress bar cancel false
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(2500);//sleep for 2.5s
                } catch (Exception e) {
                }
                progressDialog.dismiss();//progress dialog dismiss
            }
        }).start();
        progressDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                sharedPreferencesData.clearData();//remove all initial data of this user
                sharedPreferencesData.userLogingStatus(false);//set log in status as false
                additionalMethods.closeActivity(getActivity(),SignIn.class);//close all activity and go to sign out activity
            }
        });
    }

}
