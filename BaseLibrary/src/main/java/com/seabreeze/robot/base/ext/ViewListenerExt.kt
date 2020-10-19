package com.seabreeze.robot.base.ext

import android.app.Activity
import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import java.util.*

/**
 * User: milan
 * Time: 2020/9/29 23:42
 * Des:
 */
fun Activity.hideSoftKeyboard(focusView: View? = null) {
    val focusV = focusView ?: currentFocus
    focusV?.apply {
        val inputMethodManager =
            getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(windowToken, 0)
    }
}

fun Activity.showSoftKeyboard(editText: EditText) {
    editText.postDelayed({
        editText.requestFocus()
        val inputMethodManager =
            getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.showSoftInput(editText, InputMethodManager.SHOW_FORCED)
    }, 30)
}

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