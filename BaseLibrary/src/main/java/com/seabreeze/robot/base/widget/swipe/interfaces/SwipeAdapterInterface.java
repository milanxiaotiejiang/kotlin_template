package com.seabreeze.robot.base.widget.swipe.interfaces;

/**
 * <pre>
 * author : 76515
 * time   : 2020/6/19
 * desc   :
 * </pre>
 */
public interface SwipeAdapterInterface {

    int getSwipeLayoutResourceId(int position);

    void notifyDatasetChanged();

}
