package android.content

import android.graphics.Color
import com.google.android.material.color.MaterialColors

val Context.surfaceColor
  get() = MaterialColors.getColor(
    this,
    com.google.android.material.R.attr.colorSurface,
    Color.WHITE
  )