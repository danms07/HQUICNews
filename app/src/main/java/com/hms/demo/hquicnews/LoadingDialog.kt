package com.hms.demo.hquicnews

import android.content.Context
import android.view.LayoutInflater
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import kotlinx.android.synthetic.main.loading_layout.view.*

class LoadingDialog  {
    companion object{
        fun createDialog(context: Context, titleId:Int=R.string.load_dialog_title, messageId:Int=R.string.load_dialog_message): AlertDialog {
            val builder= AlertDialog.Builder(context)
            builder.setTitle(titleId)
            val inflater= LayoutInflater.from(context)
            val view=inflater.inflate(R.layout.loading_layout,null)
            view.dialogMessage.setText(messageId)
            builder.setView(view)
            builder.setCancelable(false)
            return builder.create()
        }

    }
}