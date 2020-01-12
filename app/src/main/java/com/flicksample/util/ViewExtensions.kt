package com.flicksample.util

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.annotation.LayoutRes
import androidx.annotation.NonNull
import androidx.core.content.ContextCompat
import com.flicksample.SafeClickListener

fun <T : View> inflater(
    @NonNull root: View, @LayoutRes layoutId: Int,
    attach: Boolean
): T {
    return LayoutInflater.from(root.context)
        .inflate(layoutId, root as ViewGroup, attach) as T
}

fun View.setOnDelayedClickListener(listener: (view: View) -> Unit) {
    isClickable = true
    isFocusableInTouchMode = false
    val safeClickListener = SafeClickListener {
        listener(it)
        it.hideKeyboard()
    }
    setOnClickListener(safeClickListener)
}

fun View.hideKeyboard() {
    val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(windowToken, 0)
}


fun showKeyboard(view: View) {
    val imm = ContextCompat.getSystemService<InputMethodManager>(
        view.context, InputMethodManager::class.java
    )
    imm?.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0)
}