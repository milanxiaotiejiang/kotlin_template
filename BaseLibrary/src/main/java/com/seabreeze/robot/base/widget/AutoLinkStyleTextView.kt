package com.seabreeze.robot.base.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.text.*
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.ImageSpan
import android.util.AttributeSet
import android.view.View
import androidx.appcompat.widget.AppCompatTextView
import com.seabreeze.robot.base.R

/**
 * <pre>
 * @user : milanxiaotiejiang
 * @email : 765151629@qq.com
 * @version : 1.0
 * @date : 2020/8/7
 * @description : 添加链接
</pre> *
 */
class AutoLinkStyleTextView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : AppCompatTextView(context, attrs, defStyleAttr) {
    private var styleType = TYPE_CONTENT_TEXT
    private var defaultColor = Color.parseColor("#1d9add")
    private var hasUnderLine = true
    private var defaultTextValue: String? = null
    private var startImageDes = 0

    private lateinit var mClickable: Clickable

    init {
        init(context, attrs, defStyleAttr)
    }

    private fun init(
        context: Context,
        attrs: AttributeSet?,
        defStyleAttr: Int
    ) {
        val typedArray = context.obtainStyledAttributes(
            attrs,
            R.styleable.AutoLinkStyleTextView,
            defStyleAttr,
            0
        )
        styleType = typedArray.getInt(
            R.styleable.AutoLinkStyleTextView_AutoLinkStyleTextView_type,
            TYPE_CONTENT_TEXT
        )
        defaultTextValue =
            typedArray.getString(R.styleable.AutoLinkStyleTextView_AutoLinkStyleTextView_text_value)
        defaultColor = typedArray.getColor(
            R.styleable.AutoLinkStyleTextView_AutoLinkStyleTextView_default_color,
            defaultColor
        )
        hasUnderLine = typedArray.getBoolean(
            R.styleable.AutoLinkStyleTextView_AutoLinkStyleTextView_has_under_line,
            hasUnderLine
        )
        startImageDes = typedArray.getResourceId(
            R.styleable.AutoLinkStyleTextView_AutoLinkStyleTextView_start_image,
            0
        )
        addStyle()
        typedArray.recycle()
        mClickable = Clickable.init(defaultColor, hasUnderLine)
        importantForAccessibility = View.IMPORTANT_FOR_ACCESSIBILITY_NO
    }


    /**
     * 部分文字链接的通过xml设置静态渲染
     */
    private fun addStyle() {
        if (TextUtils.isEmpty(defaultTextValue)) {
            return
        }
        val text = text.toString().trim { it <= ' ' }
        val spannableString = SpannableString(text)
        spannableString.setSpan(
            mClickable,
            text.lastIndexOf(defaultTextValue!!),
            text.lastIndexOf(defaultTextValue!!) + defaultTextValue!!.length,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        setText(spannableString)
        movementMethod = LinkMovementMethod.getInstance()
    }

    fun setDefaultTextValue(value: String?) {
        if (text.toString().contains(value!!)) {
            defaultTextValue = value
            addStyle()
        }
    }

    /**
     * 以image开头风格的需要动态调用这个方法
     * 图片和文字已经过居中适配
     *
     * @param text
     */
    fun setStartImageText(text: CharSequence) {
        if (styleType == TYPE_START_IMAGE && !TextUtils.isEmpty(
                text
            ) && startImageDes != 0
        ) {
            val spannableString = SpannableString("   $text")
            val span = CenteredImageSpan(context, startImageDes)
            spannableString.setSpan(span, 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
            setText(spannableString)
        }
    }

    private class CenteredImageSpan(
        context: Context,
        drawableRes: Int
    ) : ImageSpan(context, drawableRes) {
        override fun draw(
            canvas: Canvas, text: CharSequence,
            start: Int, end: Int, x: Float,
            top: Int, y: Int, bottom: Int, paint: Paint
        ) {
            val b = drawable
            val fm = paint.fontMetricsInt
            val transY = (y + fm.descent + y + fm.ascent) / 2 - b.bounds.bottom / 2
            canvas.save()
            canvas.translate(x, transY.toFloat())
            b.draw(canvas)
            canvas.restore()
        }
    }

    fun addClickCallBack(listener: () -> Unit) {
        mClickable.mListener.add(listener)
    }

    fun removeClickCallBack(listener: () -> Unit) {
        mClickable.mListener.remove(listener)
    }

    companion object {
        private const val TYPE_START_IMAGE = 0
        private const val TYPE_CONTENT_TEXT = 1
    }
}

object Clickable : ClickableSpan(), NoCopySpan {

    private var defaultColor: Int = 0
    private var hasUnderLine: Boolean = false

    var mListener = mutableListOf<() -> Unit>()

    fun init(defaultColor: Int, hasUnderLine: Boolean): Clickable {
        this.defaultColor = defaultColor
        this.hasUnderLine = hasUnderLine
        return this
    }

    override fun onClick(widget: View) {
        mListener.forEach {
            it.invoke()
        }
    }

    override fun updateDrawState(ds: TextPaint) {
        super.updateDrawState(ds)
        ds.color = defaultColor
        ds.isUnderlineText = hasUnderLine
    }

}