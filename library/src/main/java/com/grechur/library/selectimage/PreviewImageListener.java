package com.grechur.library.selectimage;


import android.content.Context;

import java.util.List;

public interface PreviewImageListener {
    // 预览回调
    void preview(Context context, List<String> list);
}
