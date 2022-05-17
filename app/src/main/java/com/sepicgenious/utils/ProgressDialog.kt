package com.sepicgenious.utils

import android.app.Dialog
import android.view.Window
import com.sepicgenious.R


class ProgressDialog {

    companion object {
        fun show(mProgressDialog: Dialog) {
            try {
                if (mProgressDialog.isShowing) return
                mProgressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
                mProgressDialog.setContentView(R.layout.dialog_loading_layout)
                mProgressDialog.setCancelable(false)
                if (mProgressDialog.window != null) mProgressDialog.window!!
                    .setBackgroundDrawableResource(android.R.color.transparent)
                mProgressDialog.show()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        fun hide(mProgressDialog: Dialog?) {
            if (mProgressDialog != null && mProgressDialog.isShowing) {
                mProgressDialog.dismiss()
            }
        }
    }

}