@file:Suppress("UNCHECKED_CAST")

package com.seabreeze.robot.base.ext.view

import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import java.util.*

/**
 * User: milan
 * Time: 2020/9/29 23:42
 * Des:
 */
private var clickInterval = 400L
private var lastTime = 0L

fun View.setOnIntervalClickListener(onIntervalClickListener: (View) -> Unit) {
    this.setOnClickListener {
        if (System.currentTimeMillis() - lastTime > clickInterval) {
            lastTime = System.currentTimeMillis()
            onIntervalClickListener.invoke(it)
        }
    }
}

fun EditText.afterTextChanged(afterTextChanged: (String) -> Unit) {
    this.addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(editable: Editable?) {
            afterTextChanged.invoke(editable.toString())
        }

        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
    })
}

fun <T : View> T.click(block: (T) -> Unit) = setOnClickListener { block(it as T) }

fun <T : View> T.singleClick(block: (T) -> Unit) =
    setOnClickListener(object : SingleClickListener() {
        override fun onSingleClick(v: View) {
            block(v as T)
        }
    })


fun <T : View> T.longClick(block: (T) -> Boolean) = setOnLongClickListener {
    block(it as T)
}

fun SingleClickListener.setViews(vararg views: View) {
    for (view in views) {
        view.setOnClickListener(this)
    }
}

abstract class SingleClickListener : View.OnClickListener {

    companion object {
        const val MIN_CLICK_DELAY_TIME = 500
    }

    private var lastClickTime: Long = 0L

    override fun onClick(v: View) {
        val currentTime = Calendar.getInstance().timeInMillis
        if (currentTime - lastClickTime > MIN_CLICK_DELAY_TIME) {
            lastClickTime = currentTime;
            onSingleClick(v);
        }
    }

    abstract fun onSingleClick(v: View)

}