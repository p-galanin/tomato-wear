package ru.pavel.tomato.wear

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.View

fun <T : Activity> createIntent(
    clazz: Class<T>,
    context: Context,
    saveInHistory: Boolean = true
): Intent {
    val intent = Intent(context, clazz)
    if (!saveInHistory) {
        intent.flags = Intent.FLAG_ACTIVITY_NO_HISTORY
    }
    return intent
}

fun setVisibility(isVisible: Boolean) = if (isVisible) {
    View.VISIBLE
} else {
    View.INVISIBLE
}
