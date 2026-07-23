package com.tavro.outstanding

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import androidx.activity.ComponentActivity

fun Context.findComponentActivity(): ComponentActivity = findActivity()

// Walks the ContextWrapper chain, necessary because Compose's LocalContext may be a wrapper
// rather than the Activity itself.
private inline fun <reified T : Activity> Context.findActivity(): T {
    var context = this
    while (context is ContextWrapper) {
        if (context is T) return context
        context = context.baseContext
    }
    error("No activity instance found.")
}
