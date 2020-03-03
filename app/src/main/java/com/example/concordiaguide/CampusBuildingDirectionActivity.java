package com.example.concordiaguide;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class CampusBuildingDirectionActivity extends AppCompatActivity {

    private static final String TAG = "CampusBuildingDirection";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_building_info);
        Log.d(TAG, "onCreate: Starting.");
    }



    public void goToMainActivity(View view){
        Log.d(TAG, "onClick: Clicked directionBtn.");
        Intent intent = new Intent (getApplicationContext(), MainActivity.class);
        startActivity(intent);

    }


}
