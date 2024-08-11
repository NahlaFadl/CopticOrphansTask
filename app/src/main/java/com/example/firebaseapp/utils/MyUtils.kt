package com.example.firebaseapp.utils

import android.app.Activity
import android.app.AlertDialog
import android.view.LayoutInflater
import androidx.core.view.isVisible
import com.example.firebaseapp.R
import com.example.firebaseapp.databinding.DialogConfirmBinding

object MyUtils {
    fun Activity.dialogMessage(
        message: String,
        title: String = "",
        hasCancel: Boolean = false,
        action: () -> Unit = {}
    ) {
        val view = DialogConfirmBinding.inflate(LayoutInflater.from(this))
        val dialog = AlertDialog.Builder(this, R.style.DialogStyle)
            .setView(view.root)
            .setCancelable(false)
            .show()

        view.apply {
            txtTitle.isVisible = title.isNotEmpty()
            btnCancel.isVisible = hasCancel

            txtTitle.text = title
            txtMassage.text = message

            btnCancel.setOnClickListener {
                if (dialog?.isShowing == true) dialog.dismiss()
            }
            btnOk.setOnClickListener {
                if (dialog?.isShowing == true) dialog.dismiss()
                action()
            }
        }
    }
}