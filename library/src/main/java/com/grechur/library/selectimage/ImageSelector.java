package com.grechur.library.selectimage;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.util.Log;

import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static com.grechur.library.selectimage.Constant.EXTRA_DEFAULT_SELECTED_LIST;
import static com.grechur.library.selectimage.Constant.EXTRA_SELECT_COUNT;
import static com.grechur.library.selectimage.Constant.EXTRA_SELECT_MODE;
import static com.grechur.library.selectimage.Constant.EXTRA_SHOW_CAMERA;
import static com.grechur.library.selectimage.Constant.MODE_MULTI;
import static com.grechur.library.selectimage.Constant.MODE_SINGLE;

public class ImageSelector {
    // 最多可以选择多少张图片 - 默认8张
    private int mMaxCount = 9;
    // 选择图片的模式 - 默认多选
    private int mMode = MODE_MULTI;
    // 是否显示拍照的相机
    private boolean mShowCamera = true;
    // 原始的图片
    private ArrayList<String> mOriginData;

    //添加一个池子，放置ImageSelector
    private static Map<String, ImageSelector> mImgMap = new ConcurrentHashMap<>();
    private static ImageSelector mImageSelector;

    private PreviewImageListener mListener;

    private ImageSelector() {
    }

    public static ImageSelector create(boolean hasMap) {
        //是否需要池子
        if(hasMap){
            mImageSelector = mImgMap.get(Constant.EXTRA_KEY);
            if(mImageSelector == null){
                mImageSelector = new ImageSelector();
                mImgMap.put(Constant.EXTRA_KEY,mImageSelector);
                return mImageSelector;
            }
            return mImageSelector;
        }
        return new ImageSelector();
    }


    public ImageSelector addPreviewListener(PreviewImageListener listener){
        this.mListener = listener;
        return this;
    }
    public PreviewImageListener getPreviewListener(){
        return mListener;
    }
    /**
     * 单选模式
     */
    public ImageSelector single() {
        mMode = MODE_SINGLE;
        return this;
    }

    /**
     * 多选模式
     */
    public ImageSelector multi() {
        mMode = MODE_MULTI;
        return this;
    }

    /**
     * 设置可以选多少张图片
     */
    public ImageSelector count(int count) {
        mMaxCount = count;
        return this;
    }

    /**
     * 是否显示相机
     */
    public ImageSelector showCamera(boolean showCamera) {
        mShowCamera = showCamera;
        return this;
    }

    /**
     * 原来选择好的图片
     */
    public ImageSelector origin(ArrayList<String> originList) {
        this.mOriginData = originList;
        return this;
    }


    /**
     * 启动执行 权限6.0自己需要去申请，也可以用我的权限申请框架
     */
    public void start(Activity activity, int requestCode) {
        Intent intent = new Intent(activity, SelectImageActivity.class);
        addParamsByIntent(intent);
        activity.startActivityForResult(intent, requestCode);
    }

    /**
     * 启动执行 权限6.0自己需要去申请，也可以用我的权限申请框架
     */
    public void start(Fragment fragment, int requestCode) {
        Intent intent = new Intent(fragment.getContext(), SelectImageActivity.class);
        addParamsByIntent(intent);
        fragment.startActivityForResult(intent, requestCode);
    }

    /**
     * 给Intent添加参数
     *
     * @param intent
     */
    private void addParamsByIntent(Intent intent) {
        intent.putExtra(EXTRA_SHOW_CAMERA, mShowCamera);
        intent.putExtra(EXTRA_SELECT_COUNT, mMaxCount);
        if (mOriginData != null && mMode == MODE_MULTI) {
            intent.putStringArrayListExtra(EXTRA_DEFAULT_SELECTED_LIST, mOriginData);
        }
        intent.putExtra(EXTRA_SELECT_MODE, mMode);
    }


    public void clearMap(){
        //清空池子
        mImgMap.clear();
        //清除预览监听，否则会引起内存问题
        mListener = null;
    }
}
