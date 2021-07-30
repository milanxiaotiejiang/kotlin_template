package com.thirtydays.kotlin.ui.ext.activity

import android.os.Bundle
import com.jakewharton.rxbinding3.view.clicks
import com.seabreeze.robot.base.ext.view.isVisible
import com.seabreeze.robot.base.ext.view.setGone
import com.seabreeze.robot.base.ext.view.setVisible
import com.seabreeze.robot.base.ui.foundation.activity.RxAppCompatActivity
import com.thirtydays.kotlin.R
import com.thirtydays.kotlin.databinding.ActivityViewBinding
import java.util.concurrent.TimeUnit

class ViewActivity : RxAppCompatActivity<ActivityViewBinding>() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view)

        addDisposable(
            mViewBinding.btn.clicks()
                .throttleFirst(2, TimeUnit.SECONDS)
                .subscribe {
                    if (mViewBinding.imageView.isVisible)
                        mViewBinding.imageView.setGone()
                    else
                        mViewBinding.imageView.setVisible()
                })

    }
}