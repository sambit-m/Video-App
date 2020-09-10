package poc.sambitm.videoapp.utils

import android.content.Context
import android.util.DisplayMetrics
import kotlin.math.roundToInt

fun pxToDp(context: Context, px: Int): Int {
    val displayMetrics = context.resources.displayMetrics
    return (px / (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT)).roundToInt()
}

fun dpToPx(context: Context, dp: Float): Int {
    val displayMetrics = context.resources.displayMetrics
    return (dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT)).roundToInt()
}