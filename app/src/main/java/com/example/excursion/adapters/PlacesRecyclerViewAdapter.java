package com.example.excursion.adapters;

import android.content.Context;
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

import com.example.excursion.R;

import java.util.ArrayList;

public class PlacesRecyclerViewAdapter extends RecyclerView.Adapter<PlacesRecyclerViewAdapter.ViewHolder>{

    private static final String TAG = "RecyclerViewAdapter";

    private ArrayList<String> sightName, sightAddress;
    private ArrayList<Integer> sightImage;
    private Context context;

    public PlacesRecyclerViewAdapter(Context context, ArrayList<String> sightName, ArrayList<String> sightAddress, ArrayList<Integer> sightImage) {
        this.context = context;
        this.sightName = sightName;
        this.sightAddress = sightAddress;
        this.sightImage = sightImage;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_listitem, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        Log.d(TAG, "onBindViewHolder: called");

        holder.list_itemName.setText(String.valueOf(sightName.get(position)));
        holder.list_itemAddress.setText(String.valueOf(sightAddress.get(position)));
        holder.list_itemImage.setImageResource(sightImage.get(position));

        holder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: clicked on: " + sightName.get(position));
                Toast.makeText(context, sightName.get(position), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return sightName.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView list_itemName, list_itemAddress;
        ImageView list_itemImage;
        RelativeLayout parentLayout;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            list_itemName = itemView.findViewById(R.id.list_item_name);
            list_itemAddress = itemView.findViewById(R.id.list_item_address);
            list_itemImage = itemView.findViewById(R.id.list_item_image);
            parentLayout = itemView.findViewById(R.id.parent_layout);
        }
    }
}
