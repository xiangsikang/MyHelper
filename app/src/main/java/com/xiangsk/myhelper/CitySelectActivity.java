package com.xiangsk.myhelper;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SearchView;

import com.xiangsk.myhelper.bean.CityBean;
import com.xiangsk.myhelper.db.DatabaseHelper;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class CitySelectActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city_select);

        SearchView searchView = (SearchView) findViewById(R.id.city_s_sv);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                new CitySearchAsyncTask().execute(newText);
                return false;
            }
        });


    }


    class CitySearchAsyncTask extends AsyncTask<String, Integer, String> {
        private List<CityBean> cityList;
        @Override
        protected String doInBackground(String... params) {
            cityList = new DatabaseHelper(getBaseContext()).query(params[0]);
            return null;
        }

        private Set<String> getUsedCityCode() {
            SharedPreferences sp = getBaseContext().getSharedPreferences("CITY", Context.MODE_PRIVATE);
            return sp.getStringSet("codes", new HashSet<String>());
        }

        @Override
        protected void onPostExecute(String s) {
            ArrayAdapter adapter = new ArrayAdapter(getBaseContext(), android.R.layout.simple_list_item_1);
            if (!WeatherUtils.isEmpty(cityList)) {
                for (CityBean cityBean : cityList) {
                    adapter.add(cityBean.getFullName());
                }
            }

            ListView listView = (ListView) findViewById(R.id.city_s_list);
            listView.setAdapter(adapter);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    CityBean cityBean = cityList.get(position);
                    new DatabaseHelper(getBaseContext()).addUserCity(cityBean.getCode());

                    Intent intent = new Intent();
                    intent.setClass(getBaseContext(), CityManagerActivity.class);
                    startActivity(intent);
                }
            });
        }
    }
}
