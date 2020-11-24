package com.example.odds.ui

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.FrameLayout
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.example.odds.R
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

/**
 * Created by odds, 2020/10/27 19:15
 * Desc:
 */
open class BottomDialog : BottomSheetDialogFragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        return inflater.inflate(R.layout.fragment_dialog_bottom, container, false)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = BottomSheetDialog(this.requireContext(), R.style.BottomDialg)
        dialog.window?.let { fitWindow(it) }
//        ImmersionBar.with(this).init()
        return dialog
    }

    private fun fitWindow(window: Window): Window {
//        TintStateBarUtil.setStatusBarTranslucent(window)
//        TintStateBarUtil.setStatusBarIconDark(window, true)

        return window
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        //去除默认白色背景，并设置圆角背景
        if (view?.parent != null && context != null && dialog?.window != null) {
//            ((View) getView().getParent()).setBackgroundColor(ContextCompat.getColor(getContext(), R.color.transparent));
            val peekHeight: Int = 300
            dialog?.window!!.setBackgroundDrawableResource(R.color.colorPrimary)
            //获取diglog的根部局
            val bottomSheet: FrameLayout? = (dialog as BottomSheetDialog?)?.getDelegate()?.findViewById(R.id.design_bottom_sheet)
            if (bottomSheet != null) {
                //把windowsd的默认背景颜色去掉，不然圆角显示不见
//                bottomSheet.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.colorPrimary))
                //获取根部局的LayoutParams对象
                val layoutParams: CoordinatorLayout.LayoutParams = bottomSheet.layoutParams as CoordinatorLayout.LayoutParams
                layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT
                //修改弹窗的最大高度，不允许上滑（默认可以上滑）
                bottomSheet.layoutParams = layoutParams
                val behavior: BottomSheetBehavior<FrameLayout> = BottomSheetBehavior.from(bottomSheet)
                //peekHeight即弹窗的最大高度
                behavior.setPeekHeight(peekHeight)
                // 初始为展开状态
                behavior.setState(BottomSheetBehavior.STATE_EXPANDED)
            }
        }
    }
}