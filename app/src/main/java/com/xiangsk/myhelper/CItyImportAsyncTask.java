package com.xiangsk.myhelper;

import android.content.Context;
import android.os.AsyncTask;

import com.xiangsk.myhelper.db.DatabaseHelper;

/**
 * Created by holmes-zhenyu on 2016/7/12.
 */
public class CItyImportAsyncTask extends AsyncTask<String,Integer,String> {
    private Context mContext;
    public CItyImportAsyncTask(Context context) {
        this.mContext = context;
    }

    @Override
    protected String doInBackground(String... params) {
        DatabaseHelper.importDB(mContext);
        return null;
    }
}
