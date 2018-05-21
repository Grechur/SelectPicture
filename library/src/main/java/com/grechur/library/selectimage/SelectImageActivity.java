package com.grechur.library.selectimage;

import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.provider.MediaStore;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.grechur.library.R;
import com.grechur.library.base.BaseActivity;

import java.util.ArrayList;

import static com.grechur.library.selectimage.Constant.EXTRA_DEFAULT_SELECTED_LIST;
import static com.grechur.library.selectimage.Constant.EXTRA_RESULT;
import static com.grechur.library.selectimage.Constant.EXTRA_SHOW_CAMERA;
import static com.grechur.library.selectimage.Constant.REQUEST_ID;

public class SelectImageActivity extends BaseActivity implements View.OnClickListener, SelectImageListener {

    public static final String TAG = "SelectImageActivity";

    // 单选或者多选，int类型的type
    private int mMode = Constant.MODE_MULTI;
    // int 类型的图片张数
    private int mMaxCount = 8;
    // boolean 类型的是否显示拍照按钮
    private boolean mShowCamera = true;
    // ArraryList<String> 已经选择好的图片
    private ArrayList<String> mResultList;


    private RecyclerView mImageListRv;
    private TextView mSelectNumTv;
    private TextView mSelectPreview;
    private TextView select_finish;

    private ImageSelector mImageSelector;
    private PreviewImageListener mListener;
    public void setPreviewListener(PreviewImageListener listener){
        this.mListener = listener;
    }
    @Override
    protected void initData() {
        // 1.获取传递过来的参数
        Intent intent = getIntent();
        mMode = intent.getIntExtra(Constant.EXTRA_SELECT_MODE, mMode);
        mMaxCount = intent.getIntExtra(Constant.EXTRA_SELECT_COUNT, mMaxCount);
        mShowCamera = intent.getBooleanExtra(EXTRA_SHOW_CAMERA, mShowCamera);
        mResultList = intent.getStringArrayListExtra(EXTRA_DEFAULT_SELECTED_LIST);
        if (mResultList == null) {
            mResultList = new ArrayList<>();
        }

        mImageSelector = ImageSelector.create(true);
        if(mImageSelector!=null){
            setPreviewListener(mImageSelector.getPreviewListener());
        }

        // 2.初始化本地图片数据
        initImageList();

        // 3.改变显示
        exchangeViewShow();
    }

    // 改变布局显示 需要及时更新，每次点击的地方下手
    private void exchangeViewShow() {
        if(mImageSelector!=null){
            mSelectPreview.setVisibility(View.VISIBLE);
        }else{
            mSelectPreview.setVisibility(View.GONE);
        }
        // 预览是不是可以点击，显示什么颜色
        if(mResultList.size()>0){
            // 至少选择了一张
            mSelectPreview.setEnabled(true);
            mSelectPreview.setOnClickListener(this);
        }else{
            // 一张都没选
            mSelectPreview.setEnabled(false);
            mSelectPreview.setOnClickListener(null);
        }



        // 中间图片的张数也要显示
        mSelectNumTv.setText(mResultList.size()+"/"+mMaxCount);
    }

    /**
     * 2.ContentProvider获取内存卡中所有的图片
     */
    private void initImageList() {
        // 耗时操作，开线程，AsyncTask,
        // int id 查询全部
        getLoaderManager().initLoader(Constant.LOADER_TYPE, null, mLoaderCallback);
    }

    /**
     * 加载图片的CallBack
     */
    private LoaderManager.LoaderCallbacks<Cursor> mLoaderCallback =
            new LoaderManager.LoaderCallbacks<Cursor>() {
                private final String[] IMAGE_PROJECTION = {
                        MediaStore.Images.Media.DATA,
                        MediaStore.Images.Media.DISPLAY_NAME,
                        MediaStore.Images.Media.DATE_ADDED,
                        MediaStore.Images.Media.MIME_TYPE,
                        MediaStore.Images.Media.SIZE,
                        MediaStore.Images.Media._ID};

                @Override
                public Loader<Cursor> onCreateLoader(int id, Bundle args) {
                    // 查询数据库一样 语句
                    CursorLoader cursorLoader = new CursorLoader(SelectImageActivity.this,
                            MediaStore.Images.Media.EXTERNAL_CONTENT_URI, IMAGE_PROJECTION,
                            IMAGE_PROJECTION[4] + ">0 AND " + IMAGE_PROJECTION[3] + "=? OR "
                                    + IMAGE_PROJECTION[3] + "=? ",
                            new String[]{"image/jpeg", "image/png"}, IMAGE_PROJECTION[2] + " DESC");
                    return cursorLoader;
                }

                @Override
                public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
                    // 解析，封装到集合  只保存String路径
                    if (data != null && data.getCount() > 0) {
                        ArrayList<String> images = new ArrayList<>();

                        // 如果需要显示拍照，就在第一个位置上加一个空String
                        if(mShowCamera){
                            images.add("");
                        }


                        // 不断的遍历循环
                        while (data.moveToNext()) {
                            // 只保存路径
                            String path = data.getString(data.getColumnIndexOrThrow(IMAGE_PROJECTION[0]));
                            images.add(path);
                        }

                        // 显示列表数据
                        showImageList(images);
                    }
                }

                @Override
                public void onLoaderReset(Loader<Cursor> loader) {

                }
            };

    /**
     * 3.展示获取到的图片显示到列表
     * @param images
     */
    private void showImageList(ArrayList<String> images) {
        SelectImageListAdapter listAdapter = new SelectImageListAdapter(this,images,mResultList,mMaxCount);
        listAdapter.setOnSelectImageListener(this);
        mImageListRv.setLayoutManager(new GridLayoutManager(this,4));
        mImageListRv.setAdapter(listAdapter);
    }

    @Override
    protected void initView() {
        mImageListRv = findViewById(R.id.image_list_rv);
        mSelectNumTv = findViewById(R.id.select_num);
        mSelectPreview = findViewById(R.id.select_preview);
        select_finish = findViewById(R.id.select_finish);
        select_finish.setOnClickListener(this);
    }

    @Override
    protected void initTitle() {
//        DefaultNavigationBar navigationBar = new
//                DefaultNavigationBar.Builder(this)
//                .setTitle("所有图片")
//                .builder();
//        // 改变状态栏的颜色
//        StatusBarUtil.statusBarTintColor(this, Color.parseColor("#261f1f"));
    }

    @Override
    protected void setContentView() {
        setContentView(R.layout.activity_image_selector);
    }

    @Override
    public void onClick(View v) {
        // 图片预览不写了
        int i = v.getId();
        if (i == R.id.select_finish) {// 选择好的图片传过去
            forResult();
        } else if (i == R.id.select_preview){
            if(mListener!=null){
                mListener.preview(this,mResultList);
            }
        }
    }

    private void forResult(){
        Intent intent = new Intent();
        intent.putStringArrayListExtra(EXTRA_RESULT, mResultList);
        setResult(RESULT_OK, intent);
        // 关闭当前页面
        finish();
    }

    @Override
    public void select() {
        exchangeViewShow();
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // 1.第一个要把图片加到集合

        // 2.调用sureSelect()方法


        // 3.通知系统本地有图片改变，下次进来可以找到这张图片
        // notify system the image has change
        // sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(mTempFile));

            if(requestCode == REQUEST_ID && data != null){
                mResultList = data.getStringArrayListExtra(EXTRA_RESULT);
                // 做一下显示
                Log.e(TAG,mResultList.toString());
                forResult();
            }

    }

    @Override
    protected void onDestroy() {
        //这一步很重要
        mImageSelector.clearMap();
        mImageSelector=null;
        mListener = null;
        super.onDestroy();
    }
}
