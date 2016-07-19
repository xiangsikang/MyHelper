package com.xiangsk.myhelper.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.xiangsk.myhelper.R;
import com.xiangsk.myhelper.bean.CityBean;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by holmes-zhenyu on 2016/7/12.
 */
public class DatabaseHelper extends SQLiteOpenHelper {
    private final static String DB_NAME = "city.db";
    private final static String TABLE_CITY = "city";
    private final static String TABLE_USER_CITY = "user_city";
    private final static String[] CITY_COLUMNS = {"code", "name1", "name2", "name3"};
    private final static String[] USER_CITY_COLUMNS = {"code"};
    private final static int VERSION = 1;

    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "create table if not exists " + TABLE_CITY + " (code integer,name1 varchar(60),name2 varchar(60),name3 varchar(60))";
        db.execSQL(sql);

        sql = "create table if not exists " + TABLE_USER_CITY + " (code integer primary key)";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public List<CityBean> query(String name) {
        String sql = "select * from city where name1 like ? or name2 like ? or name3 like ? limit 10";
        String likeName = "%" + name + "%";
        String[] selectionArgs = {likeName, likeName, likeName};

        Cursor c = getReadableDatabase().rawQuery(sql, selectionArgs);

        return convertCityList(c);
    }

    private List<CityBean> convertCityList(Cursor c) {
        List<CityBean> resultList = new ArrayList<>();
        while (c.moveToNext()) {
            CityBean cb = new CityBean();
            cb.setCode(c.getInt(c.getColumnIndex("code")));
            cb.setName1(c.getString(c.getColumnIndex("name1")));
            cb.setName2(c.getString(c.getColumnIndex("name2")));
            cb.setName3(c.getString(c.getColumnIndex("name3")));

            resultList.add(cb);
        }
        return resultList;
    }

    public List<CityBean> getUserCity() {
        String sql = "select a.* from city a,user_city b where a.code=b.code";
        Cursor c = getReadableDatabase().rawQuery(sql, null);

        return convertCityList(c);
    }

    public List<Integer> getUserCityCode() {
        Cursor c = getReadableDatabase().rawQuery("select * from user_city", null);
        List<Integer> resultList = new ArrayList<>();
        while (c.moveToNext()) {
            resultList.add(c.getInt(c.getColumnIndex("code")));
        }
        return resultList;
    }

    public int deleteUserCity(int cityCode) {
        return getWritableDatabase().delete(TABLE_USER_CITY, "code=?", new String[]{String.valueOf(cityCode)});
    }
    public long addUserCity(int cityCode) {
        List<Integer> userCityList = getUserCityCode();
        if (userCityList.contains(cityCode)) {
            return 0;
        }

        ContentValues values = new ContentValues();
        values.put("code", cityCode);
        return getWritableDatabase().insert(TABLE_USER_CITY, null, values);
    }

    public static void importDB(Context context) {
        File dbFile = context.getDatabasePath(DB_NAME);
        if (dbFile.exists()) {
            return;
        }

        File dir = dbFile.getParentFile();
        if (!dir.exists()) {
            dir.mkdirs();
        }

        FileOutputStream fos = null;
        InputStream is = null;
        try {
            is = context.getResources().openRawResource(R.raw.city);
            fos = new FileOutputStream(dbFile);

            byte[] buffer = new byte[1024];
            int len = 0;
            while((len = is.read(buffer)) != -1) {
                fos.write(buffer, 0, len);
            }
            fos.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (null != fos) {
                    fos.close();
                }
                if (null != is) {
                    is.close();
                }
            }catch(IOException e) {
            }
        }


    }
}