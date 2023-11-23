package android.view

import androidx.core.view.*

fun Window.showKeyboard(view: View) {
  view.requestFocus()
  view.post {
    WindowCompat
      .getInsetsController(this, view)
      .show(WindowInsetsCompat.Type.ime())
  }
}

fun Window.hideKeyboard(view: View) {
  view.clearFocus()
  WindowCompat
    .getInsetsController(this, view)
    .hide(WindowInsetsCompat.Type.ime())
}
