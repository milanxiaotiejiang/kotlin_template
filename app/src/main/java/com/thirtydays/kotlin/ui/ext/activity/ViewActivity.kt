package com.thirtydays.kotlin.ui.ext.activity

import android.os.Bundle
import com.jakewharton.rxbinding3.view.clicks
import com.seabreeze.robot.base.ext.isVisible
import com.seabreeze.robot.base.ext.setGone
import com.seabreeze.robot.base.ext.setVisible
import com.seabreeze.robot.base.ui.rx.RxAppCompatActivity
import com.thirtydays.kotlin.R
import kotlinx.android.synthetic.main.activity_view.*
import java.util.concurrent.TimeUnit

class ViewActivity : RxAppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view)

        addDisposable(
            btn.clicks()
                .throttleFirst(2, TimeUnit.SECONDS)
                .subscribe {
                    if (imageView.isVisible)
                        imageView.setGone()
                    else
                        imageView.setVisible()
                })

    }
}