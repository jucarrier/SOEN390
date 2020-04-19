package Helpers;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.concordiaguide.R;

import java.util.ArrayList;

import Models.CalendarEventDisplayCard;

public class CalendarEventDisplayAdapter extends RecyclerView.Adapter<CalendarEventDisplayAdapter.ViewHolder> {
    private ArrayList<CalendarEventDisplayCard> list;

    public CalendarEventDisplayAdapter(ArrayList<CalendarEventDisplayCard> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.calendar_event_card, parent, false);
        return new ViewHolder(v);
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CalendarEventDisplayCard currentItem = list.get(position);
        String white = "#FFFFFF";


        try {
            holder.imageView.setImageResource(currentItem.getImageResource());
            holder.title.setText(currentItem.getTitle());
            holder.description.setText(currentItem.getDescription());

            if (currentItem.getDays().get("Monday")) {
                holder.monday.setBackgroundResource(R.drawable.day_indicator_background);
                holder.monday.setTextColor(Color.parseColor(white));    //using color.white turns the text blue, need to use parseColor instead
            }

            if (currentItem.getDays().get("Tuesday")) {
                holder.tuesday.setBackgroundResource(R.drawable.day_indicator_background);
                holder.tuesday.setTextColor(Color.parseColor(white));
            }
            if (currentItem.getDays().get("Wednesday")) {
                holder.wednesday.setBackgroundResource(R.drawable.day_indicator_background);
                holder.wednesday.setTextColor(Color.parseColor(white));
            }
            if (currentItem.getDays().get("Thursday")) {
                holder.thursday.setBackgroundResource(R.drawable.day_indicator_background);
                holder.thursday.setTextColor(Color.parseColor(white));
            }
            if (currentItem.getDays().get("Friday")) {
                holder.friday.setBackgroundResource(R.drawable.day_indicator_background);
                holder.friday.setTextColor(Color.parseColor(white));
            }

        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView imageView;
        public TextView title;
        public TextView description;
        public TextView sunday;
        public TextView monday;
        public TextView tuesday;
        public TextView wednesday;
        public TextView thursday;
        public TextView friday;
        public TextView saturday;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.classCardImageView);
            title = itemView.findViewById(R.id.classCardTitle);
            description = itemView.findViewById(R.id.classCardDescription);

            monday = itemView.findViewById(R.id.classCardMonday);
            tuesday = itemView.findViewById(R.id.classCardTuesday);
            wednesday = itemView.findViewById(R.id.classCardWednesday);
            thursday = itemView.findViewById(R.id.classCardThursday);
            friday = itemView.findViewById(R.id.classCardFriday);

        }
    }
}
