package com.giziguru.app.ui.singleExplore

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.giziguru.app.adapter.SingleExploreAdapter
import com.giziguru.app.data.remote.response.meal.DataMeals
import com.giziguru.app.data.remote.response.meal.YourMealsResponse
import com.giziguru.app.databinding.ActivityExploreSingleBinding
import com.giziguru.app.ui.addMeals.AddMealsActivity
import com.giziguru.app.ui.explore.ExploreViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ExploreSingleActivity : AppCompatActivity(), SingleExploreAdapter.OnItemClickListener {

    private lateinit var binding: ActivityExploreSingleBinding
    private val viewModel: ExploreViewModel by viewModels()

    private val singleExploreAdapter: SingleExploreAdapter by lazy {
        SingleExploreAdapter(mutableListOf(), this)
    }

    private var selectedMealId: String? = null
    private var selectedMealName: String? = null
    private var selectedMealValue: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityExploreSingleBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRecyclerView()
        observeExplore()

        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (!query.isNullOrEmpty()) {
                    Toast.makeText(
                        this@ExploreSingleActivity,
                        "Submitted query: $query",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                singleExploreAdapter.filter(newText)
                return true
            }
        })

        binding.btnExploreAddMeals.setOnClickListener {
            val serve = binding.serveValue.text.toString().toInt()
            startActivity(intent)

            val yourMealsResponse =
                YourMealsResponse(selectedMealId, serve, selectedMealName, selectedMealValue)

            val intent = Intent(this, AddMealsActivity::class.java)
            intent.putExtra("yourMealsResponse", yourMealsResponse)
            startActivity(intent)
        }
    }

    private fun setupRecyclerView() {
        binding.rvSingleExplore.apply {
            layoutManager = LinearLayoutManager(this@ExploreSingleActivity)
            adapter = singleExploreAdapter
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
                            singleExploreAdapter.setExplorerList(data)
                        }
                    } else {
                        val exception = result.exceptionOrNull()
                        Toast.makeText(
                            this@ExploreSingleActivity,
                            "Error: ${exception?.message}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }
    }

    override fun onItemClick(singleExplore: DataMeals) {
        selectedMealId = singleExplore.mealId
        selectedMealName = singleExplore.mealName
        selectedMealValue = singleExplore.calories.toString()
    }
}

