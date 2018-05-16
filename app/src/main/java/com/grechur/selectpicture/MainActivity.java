package com.grechur.selectpicture;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.grechur.library.selectimage.ImageSelector;
import com.grechur.library.selectimage.SelectImageActivity;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {
    private ArrayList<String> mImageList;
    private final int SELECT_IMAGE_REQUEST = 0x0011;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mImageList = new ArrayList<>();
        findViewById(R.id.text).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 第一个只关注想要什么，良好的封装性，不要暴露太多
                ImageSelector.create().count(9).multi().origin(mImageList)
                        .showCamera(true).start(MainActivity.this, SELECT_IMAGE_REQUEST);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK){
            if(requestCode == SELECT_IMAGE_REQUEST && data != null){
                mImageList = data.getStringArrayListExtra(SelectImageActivity.EXTRA_RESULT);
                // 做一下显示
                Log.e("TAG",mImageList.toString());
            }
        }
    }
}
