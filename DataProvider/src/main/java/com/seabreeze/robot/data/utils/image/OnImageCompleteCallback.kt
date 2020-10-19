package com.seabreeze.robot.data.utils.image

/**
 * <pre>
 * author : 76515
 * time   : 2020/7/17
 * desc   :
</pre> *
 */
interface OnImageCompleteCallback {
    /**
     * Start loading
     */
    fun onShowLoading()

    /**
     * Stop loading
     */
    fun onHideLoading()
}