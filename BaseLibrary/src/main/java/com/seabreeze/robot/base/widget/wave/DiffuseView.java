package com.seabreeze.robot.base.widget.wave;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.ColorRes;

import com.elvishew.xlog.XLog;
import com.seabreeze.robot.base.R;

import java.util.ArrayList;
import java.util.List;

/**
 * User: milan
 * Time: 2019/3/27 2:12
 * Des:
 */
public class DiffuseView extends View {

    /**
     * 扩散圆圈颜色
     */
    private int mColor = getResources().getColor(android.R.color.darker_gray);
    /**
     * 圆圈中心颜色
     */
    private int mCoreColor = getResources().getColor(android.R.color.white);
    /**
     * 圆圈中心图片
     */
    private Bitmap mBitmap;
    /**
     * 中心圆半径
     */
    private float mCoreRadius = 250;
    /**
     * 扩散圆宽度
     */
    private int mDiffuseWidth = 40;
    /**
     * 最大宽度
     */
    private Integer mMaxWidth = 600;
    /**
     * 扩散速度
     */
    private int mDiffuseSpeed = 100;
    /**
     * 颜色变换速度
     */
    private int mAlphaDiffuseSpeed = 30;

    /**
     * 是否正在扩散中
     */
    private boolean mIsDiffuse = false;
    // 透明度集合
    private List<Integer> mAlphas = new ArrayList<>();
    // 扩散圆半径集合
    private List<Integer> mWidths = new ArrayList<>();

    private Paint mPaint;
    private Paint mPaint2;

    public DiffuseView(Context context) {
        this(context, null);
    }

    public DiffuseView(Context context, AttributeSet attrs) {
        this(context, attrs, -1);
    }

    public DiffuseView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.DiffuseView, defStyleAttr, 0);
        mColor = a.getColor(R.styleable.DiffuseView_diffuse_color, mColor);
        mCoreColor = a.getColor(R.styleable.DiffuseView_diffuse_coreColor, mCoreColor);
        mCoreRadius = a.getFloat(R.styleable.DiffuseView_diffuse_coreRadius, mCoreRadius);
        mDiffuseWidth = a.getInt(R.styleable.DiffuseView_diffuse_width, mDiffuseWidth);
        mMaxWidth = a.getInt(R.styleable.DiffuseView_diffuse_maxWidth, mMaxWidth);
        mDiffuseSpeed = a.getInt(R.styleable.DiffuseView_diffuse_speed, mDiffuseSpeed);
        mAlphaDiffuseSpeed = a.getInt(R.styleable.DiffuseView_diffuse_alpha_speed, mAlphaDiffuseSpeed);
        int imageId = a.getResourceId(R.styleable.DiffuseView_diffuse_coreImage, -1);
        if (imageId != -1) {
            mBitmap = BitmapFactory.decodeResource(getResources(), imageId);
        }

        a.recycle();
    }

    private void init() {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);

        mPaint2 = new Paint();
        mPaint2.setAntiAlias(true);
        mPaint2.setStyle(Paint.Style.STROKE);

        mAlphas.add(255);
        mWidths.add(0);
    }

    @Override
    public void invalidate() {
        if (hasWindowFocus()) {
            super.invalidate();
        }
    }

    @Override
    public void onWindowFocusChanged(boolean hasWindowFocus) {
        super.onWindowFocusChanged(hasWindowFocus);
        if (hasWindowFocus) {
            invalidate();
        }
    }

    @Override
    public void onDraw(Canvas canvas) {

        // 绘制扩散圆
        mPaint2.setColor(mColor);

        canvas.drawCircle(getWidth() >> 1, getHeight() >> 1, mCoreRadius, mPaint2);

        for (int i = 0; i < mAlphas.size(); i++) {
            // 设置透明度
            Integer alpha = mAlphas.get(i);
            mPaint2.setAlpha(alpha);
            // 绘制扩散圆
            Integer width = mWidths.get(i);
            canvas.drawCircle(getWidth() >> 1, getHeight() >> 1, mCoreRadius + width, mPaint2);

            if (alpha > 0) {
                mAlphas.set(i, alpha - mAlphaDiffuseSpeed > 0 ? alpha - mAlphaDiffuseSpeed : 1);
                mWidths.set(i, width + mDiffuseSpeed);
            }
        }
        // 判断当扩散圆扩散到指定宽度时添加新扩散圆
        if (mAlphas.get(mAlphas.size() - 1) > 0) {
            mAlphas.add(255);
            mWidths.add(0);
        }
        // 超过10个扩散圆，删除最外层
        if (mWidths.size() >= 8) {
            mWidths.clear();
            mAlphas.clear();
            mAlphas.add(255);
            mWidths.add(0);
        }

        // 绘制中心圆及图片
        mPaint.setAlpha(255);
        mPaint.setColor(mCoreColor);
        canvas.drawCircle(getWidth() >> 1, getHeight() >> 1, mCoreRadius, mPaint);

        if (mBitmap != null) {
            canvas.drawBitmap(mBitmap, (getWidth() >> 1) - (mBitmap.getWidth() >> 1),
                    (getHeight() >> 1) - (mBitmap.getHeight() >> 1), mPaint);
        }

        if (mIsDiffuse) {
            postInvalidateDelayed(500);
//            invalidate();
        }
    }

    /**
     * 开始扩散
     */
    public void start() {
        mIsDiffuse = true;
        invalidate();
    }

    /**
     * 停止扩散
     */
    public void stop() {
        mIsDiffuse = false;
        mWidths.clear();
        mAlphas.clear();
        mAlphas.add(255);
        mWidths.add(0);
        invalidate();
    }

    /**
     * 是否扩散中
     */
    public boolean isDiffuse() {
        return mIsDiffuse;
    }

    /**
     * 设置扩散圆颜色
     */
    public void setColor(@ColorRes int colorId) {
        mColor = getResources().getColor(colorId);
    }

    /**
     * 设置中心圆颜色
     */
    public void setCoreColor(@ColorRes int colorId) {
        mCoreColor = getResources().getColor(colorId);
    }

    /**
     * 设置中心圆图片
     */
    public void setCoreImage(int imageId) {
        mBitmap = BitmapFactory.decodeResource(getResources(), imageId);
    }

    /**
     * 设置中心圆半径
     */
    public void setCoreRadius(int radius) {
        mCoreRadius = radius;
    }

    /**
     * 设置扩散圆宽度(值越小宽度越大)
     */
    public void setDiffuseWidth(int width) {
        mDiffuseWidth = width;
    }

    /**
     * 设置最大宽度
     */
    public void setMaxWidth(int maxWidth) {
        mMaxWidth = maxWidth;
    }

    /**
     * 设置扩散速度，值越大速度越快
     */
    public void setDiffuseSpeed(int speed) {
        mDiffuseSpeed = speed;
    }

    /**
     * 设置颜色速度，值越大速度越快
     */
    public void setDiffuseAlphaSpeed(int alphaSpeed) {
        mAlphaDiffuseSpeed = alphaSpeed;
    }

}
