package com.example.books.feature.login

import android.os.Bundle
import android.text.Editable
import android.view.*
import android.widget.TextView
import androidx.core.view.*
import androidx.fragment.*
import androidx.fragment.app.*
import com.example.books.R
import com.example.books.core.usecase.profile.UserAuthenticator
import com.example.books.databinding.FragmentLoginBinding
import com.example.books.feature.profile.create.CreateProfileFragment
import com.google.android.material.textfield.TextInputLayout
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LoginFragment : Fragment(),
                      LoginScreenEventListener,
                      TextView.OnEditorActionListener,
                      OnApplyWindowInsetsListener {

  companion object {
    val TAG: String = LoginFragment::class.java.simpleName
  }

  private var _binding: FragmentLoginBinding? = null
  private val binding get() = _binding!!
  private val viewModel by viewModels<LoginViewModel>()

  private var isKeyboardVisible = false

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?,
  ): View {
    return if (_binding != null) binding else {
      FragmentLoginBinding.inflate(inflater, container, false).also {
        _binding = it
        ViewCompat.setOnApplyWindowInsetsListener(it.root, this)
        ViewCompat.setWindowInsetsAnimationCallback(it.root, WindowInsetsAnimationCompatCallback())
      }.apply {
        listener = this@LoginFragment
        pwdInputEditTxt.setOnEditorActionListener(this@LoginFragment)
        emailAutoCompleteTxtView.apply {
          setOnItemClickListener { _, _, _, _ -> pwdTxtInputLayout.requestFocus() }
          setOnEditorActionListener(this@LoginFragment)
          setOnScrollChangeListener { _, _, _, _, _ -> hideKeyboard() }
        }
      }
    }.root
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    postponeEnterTransition()
    view.doOnPreDraw { startPostponedEnterTransition() }

    launchInLifecycleRepeatingScope {
      binding.run {
        with(viewModel) {
          launch { emailSuggestions.collectLatest(::onSuggestions) }
          launch { authFlow.collectLatest(::onAuthenticationStep) }
          launch {
            emailAutoCompleteTxtView.setText(email)
            pwdInputEditTxt.setText(pwd)
          }
        }
      }
    }
  }

  override fun onDestroyView() {
    _binding = null
    super.onDestroyView()
  }

  override fun afterEmailTxtChanged(e: Editable?) {
    viewModel.email = e
    binding.emailTxtInputLayout.error = null
  }

  override fun afterPwdTxtChanged(e: Editable?) {
    viewModel.pwd = e
    binding.pwdTxtInputLayout.error = null
  }

  override fun signIn(btn: View) {
    hideKeyboard()
    btn.isPressed = true
    viewModel.signIn(viewModel.email, viewModel.pwd)
  }

  override fun signUp(btn: View) {
    hideKeyboard()
    parentFragmentManager.commit {
      setReorderingAllowed(true)
      add<CreateProfileFragment>(R.id.frag_container_view, CreateProfileFragment.TAG)
      addToBackStack(TAG)
    }
  }

  override fun onEditorAction(v: TextView?, actionId: Int, event: KeyEvent?): Boolean {
    return with(binding) {
      when {
        v === emailAutoCompleteTxtView -> pwdTxtInputLayout.requestFocus()
        v === pwdInputEditTxt          -> {
          signIn(signInBtn)
          true
        }
        else                           -> false
      }
    }
  }

  override fun onApplyWindowInsets(v: View, insets: WindowInsetsCompat): WindowInsetsCompat {
    isKeyboardVisible = insets.isVisible(WindowInsetsCompat.Type.ime())
    if (isKeyboardVisible.not()) {
      arrayOf(binding.emailAutoCompleteTxtView, binding.pwdInputEditTxt).forEach { it.clearFocus() }
    }
    return insets
  }

  private fun hideKeyboard() {
    if (isKeyboardVisible) hideKeyboard(requireView())
  }

  private inner class WindowInsetsAnimationCompatCallback(
  ) : WindowInsetsAnimationCompat.Callback(DISPATCH_MODE_STOP) {
    private var translationY = 0f

    override fun onPrepare(animation: WindowInsetsAnimationCompat) {
      translationY = 0f
      val startBottom = binding.containerCardView.bottom.toFloat()
      translationY += startBottom
    }

    override fun onStart(
      animation: WindowInsetsAnimationCompat,
      bounds: WindowInsetsAnimationCompat.BoundsCompat,
    ): WindowInsetsAnimationCompat.BoundsCompat {
      val marginBottom = resources.getDimension(R.dimen._16dp)
      val endBottom = binding.root.height - bounds.upperBound.bottom - marginBottom
      translationY -= endBottom
      translationY = translationY.coerceAtLeast(0f)
      return bounds
    }

    override fun onProgress(
      insets: WindowInsetsCompat,
      runningAnimations: MutableList<WindowInsetsAnimationCompat>,
    ): WindowInsetsCompat {
      val imeAnimation = runningAnimations.find { it.typeMask and WindowInsetsCompat.Type.ime() != 0 } ?: return insets
      val fraction = imeAnimation.interpolatedFraction
      binding.containerCardView.translationY = -translationY * if (isKeyboardVisible) fraction else 1 - fraction
      return insets
    }

  }

}

private fun FragmentLoginBinding.onAuthenticationStep(step: AuthenticationStep) {
//  containerLinearLayout.apply {
//    val shallFocus = step is AuthenticationStep.Error
//    isFocusable = shallFocus
//    isFocusableInTouchMode = shallFocus
//  }

  when (step) {
    AuthenticationStep.Authenticating -> { /* todo */
    }
    AuthenticationStep.Success        -> {}
    is AuthenticationStep.Error       -> {
      when (step.reason) {
        UserAuthenticator.InvalidReason.BLANK_EMAIL     -> {
          emailTxtInputLayout.error = "please enter an email address"
        }
        UserAuthenticator.InvalidReason.BLANK_PWD       -> {
          pwdTxtInputLayout.error = "please enter a password"
        }
        UserAuthenticator.InvalidReason.EMAIL_NOT_FOUND -> {
          emailTxtInputLayout.error = "this email is not found"
        }
        UserAuthenticator.InvalidReason.PWD_NOT_MATCHED -> {
          pwdTxtInputLayout.error = "incorrect password"
        }
      }
    }
  }
}

private fun FragmentLoginBinding.onSuggestions(emails: List<String>) {
  val noEmails = emails.isEmpty()
  emailTxtInputLayout.endIconMode = if (noEmails) TextInputLayout.END_ICON_NONE else TextInputLayout.END_ICON_DROPDOWN_MENU
  emailAutoCompleteTxtView.setSimpleItems(emails.toTypedArray())
}

//private fun FragmentLoginBinding.onTrySignIn(): Boolean {
//  if (emailAutoCompleteTxtView.text.isNullOrBlank()) {
//    emailTxtInputLayout.error = "email is blank"
//    return false
//  }
//
//  if (pwdInputEditTxt.text.isNullOrBlank()) {
//    pwdTxtInputLayout.error = "password is blank"
//    return false
//  }
//
//  if (pwdInputEditTxt.text!!.length !in 4..8) {
//    pwdTxtInputLayout.error = "password should be in range 4 to 8"
//    return false
//  }
//
//  return true
//}

//
//private fun FragmentLoginBinding.signIn() {
//  hideKeyboard(root)
//  val canTrySignIn = binding?.onTrySignIn() == true
//  if (canTrySignIn) {
//    viewLifecycleOwner.lifecycleScope.launch {
//      val user = signInUser.signInUser(emailAutoCompleteTxtView.text, pwdInputEditTxt.text!!)
//      if (user == null) {
//        Toast
//          .makeText(requireContext(), "No account found!, Try again", Toast.LENGTH_SHORT)
//          .show()
//      }
//    }
//  }
//}
