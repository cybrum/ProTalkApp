package com.example.mrmohammad.protalk;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class SplashWelcome extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);

        Thread thread = new Thread(){
            @Override
            public void run() {

                try
                {
                    sleep(3300);
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
                finally
                {

                    //Add Intent...to MainActivity
                    Intent splashIntent = new Intent(SplashWelcome.this, MainActivity.class);
                    startActivity(splashIntent);


                }

            }
        };
        thread.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }
}
