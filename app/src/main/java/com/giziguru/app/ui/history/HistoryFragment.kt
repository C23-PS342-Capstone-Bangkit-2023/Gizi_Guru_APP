package com.giziguru.app.ui.history

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
import com.giziguru.app.adapter.HistoryAdapter
import com.giziguru.app.data.remote.response.dashboard.HistoryResponse
import com.giziguru.app.databinding.FragmentHistoryBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HistoryFragment : Fragment() {
    private lateinit var binding: FragmentHistoryBinding
    private val viewModel: HistoryViewModel by viewModels()
    private val historyAdapter: HistoryAdapter by lazy { HistoryAdapter(mutableListOf()) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentHistoryBinding.inflate(inflater, container, false)
        return (binding.root)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        observeHistory()
    }

    private fun setupRecyclerView() {
        binding.rvHistory.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = historyAdapter
            setHasFixedSize(true)
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
                            historyAdapter.setHistoryList(historyList)
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