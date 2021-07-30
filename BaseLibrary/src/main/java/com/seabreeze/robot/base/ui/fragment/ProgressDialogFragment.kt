package com.seabreeze.robot.base.ui.fragment

import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.scwang.smart.refresh.header.material.CircleImageView
import com.scwang.smart.refresh.header.material.MaterialProgressDrawable
import com.seabreeze.robot.base.R

/**
 * <pre>
 * @user : milanxiaotiejiang
 * @email : 765151629@qq.com
 * @version : 1.0
 * @date : 2020/9/11
 * @description : 等待框 loading ...
 * </pre>
 */
class ProgressDialogFragment : DialogFragment() {

    companion object {

        const val CIRCLE_BG_LIGHT = -0x50506
        const val MAX_PROGRESS_ANGLE = .8f

        fun newInstance() = ProgressDialogFragment()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NO_FRAME, R.style.Dialog_FullScreen)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_progress_dialog, container, false)
    }

    private var mMessage: String? = null
    private lateinit var mProgress: MaterialProgressDrawable
    private lateinit var mCircleView: ImageView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val tvMessage = view.findViewById<TextView>(R.id.tvMessage)
        tvMessage.text = mMessage ?: getString(R.string.loading)


        val frameLayout = view.findViewById<FrameLayout>(R.id.frameLayout)

        mProgress = MaterialProgressDrawable(frameLayout)
        mProgress.setColorSchemeColors(-0xff6634, -0xbbbc, -0x996700, -0x559934, -0x7800)
        mCircleView = CircleImageView(requireContext(), CIRCLE_BG_LIGHT)
        mCircleView.setImageDrawable(mProgress)

        mProgress.showArrow(true)
        mProgress.setStartEndTrim(0f, MAX_PROGRESS_ANGLE)
        mProgress.setArrowScale(1f)
        mCircleView.alpha = 1f
        mCircleView.visibility = View.VISIBLE
        frameLayout.addView(mCircleView)

        mProgress.start()
    }

    override fun onStart() {
        super.onStart()
        dialog!!.apply {
            window!!.apply {
                val windowParams = attributes
                windowParams.dimAmount = 0.0f //将Window周边设置透明为0.7
                setCanceledOnTouchOutside(false) //点击周边不隐藏对话框
                attributes = windowParams
                setGravity(Gravity.TOP)
            }
        }
    }

    fun show(
        fragmentManager: FragmentManager,
        message: String?,
        isCancelable: Boolean = false
    ) {
        this.mMessage = message
        this.isCancelable = isCancelable
        try {
            super.show(fragmentManager, "progressDialogFragment")
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

}