package com.atikfaysal.foodapp.main;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import com.atikfaysal.foodapp.R;
import com.atikfaysal.foodapp.backend.BackgroundTask;
import com.atikfaysal.foodapp.backend.CheckInternetConnection;
import com.atikfaysal.foodapp.backend.SharedPreferencesData;
import com.atikfaysal.foodapp.fragments.Home;
import com.atikfaysal.foodapp.interfaces.InitialMethods;
import com.atikfaysal.foodapp.interfaces.OnResponseTask;
import com.atikfaysal.foodapp.others.AdditionalMethods;
import com.atikfaysal.foodapp.others.DataValidation;
import com.atikfaysal.foodapp.others.DesEncryptionAlgo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class SignIn extends AppCompatActivity implements InitialMethods,View.OnClickListener
{

    private TextInputEditText txtEmail,txtPassword;
    private DesEncryptionAlgo encryptionAlgo;
    private CheckInternetConnection internetConnection;
    private AdditionalMethods additionalMethods;
    private SharedPreferencesData sharedPreferencesData;
    private DataValidation validation;
    private CheckBox checkBox;
    private ProgressDialog progressDialog;

    private String userEmail,userPass;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_in);
        initComponent();
    }


    @Override
    protected void onStart() {
        super.onStart();

        if(sharedPreferencesData.getLogginStatus())
            startActivity(new Intent(SignIn.this,HomePage.class));
        else
        {
            if(sharedPreferencesData.getRemStatus())//if user remembering enable
            {
                txtEmail.setText(sharedPreferencesData.getRemEmail());//getting remembering email from sharedPreferences
                txtPassword.setText(sharedPreferencesData.getRemPass());//getting remembering password from sharedPreferences
                checkBox.setChecked(true);//set enable because of user remember password is enable

            }else//if user remembering disable
            {
                checkBox.setChecked(false);
                txtEmail.setText("");
                txtPassword.setText("");
            }
        }

    }

    //initialize all component
    @Override
    public void initComponent()
    {
        TextView txtSignUp = findViewById(R.id.txtSingUp);//signUp textView
        Button bSignIn = findViewById(R.id.bSignIn);//signIn button
        txtEmail = findViewById(R.id.inEmail);//email editText
        txtPassword = findViewById(R.id.inPassword);//password editText
        checkBox = findViewById(R.id.cRememberMe);//remember me checkbox

        encryptionAlgo = new DesEncryptionAlgo(this);//Password encryption
        internetConnection = new CheckInternetConnection(this);
        additionalMethods = new AdditionalMethods(this);
        sharedPreferencesData = new SharedPreferencesData(this);
        validation = new DataValidation();//user data validation
        progressDialog = new ProgressDialog(this);

        //set click listener
        txtSignUp.setOnClickListener(this);
        bSignIn.setOnClickListener(this);
        checkBox.setOnClickListener(this);
    }

    @Override
    public void setToolbar() { }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.bSignIn:

                if(internetConnection.isOnline())
                {
                    if (checkUserLogInInfo())
                    {
                        //startActivity(new Intent(SignIn.this,HomePage.class));
                        Map<String,String> map = new HashMap<>();

                        map.put("cases","login");//passing type of cases
                        map.put("email",userEmail);//passing current user email
                        map.put("password",userPass);//passing current user encrypted password

                        progressDialog.setTitle("Please wait");//set progressbar title
                        progressDialog.setMessage("Authenticating....");//set progress dialog message
                        progressDialog.show();//show progress dialog

                        BackgroundTask backgroundTask = new BackgroundTask(this,onResponseTask);
                        backgroundTask.serverResponse(getResources().getString(R.string.registration),map);//sending user info to server for authentication

                    }else additionalMethods.errorMessage(getResources().getString(R.string.invalidInfo));//if inserted data are not valid
                }else additionalMethods.errorMessage(getResources().getString(R.string.offline));//if user not connected to internet

                break;

            case R.id.txtSingUp:
                startActivity(new Intent(SignIn.this,Registration.class));//client and employee Registration
                break;

            case R.id.cRememberMe://remember user email and password

                if(checkUserLogInInfo())//check inserted data are valid ?
                {
                    String email = Objects.requireNonNull(txtEmail.getText()).toString();//getting email
                    String pass = Objects.requireNonNull(txtPassword.getText()).toString();//getting password

                    if(checkBox.isChecked())//if remember me checkbox is enable
                        sharedPreferencesData.rememberUserInfo(email,pass,true);//if checkbox is enable then storing user info and checkbox status in sharedPreferences
                    else
                        sharedPreferencesData.rememberUserInfo(null,null,false);//if checkbox is disable then storing user info as null and checkbox info as false in sharedPreferences

                }else
                {
                    additionalMethods.errorMessage(getResources().getString(R.string.invalidInfo));//if inserted data are not valid
                    checkBox.setChecked(false);//set remember me checkbox disable
                }

                break;
        }
    }


    //user log in info validation
    private boolean checkUserLogInInfo()
    {
        userEmail = Objects.requireNonNull(txtEmail.getText()).toString();
        userPass = Objects.requireNonNull(txtPassword.getText()).toString();

        return validation.emailValidation(userEmail) && validation.passwordValidation(userPass);
    }

    @Override
    public void processJsonData(String json) { }

    @Override
    public List<?> processJsonValue(String json) {
        return null;
    }

    //getting user log in response
    private OnResponseTask onResponseTask = new OnResponseTask() {
        @Override
        public void onResultSuccess(final String value) {
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(1500);

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                sharedPreferencesData.userLogingStatus(true);
                                startActivity(new Intent(SignIn.this,HomePage.class));
                                progressDialog.dismiss();
                            }
                        });

                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            });

            thread.start();
        }
    };

}
