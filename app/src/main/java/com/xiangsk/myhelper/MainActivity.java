package com.xiangsk.myhelper;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.xiangsk.myhelper.bean.CityBean;
import com.xiangsk.myhelper.db.DatabaseHelper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private Map<Integer, Fragment> fragmentMap;

    private ViewGroup mBottomVg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        new CItyImportAsyncTask(this).execute();

        fragmentMap = new HashMap<>();
    }

    @Override
    protected void onResume() {
        super.onResume();

        List<CityBean> userCityList = new DatabaseHelper(this).getUserCity();
        // 用户没有配置城市，跳转到城市管理
        if (WeatherUtils.isEmpty(userCityList)) {
            Intent intent = new Intent();
            intent.setClass(this, CityManagerActivity.class);
            startActivity(intent);
            return;
        }

        for (int i = 0; i < userCityList.size(); i++) {
            int code = userCityList.get(i).getCode();
            if (null != fragmentMap.get(code)) {
                continue;
            }

            CityWeatherFragment fragment = CityWeatherFragment.newInstance(code);
            fragmentMap.put(code, fragment);
        }

        mBottomVg = (ViewGroup)findViewById(R.id.city_bottom);
        int diffCount = mBottomVg.getChildCount() - userCityList.size();
        if (diffCount != 0) {
            for (int i = 0; i < Math.abs(diffCount); i++) {
                if (diffCount > 0) {
                    mBottomVg.removeViewAt(0);
                } else {
                    ImageView iv = new ImageView(this);
                    mBottomVg.addView(iv);
                }
            }
        }
        showCity(0);

        ViewPager viewPager = (ViewPager)findViewById(R.id.city_paper);
        viewPager.setAdapter(new CityPagerAdapter(getSupportFragmentManager(), userCityList));
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }
            @Override
            public void onPageSelected(int position) {
                showCity(position);
            }
            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    private void showCity(int position) {
        for (int i = 0; i < mBottomVg.getChildCount(); i++) {
            ImageView iv = (ImageView)mBottomVg.getChildAt(i);
            iv.setImageResource(position == i ? R.drawable.circle_solid : R.drawable.circle_hollow);
        }
    }

    class CityPagerAdapter extends FragmentStatePagerAdapter {
        private List<CityBean> mCitys;

        public CityPagerAdapter(FragmentManager fm, List<CityBean> citys) {
            super(fm);

            mCitys = citys;
        }


        @Override
        public android.support.v4.app.Fragment getItem(int position) {
            return fragmentMap.get(mCitys.get(position).getCode());
        }

        @Override
        public int getCount() {
            return mCitys.size();
        }
    }
}
