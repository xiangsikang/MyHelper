package com.xiangsk.myhelper;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.xiangsk.myhelper.bean.Forecast;

import java.util.List;

/**
 * Created by holmes-zhenyu on 2016/7/1.
 */
public class MyRecyclerAdapter extends RecyclerView.Adapter<MyRecyclerAdapter.MyViewHolder> {
    private Context mContext;
    private LayoutInflater inflater;

    private List<Forecast> mDatas;
    private float mMaxTemp = -99;
    private float mMinTemp = 99;

    private float mTop = 0F;
    private float mMiddle = 0.1F;
    private float mBottom = 0.25F;

    public MyRecyclerAdapter(Context context, List<Forecast> datas){
        this. mContext = context;
        this. mDatas = datas;
        inflater = LayoutInflater.from(mContext);

        for (Forecast forecast : mDatas) {
            float tempHigh = WeatherUtils.getTempValue(forecast.getHigh());
            float tempLow = WeatherUtils.getTempValue(forecast.getLow());

            if (tempHigh > mMaxTemp) {
                mMaxTemp = tempHigh;
            }
            if (tempLow < mMinTemp) {
                mMinTemp = tempLow;
            }
        }
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    //填充onCreateViewHolder方法返回的holder中的控件
    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        Forecast forecast = mDatas.get(position);
        holder.item_date.setText(forecast.getDate().substring(0, forecast.getDate().indexOf("日")+1));
        holder.item_wind.setText(forecast.getFengxiang());
        holder.item_wind_level.setText(forecast.getFengli());
        holder.item_temp_high.setText(forecast.getHigh().substring(2));
        holder.item_weather.setText(forecast.getType());
        holder.item_weather_img.setImageResource(WeatherUtils.getResourceId(forecast.getType()));
        holder.item_temp_low.setText(forecast.getLow().substring(2));

        float tempHigh = WeatherUtils.getTempValue(forecast.getHigh());
        float tempLow = WeatherUtils.getTempValue(forecast.getLow());

        ViewGroup vg = (ViewGroup)holder.item_temp_high.getParent();
        float totalHeight = vg.getLayoutParams().height;
        float oneHeight = totalHeight * (1- mTop - mMiddle - mBottom) / (mMaxTemp - mMinTemp);

        //float padding = holder.item_temp_high.getCompoundPaddingBottom();
        holder.item_temp_high.setTranslationY(oneHeight * (mMaxTemp - tempHigh) + totalHeight* mTop);
        holder.item_temp_low.setTranslationY(oneHeight * (mMaxTemp - tempLow) + totalHeight*(mTop + mMiddle));

    }

    //重写onCreateViewHolder方法，返回一个自定义的ViewHolder
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.date_item, parent, false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    class MyViewHolder extends RecyclerView.ViewHolder{
        TextView item_date;
        TextView item_wind;
        TextView item_wind_level;
        TextView item_temp_high;
        TextView item_weather;
        ImageView item_weather_img;
        TextView item_temp_low;

        public MyViewHolder(View view) {
            super(view);
            item_date=(TextView) view.findViewById(R.id.item_date);
            item_wind=(TextView) view.findViewById(R.id.item_wind);
            item_wind_level=(TextView) view.findViewById(R.id.item_wind_level);
            item_temp_high=(TextView) view.findViewById(R.id.item_temp_high);
            item_weather=(TextView) view.findViewById(R.id.item_weather);
            item_weather_img=(ImageView) view.findViewById(R.id.item_weather_img);
            item_temp_low=(TextView) view.findViewById(R.id.item_temp_low);
        }

    }
}
