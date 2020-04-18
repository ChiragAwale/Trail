package com.chiragawale.trail.ui.main;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chiragawale.trail.BaseActivity;
import com.chiragawale.trail.R;

public class AdminView extends BaseActivity {
    RecyclerView recyclerView;
    TrailRecyclerViewAdapter adapter;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_view_layout);
        recyclerView = findViewById(R.id.rv_history);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        adapter = new TrailRecyclerViewAdapter(dao.getEntryList(getApplicationContext()));
        recyclerView.setAdapter(adapter);
    }
}
