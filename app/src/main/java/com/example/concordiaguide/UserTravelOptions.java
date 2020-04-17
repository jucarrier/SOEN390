package com.example.concordiaguide;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;

public class UserTravelOptions extends AppCompatActivity {

    private Button walkButton;
    private Button publicTransportation;
    private Button carButton;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_travel_options);

        publicTransportation = findViewById(R.id.button1);
        carButton = findViewById(R.id.button2);
        walkButton = findViewById(R.id.button3);


    }
}
