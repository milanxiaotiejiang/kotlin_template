package com.seabreeze.robot.base.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.seabreeze.robot.base.R
import kotlinx.android.synthetic.main.fragment_progress_dialog.*

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

    private var mMessage: String? = null

    companion object {
        fun newInstance() = ProgressDialogFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_progress_dialog, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tvMessage.text = mMessage ?: getString(R.string.loading)
    }

    fun show(
        fragmentManager: FragmentManager,
        message: String,
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