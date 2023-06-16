package com.giziguru.app.ui.login

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
import androidx.navigation.Navigation
import com.giziguru.app.R
import com.giziguru.app.databinding.FragmentLoginBinding
import com.giziguru.app.ui.main.MainActivity
import com.giziguru.app.ui.main.MainActivity.Companion.EXTRA_TOKEN
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    private var loginJob: Job = Job()
    private val viewModel: LoginViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setActions()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setActions() {
        binding.apply {
            registerTextView.setOnClickListener(
                Navigation.createNavigateOnClickListener(R.id.action_loginFragment_to_registerFragment)
            )

            loginButton.setOnClickListener {
                handleLogin()
            }
        }
    }

    private fun handleLogin() {
        val username = binding.usernameEditText.text.toString()
        val password = binding.passwordEditText.text.toString()

        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.RESUMED) {

                if (loginJob.isActive) loginJob.cancel()

                loginJob = launch {
                    viewModel.userLogin(username, password).collect { result ->
                        result.onSuccess { credentials ->

                            credentials.data?.token?.let { token ->
                                viewModel.saveAuthToken(token)
                                Intent(requireContext(), MainActivity::class.java).also { intent ->
                                    intent.putExtra(EXTRA_TOKEN, token)
                                    startActivity(intent)
                                    requireActivity().finish()
                                }
                            }

                            Toast.makeText(
                                requireContext(),
                                "Success",
                                Toast.LENGTH_SHORT
                            ).show()
                        }

                        result.onFailure { throwable ->
                            val errorMessage =
                                throwable.message ?: "Error"
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