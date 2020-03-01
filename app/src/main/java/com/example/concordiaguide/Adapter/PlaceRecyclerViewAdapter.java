package com.example.concordiaguide.Adapter;

import com.example.concordiaguide.R;
import com.example.concordiaguide.Models.MyPlaces;
import com.example.concordiaguide.Models.Results;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class PlaceRecyclerViewAdapter extends RecyclerView.Adapter<PlaceRecyclerViewAdapter.ViewHolder> {

    private Context context;
    private MyPlaces myPlaces;
    private double lat, lng;

    public PlaceRecyclerViewAdapter(Context context, MyPlaces myPlaces, double lat, double lng){
        this.context = context;
        this.myPlaces = myPlaces;
        this.lat = lat;
        this.lng = lng;
    }
    @NonNull
    @Override
    public PlaceRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.singleline_layout, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull PlaceRecyclerViewAdapter.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView name, address;
        public LinearLayout linearLayoutDetails;
        ImageView placeIV;

        public ViewHolder(View view) {
            super(view);
            name = view.findViewById(R.id.textViewPlaceName);//should be defined in textview xml
            address = view.findViewById(R.id.textViewAddress);
            linearLayoutDetails = view.findViewById(R.id.linearLayoutDetails);
            placeIV = view.findViewById(R.id.placeImageView);
        }

        public void bind(Results results) { //should be defined in models package
            name.setText(results.getName());
            address.setText(results.getVicinity());
        }
    }
}
