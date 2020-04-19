package Helpers;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.concordiaguide.BuildingInfoActivity;
import com.example.concordiaguide.R;

import java.util.List;

import Models.Building;

public class BuildingListAdapter extends RecyclerView.Adapter<BuildingListAdapter.BuildingListViewHolder> {
    private static final String TAG = "BuildingListAdapter";

    private List<Building> mBuildings;
    private Context mContext;

    /**
     * Provide a suitable constructor (depends on the kind of dataset)
     *
     * @param context Android context
     * @param buildings list of buildings
     */
    public BuildingListAdapter(Context context, List<Building> buildings) {
        mContext = context;
        mBuildings = buildings;
    }

    /**
     * Create new views (invoked by the layout manager)
     */
    @Override
    public BuildingListAdapter.BuildingListViewHolder onCreateViewHolder(ViewGroup parent,
                                                                         int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_building_list_item, parent, false);

        return new BuildingListViewHolder(v);
    }

    /**
     * Replace the contents of a view (invoked by the layout manager)
     */
    @Override
    public void onBindViewHolder(BuildingListViewHolder holder, final int position) {
        Log.d(TAG, "onBindViewHolder: called");

        //get element from your dataset at this position
        //and replace the contents of the view with that element
        holder.initials.setText(mBuildings.get(position).getInitials());
        holder.name.setText(mBuildings.get(position).getName());

        holder.parentLayout.setOnClickListener(view -> {
            Log.d(TAG, "onClickEvent: caught");

            final Bundle bundle = new Bundle();
            bundle.putBinder("building", new ObjectWrapperForBinder(mBuildings.get(position)));
            mContext.startActivity(new Intent(mContext, BuildingInfoActivity.class).putExtras(bundle));
        });

    }

    /**
     * Return the size of your dataset
     *
     * @return size of dataset
     */
    //
    @Override
    public int getItemCount() {
        return mBuildings.size();
    }

    // Provide a reference to the views for each data item
    public static class BuildingListViewHolder extends RecyclerView.ViewHolder {

        TextView initials;
        TextView name;
        RelativeLayout parentLayout;

        public BuildingListViewHolder(View itemView) {
            super(itemView);
            initials = itemView.findViewById(R.id.building_initials);
            name = itemView.findViewById(R.id.building_name);
            parentLayout = itemView.findViewById(R.id.building_list_item_parent_layout);
        }
    }

}
