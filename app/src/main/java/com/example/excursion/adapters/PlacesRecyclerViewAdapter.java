package com.example.excursion.adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.excursion.PlaceDetailActivity;
import com.example.excursion.R;
import com.example.excursion.Sight;

import java.util.ArrayList;
import java.util.Objects;

public class PlacesRecyclerViewAdapter extends RecyclerView.Adapter<PlacesRecyclerViewAdapter.ViewHolder> {

    private static final String TAG = "RecyclerViewAdapter";

    private Context context;
    private ArrayList<Sight> sights;

    public PlacesRecyclerViewAdapter(Context context, ArrayList<Sight> sights) {
        this.context = context;
        this.sights = sights;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_places_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        Log.d(TAG, "onBindViewHolder: called");

        holder.list_itemName.setText(String.valueOf(sights.get(holder.getAdapterPosition()).getSightName()));
        holder.list_itemAddress.setText(String.valueOf(sights.get(holder.getAdapterPosition()).getSightAddress()));
        holder.list_itemImage.setImageResource(setImage(sights.get(holder.getAdapterPosition()).getSightImagePath()));

        holder.list_itemParentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: clicked on: " + sights.get(holder.getAdapterPosition()).getSightName() + sights.get(holder.getAdapterPosition()).getSightID());
//                Toast.makeText(context, sights.get(position).getSightName(), Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(context, PlaceDetailActivity.class);
                intent.putExtra("sightName", sights.get(holder.getAdapterPosition()).getSightName());
                intent.putExtra("sightAddress", sights.get(holder.getAdapterPosition()).getSightAddress());
                intent.putExtra("sightImagePath", sights.get(holder.getAdapterPosition()).getSightImagePath());
                intent.putExtra("sightDetails", sights.get(holder.getAdapterPosition()).getSightDetails());
                intent.putExtra("sightSourceLink", sights.get(holder.getAdapterPosition()).getSightSourceLink());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return sights.size();
    }

    private int setImage(String imagePath) {
        return context.getResources().getIdentifier(imagePath, null, Objects.requireNonNull(context).getPackageName());
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView list_itemName, list_itemAddress;
        ImageView list_itemImage;
        RelativeLayout list_itemParentLayout;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            list_itemName = itemView.findViewById(R.id.places_list_item_name);
            list_itemAddress = itemView.findViewById(R.id.places_list_item_address);
            list_itemImage = itemView.findViewById(R.id.places_list_item_image);
            list_itemParentLayout = itemView.findViewById(R.id.places_list_item_parent_layout);
        }
    }
}
