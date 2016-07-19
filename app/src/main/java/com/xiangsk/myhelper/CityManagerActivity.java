package com.xiangsk.myhelper;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;

import com.xiangsk.myhelper.bean.CityBean;
import com.xiangsk.myhelper.db.DatabaseHelper;

import java.util.List;

public class CityManagerActivity extends AppCompatActivity {
    private SearchView mSearchView;
    private RecyclerView mRecyclerView;
    private CityRecyclerAdapter mRecycleAdapter;
    private ListView mListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city_manager);

        // SearchView
        mSearchView = (SearchView) findViewById(R.id.city_m_sv);
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {return false;}

            @Override
            public boolean onQueryTextChange(String newText) {
                new CitySearchAsyncTask().execute(newText);
                return false;
            }
        });
        mSearchView.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                mListView.setVisibility(hasFocus ? View.VISIBLE : View.GONE);
            }
        });

        // ListView
        mListView = (ListView) findViewById(R.id.city_m_list);
        mListView.bringToFront();

        // RecyclerView
        mRecyclerView = (RecyclerView)findViewById(R.id.city_m_view);
        mRecycleAdapter = new CityRecyclerAdapter(CityManagerActivity.this);

        //设置布局管理器
        GridLayoutManager layoutManager = new GridLayoutManager(getBaseContext(), 3);
        mRecyclerView.setLayoutManager(layoutManager);

        //设置Adapter
        mRecyclerView.setAdapter(mRecycleAdapter);
        //设置增加或删除条目的动画
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
    }

    @Override
    protected void onResume() {
        super.onResume();

        List<CityBean> list = new DatabaseHelper(getBaseContext()).getUserCity();

        mRecycleAdapter.setCitys(list);
        mRecyclerView.getAdapter().notifyDataSetChanged();
    }

    class CitySearchAsyncTask extends AsyncTask<String, Integer, String> {
        private List<CityBean> cityList;
        @Override
        protected String doInBackground(String... params) {
            cityList = new DatabaseHelper(getBaseContext()).query(params[0]);
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            final ArrayAdapter adapter = new ArrayAdapter(getBaseContext(), android.R.layout.simple_list_item_1);
            if (!WeatherUtils.isEmpty(cityList)) {
                for (CityBean cityBean : cityList) {
                    adapter.add(cityBean.getFullName());
                }
            }

            mListView.setAdapter(adapter);
            mListView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    CityBean cityBean = cityList.get(position);
                    long result = new DatabaseHelper(getBaseContext()).addUserCity(cityBean.getCode());

                    if (result > 0) {
                        mRecycleAdapter.addCity(cityBean);
                        mRecycleAdapter.notifyDataSetChanged();

                        mSearchView.clearFocus();
                    }
                }
            });
        }
    }
}
