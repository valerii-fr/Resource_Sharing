package dev.nordix.core.annotations

import android.content.Context

fun interface OnApplicationCreated {
    fun handler(context: Context)
}
