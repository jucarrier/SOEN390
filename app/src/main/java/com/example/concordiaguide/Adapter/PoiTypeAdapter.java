package com.example.concordiaguide.Adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import Helpers.ObjectWrapperForBinder;
import com.example.concordiaguide.Models.PoiType;
import com.example.concordiaguide.NearByPlacesActivity;
import com.example.concordiaguide.PoiTypesActivity;
import com.example.concordiaguide.R;


import java.util.List;

import androidx.recyclerview.widget.RecyclerView;


public class PoiTypeAdapter extends RecyclerView.Adapter<PoiTypeAdapter.PoiTypeHolder> {
    private static final String TAG = "PoiTypeAdapter";

    private List<PoiType> mPlaceTypes;
    private Context mContext;

    // Provide a reference to the views for each data item
    public static class PoiTypeHolder extends RecyclerView.ViewHolder {

        ImageView icon;
        TextView name;
        RelativeLayout parentLayout;

        public PoiTypeHolder(View itemView) {
            super(itemView);
            icon = itemView.findViewById(R.id.type_icon);
            name = itemView.findViewById(R.id.type_name);
            parentLayout = itemView.findViewById(R.id.singleTypeItemLayout);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public PoiTypeAdapter(Context context, List<PoiType> placeTypes) {
        mContext = context;
        mPlaceTypes= placeTypes;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public PoiTypeHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.place_type_single_item, parent, false);

        PoiTypeHolder vh = new PoiTypeHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(PoiTypeHolder holder, final int position) {
        Log.d(TAG, "onBindViewHolder: called");

        //get element from your dataset at this position
        //and replace the contents of the view with that element
        holder.icon.setImageResource(mPlaceTypes.get(position).getIconRes());
        holder.name.setText(mPlaceTypes.get(position).getType());

        holder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClickEvent: caught");

               final Bundle bundle = new Bundle();
               bundle.putBinder("type", new ObjectWrapperForBinder(mPlaceTypes.get(position)));

               Intent intent= new Intent(mContext, NearByPlacesActivity.class);
               intent.putExtras(bundle);
               mContext.startActivity(intent);//need to make a new page and pass the bundle values

              /* if(mPlaceTypes.get(position).getType()=="Restaurant"){
                    bundle.putBinder("type", new ObjectWrapperForBinder(mPlaceTypes.get(position)));
                }*/
            }
        });

    }
    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {

        return mPlaceTypes.size();
    }

}
