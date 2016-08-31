package com.xiangsk.myhelper;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.xiangsk.myhelper.bean.Data;
import com.xiangsk.myhelper.bean.Forecast;
import com.xiangsk.myhelper.bean.WeatherDataRsp;

import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;


public class CityWeatherFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "cityCode";

    // TODO: Rename and change types of parameters
    private int mCityCode;

    private View mView;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param cityCode Parameter 1.
     * @return A new instance of fragment CityWeatherFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CityWeatherFragment newInstance(int cityCode) {
        CityWeatherFragment fragment = new CityWeatherFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM1, cityCode);
        fragment.setArguments(args);
        return fragment;
    }
    public CityWeatherFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mCityCode = getArguments().getInt(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_city_weather, container, false);
        return mView;
    }

    @Override
    public void onResume() {
        super.onResume();

        new DataAsyncTask().execute(mCityCode);
    }

    class DataAsyncTask extends AsyncTask<Integer,Integer,String> {
        private WeatherDataRsp rsp;

        @Override
        protected String doInBackground(Integer... params) {
            try {
                URL url = new URL("http://wthrcdn.etouch.cn/weather_mini?citykey=" + params[0]);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                byte[] buffer = new byte[1024];
                StringBuffer sb = new StringBuffer();
                int len = 0;
                while ((len = conn.getInputStream().read(buffer)) != -1) {
                    sb.append(new String(buffer, 0, len));
                }
                conn.disconnect();

                rsp = new Gson().fromJson(sb.toString(), WeatherDataRsp.class);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            if (null == rsp) {
                Log.e("MainActivity", "rsp is null");
                return;
            }
            Data data = rsp.getData();
            Forecast todayData = data.getForecast().get(0);

            TextView city = (TextView) mView.findViewById(R.id.header_city);
            city.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent();
                    intent.setClass(getActivity(), CityManagerActivity.class);
                    startActivity(intent);
                }
            });
            city.setText(data.getCity());

            TextView updateTime = (TextView) mView.findViewById(R.id.header_update_time);
            updateTime.setText(new SimpleDateFormat("HH:mm").format(new Date()));
            ;

            ImageView weatherImg = (ImageView) mView.findViewById(R.id.header_weather_img);
            weatherImg.setImageResource(WeatherUtils.getResourceId(todayData.getType()));

            TextView weatherType = (TextView) mView.findViewById(R.id.header_weather);
            weatherType.setText(todayData.getType());

            TextView wendu = (TextView) mView.findViewById(R.id.header_wendu);
            wendu.setText(data.getWendu() + "℃");


            TextView windRange = (TextView) mView.findViewById(R.id.header_wendu_range);
            int highTemp = (int) WeatherUtils.getTempValue(todayData.getHigh());
            int lowTemp = (int) WeatherUtils.getTempValue(todayData.getLow());
            windRange.setText(String.format("%s-%s℃", lowTemp, highTemp));

            TextView wind = (TextView) mView.findViewById(R.id.header_wind);
            String windStr = "无持续风向".equals(todayData.getFengxiang()) ? "" :todayData.getFengxiang();
            windStr += (windStr.length() > 0 ? " " : "") + todayData.getFengli();
            wind.setText(windStr);

            RecyclerView recyclerView = (RecyclerView) mView.findViewById(R.id.list);
            MyRecyclerAdapter recycleAdapter = new MyRecyclerAdapter(getContext(), data.getForecast());
            recycleAdapter.setHasStableIds(true);
            LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
            //设置为垂直布局，这也是默认的
            layoutManager.setOrientation(OrientationHelper.HORIZONTAL);
            //设置布局管理器
            recyclerView.setLayoutManager(layoutManager);
            //设置Adapter
            recyclerView.setAdapter(recycleAdapter);
            //设置增加或删除条目的动画
            recyclerView.setItemAnimator(new DefaultItemAnimator());

        }
    }
}
