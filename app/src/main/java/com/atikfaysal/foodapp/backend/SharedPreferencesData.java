package com.atikfaysal.foodapp.backend;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.Map;

public class SharedPreferencesData
{

    private Context context;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    private static final String REMEMBER_ME = "remember";
    private static final String LOGGING_STATUS = "loggingstatus";
    private static final String USER_INFO = "userinfo";


    public SharedPreferencesData(Context context)
    {
        this.context = context;
    }

    //remember log in user info,if user checkbox enable then email and password will be saved
    public void rememberUserInfo(String email,String pass,boolean flag)
    {
        sharedPreferences = context.getSharedPreferences(REMEMBER_ME,Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        editor.putString("email",email);//storing email
        editor.putString("pass",pass);//storing password
        editor.putBoolean("status",flag);//storing checkbox status
        editor.apply();//apply edit
    }

    public void userLogingStatus(boolean flag)
    {
        sharedPreferences = context.getSharedPreferences(LOGGING_STATUS,Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        editor.putBoolean("status",flag);//storing checkbox status
        editor.apply();//apply edit
    }


    //store current user info in xml
    public void currentUserInfo(Map<String,String> infoMap)
    {
        sharedPreferences = context.getSharedPreferences(USER_INFO,Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        editor.putString("id",infoMap.get("userId"));
        editor.putString("username",infoMap.get("username"));
        editor.putString("name",infoMap.get("name"));
        editor.putString("gender",infoMap.get("gender"));
        editor.putString("email",infoMap.get("email"));
        editor.putString("phone",infoMap.get("phone"));
        editor.putString("dob",infoMap.get("dob"));
        editor.putString("password",infoMap.get("password"));
        editor.putString("image",infoMap.get("image"));

        editor.apply();

    }

    //getting remember status status
    public boolean getRemStatus()
    {
        sharedPreferences = context.getSharedPreferences(REMEMBER_ME,Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean("status",false);//return storing checkbox status
    }

    public boolean getLogginStatus()
    {
        sharedPreferences = context.getSharedPreferences(LOGGING_STATUS,Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean("status",false);//return storing checkbox status
    }

    //getting user remembering email
    public String getRemEmail()
    {
        sharedPreferences = context.getSharedPreferences(REMEMBER_ME,Context.MODE_PRIVATE);
        return sharedPreferences.getString("email",null);//return storing user email
    }

    //getting user remembering password
    public String getRemPass()
    {
        sharedPreferences = context.getSharedPreferences(REMEMBER_ME,Context.MODE_PRIVATE);
        return sharedPreferences.getString("pass",null);//return storing user password
    }

    //when user sign out,all initial information will be removed
    public void clearData()
    {

    }

}
