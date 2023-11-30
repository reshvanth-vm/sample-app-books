package android.view

import android.content.res.Configuration
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

val View.isKeyboardVisible: Boolean
  get() {
    val insets = ViewCompat.getRootWindowInsets(this) ?: return false
    return insets.isVisible(WindowInsetsCompat.Type.ime())
  }

val View.orientation
  get() = resources.configuration.orientation

val View.isInLandscapeMode
  get() = orientation == Configuration.ORIENTATION_LANDSCAPE

val View.isInPortraitMode
  get() = orientation == Configuration.ORIENTATION_PORTRAIT
