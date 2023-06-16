package com.giziguru.app.ui.home

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
import androidx.recyclerview.widget.LinearLayoutManager
import com.giziguru.app.R
import com.giziguru.app.adapter.HistoryAdapter
import com.giziguru.app.adapter.SuggestionAdapter
import com.giziguru.app.data.remote.response.dashboard.HistoryResponse
import com.giziguru.app.data.remote.response.dashboard.SuggestionResponse
import com.giziguru.app.databinding.FragmentHomeBinding
import com.giziguru.app.ui.addMeals.AddMealsActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private val viewModel: HomeViewModel by viewModels()

    private val historyAdapter: HistoryAdapter by lazy { HistoryAdapter(mutableListOf()) }
    private val suggestionAdapter: SuggestionAdapter by lazy { SuggestionAdapter(mutableListOf()) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        observeNutrition()
        observeHistory()
        observeSuggestion()

        binding.cvAddMeals.setOnClickListener {
            val intent = Intent(requireContext(), AddMealsActivity::class.java)
            startActivity(intent)
        }

        binding.cvRecipeMeals.setOnClickListener {
            Toast.makeText(requireContext(), "Coming Soon", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setupRecyclerView() {
        binding.historyRecycler.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = historyAdapter
            setHasFixedSize(true)
        }

        binding.suggestionRecycler.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = suggestionAdapter
            setHasFixedSize(true)
        }
    }

    private fun observeNutrition() {
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.CREATED) {
                viewModel.getAkg().collect { result ->
                    if (result.isSuccess) {
                        val nutritionResponse = result.getOrNull()
                        nutritionResponse?.let { response ->
                            binding.apply {
                                val dataNutrition = response.data
                                yourDaily.text = getString(
                                    R.string.your_dailyCalories,
                                    dataNutrition?.calories.toString()
                                )
                                percentageText.text = getString(
                                    R.string.percentage_fulled,
                                    dataNutrition?.akg.toString()
                                )
                                waterValue.text = dataNutrition?.water.toString()
                                proteinValue.text = dataNutrition?.protein.toString()
                                carbValue.text = dataNutrition?.carb.toString()
                                fatValue.text = dataNutrition?.fat.toString()
                            }
                        }
                    } else {
                        val exception = result.exceptionOrNull()
                        Toast.makeText(
                            requireContext(),
                            "Error: ${exception?.message}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }
    }

    private fun observeHistory() {
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.CREATED) {
                viewModel.getHistory().collect { result ->
                    if (result.isSuccess) {
                        val historyResponse = result.getOrNull()
                        historyResponse?.let { response ->
                            val historyList = response.data.orEmpty().map { data ->
                                HistoryResponse(data = listOf(data))
                            }
                            val maxHistoryList = historyList.takeLast(3)
                            historyAdapter.setHistoryList(maxHistoryList)
                        }
                    } else {
                        val exception = result.exceptionOrNull()
                        Toast.makeText(
                            requireContext(),
                            "Error: ${exception?.message}",
                            Toast.LENGTH_SHORT
                        ).show()

                    }
                }
            }
        }
    }

    private fun observeSuggestion() {
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.CREATED) {
                viewModel.getSuggestion().collect { result ->
                    if (result.isSuccess) {
                        val suggestionResponse = result.getOrNull()
                        suggestionResponse?.let { response ->
                            val suggestionList = response.data.orEmpty().map { data ->
                                SuggestionResponse(data = listOf(data))
                            }
                            suggestionAdapter.setSuggestionList(suggestionList)
                        }
                    } else {
                        val exception = result.exceptionOrNull()
                        Toast.makeText(
                            requireContext(),
                            "Error: ${exception?.message}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }
    }
}