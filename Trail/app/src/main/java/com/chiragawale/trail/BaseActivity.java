package com.chiragawale.trail;

import androidx.appcompat.app.AppCompatActivity;

import com.chiragawale.trail.dao.Dao;
import com.chiragawale.trail.dao.DaoImpl;

import io.realm.Realm;

public class BaseActivity extends AppCompatActivity {
    Dao dao = new DaoImpl();
}
