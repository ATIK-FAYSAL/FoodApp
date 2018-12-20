package com.atikfaysal.foodapp.others;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.WindowManager;
import android.widget.Toast;

import com.atikfaysal.foodapp.backend.CheckInternetConnection;
import com.gdacciaro.iOSDialog.iOSDialog;
import com.gdacciaro.iOSDialog.iOSDialogBuilder;
import com.gdacciaro.iOSDialog.iOSDialogClickListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Objects;

@SuppressLint("Registered")
public class AdditionalMethods extends AlertDialog
{
    private Context context;
    private Activity activity;
    private CheckInternetConnection internetConnection;
    private boolean flag;
    private String newPass;
    private AlertDialog alertDialog;


    public AdditionalMethods(Context context)
    {
        super(context);
        this.context = context;
        this.activity = (Activity)context;
    }

    //get current time and date
    @SuppressLint("SimpleDateFormat")
    public String getDateWithTime()
    {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy.MMM.dd hh:mm aaa");
        return dateFormat.format(calendar.getTime());
    }

    //get current time and date
    @SuppressLint("SimpleDateFormat")
    public String getDate()
    {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return dateFormat.format(calendar.getTime());
    }

    //close top all activity and go to specific activity
    public void closeActivity(Activity context, Class<?> clazz) {
        Intent intent = new Intent(context, clazz);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(intent);
        context.finish();
    }

    //refresh current page
    public void reloadPage(final SwipeRefreshLayout refreshLayout, final Class<?>nameOfClass)
    {
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshLayout.setRefreshing(true);

                (new Handler()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        refreshLayout.setRefreshing(false);
                        context.startActivity(new Intent(context,nameOfClass));
                        activity.finish();
                    }
                },2500);
            }
        });
    }


    //if any execution failed then show this message
    public void errorMessage(String message)
    {
        Objects.requireNonNull(getWindow()).setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        iOSDialogBuilder builder = new iOSDialogBuilder(context);

        builder.setTitle("Attention")//set Title
                .setSubtitle(message)//set message
                .setBoldPositiveLabel(true)
                .setCancelable(false)
                .setPositiveListener("ok",new iOSDialogClickListener() {
                    @Override
                    public void onClick(iOSDialog dialog) {//set positive button
                        dialog.dismiss();
                    }
                }).build().show();
    }

    public void progressDialog(String message1, String message2, final Class<?>className)
    {
        final ProgressDialog ringProgressDialog = ProgressDialog.show(context, message1,message2, true);
        ringProgressDialog.setCancelable(true);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(2500);
                } catch (Exception e) {
                }
                ringProgressDialog.dismiss();
            }
        }).start();
        ringProgressDialog.setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                Toast.makeText(context,"Information's update successfully.",Toast.LENGTH_LONG).show();
                closeActivity(activity,className);
            }
        });
    }

//    public void onResultSuccess(BooleanResponse response)
//    {
//        this.booleanResponse = response;
//    }
//
//    //show alert box with confirmation
//    public void warning(String text)
//    {
//        Objects.requireNonNull(getWindow()).setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
//        iOSDialogBuilder builder = new iOSDialogBuilder(context);
//
//        builder.setTitle("Warning!!!")
//                .setSubtitle(text)
//                .setBoldPositiveLabel(true)
//                .setCancelable(false)
//                .setPositiveListener("Yes",new iOSDialogClickListener() {
//                    @Override
//                    public void onClick(iOSDialog dialog) {
//                        booleanResponse.onCompleteResult(true);
//                        dialog.dismiss();
//
//                    }
//                })
//                .setNegativeListener("No", new iOSDialogClickListener() {
//                    @Override
//                    public void onClick(iOSDialog dialog) {
//                        booleanResponse.onCompleteResult(false);
//                        dialog.dismiss();
//                    }
//                })
//                .build().show();
//    }
//
//    public void congratesMessage(String text)
//    {
//        AlertDialog.Builder builder;
//        final AlertDialog alertDialog;
//        @SuppressLint("InflateParams")
//        View view = LayoutInflater.from(context).inflate(R.layout.congrates_layout,null);
//        Button bGo = view.findViewById(R.id.bGo);
//        TextView textView = view.findViewById(R.id.text1);
//        textView.setText(text);
//        builder = new AlertDialog.Builder(context);
//        builder.setView(view);
//        builder.setCancelable(false);
//        alertDialog = builder.create();
//        Objects.requireNonNull(alertDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//        alertDialog.show();
//
//        bGo.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                closeActivity((Activity) context,JobPortal.class);
//                alertDialog.dismiss();
//            }
//        });
//    }

}
