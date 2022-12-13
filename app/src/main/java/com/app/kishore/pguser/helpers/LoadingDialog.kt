package com.app.kishore.pguser.helpers

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import com.app.kishore.pguser.R


class LoadingDialog(context: Context) : Dialog(context){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_loading_dialog)
    }
}