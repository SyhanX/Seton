package com.example.seton.common.domain.util

import android.content.Context
import androidx.annotation.StringRes
import com.example.seton.R
import com.google.android.material.dialog.MaterialAlertDialogBuilder

fun showAlertDialog(
    context: Context,
    @StringRes message: Int,
    @StringRes title: Int = R.string.confirm_action,
    @StringRes positiveText: Int = R.string.yes,
    @StringRes negativeText: Int = R.string.no,
    negativeCallback: () -> Unit = {},
    positiveCallback: () -> Unit = {}
) {
    MaterialAlertDialogBuilder(context)
        .setTitle(title)
        .setMessage(message)
        .setPositiveButton(positiveText) { _, _ -> positiveCallback() }
        .setNegativeButton(negativeText) { _, _ -> negativeCallback() }
        .show()
}
//this mf is from material2 library and looks ugly af
//    val builder: AlertDialog.Builder = AlertDialog.Builder(context)
//    builder
//        .setTitle(title)
//        .setMessage(message)
//        .setPositiveButton(positiveText) { _, _ -> positiveCallback() }
//        .setNegativeButton(negativeText) { _, _ -> negativeCallback() }
//        .create()
//    builder.show()