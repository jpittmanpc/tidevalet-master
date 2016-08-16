package com.tidevalet;

import android.app.Application;
import android.content.Context;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

public class App extends Application {

    private static App sInstance;
    private static Context mContext;
    public static App getInstance() {
        return sInstance;
    }
    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;
        App.mContext = getApplicationContext();
        DisplayImageOptions defaultOptions = new DisplayImageOptions
                .Builder()
                .cacheOnDisk(true)
                .cacheInMemory(true)
                .build();
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this)
                .defaultDisplayImageOptions(defaultOptions)
                .build();
        ImageLoader.getInstance()
                .init(config);
    }
    public static Context getAppContext() {
        return App.mContext;
    }
    public static String getSiteUrl() {
        return getAppContext().getResources().getString(R.string.tide_default_url);
    }
}
