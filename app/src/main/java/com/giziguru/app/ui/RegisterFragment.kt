package com.giziguru.ui

import android.animation.ObjectAnimator
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.giziguru.R
import com.giziguru.databinding.FragmentRegisterBinding
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class RegisterFragment : Fragment() {

    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!

    private var registerJob: Job = Job()
    private val viewModel: RegisterViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setActions()
        playAnimation()
    }

    private fun playAnimation() {
        val registerViews = listOf(
            binding.tvRegister,
            binding.tvRegisterMsg,
            binding.etFullName,
            binding.etEmail,
            binding.etPassword,
            binding.btnRegister,
            binding.btnLogin
        )

        registerViews.forEachIndexed { index, view ->
            ObjectAnimator.ofFloat(view, View.ALPHA, 0f, 1f).apply {
                startDelay = 200 * index.toLong()
                duration = 500
            }.start()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setActions() {
        binding.apply {
            loginTextView.setOnClickListener(
                Navigation.createNavigateOnClickListener(R.id.action_registerFragment_to_loginFragment)
            )

            registerButton.setOnClickListener {
                handleRegister()
            }
        }
    }

    private fun handleRegister() {
        val name = binding.nameEditText.text.toString().trim()
        val email = binding.usernameEditText.text.toString().trim()
        val password = binding.passwordEditText.text.toString()
        val age = binding.ageEditText.text.toString().toIntOrNull() ?: 0
        val bodyWeight = binding.weightEditText.text.toString().toIntOrNull() ?: 0
        val bodyHeight = binding.heightEditText.text.toString().toIntOrNull() ?: 0
        val gender = binding.genderEditText.text.toString().trim()

        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.RESUMED) {
                if (registerJob.isActive) registerJob.cancel()

                registerJob = launch {
                    viewModel.userRegister(name, email, password).collect { result ->
                        result.onSuccess {
                            Toast.makeText(
                                requireContext(),
                                "Registration success, please login",
                                Toast.LENGTH_SHORT
                            ).show()

                            findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
                        }

                        result.onFailure { throwable ->
                            val errorMessage = throwable.message ?: "Registration failed"
                            Snackbar.make(
                                binding.root,
                                errorMessage,
                                Snackbar.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
            }
        }
    }
}