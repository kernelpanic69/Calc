package com.github.arturnikolaenko.calc.util

import android.app.Activity
import android.content.res.Resources
import android.graphics.drawable.Drawable
import android.util.DisplayMetrics
import android.util.Log
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import androidx.annotation.AttrRes
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.children

fun View.applyToAllChildren(action: (child: View) -> Unit) {
    if (this is ViewGroup) {
        children.forEach { child ->
            if (child is ViewGroup) {
                child.applyToAllChildren(action)
            } else {
                action(child)
            }
        }
    }
}

fun Activity.drawableFromAttribute(@AttrRes id: Int): Drawable? {
    val typedValue = TypedValue()
    theme.resolveAttribute(id, typedValue, true)

    val drawableId = typedValue.resourceId

    try {
        return ResourcesCompat.getDrawable(resources, drawableId, theme)
    } catch (e: Resources.NotFoundException) {
        Log.w("Resource", "drawable with id $drawableId not found")
    }

    return null
}

data class Dimension(val width: Int, val height: Int)

fun Activity.displayMetrics(): Dimension {
    return Dimension(resources.displayMetrics.widthPixels, resources.displayMetrics.heightPixels)
}