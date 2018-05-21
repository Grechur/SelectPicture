package com.grechur.selectpicture;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.grechur.library.selectimage.Constant;
import com.grechur.library.selectimage.ImageSelector;
import com.grechur.library.selectimage.PreviewImageListener;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.functions.Consumer;

import static com.grechur.library.selectimage.Constant.EXTRA_RESULT;


public class MainActivity extends AppCompatActivity{
    private ArrayList<String> mImageList;
    private final int SELECT_IMAGE_REQUEST = 0x0011;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        RxPermissions rxPermissions = new RxPermissions(this);
        rxPermissions.request(Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE)
        .subscribe(new Consumer<Boolean>() {
            @Override
            public void accept(Boolean aBoolean) throws Exception {
                if(aBoolean){
                    Log.e("TAG","授权成功");
                }else{
                    Log.e("TAG","授权失败");
                }
            }
        });
        mImageList = new ArrayList<>();
        findViewById(R.id.text).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 第一个只关注想要什么，良好的封装性，不要暴露太多
                ImageSelector.create(true).count(9).multi().origin(mImageList)
                        .showCamera(false)
                        .addPreviewListener(new PreviewImageListener() {
                            @Override
                            public void preview(Context context, List<String> list) {
                                Activity activity = (Activity) context;
                                Toast.makeText(context,"进入预览list:"+list.toString(),Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent();
                                intent.setClass(context,PreviewActivity.class);
                                intent.putStringArrayListExtra(EXTRA_RESULT, (ArrayList<String>) list);
                                activity.startActivityForResult(intent, Constant.REQUEST_ID);

                            }
                        })
                        .start(MainActivity.this, SELECT_IMAGE_REQUEST);
            }
        });

        findViewById(R.id.text2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImageSelector.create(false).count(6).multi().origin(mImageList)
                        .showCamera(true)
                        .start(MainActivity.this, SELECT_IMAGE_REQUEST);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == SELECT_IMAGE_REQUEST && data != null){
            mImageList = data.getStringArrayListExtra(EXTRA_RESULT);
            // 做一下显示
            Log.e("TAG",mImageList.toString());
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }
}
