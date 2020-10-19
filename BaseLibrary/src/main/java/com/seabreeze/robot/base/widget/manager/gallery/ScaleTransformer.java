package com.seabreeze.robot.base.widget.manager.gallery;

import android.view.View;

/**
 * User: milan
 * Time: 2019/3/6 21:32
 * Des:
 */
public class ScaleTransformer implements GalleryLayoutManager.ItemTransformer {

    @Override
    public void transformItem(GalleryLayoutManager layoutManager, View item, float fraction) {
        item.setPivotX(item.getWidth() / 2.f);
        item.setPivotY(item.getHeight() / 2.0f);
        float scale = 1 - 0.2f * Math.abs(fraction);
        item.setScaleX(scale);
        item.setScaleY(scale);
    }
}
