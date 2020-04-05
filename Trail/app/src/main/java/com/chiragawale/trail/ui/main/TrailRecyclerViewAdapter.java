package com.chiragawale.trail.ui.main;

import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.chiragawale.trail.R;
import com.chiragawale.trail.models.RealmEntry;
import com.chiragawale.trail.utils.CustomTimeUtils;

import java.util.List;

public class TrailRecyclerViewAdapter extends RecyclerView.Adapter<TrailRecyclerViewAdapter.ViewHolder> {

    private final List<RealmEntry> mValues;

    public TrailRecyclerViewAdapter(List<RealmEntry> items) {
        mValues = items;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.admin_view_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.tv_time.setText(mValues.get(position).getTime() + "");
        holder.tv_location.setText(mValues.get(position).getLocation());
        holder.tv_bluetooth_address.setText(mValues.get(position).getBluetoothAddress());
        holder.tv_type.setText(mValues.get(position).getType());
        holder.tv_distance.setText(mValues.get(position).getDistance() + "");
        holder.tv_location.setText(mValues.get(position).getLocation());

    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final TextView tv_time,tv_location,tv_bluetooth_address,tv_type,tv_distance;

        public ViewHolder(View view) {
            super(view);
            tv_time = (TextView) view.findViewById(R.id.tv_time);
            tv_location = (TextView) view.findViewById(R.id.tv_location);
            tv_bluetooth_address = (TextView) view.findViewById(R.id.tv_bluetooth_address);
            tv_type = (TextView) view.findViewById(R.id.tv_type);
            tv_distance = (TextView) view.findViewById(R.id.tv_distance);

        }
    }
}
