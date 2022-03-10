package com.example.yourguard;


import com.example.yourguard.Fragment.CenterFm;
import com.example.yourguard.Fragment.MeasureFm;
import com.example.yourguard.Fragment.ResultFm;

public class TabDb {
    // 获得底部所有项
    public static String[] getTabsTxt() {
        String[] tabs = {"测量", "报告", "中心"};
        return tabs;
    }

    // 获得所有碎片
    public static Class[] getFragment() {
        Class[] cls = {MeasureFm.class, ResultFm.class, CenterFm.class};
        return cls;
    }

    // 获得点击前的图片
    public static int[] getTabsImg() {
        int[] img = {R.drawable.measure_icon, R.drawable.result_icon, R.drawable.center_icon};
        return img;
    }

    // 获得点击后的图片
    public static int[] getTabsImgLight() {
        int[] img = {R.drawable.measure_selected, R.drawable.result_selected, R.drawable.center_selected};
        return img;
    }

}
