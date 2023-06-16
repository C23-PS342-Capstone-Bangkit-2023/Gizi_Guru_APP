package com.giziguru.app.ui.profile

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.bumptech.glide.Glide
import com.giziguru.app.R
import com.giziguru.app.databinding.FragmentProfileBinding
import com.giziguru.app.ui.auth.AuthActivity
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ProfileFragment : Fragment() {
    private val profileViewModel: ProfileViewModel by viewModels()
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeProfile()

        binding.profileLogout.setOnClickListener {
            showLogoutDialog()
        }
    }

    private fun observeProfile() {
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                profileViewModel.getMyProfile().collect { result ->
                    if (result.isSuccess) {
                        val profileResponse = result.getOrNull()
                        profileResponse?.let { response ->
                            binding.apply {
                                Glide.with(requireContext())
                                    .load(response.data.image)
                                    .placeholder(R.drawable.img_here)
                                    .into(profileImg)

                                profileName.text = response.data.name
                                profileAge.text =
                                    getString(R.string.profile_age, response.data.age.toString())
                                profileWeight.text = getString(
                                    R.string.profile_weight,
                                    response.data.bodyWeight.toString()
                                )
                                profileHeight.text = getString(
                                    R.string.profile_height,
                                    response.data.bodyHeight.toString()
                                )
                                profileGender.text =
                                    getString(R.string.profile_gender, response.data.gender)
                                profileIsPregnant.text = getString(
                                    R.string.profile_isPregnant,
                                    if (response.data.isPregnant == 1) "Yes" else "No"
                                )
                            }
                        }
                    } else {
                        Toast.makeText(requireContext(), "Error", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    private fun showLogoutDialog() {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(getString(R.string.logout_dialog_title))
            .setMessage(getString(R.string.logout_dialog_message))
            .setNegativeButton(getString(R.string.cancel)) { dialog, _ ->
                dialog.dismiss()
            }
            .setPositiveButton(getString(R.string.logout)) { _, _ ->
                performLogout()
            }
            .show()
    }

    private fun performLogout() {
        lifecycleScope.launch {
            profileViewModel.doLogout().collect { result ->
                if (result.isSuccess) {
                    profileViewModel.saveAuthToken("")
                    startActivity(Intent(requireContext(), AuthActivity::class.java))
                    requireActivity().finish()
                    Toast.makeText(
                        requireContext(),
                        getString(R.string.logout_message_success),
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    Toast.makeText(
                        requireContext(),
                        getString(R.string.logout_message_failed),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
