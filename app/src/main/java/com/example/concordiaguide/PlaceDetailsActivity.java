package com.example.concordiaguide;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.squareup.picasso.Picasso;

import Models.Photos;
import Models.Results;

public class PlaceDetailsActivity extends AppCompatActivity {
    private TextView textViewName;
    private TextView textViewAddress;
    private LinearLayout linearLayoutShowOnMap;
    private LinearLayout linearLayoutShowDistanceOnMap;
    // variable
    private Results results;
    private double lat, lng;
    private String result = "result";

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_details);

        // init UI
        init();

        Bundle bundle = getIntent().getExtras();

        if (bundle != null) {
            results = (Results) bundle.getSerializable(result);
            lat = bundle.getDouble("lat");
            lng = bundle.getDouble("lng");
        } else {
            Toast.makeText(this, "Got Nothing!!", Toast.LENGTH_SHORT).show();
            return;
        }


        ImageView imageView = findViewById(R.id.imageView);

        try {
            // get photo
            Photos photos = results.getPhotos()[0];
            String photoUrl = String.format("https://maps.googleapis.com/maps/api/place/photo?maxwidth=%s&photoreference=%s&key=%s", 400, photos.getPhotoReference(), getResources().getString(R.string.api_key));
            Log.d("photoUrl", photoUrl);
            Picasso
                    .get()
                    .load(photoUrl)
                    .into(imageView);
        } catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            Picasso
                    .get()
                    .load(R.drawable.ic_error_image)
                    .into(imageView);
        }

        textViewName.setText(results.getName());
        textViewAddress.setText(results.getVicinity());

        linearLayoutShowOnMap.setOnClickListener(view -> {
            Intent intent = new Intent(PlaceDetailsActivity.this, PlaceOnMapActivity.class);
            intent.putExtra(result, results);
            intent.putExtra("lat", lat);
            intent.putExtra("lng", lng);
            intent.putExtra("type", "map");
            startActivity(intent);
        });

        linearLayoutShowDistanceOnMap.setOnClickListener(view -> {
            Intent intent = new Intent(PlaceDetailsActivity.this, PlaceOnMapActivity.class);
            intent.putExtra(result, results);
            intent.putExtra("lat", lat);
            intent.putExtra("lng", lng);
            intent.putExtra("type", "distance");
            startActivity(intent);
        });
    }
/*
    public void onBackPressed() {
        Intent intent2 = new Intent(this, MainActivity.class);
        finish();
        startActivity(intent2);
    }*/

    private void init() {
        linearLayoutShowOnMap = findViewById(R.id.linearLayoutShowOnMap);
        linearLayoutShowDistanceOnMap = findViewById(R.id.linearLayoutShowDistanceOnMap);
        textViewName = findViewById(R.id.textViewName);
        textViewAddress = findViewById(R.id.textViewAddress);
    }
}
