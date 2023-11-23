package com.example.books.feature.profile

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.view.doOnPreDraw
import androidx.fragment.app.*
import androidx.fragment.launchInLifecycleRepeatingScope
import androidx.lifecycle.*
import androidx.transition.Transition
import com.example.books.R
import com.example.books.core.usecase.profile.*
import com.example.books.databinding.FragmentCreateProfileBinding
import com.example.books.databinding.FragmentProfileBinding
import com.google.android.material.transition.MaterialContainerTransform
import com.google.android.material.transition.MaterialFadeThrough
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
  signOutUser: SignOutUser,
  userFlow: CurrentUserFlow,
) : ViewModel(),
    SignOutUser by signOutUser {

  val email = userFlow
    .currentUserFlow()
    .mapLatest { it.email }
    .shareIn(viewModelScope, SharingStarted.Eagerly, 1)

}

@AndroidEntryPoint
class ProfileFragment : Fragment(R.layout.fragment_profile),
                        ProfileScreenEventHandler {

  companion object {
    val TAG: String = ProfileFragment::class.java.simpleName
  }

  private val viewModel by viewModels<ProfileViewModel>()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    enterTransition = MaterialFadeThrough()
    exitTransition = MaterialFadeThrough()
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    val binding = FragmentProfileBinding.bind(view).apply {
      handler = this@ProfileFragment
      toolBar.setNavigationOnClickListener { parentFragmentManager.popBackStack() }
    }

    launchInLifecycleRepeatingScope {
      launch { viewModel.email.collectLatest(binding.emailTxtView::setText) }
    }
  }

  override fun goBack() {
    parentFragmentManager.popBackStack()
  }

  override fun signOut(btn: View) {
    // todo alert
    viewModel.signOutUser()
  }

}