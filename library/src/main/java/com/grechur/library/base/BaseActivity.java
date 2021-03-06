package com.grechur.library.base;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;


public abstract class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 设置布局layout
        setContentView();

        //Log.e("TAG", viewRoot + "");


        // 初始化头部
        initTitle();

        // 初始化界面
        initView();

        // 初始化数据
        initData();
    }

    // 初始化数据
    protected abstract void initData();

    // 初始化界面
    protected abstract void initView();

    // 初始化头部
    protected abstract void initTitle();

    // 设置布局layout
    protected abstract void setContentView();



    /**
     * findViewById
     *
     * @return View
     */
    protected <T extends View> T viewById(int viewId) {
        return (T) findViewById(viewId);
    }

    // 只能放一些通用的方法，基本每个Activity都需要使用的方法，readDataBase最好不要放进来 ，
    // 如果是两个或两个以上的地方要使用,最好写一个工具类。
    // 为什么？下周末会讲热修复  阿里开源的 divalk层的方法是怎么加载进来的   腾讯使用的分包问题  是性能方面的问题
    // 永远预留一层
}
