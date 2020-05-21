package com.example.concordiaguide;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.squareup.picasso.Picasso;

import Models.Photos;
import Models.Results;

/**
 * this activity displays the card view of the chosen place from the nearbyPoiActivity
 * where the user can see additional informations regarding that place and choose between
 * displaying the location on map or seeing the distance between the user and the Point of interest
 */
public class PlaceDetailsActivity extends AppCompatActivity {
    private ImageView imageView;
    private Photos photos;
    private TextView textViewName;
    private TextView textViewAddress;
    private LinearLayout linearLayoutShowOnMap; //show places on map
    private LinearLayout linearLayoutShowDistanceOnMap;
    // variable
    private Results results;
    private double lat, lng;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_details);

        // init UI
        init();

        Bundle bundle = getIntent().getExtras();

        if (bundle != null) {
            results = (Results) bundle.getSerializable("result");
            lat = bundle.getDouble("lat");
            lng = bundle.getDouble("lng");
            //Toast.makeText(this, String.valueOf(results.getPhotos()[0].getPhoto_reference()), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Got Nothing!!", Toast.LENGTH_SHORT).show();
            return;
        }


        imageView = findViewById(R.id.imageView);

        imageView = findViewById(R.id.imageView);

        try {
            // get photo
            photos = results.getPhotos()[0];
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

        linearLayoutShowOnMap.setOnClickListener(new View.OnClickListener() {
            /**
             * @param view
             * on click of the show on map button, it will redirect to placeOnMapActivity in order to display the desired POI
             */
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(PlaceDetailsActivity.this, PlaceOnMapActivity.class);
                intent.putExtra("result", results);
                intent.putExtra("lat", lat);
                intent.putExtra("lng", lng);
                intent.putExtra("type", "map");
                startActivity(intent);
            }
        });

        linearLayoutShowDistanceOnMap.setOnClickListener(new View.OnClickListener() {
            /**
             * @param view
             * on click of the Show directions on Map, it will redirect to placeOnMapActivity and show the polyline
             * between the users location and the POI location
             */
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PlaceDetailsActivity.this, PlaceOnMapActivity.class);
                intent.putExtra("result", results);
                intent.putExtra("lat", lat);
                intent.putExtra("lng", lng);
                intent.putExtra("type", "distance");
                startActivity(intent);
            }
        });
    }
/*
    public void onBackPressed() {
        Intent intent2 = new Intent(this, MainActivity.class);
        finish();
        startActivity(intent2);
    }*/

    /**
     * initializes the layout and lets user choose between showing place location or distance to that location
     */
    private void init() {
        linearLayoutShowOnMap = findViewById(R.id.linearLayoutShowOnMap);
        linearLayoutShowDistanceOnMap = findViewById(R.id.linearLayoutShowDistanceOnMap);
        textViewName = findViewById(R.id.textViewName);
        textViewAddress = findViewById(R.id.textViewAddress);
    }
}
