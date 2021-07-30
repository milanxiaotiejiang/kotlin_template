package com.seabreeze.robot.base.framework.mvvm

interface IViewModel<out ViewModel : BaseViewModel> {
    val mViewModel: ViewModel
}