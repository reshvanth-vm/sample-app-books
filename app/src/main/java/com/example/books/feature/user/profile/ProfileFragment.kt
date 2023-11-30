package com.example.books.feature.user.profile

import android.content.res.Configuration
import android.os.Bundle
import android.view.*
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.viewModels
import androidx.fragment.launchInLifecycleRepeatingScope
import com.example.books.base.exception.NonInflatedBindingException
import com.example.books.databinding.FragmentProfileBinding
import com.example.books.feature.common.BaseFragment
import com.google.android.material.transition.MaterialFadeThrough
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class ProfileFragment : BaseFragment() {

  companion object {
    const val TAG = "user_profile_screen"
  }

  override val backStackTag = TAG

  private var _binding: FragmentProfileBinding? = null
  private val binding
    get() = _binding ?: throw NonInflatedBindingException(this::class)

  private val viewModel by viewModels<ProfileViewModel>()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    enterTransition = MaterialFadeThrough()
    exitTransition = MaterialFadeThrough()
  }

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?,
  ) = FragmentProfileBinding.inflate(inflater, container, false).also { _binding = it }.root

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    binding.apply {
      toolBar.setNavigationOnClickListener { parentFragmentManager.popBackStack() }
      goBackBtn?.setOnClickListener { parentFragmentManager.popBackStack() }
      signOutBtn.setOnClickListener { viewModel.signOutUser() }
    }

    launchInLifecycleRepeatingScope {
      viewModel.email.collectLatest(binding.emailTxtView::setText)
    }
  }

  override fun onApplyWindowInsets(v: View, insets: WindowInsetsCompat): WindowInsetsCompat {
    // todo
    return super.onApplyWindowInsets(v, insets)
  }

}