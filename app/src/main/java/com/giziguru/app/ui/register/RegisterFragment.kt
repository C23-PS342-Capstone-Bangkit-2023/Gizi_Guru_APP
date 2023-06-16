package com.giziguru.app.ui.register

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.Navigation
import com.giziguru.app.R
import com.giziguru.app.databinding.FragmentRegisterBinding
import com.giziguru.app.ui.main.MainActivity
import com.giziguru.app.utils.Constanta
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch


@AndroidEntryPoint
class RegisterFragment : Fragment(), AdapterView.OnItemSelectedListener {

    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!
    private var registerJob: Job = Job()

    private val viewModel: RegisterViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setActions()
        val genderAdapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item,
            Constanta.Gender.values()
        )
        genderAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.genderSpinner.adapter = genderAdapter
        binding.genderSpinner.onItemSelectedListener = this
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        val selectedGender = parent?.getItemAtPosition(position) as Constanta.Gender
        if (selectedGender == Constanta.Gender.Male) {
            binding.pregnantLinearLayout.visibility = View.GONE
        } else {
            binding.pregnantLinearLayout.visibility = View.VISIBLE
        }
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
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
        val username = binding.usernameEditText.text.toString().trim()
        val password = binding.passwordEditText.text.toString()
        val age = binding.ageEditText.text.toString().toIntOrNull() ?: 0
        val bodyWeight = binding.weightEditText.text.toString().toIntOrNull() ?: 0
        val bodyHeight = binding.heightEditText.text.toString().toIntOrNull() ?: 0
        val gender = binding.genderSpinner.selectedItem as Constanta.Gender
        val isPregnant = if (binding.pregnantEditText.isChecked) 1 else 0

        if (name.length < 5) {
            Snackbar.make(
                binding.root,
                "Name should be at least 5 characters long",
                Snackbar.LENGTH_SHORT
            ).show()
            return
        }

        if (username.length < 5) {
            Snackbar.make(
                binding.root,
                "Username should be at least 5 characters long",
                Snackbar.LENGTH_SHORT
            ).show()
            return
        }

        if (password.length < 6) {
            Snackbar.make(
                binding.root,
                "Password should be at least 6 characters long",
                Snackbar.LENGTH_SHORT
            ).show()
            return
        }

        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.RESUMED) {

                if (registerJob.isActive) registerJob.cancel()

                registerJob = launch {
                    viewModel.userRegister(
                        name,
                        username,
                        password,
                        age,
                        bodyWeight,
                        bodyHeight,
                        gender,
                        isPregnant
                    ).collect { result ->
                        result.onSuccess { credentials ->

                            credentials.data.token.let { token ->
                                viewModel.saveAuthToken(token!!)
                                Intent(requireContext(), MainActivity::class.java).also { intent ->
                                    intent.putExtra(MainActivity.EXTRA_TOKEN, token)
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