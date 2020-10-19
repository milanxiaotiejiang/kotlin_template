package com.seabreeze.robot.base.widget.manager.carouse;

/**
 * User: milan
 * Time: 2019/3/27 2:12
 * Des:
 */
public class ItemTransformation {

    final float mScaleX;
    final float mScaleY;
    final float mTranslationX;
    final float mTranslationY;

    public ItemTransformation(final float scaleX, final float scaleY, final float translationX, final float translationY) {
        mScaleX = scaleX;
        mScaleY = scaleY;
        mTranslationX = translationX;
        mTranslationY = translationY;
    }
}