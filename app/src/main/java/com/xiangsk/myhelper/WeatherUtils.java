package com.xiangsk.myhelper;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by holmes-zhenyu on 2016/7/5.
 */
public class WeatherUtils {
    public static Map<String, Integer> imageMap;
    static {
        imageMap = new HashMap<String, Integer>();

        imageMap.put("云雾", R.drawable.weather_cloudy_fog);
        imageMap.put("阴", R.drawable.weather_cloudy);
        imageMap.put("雾", R.drawable.weather_fog);
        imageMap.put("月亮", R.drawable.weather_moon);
        imageMap.put("多云", R.drawable.weather_partlycloudy);
        imageMap.put("阵雨", R.drawable.weather_showers);
        imageMap.put("大雨", R.drawable.weather_rainy_h);
        imageMap.put("中到大雨", R.drawable.weather_rainy_m);
        imageMap.put("中雨", R.drawable.weather_rainy_m);
        imageMap.put("小到中雨", R.drawable.weather_rainy_s);
        imageMap.put("小雨", R.drawable.weather_rainy_s);
        imageMap.put("雨夹雪", R.drawable.weather_rainy_snowy);
        imageMap.put("大雪", R.drawable.weather_snowy_h);
        imageMap.put("中雪", R.drawable.weather_snowy_m);
        imageMap.put("小雪", R.drawable.weather_snowy_s);
        imageMap.put("晴", R.drawable.weather_sunny);
        imageMap.put("大雷雨", R.drawable.weather_thunder_rainy_h);
        imageMap.put("雷阵雨", R.drawable.weather_thunder_rainy_m);
        imageMap.put("小雷雨", R.drawable.weather_thunder_rainy);
        imageMap.put("雷", R.drawable.weather_thunder);
        imageMap.put("风", R.drawable.weather_windy);
    }

    public static int getResourceId(String key) {
        Integer rid = imageMap.get(key);
        return null == rid ? R.drawable.smile : rid;
    }

    public static float getTempValue(String temp) {
        return Float.parseFloat(temp.substring(2, temp.length() - 1).trim());
    }

    public static boolean isEmpty(Collection<?> collection) {
        return null == collection || collection.isEmpty();
    }
}
