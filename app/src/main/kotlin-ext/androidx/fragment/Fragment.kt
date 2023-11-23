package androidx.fragment

import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.*
import kotlinx.coroutines.*

inline val Fragment.window: Window get() = requireActivity().window

fun Fragment.showKeyboard(view: View) {
  window.showKeyboard(view)
}

fun Fragment.hideKeyboard(view: View) {
  window.hideKeyboard(view)
}

fun Fragment.launchInLifecycleScope(block: suspend CoroutineScope.() -> Unit) {
  viewLifecycleOwner.lifecycleScope.launch(block = block)
}

fun Fragment.launchInLifecycleRepeatingScope(
  repeatState: Lifecycle.State = Lifecycle.State.STARTED,
  block: suspend CoroutineScope.() -> Unit,
) {
  launchInLifecycleScope {
    repeatOnLifecycle(repeatState, block)
  }
}

