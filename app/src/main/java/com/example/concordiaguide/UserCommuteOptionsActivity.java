package com.example.concordiaguide;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.view.View;
import android.widget.Button;


import android.os.Bundle;

import java.util.Locale;

public class UserCommuteOptionsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_commute_options);

        final Button button1 = findViewById(R.id.button);
        final Button button2 = findViewById(R.id.button3);
        final Button button3 = findViewById(R.id.button4);

       /* button1.setOnCapturedPointerListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {

            }
        });*/
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Handler().postDelayed(new Runnable() {
                    @Override

                    public void run() {
                        Uri googlemapsIntentUri = Uri.parse("geo:0,0?q=");
                        Intent mapInt = new Intent(Intent.ACTION_VIEW, googlemapsIntentUri);
                        mapInt.setPackage("com.google.android.apps.maps");
                        startActivity(mapInt);
                    }
                }, 1000);
            }
        });

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Uri googlemapsIntentUri = Uri.parse("geo:0,0?q=");
                        Intent mapInt = new Intent(Intent.ACTION_VIEW, googlemapsIntentUri);
                        mapInt.setPackage("com.google.android.apps.maps");
                        startActivity(mapInt);
                    }
                }, 1000);
            }
        });

        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Uri googlemapsIntentUri = Uri.parse("geo:0,0?q=");
                        Intent mapInt = new Intent(Intent.ACTION_VIEW, googlemapsIntentUri);
                        mapInt.setPackage("com.google.android.apps.maps");
                        startActivity(mapInt);
                    }
                }, 1000);
            }
        });


    }













} //END CLASS
