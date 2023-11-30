package com.example.books.feature.user.profile.create

import android.os.Bundle
import android.text.*
import android.util.Log
import android.view.*
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.core.view.*
import androidx.core.widget.*
import androidx.fragment.*
import androidx.fragment.app.*
import androidx.paging.LOG_TAG
import com.example.books.R
import com.example.books.base.exception.NonInflatedBindingException
import com.example.books.core.usecase.profile.*
import com.example.books.databinding.FragmentCreateProfileBinding
import com.example.books.feature.common.BaseFragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.transition.MaterialContainerTransform
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CreateProfileFragment : BaseFragment(),
                              Toolbar.OnMenuItemClickListener,
                              View.OnFocusChangeListener,
                              TextView.OnEditorActionListener {

  companion object {
    const val TAG = "new_account_creation_screen"
  }

  override val backStackTag = TAG

  private var _binding: FragmentCreateProfileBinding? = null
  private val binding
    get() = _binding ?: throw NonInflatedBindingException(this::class)

  private val viewModel by viewModels<CreateProfileViewModel>()

  private var isKeyboardVisible = false

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    sharedElementEnterTransition = MaterialContainerTransform(requireContext(), true).apply {
      drawingViewId = R.id.frag_container_view
    }
  }

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?,
  ) = FragmentCreateProfileBinding.inflate(inflater, container, false).also { _binding = it }.root

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    binding.apply {
      toolBar.apply {
        setOnMenuItemClickListener(this@CreateProfileFragment)
        setNavigationOnClickListener { parentFragmentManager.popBackStack() }
      }

      with(viewModel) {
        mapOf(
          emailInputEditTxt to email,
          pwdInputEditTxt to pwd,
          confirmPwdInputEditTxt to confirmPwd,
        )
      }.forEach { (v, txt) ->
        v.setText(txt)
        v.filters += WhiteSpaceIgnorantInputFilter
        v.onFocusChangeListener = this@CreateProfileFragment
        v.setOnEditorActionListener(this@CreateProfileFragment)
        v.doAfterTextChanged { v.afterTxtChanged(it) }
      }
    }

    launchInLifecycleRepeatingScope {
      viewModel.errorState.collectLatest(binding::onErrorState)
    }

    view.doOnPreDraw {
      if (isKeyboardVisible.not()) {
        showKeyboard(binding.emailInputEditTxt)
      }
    }
  }

  override fun onDestroyView() {
    super.onDestroyView()
    _binding = null
  }

  override fun onMenuItemClick(item: MenuItem?): Boolean {
    if (item?.itemId == R.id.create_menu_item) {
      if (isKeyboardVisible) {
        hideKeyboard(requireView())
      }

      viewModel.validateAllAndCreate()
    }

    return true
  }

  override fun onEditorAction(v: TextView?, actionId: Int, event: KeyEvent?): Boolean {
    val b = binding

    var shouldHideKeyboard = true
    when (v?.id) {
      R.id.email_input_edit_txt       -> {
        b.emailInputEditTxt.clearFocus()
        b.pwdInputEditTxt.run {
          if (text.isNullOrBlank()) {
            shouldHideKeyboard = false
            requestFocus()
          }
        }
      }

      R.id.pwd_input_edit_txt         -> {
        b.pwdInputEditTxt.clearFocus()
        b.confirmPwdInputEditTxt.run {
          if (text.toString() != b.pwdInputEditTxt.text.toString()) {
            shouldHideKeyboard = false
            requestFocus()
          }
        }
      }

      R.id.confirm_pwd_input_edit_txt -> {
        b.confirmPwdInputEditTxt.clearFocus()
      }

      else                            -> return false
    }

    if (shouldHideKeyboard && isKeyboardVisible) {
      hideKeyboard(v)
    }

    return true
  }

  override fun onFocusChange(v: View?, hasFocus: Boolean) {
    if (hasFocus) return

    with(viewModel) {
      when (v?.id) {
        R.id.email_input_edit_txt       -> {
          if (binding.emailInputEditTxt.text.isNullOrBlank().not()) validateEmail()
        }

        R.id.pwd_input_edit_txt         -> validatePwd()
        R.id.confirm_pwd_input_edit_txt -> validateConfirmPwd()
        else                            -> throw RuntimeException()
      }
    }
  }

  private fun TextInputEditText.afterTxtChanged(e: Editable?) {
    val txt = e?.toString()
    val (property, inputLayout) = with(viewModel) {
      binding.run {
        when (id) {
          R.id.email_input_edit_txt       -> ::email to emailTxtInputLayout
          R.id.pwd_input_edit_txt         -> ::pwd to pwdTxtInputLayout
          R.id.confirm_pwd_input_edit_txt -> ::confirmPwd to confirmPwdTxtInputLayout
          else                            -> throw RuntimeException()
        }
      }
    }

    property.set(txt)
    inputLayout.error = null
  }

  override fun onApplyWindowInsets(v: View, insets: WindowInsetsCompat): WindowInsetsCompat {
    isKeyboardVisible = insets.isVisible(WindowInsetsCompat.Type.ime())
    return super.onApplyWindowInsets(v, insets)
  }
}

private fun FragmentCreateProfileBinding.onErrorState(errorState: AcctCreationErrorState) {
  emailTxtInputLayout.error = when (errorState.email) {
    NewUserEmailValidator.InvalidReason.BLANK -> "email-id should not be blank*"
    NewUserEmailValidator.InvalidReason.EXIST -> "this email is already exists!"
    NewUserEmailValidator.InvalidReason.ERROR -> "please enter a valid email-id!"
    null                                      -> null
  }

  pwdTxtInputLayout.error = when (errorState.pwd) {
    NewUserPwdValidator.InvalidReason.BLANK                 -> "password should not be blank"
    NewUserPwdValidator.InvalidReason.NOT_MATCHED_CONDITION -> pwdTxtInputLayout.helperText
    null                                                    -> null
  }

  if (errorState.confirmPwdNotMatched) {
    confirmPwdTxtInputLayout.error = "Confirm password not matched password"
  }
}

//  private fun onScreenState(screenState: CreateProfileViewModel.ScreenState) {
//    when (screenState) {
//      CreateProfileViewModel.ScreenState.Init                         -> Unit
//      CreateProfileViewModel.ScreenState.SigningIn                    -> {/* todo */
//      }
//      is CreateProfileViewModel.ScreenState.ProcessingAccountCreation -> {
//        when (screenState.state) {
//          CreateProfileViewModel.ScreenState.TransactionState.FAILED      -> TODO()
//          CreateProfileViewModel.ScreenState.TransactionState.IN_PROGRESS -> Unit
//          CreateProfileViewModel.ScreenState.TransactionState.SUCCESS     -> {
//            val msg = resources.getString(R.string.account_created_dialog_msg)
//            accountCreationProcessDialog.run {
//              setMessage(msg)
//              show()
//            }
//          }
//        }
//      }
//    }
//  }

private object WhiteSpaceIgnorantInputFilter : InputFilter {

  override fun filter(
    source: CharSequence?,
    start: Int,
    end: Int,
    dest: Spanned?,
    dstart: Int,
    dend: Int,
  ): CharSequence? {
    for (i in start until end) {
      if (source != null && source[i].isWhitespace()) {
        return ""
      }
    }

    return null
  }
}

