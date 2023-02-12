package com.example.excursion.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.excursion.R;
import com.example.excursion.Sight;

import java.util.ArrayList;

public class WaypointsAutoCompleteAdapter extends ArrayAdapter<Sight> {

    private ArrayList<Sight> sights;

    public WaypointsAutoCompleteAdapter(@NonNull Context context, @NonNull ArrayList<Sight> sights) {
        super(context, 0, sights);
        this.sights = new ArrayList<>(sights);
    }

    @NonNull
    @Override
    public Filter getFilter() {
        return waypointFilter;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.waypoint_autocomplete_row, parent, false);
        }

        Sight sight = getItem(position);

        if (sight != null) {
            ((TextView) convertView.findViewById(R.id.waypoint_autocomplete_row_name)).setText(sight.getSightName());
            ((TextView) convertView.findViewById(R.id.waypoint_autocomplete_row_address)).setText(sight.getSightAddress());
        }

        return convertView;
    }

    private Filter waypointFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults filterResults = new FilterResults();
            ArrayList<Sight> suggestions = new ArrayList<>();

            if (constraint == null || constraint.length() == 0) {
                suggestions.addAll(sights);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();

                for (Sight sight : sights) {
                    if (sight.getSightName().toLowerCase().contains(filterPattern)) {
                        suggestions.add(sight);
                    }
                }
            }

            filterResults.values = suggestions;
            filterResults.count = suggestions.size();

            return  filterResults;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            clear();
            addAll((ArrayList) results.values);
            notifyDataSetChanged();
        }

        @Override
        public CharSequence convertResultToString(Object resultValue) {
            return ((Sight) resultValue).getSightName();
        }
    };
}
