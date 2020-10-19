package com.seabreeze.robot.data.ui

import android.content.res.Configuration
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.widget.ImageView
import android.widget.RelativeLayout
import androidx.fragment.app.DialogFragment
import com.gyf.immersionbar.ImmersionBar
import com.gyf.immersionbar.components.SimpleImmersionOwner
import com.gyf.immersionbar.components.SimpleImmersionProxy
import com.luck.picture.lib.widget.longimage.ImageSource
import com.luck.picture.lib.widget.longimage.SubsamplingScaleImageView
import com.seabreeze.robot.base.ext.find
import com.seabreeze.robot.data.R
import com.seabreeze.robot.data.net.DataRepository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

/**
 * <pre>
 * @user : milanxiaotiejiang
 * @email : 765151629@qq.com
 * @version : 1.0
 * @date : 2020/8/27
 * @description : TODO
 * </pre>
 */

abstract class ImmersionFragment2 : DialogFragment(), SimpleImmersionOwner {
    /**
     * ImmersionBar代理类
     */
    private val mSimpleImmersionProxy = SimpleImmersionProxy(this)
    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        mSimpleImmersionProxy.isUserVisibleHint = isVisibleToUser
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        mSimpleImmersionProxy.onActivityCreated(savedInstanceState)
    }

    override fun onDestroy() {
        super.onDestroy()
        mSimpleImmersionProxy.onDestroy()
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        mSimpleImmersionProxy.onHiddenChanged(hidden)
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        mSimpleImmersionProxy.onConfigurationChanged(newConfig)
    }

    /**
     * 是否可以实现沉浸式，当为true的时候才可以执行initImmersionBar方法
     * Immersion bar enabled boolean.
     *
     * @return the boolean
     */
    override fun immersionBarEnabled(): Boolean {
        return true
    }
}

class ImageFragment : ImmersionFragment2() {

    companion object {
        private const val IMAGE_URL = "image_url"

        fun newInstance(imageUrl: String): ImageFragment {
            val args = Bundle()
            args.putString(IMAGE_URL, imageUrl)
            val fragment = ImageFragment()
            fragment.arguments = args
            return fragment
        }

    }

    private lateinit var rootView: View
    private lateinit var mScaleImageView: SubsamplingScaleImageView

    private var imageUrl: String? = null

    private var isShow = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, android.R.style.Theme_Black_NoTitleBar)
        imageUrl = arguments?.getString(IMAGE_URL)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dialog?.window?.setWindowAnimations(R.style.dialogSlideAnim)
        rootView = inflater.inflate(R.layout.dialog_image, container, false)

        val fragmentBg = rootView.find<RelativeLayout>(R.id.fragment_bg)
        fragmentBg.background.alpha = 255

        val rlTop = rootView.find<RelativeLayout>(R.id.rl_top)
        rlTop.background.mutate().alpha = 50

        mScaleImageView = rootView.find<SubsamplingScaleImageView>(R.id.scale_image_view)

        val ivBack = rootView.findViewById<ImageView>(R.id.iv_back)
        ivBack.setOnClickListener { dismiss() }

        imageUrl?.let {
            loadImageBitmap(it)
        }

        setView(rlTop, isShow)
        mScaleImageView.setOnClickListener {
//            if (isShow) {
//                isShow = false
//                setView(rlTop, isShow)
//            } else {
//                isShow = true
//                setView(rlTop, isShow)
//            }
            dismiss()
        }

        return rootView
    }

    private var mDisposable: Disposable? = null

    private fun loadImageBitmap(imgUrl: String) {
        mDisposable = DataRepository.INSTANCE.downloadPicFromNet(imgUrl)
            .map {
                val bys = it.bytes() //注意：把byte[]转换为bitmap时，也是耗时操作，也必须在子线程
                BitmapFactory.decodeByteArray(bys, 0, bys.size)
            }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                mScaleImageView.setImage(ImageSource.bitmap(it))
            }
    }

    override fun onDestroyView() {
        mDisposable?.apply {
            if (!isDisposed)
                dispose()
        }
        super.onDestroyView()
    }

    override fun initImmersionBar() {
        ImmersionBar.with(this)
            .keyboardEnable(true)
            .titleBarMarginTop(R.id.toolbar)
//            .statusBarDarkFont(true)
            .statusBarColor(android.R.color.black)
            .navigationBarColor(android.R.color.white) //导航栏颜色，不写默认黑色
            .navigationBarDarkIcon(true) //导航栏图标是深色，不写默认为亮色
            .init()
    }

    private fun setView(view: View, isShow: Boolean) {
        val alphaAnimation: AlphaAnimation = if (isShow) {
            AlphaAnimation(0.toFloat(), 1.toFloat())
        } else {
            AlphaAnimation(1.toFloat(), 0.toFloat())
        }
        alphaAnimation.fillAfter = true
        alphaAnimation.duration = 500
        view.startAnimation(alphaAnimation)
        alphaAnimation.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation) {}
            override fun onAnimationEnd(animation: Animation) {
                view.visibility = if (isShow) View.VISIBLE else View.GONE
            }

            override fun onAnimationRepeat(animation: Animation) {}
        })
    }
}