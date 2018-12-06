package com.olmos.javier.whereis.Adapters;

import java.util.List;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.olmos.javier.whereis.Objects.Location;
import com.olmos.javier.whereis.R;

public class LocationItemAdapter extends BaseAdapter {

    private final Context context;
    private List<Location> items;

    public LocationItemAdapter(Context context, List<Location> items) {
        this.context = context;
        this.items = items;
    }

    @Override
    public int getCount() {
        return this.items.size();
    }

    @Override
    public Object getItem(int position) {
        return this.items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View rowView = convertView;

        if (rowView == null) {
            // Create a new view into the list.
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            if (inflater != null) {
                rowView = inflater.inflate(R.layout.list_location_item, parent, false);

                // Set data into the view.
                TextView itemTitle = rowView.findViewById(R.id.itemTitle);
                TextView itemDescription = rowView.findViewById(R.id.itemDescription);

                Location location = this.items.get(position);
                itemTitle.setText(location.getTitle());
                itemDescription.setText(location.getDescription());
            }
        }
        return rowView;
    }
}