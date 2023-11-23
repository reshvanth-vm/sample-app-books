package com.example.books.feature.profile.create

import android.os.Bundle
import android.text.Editable
import android.view.*
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.core.view.*
import androidx.fragment.*
import androidx.fragment.app.*
import com.example.books.R
import com.example.books.core.usecase.profile.*
import com.example.books.databinding.FragmentCreateProfileBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CreateProfileFragment : Fragment(),
                              Toolbar.OnMenuItemClickListener,
                              TextView.OnEditorActionListener,
                              CreateProfileScreenEventHandler,
                              OnApplyWindowInsetsListener {

  companion object {
    val TAG: String = CreateProfileFragment::class.java.simpleName
    const val REQUEST_KEY = "created_user_acct"
  }

  // binding
  private var _binding: FragmentCreateProfileBinding? = null
  private val binding get() = _binding!!

  // members
  private val viewModel by viewModels<CreateProfileViewModel>()
  private var isKeyboardVisible = false
  private val accountCreationProcessDialog by lazy {
    MaterialAlertDialogBuilder(requireContext())
      .setTitle("Account Status")
      .setMessage("Account creation in progress...")
      .setOnCancelListener { parentFragmentManager.popBackStack() }
      .setNegativeButton("Cancel") { _, _ -> parentFragmentManager.popBackStack() }
      .setPositiveButton("Proceed") { _, _ -> viewModel.signIn() }
      .create()
  }

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?,
  ): View {
    return if (_binding != null) binding else {
      FragmentCreateProfileBinding
        .inflate(inflater, container, false)
        .also { _binding = it }
        .apply {
          eventHandler = this@CreateProfileFragment
          emailInputEditTxt.setOnEditorActionListener(this@CreateProfileFragment)
          pwdInputEditTxt.setOnEditorActionListener(this@CreateProfileFragment)
          toolBar.apply {
            setOnMenuItemClickListener(this@CreateProfileFragment)
            setNavigationOnClickListener { cancelAccountCreation() }
          }
        }
    }.root
  }


  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    postponeEnterTransition()
    view.doOnPreDraw { startPostponedEnterTransition() }

    if (isKeyboardVisible.not()) {
      showKeyboard(binding.emailInputEditTxt)
    }

    launchInLifecycleRepeatingScope {
      binding.run {
        with(viewModel) {
          launch { screenState.collectLatest(::onScreenState) }
          launch { actionsState.collectLatest(::onActionsState) }
          launch { errorState.collectLatest(::onErrorState) }
          launch {
            emailInputEditTxt.setText(email)
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

  override fun onMenuItemClick(item: MenuItem?): Boolean {
    when (item?.itemId) {
      R.id.create_menu_item -> {
        if (isKeyboardVisible) hideKeyboard(requireView())
        createAccount()
      }
      else                  -> return false
    }
    return true
  }

  override fun onEditorAction(v: TextView?, actionId: Int, event: KeyEvent?): Boolean {
    binding.run {
      when {
        v === emailInputEditTxt -> {
          emailInputEditTxt.clearFocus()
          if (pwdInputEditTxt.text.isNullOrBlank()) {
            pwdInputEditTxt.requestFocus()
          }
        }
        v === pwdInputEditTxt   -> {
          pwdInputEditTxt.clearFocus()
        }
      }
    }
    return true
  }

  override fun cancelAccountCreation() {
    parentFragmentManager.popBackStack()
  }

  override fun afterEmailTextChanged(e: Editable?) {
    viewModel.email = e
  }

  override fun afterPwdTextChanged(e: Editable?) {
    viewModel.pwd = e
  }

  override fun createAccount() {
    viewModel.createUserAccount()
  }

  override fun onApplyWindowInsets(v: View, insets: WindowInsetsCompat): WindowInsetsCompat {
    isKeyboardVisible = insets.isVisible(WindowInsetsCompat.Type.ime())
    return insets
  }

  private fun onScreenState(screenState: CreateProfileViewModel.ScreenState) {
    when (screenState) {
      CreateProfileViewModel.ScreenState.Init                         -> Unit
      CreateProfileViewModel.ScreenState.SigningIn                    -> {
        /* todo */
      }
      is CreateProfileViewModel.ScreenState.ProcessingAccountCreation -> {
        when (screenState.state) {
          CreateProfileViewModel.ScreenState.TransactionState.FAILED      -> TODO()
          CreateProfileViewModel.ScreenState.TransactionState.IN_PROGRESS -> Unit
          CreateProfileViewModel.ScreenState.TransactionState.SUCCESS     -> {
            val msg = resources.getString(R.string.account_created_dialog_msg)
            accountCreationProcessDialog.run {
              setMessage(msg)
              show()
            }
          }
        }
      }
    }
  }

}

private fun FragmentCreateProfileBinding.onActionsState(actionsState: CreateProfileViewModel.ActionsState) {
  toolBar.menu.findItem(R.id.create_menu_item).isEnabled = actionsState.canCreateAccount
}

private fun FragmentCreateProfileBinding.onErrorState(errorState: CreateProfileViewModel.ErrorState) {
  emailTxtInputLayout.error = when (errorState.email) {
    NewUserEmailValidator.InvalidReason.BLANK -> "email-id should not be blank*"
    NewUserEmailValidator.InvalidReason.EXIST -> "this email is already exists!"
    NewUserEmailValidator.InvalidReason.ERROR -> "please enter a valid email-id!"
    null                                      -> null
  }

  pwdTxtInputLayout.error = when (errorState.pwd) {
    NewUserPwdValidator.InvalidReason.BLANK                                         -> "password should not be blank"
    NewUserPwdValidator.InvalidReason.LONG, NewUserPwdValidator.InvalidReason.SHORT -> "password length should be within range 4 to 8"
    null                                                                            -> null
  }
}


//private val FragmentCreateProfileBinding.credentials
//  get() = NewUserCredentialsParcel(
//    name = emailTxt!!,
//    email = emailTxt!!,
//    pwd = pwdTxt!!,
//    profileUri = null,
//  )
//
//private val FragmentCreateProfileBinding.createMenuBtn
//  get() = toolBar.menu.findItem(R.id.create_menu_item)
//
//private val FragmentCreateProfileBinding.emailTxt
//  get() = emailInputEditTxt.text?.toString()
//
//private val FragmentCreateProfileBinding.pwdTxt
//  get() = pwdInputEditTxt.text?.toString()
//
//private fun FragmentCreateProfileBinding.onState(uiState: CreateProfileUIState) {
//  createMenuBtn.isEnabled = uiState.emailInvalidReason == null && uiState.pwdInvalidReason == null
//  emailTxtInputLayout.error = when (uiState.emailInvalidReason) {
//    NewUserEmailValidator.InvalidReason.BLANK -> "email-id should not be blank*"
//    NewUserEmailValidator.InvalidReason.EXIST -> "this email is already exists!"
//    NewUserEmailValidator.InvalidReason.ERROR -> "please enter a valid email-id!"
//    null                                      -> null
//  }
//  pwdTxtInputLayout.error = when (uiState.pwdInvalidReason) {
//    NewUserPwdValidator.InvalidReason.BLANK                                         -> "password should not be blank"
//    NewUserPwdValidator.InvalidReason.LONG, NewUserPwdValidator.InvalidReason.SHORT -> "password length should be within range 4 to 8"
//    null                                                                            -> null
//  }
//}
