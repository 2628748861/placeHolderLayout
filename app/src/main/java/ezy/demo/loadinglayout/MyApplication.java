package ezy.demo.loadinglayout;

import android.app.Application;

import ezy.ui.layout.LoadingLayout;

public class MyApplication extends Application
{
    @Override
    public void onCreate() {
        super.onCreate();
        LoadingLayout.setGlobalConfig(R.layout.ayout_empty,R.layout.layout_error,R.layout.layout_loading);
    }
}
