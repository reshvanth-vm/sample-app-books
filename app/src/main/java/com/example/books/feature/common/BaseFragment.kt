package com.example.books.feature.common

import android.os.Bundle
import android.view.View
import androidx.annotation.CallSuper
import androidx.core.view.*
import androidx.fragment.app.Fragment

// todo
// In future separate modules and set generic type of binding
// to inflate and clear in this base fragment itself
// (not not possible of some ksp error on generated classes)
// abstract class BaseFragment<B> : Fragment(),
// <B> Binding type (e.g. FragmentStoreBinding, ...)
abstract class BaseFragment : Fragment(),
                              OnApplyWindowInsetsListener {

  abstract val backStackTag: String

  @CallSuper
  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    postponeEnterTransition()
    view.doOnPreDraw { startPostponedEnterTransition() }

    ViewCompat.setOnApplyWindowInsetsListener(view, this@BaseFragment)
  }

  @CallSuper
  override fun onApplyWindowInsets(v: View, insets: WindowInsetsCompat) = insets
}
