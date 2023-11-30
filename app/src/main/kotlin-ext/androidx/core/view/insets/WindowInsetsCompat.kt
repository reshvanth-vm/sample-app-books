package androidx.core.view.insets

import androidx.core.view.WindowInsetsCompat


val WindowInsetsCompat.systemBarInsets
  get() = getInsets(WindowInsetsCompat.Type.systemBars())