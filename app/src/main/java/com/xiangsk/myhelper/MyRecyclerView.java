package com.xiangsk.myhelper;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by holmes-zhenyu on 2016/7/6.
 */
public class MyRecyclerView extends RecyclerView {
    private Paint mPaint;

    public MyRecyclerView(Context context) {
        super(context);

        init();
    }

    public MyRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        init();
    }

    public MyRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        init();
    }

    private void init() {
        mPaint = new Paint();
        mPaint.setColor(Color.BLACK);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setAntiAlias(true);
        mPaint.setStrokeWidth(8);
    }

    @Override
    public void onDraw(Canvas c) {
        super.onDraw(c);

        Path[] paths = getTempPaths();
        if (null != paths) {
            for (Path path : paths) {
                if (null != path) {
                    c.drawPath(path, mPaint);
                }
            }
        }

    }

    private Path[] getTempPaths() {
        Path highPath = new Path();
        Path lowPath = new Path();

        List<float[]> highXYList = new ArrayList<>();
        List<float[]> lowXYList = new ArrayList<>();

        List<Float> hxList = new ArrayList<>();
        List<Float> hyList = new ArrayList<>();
        List<Float> lxList = new ArrayList<>();
        List<Float> lyList = new ArrayList<>();

        for (int i = 0; i < getChildCount(); i++) {
            MyRecyclerAdapter.MyViewHolder myViewHolder = (MyRecyclerAdapter.MyViewHolder)findViewHolderForItemId(i);
            if (null != myViewHolder) {
                float[] hxy = getXY(myViewHolder.item_temp_high, false);
                hxList.add(hxy[0]);
                hyList.add(hxy[1]);

                float[] lxy = getXY(myViewHolder.item_temp_low, true);
                lxList.add(lxy[0]);
                lyList.add(lxy[1]);
            }
        }

        return new Path[]{buildPath(hxList, hyList), buildPath(lxList, lyList)};
    }

    private Path buildPath(List<Float> xList, List<Float> yList) {
        if (WeatherUtils.isEmpty(xList) || WeatherUtils.isEmpty(yList)) {
            return null;
        }

        List<Cubic> calculate_hx = calculate(xList);
        List<Cubic> calculate_hy = calculate(yList);

        Path path = new Path();
        path.moveTo(calculate_hx.get(0).eval(0), calculate_hy.get(0).eval(0));

        int STEPS = 12;

        for (int i = 0; i < calculate_hx.size(); i++) {
            for (int j = 1; j <= STEPS; j++) {
                float u = j / (float) STEPS;
                path.lineTo(calculate_hx.get(i).eval(u), calculate_hy.get(i) .eval(u));
            }
        }

        return path;
    }

    private float[] getXY(View view, boolean isTop) {
        float[] xy = new float[2];
        ViewGroup vg = (ViewGroup)view.getParent();
        ViewGroup vg2 = (ViewGroup)vg.getParent();

        xy[0] = vg2.getX() + vg.getX() + view.getTranslationX() + view.getWidth()/2;
        xy[1] = vg2.getY() + vg.getY() + view.getTranslationY() + (isTop ? 0 : view.getHeight());

        return xy;
    }

    private List<Cubic> calculate(List<Float> x) {
        int n = x.size() - 1;
        float[] gamma = new float[n + 1];
        float[] delta = new float[n + 1];
        float[] D = new float[n + 1];
        int i;
        gamma[0] = 1.0f / 2.0f;
        for (i = 1; i < n; i++) {
            gamma[i] = 1 / (4 - gamma[i - 1]);
        }
        gamma[n] = 1 / (2 - gamma[n - 1]);

        delta[0] = 3 * (x.get(1) - x.get(0)) * gamma[0];
        for (i = 1; i < n; i++) {
            delta[i] = (3 * (x.get(i + 1) - x.get(i - 1)) - delta[i - 1])
                    * gamma[i];
        }
        delta[n] = (3 * (x.get(n) - x.get(n - 1)) - delta[n - 1]) * gamma[n];

        D[n] = delta[n];
        for (i = n - 1; i >= 0; i--) {
            D[i] = delta[i] - gamma[i] * D[i + 1];
        }

		/* now compute the coefficients of the cubics */
        List<Cubic> cubics = new LinkedList<Cubic>();
        for (i = 0; i < n; i++) {
            Cubic c = new Cubic(x.get(i), D[i], 3 * (x.get(i + 1) - x.get(i))
                    - 2 * D[i] - D[i + 1], 2 * (x.get(i) - x.get(i + 1)) + D[i]
                    + D[i + 1]);
            cubics.add(c);
        }
        return cubics;
    }

    static class Cubic {

        float a,b,c,d;         /* a + b*u + c*u^2 +d*u^3 */

        public Cubic(float a, float b, float c, float d){
            this.a = a;
            this.b = b;
            this.c = c;
            this.d = d;
        }


        /** evaluate cubic */
        public float eval(float u) {
            return (((d*u) + c)*u + b)*u + a;
        }
    }
}
