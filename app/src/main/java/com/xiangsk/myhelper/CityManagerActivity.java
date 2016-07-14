package com.xiangsk.myhelper;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.xiangsk.myhelper.bean.CityBean;
import com.xiangsk.myhelper.db.DatabaseHelper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CityManagerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city_manager);
    }

    @Override
    protected void onResume() {
        super.onResume();

        List<CityBean> list = new DatabaseHelper(getBaseContext()).getUserCity();
        list.add(new CityBean(0, "添加", "", ""));

        RecyclerView recyclerView = (RecyclerView)findViewById(R.id.city_m_view);
        CityRecyclerAdapter recycleAdapter= new CityRecyclerAdapter(CityManagerActivity.this, list);

        //设置布局管理器
        GridLayoutManager layoutManager = new GridLayoutManager(getBaseContext(), 3);

        recyclerView.setLayoutManager(layoutManager);

        //设置Adapter
        recyclerView.setAdapter( recycleAdapter);
        //设置增加或删除条目的动画
        recyclerView.setItemAnimator(new DefaultItemAnimator());
    }
}
