package com.giziguru.app.ui.explore

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.giziguru.app.adapter.ExploreAdapter
import com.giziguru.app.databinding.FragmentExploreBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ExploreFragment : Fragment() {

    private lateinit var binding: FragmentExploreBinding
    private val viewModel: ExploreViewModel by viewModels()

    private val exploreAdapter: ExploreAdapter by lazy { ExploreAdapter(mutableListOf()) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentExploreBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        observeExplore()

        binding.searchView.setOnQueryTextListener(object :
            SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (!query.isNullOrEmpty()) {
                    Toast.makeText(requireContext(), "Submitted query: $query", Toast.LENGTH_SHORT)
                        .show()
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                exploreAdapter.filter(newText)
                return true
            }
        })
    }

    private fun setupRecyclerView() {
        binding.rvExplore.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = exploreAdapter
            setHasFixedSize(true)
        }
    }

    private fun observeExplore() {
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.CREATED) {
                viewModel.getExplore().collect { result ->
                    if (result.isSuccess) {
                        val exploreResponse = result.getOrNull()
                        exploreResponse?.data?.let { data ->
                            exploreAdapter.setOriginalList(data)
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