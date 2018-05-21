package com.grechur.selectpicture;

import android.app.Application;
import android.content.Context;

import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;

/**
 * Created by zz on 2018/5/17.
 */

public class SelectPictureApplication extends Application{
    public static RefWatcher getRefWatcher(Context context) {
        SelectPictureApplication application = (SelectPictureApplication) context.getApplicationContext();
        return application.mRefWatcher;
    }

    private RefWatcher mRefWatcher;
    @Override
    public void onCreate() {
        super.onCreate();
        if (LeakCanary.isInAnalyzerProcess(this)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return;
        }
        mRefWatcher = LeakCanary.install(this);
    }
}
