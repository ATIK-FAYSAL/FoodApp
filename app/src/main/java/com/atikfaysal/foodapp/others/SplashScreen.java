package com.atikfaysal.foodapp.others;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.atikfaysal.foodapp.R;
import com.atikfaysal.foodapp.main.SignIn;

public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);
        threadStart();
    }



    protected void threadStart()
    {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try
                {
                    Thread.sleep(2000);
                    startActivity(new Intent(SplashScreen.this,SignIn.class));
                    finish();
                }catch (InterruptedException e)
                {
                    e.printStackTrace();
                }
            }
        });

        thread.start();
    }

}
