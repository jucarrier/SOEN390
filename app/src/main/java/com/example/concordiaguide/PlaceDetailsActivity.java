package com.example.concordiaguide;

import Models.Photos;
import Models.Results;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import androidx.appcompat.app.AppCompatActivity;

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
    private LinearLayout linearLayoutShowOnMap;
    private LinearLayout linearLayoutShowDistanceOnMap;

    private Results results;
    private double lat, lng;
    private String result = "result";

    protected void onCreate(Bundle savedInstanceState)  {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_details);

        // initializes the layout/UI
        init();

        Bundle bundle = getIntent().getExtras();

        if (bundle != null) {
            results = (Results) bundle.getSerializable(result);
            lat = bundle.getDouble("lat");
            lng = bundle.getDouble("lng");
            //Toast.makeText(this, String.valueOf(results.getPhotos()[0].getPhoto_reference()), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Got Nothing!!", Toast.LENGTH_SHORT).show();
            return;
        }


        imageView = findViewById(R.id.imageView);

        try {
            // gets photo
            photos = results.getPhotos()[0];
            String photoUrl = String.format("https://maps.googleapis.com/maps/api/place/photo?maxwidth=%s&photoreference=%s&key=%s", 400, photos.getPhoto_reference(), getResources().getString(R.string.api_key));
            Log.d("photoUrl", photoUrl);
            Picasso
                    .get()
                    .load(photoUrl)
                    .into(imageView);
        } catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            Picasso
                    .get()
                    .load(R.drawable.ic_error_image)//if image is not found
                    .into(imageView);
        }

        textViewName.setText(results.getName());

        linearLayoutShowOnMap.setOnClickListener(new View.OnClickListener() {
            /**
             * @param view
             * on click of the show on map button, it will redirect to placeOnMapActivity in order to display the desired POI
             */
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PlaceDetailsActivity.this, PlaceOnMapActivity.class);
                intent.putExtra(result, results);
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
                intent.putExtra(result, results);
                intent.putExtra("lat", lat);
                intent.putExtra("lng", lng);
                intent.putExtra("type", "distance");
                startActivity(intent);
            }
        });
    }

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
