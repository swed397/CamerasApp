package com.android.cameras.app

import android.content.Context

fun dipToPx(context: Context, dpValue: Float) = dpValue * context.resources.displayMetrics.density