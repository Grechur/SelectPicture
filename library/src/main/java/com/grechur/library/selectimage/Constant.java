package com.grechur.library.selectimage;

/**
 * Created by zz on 2018/5/18.
 */

public class Constant {
    // 是否显示相机的EXTRA_KEY
    public static final String EXTRA_SHOW_CAMERA = "EXTRA_SHOW_CAMERA";
    // 总共可以选择多少张图片的EXTRA_KEY
    public static final String EXTRA_SELECT_COUNT = "EXTRA_SELECT_COUNT";
    // 原始的图片路径的EXTRA_KEY
    public static final String EXTRA_DEFAULT_SELECTED_LIST = "EXTRA_DEFAULT_SELECTED_LIST";
    // 选择模式的EXTRA_KEY
    public static final String EXTRA_SELECT_MODE = "EXTRA_SELECT_MODE";
    // 返回选择图片列表的EXTRA_KEY
    public static final String EXTRA_RESULT = "EXTRA_RESULT";
    // ImageSelector的key
    public static final String EXTRA_KEY = "EXTRA_KEY";


    // 加载所有的数据
    public static final int LOADER_TYPE = 0x0021;

    /*****************
     * 获取传递过来的参数
     *****************/
    // 选择图片的模式 - 多选
    public static final int MODE_MULTI = 0x0011;

    // 选择图片的模式 - 单选
    public static final int MODE_SINGLE = 0x0012;

    //预览的
    public static final int REQUEST_ID = 0x0013;
    public static final int RESPONSE_ID = 0x0014;
}
