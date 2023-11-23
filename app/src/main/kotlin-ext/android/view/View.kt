package android.view

import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

val View.isKeyboardVisible: Boolean
  get() {
    val insets = ViewCompat.getRootWindowInsets(this) ?: return false
    return insets.isVisible(WindowInsetsCompat.Type.ime())
  }