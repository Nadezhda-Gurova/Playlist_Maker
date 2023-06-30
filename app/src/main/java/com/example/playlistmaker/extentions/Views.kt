package com.example.playlistmaker.extentions

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager

fun View.hideKeyboard() {
    val inputMethodManager =
        context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    if (!inputMethodManager.isActive) return
    inputMethodManager.hideSoftInputFromWindow(
        windowToken,
        InputMethodManager.RESULT_UNCHANGED_SHOWN
    )
}