package com.seabreeze.robot.base.widget.wave;

import android.content.Context;
import android.graphics.Path;

import static me.jessyan.autosize.utils.AutoSizeUtils.dp2px;

/**
 * User: milan
 * Time: 2019/3/27 2:12
 * Des: 多重水波纹
 */
class Wave {

    private Context mContext;
    Path path;          //水波路径
    int width;          //画布宽度（2倍波长）
    int wave;           //波幅（振幅）
    float offsetX;        //水波的水平偏移量
    float offsetY;        //水波的竖直偏移量
    float velocity;       //水波移动速度（像素/秒）
    private float scaleX;       //水平拉伸比例
    private float scaleY;       //竖直拉伸比例
    private int curWave;
//    int startColor;     //开始颜色
//    int closeColor;     //结束颜色
//    float alpha;        //颜色透明度

    /**
     * 通过参数构造一个水波对象
     *
     * @param offsetX  水平偏移量
     * @param offsetY  竖直偏移量
     * @param velocity 移动速度（像素/秒）
     * @param scaleX   水平拉伸量
     * @param scaleY   竖直拉伸量
     * @param wave     波幅（波宽度）
     */
    Wave(Context context, int offsetX, int offsetY, int velocity, float scaleX, float scaleY, int wave) {
        mContext = context;
        this.wave = wave;           //波幅（波宽）
        this.scaleX = scaleX;       //水平拉伸量
        this.scaleY = scaleY;       //竖直拉伸量
        this.offsetX = offsetX;     //水平偏移量
        this.offsetY = offsetY;     //竖直偏移量
        this.velocity = velocity;   //移动速度（像素/秒）
        this.path = new Path();
    }

    protected void updateWavePath(int w, int h, int waveHeight, boolean fullScreen, float progress) {
        this.wave = waveHeight;
        this.width = (int) (2 * scaleX * w);  //画布宽度（2倍波长）
        this.path = buildWavePath(width, h, fullScreen, progress);
    }

    protected void updateWavePath(int w, int h, float progress) {
        int wave = (int) (scaleY * this.wave);//计算拉伸之后的波幅
        float maxWave = h * Math.max(0, (1 - progress));
        if (wave > maxWave) {
            wave = (int) maxWave;
        }

        if (curWave != wave) {
            this.width = (int) (2 * scaleX * w);  //画布宽度（2倍波长）
            this.path = buildWavePath(width, h, true, progress);
        }
    }

    protected Path buildWavePath(int width, int height, boolean fullScreen, float progress) {
        int DP = (int) dp2px(mContext, 1);//一个dp在当前设备表示的像素量（水波的绘制精度设为一个dp单位）
        if (DP < 1) {
            DP = 1;
        }

        int wave = (int) (scaleY * this.wave);//计算拉伸之后的波幅

        if (fullScreen) {
            float maxWave = height * Math.max(0, (1 - progress));
            if (wave > maxWave) {
                wave = (int) maxWave;
            }
        }
        this.curWave = wave;

//        Path path = new Path();
        path.reset();

        path.moveTo(0, 0);
        path.lineTo(0, height - wave);

        if (wave > 0) {
            for (int x = DP; x < width; x += DP) {
                path.lineTo(x, height - wave - wave * (float) Math.sin(4.0 * Math.PI * x / width));
            }
        }

        path.lineTo(width, height - wave);
        path.lineTo(width, 0);
        path.close();
        return path;
    }
}