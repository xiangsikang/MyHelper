package com.xiangsk.myhelper;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.xiangsk.myhelper.bean.CityBean;
import com.xiangsk.myhelper.bean.Forecast;
import com.xiangsk.myhelper.db.DatabaseHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by holmes-zhenyu on 2016/7/1.
 */
public class CityRecyclerAdapter extends RecyclerView.Adapter<CityRecyclerAdapter.CityViewHolder> {
    private Context mContext;
    private LayoutInflater inflater;

    private List<CityBean> mCitys;

    public CityRecyclerAdapter(Context context){
        this.mContext = context;
        this.mCitys = new ArrayList<>(0);
        inflater = LayoutInflater.from(mContext);
    }

    public void setCitys(List<CityBean> citys) {
        this.mCitys = citys;
    }

    public void addCity(CityBean city) {
        mCitys.add(city);
    }

    @Override
    public int getItemCount() {
        return mCitys.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    //填充onCreateViewHolder方法返回的holder中的控件
    @Override
    public void onBindViewHolder(CityViewHolder holder, final int position) {
        final CityBean city = mCitys.get(position);
        if (city.getCode() > 0) {
            holder.city_m_name.setText(mCitys.get(position).getName1());
            holder.city_m_img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mCitys.remove(position);
                    new DatabaseHelper(mContext).deleteUserCity(city.getCode());
                    notifyDataSetChanged();
                }
            });
        }
    }


    //重写onCreateViewHolder方法，返回一个自定义的ViewHolder
    @Override
    public CityViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.city_m_item, parent, false);
        CityViewHolder holder = new CityViewHolder(view);
        return holder;
    }

    class CityViewHolder extends RecyclerView.ViewHolder{
        TextView city_m_name;
        ImageView city_m_img;

        public CityViewHolder(View view) {
            super(view);
            city_m_name = (TextView) view.findViewById(R.id.city_m_name);
            city_m_img = (ImageView) view.findViewById(R.id.city_m_img);
        }

    }
}
