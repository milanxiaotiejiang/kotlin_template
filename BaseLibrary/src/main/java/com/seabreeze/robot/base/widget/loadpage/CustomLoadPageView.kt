package com.seabreeze.robot.base.widget.loadpage

import android.app.Activity
import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import com.seabreeze.robot.base.R
import com.seabreeze.robot.base.ext.find
import com.seabreeze.robot.base.ext.tool.getItemView

/**
 * <pre>
 * author : 76515
 * time   : 2020/6/30
 * desc   :
 * </pre>
 */
class SimplePageViewForStatus : BasePageViewForStatus() {
    override fun getRootView(activity: Activity): LoadPageViewForStatus =
        LoadPageViewForStatus(activity)

    override fun getLoadingView(status: LoadPageViewForStatus) = status.loadingView

    override fun getLoadFailView(status: LoadPageViewForStatus) = status.failView

    override fun getLoadEmptyView(status: LoadPageViewForStatus) = status.emptyView

    override fun getLoadNoNetView(status: LoadPageViewForStatus) = status.noNetView

}

class LoadPageViewForStatus @JvmOverloads constructor(
    context: Context?,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    var failView: View
    var noNetView: View
    var emptyView: View
    var loadingView: View

    init {
        val itemView = getItemView(R.layout.quick_view_load_more)
        failView = itemView.find(R.id.load_more_load_fail_view)
        noNetView = itemView.find(R.id.load_more_load_no_net_view)
        emptyView = itemView.find(R.id.load_more_load_empty_view)
        loadingView = itemView.find(R.id.load_more_loading_view)
        addView(itemView)
    }
}