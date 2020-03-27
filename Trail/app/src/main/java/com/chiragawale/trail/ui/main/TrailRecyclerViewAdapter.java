package com.chiragawale.trail.ui.main;

import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.chiragawale.trail.R;
import com.chiragawale.trail.models.RealmEntry;
import com.chiragawale.trail.ui.main.Trail.OnListFragmentInteractionListener;

import java.util.List;

public class TrailRecyclerViewAdapter extends RecyclerView.Adapter<TrailRecyclerViewAdapter.ViewHolder> {

    private final List<RealmEntry> mValues;
    private final OnListFragmentInteractionListener mListener;

    public TrailRecyclerViewAdapter(List<RealmEntry> items, OnListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.trail_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mContentView.setText(mValues.get(position).getName() +" "
                + mValues.get(position).getMacAddress() + " "
                + mValues.get(position).getTime() + " "
                + mValues.get(position).getLocation() + " "
                + mValues.get(position).getRssi());

    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final TextView mContentView;

        public ViewHolder(View view) {
            super(view);
            mContentView = (TextView) view.findViewById(R.id.content);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }
}
