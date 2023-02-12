package com.example.excursion.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.excursion.R;
import com.example.excursion.Sight;

import java.util.ArrayList;

public class RoutesRecyclerViewAdapter extends RecyclerView.Adapter<RoutesRecyclerViewAdapter.ViewHolder> {

    private static final String TAG = "RoutesRecyclerViewAdapt";

    private Context context;
    private ArrayList<Sight> sights;

    public RoutesRecyclerViewAdapter(Context context, ArrayList<Sight> sights) {
        this.context = context;
        this.sights = sights;
    }

    @NonNull
    @Override
    public RoutesRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_waypoints_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Log.d(TAG, "onBindViewHolder: called");

        holder.list_itemName.setText(String.valueOf(sights.get(position).getSightName()));
        holder.list_itemAddress.setText(String.valueOf(sights.get(position).getSightAddress()));
    }

    @Override
    public int getItemCount() {
        return sights.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView list_itemName, list_itemAddress;
        RelativeLayout list_itemParentLayout;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            list_itemName = itemView.findViewById(R.id.waypoints_list_item_name);
            list_itemAddress = itemView.findViewById(R.id.waypoints_list_item_address);
            list_itemParentLayout = itemView.findViewById(R.id.waypoints_list_item_parent_layout);
        }
    }
}
