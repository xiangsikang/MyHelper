package com.xiangsk.myhelper;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.ContentValues;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.xiangsk.myhelper.bean.CityBean;
import com.xiangsk.myhelper.bean.Data;
import com.xiangsk.myhelper.bean.Forecast;
import com.xiangsk.myhelper.bean.WeatherDataRsp;
import com.xiangsk.myhelper.db.DatabaseHelper;

import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private Map<Integer, Fragment> fragmentMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        new CItyImportAsyncTask(this).execute();

        fragmentMap = new LinkedHashMap<>();
    }

    @Override
    protected void onResume() {
        super.onResume();


        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        ViewGroup vg = (ViewGroup)findViewById(R.id.city_bottom);

        List<CityBean> userCityList = new DatabaseHelper(this).getUserCity();
        // 用户没有配置城市，跳转到城市管理
        if (WeatherUtils.isEmpty(userCityList)) {
            Intent intent = new Intent();
            intent.setClass(this, CityManagerActivity.class);
            startActivity(intent);
            return;
        }

        int i = 0;
        for (CityBean city : userCityList) {
            if (null != fragmentMap.get(city.getCode())) {
                continue;
            }

            CityWeatherFragment fragment = CityWeatherFragment.newInstance(city.getCode());
            fragmentTransaction.add(R.id.city_fragment, fragment);

            fragmentMap.put(city.getCode(), fragment);

            Button bt = new Button(getBaseContext());
            bt.setText(city.getName1());
            bt.setTag(city);
            bt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showCityFragment(((CityBean)v.getTag()).getCode());
                }
            });
            vg.addView(bt);
        }

        /*// 删除fragment
        for (Integer code : fragmentMap.keySet()) {
            boolean isUsed = false;
            for (CityBean city : userCityList) {
                if (city.getCode() == code) {
                    isUsed = true;
                    break;
                }
            }

            if (!isUsed) {
                fragmentTransaction.hide(fragmentMap.get(code));
            }
        }*/

        fragmentTransaction.commit();

        showCityFragment(userCityList.get(0).getCode());
    }


    private void showCityFragment(int cityCode) {
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        for (Integer code : fragmentMap.keySet()) {
            if (code == cityCode) {
                ft.show(fragmentMap.get(code));
            } else {
                ft.hide(fragmentMap.get(code));
            }
        }
        ft.commit();
    }

}
