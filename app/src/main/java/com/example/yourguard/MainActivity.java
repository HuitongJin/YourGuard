package com.example.yourguard;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTabHost;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.TextView;

public class MainActivity extends FragmentActivity implements TabHost.OnTabChangeListener{

    private FragmentTabHost mTabHost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE); // 取消自带的标题栏
        setContentView(R.layout.activity_main);

        initHost(); // 初始化FragmentTabHost
        initTab(); // 初始化底部导航栏
        mTabHost.onTabChanged(TabDb.getTabsTxt()[0]);
    }

    /***
     * 初始化tab
     */
    private void initTab() {
        String[] tabs = TabDb.getTabsTxt();
        for (int i = 0; i < tabs.length; i++) {
            // 新建TabSpec
            TabHost.TabSpec tabSpec = mTabHost.newTabSpec(TabDb.getTabsTxt()[i]);
            // 设置view
            View view = LayoutInflater.from(this).inflate(R.layout.tab_foot, null);
            ((TextView)view.findViewById(R.id.foot_tv)).setText(TabDb.getTabsTxt()[i]);
            ((ImageView)view.findViewById(R.id.foot_iv)).setImageResource(TabDb.getTabsImg()[i]);
            tabSpec.setIndicator(view);
            mTabHost.addTab(tabSpec, TabDb.getFragment()[i], null);
        }
    }

    /***
     * 初始化Host
     */
    private void initHost() {
        mTabHost = (FragmentTabHost)findViewById(R.id.main_tab);
        // 调用setup方法, 设置view
        mTabHost.setup(this, getSupportFragmentManager(), R.id.main_view);
        // 去除分割线
        mTabHost.getTabWidget().setDividerDrawable(null);
        // 监听事件
        mTabHost.setOnTabChangedListener(this);
    }

    @Override
    public void onTabChanged(String tabId) {
        // 从分割线中获得多少个切换界面
        TabWidget tabWidget = mTabHost.getTabWidget();
        for (int i = 0; i < tabWidget.getChildCount(); i++) {
            View v = tabWidget.getChildAt(i);
            ImageView iv = (ImageView)v.findViewById(R.id.foot_iv);
            // 修改当前界面按钮的颜色图片
            if (i == mTabHost.getCurrentTab()) {
                iv.setImageResource(TabDb.getTabsImgLight()[i]);
            }else {
                iv.setImageResource(TabDb.getTabsImg()[i]);
            }
        }
    }
}
