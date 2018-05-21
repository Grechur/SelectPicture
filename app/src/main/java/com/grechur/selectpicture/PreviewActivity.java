package com.grechur.selectpicture;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;

import static com.grechur.library.selectimage.Constant.EXTRA_RESULT;
import static com.grechur.library.selectimage.Constant.RESPONSE_ID;

public class PreviewActivity extends AppCompatActivity {
    public static final String TAG = "PreviewActivity";
    private ArrayList<String> mList;
    private TextView tv_preview;
    private TextView tv_delete;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview);
        tv_preview = findViewById(R.id.tv_preview);
        tv_delete = findViewById(R.id.tv_delete);
        mList = getIntent().getStringArrayListExtra(EXTRA_RESULT);
        if(mList!=null){
            Log.e(TAG,"mList:"+mList.toString());
            tv_preview.setText(mList.toString());

        }
        tv_preview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putStringArrayListExtra(EXTRA_RESULT, mList);
                setResult(202, intent);
                // 关闭当前页面
                finish();
            }
        });
        tv_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mList!=null&&mList.size()>0){
                    mList.remove(0);
                }
            }
        });
    }
}
