package com.giziguru.app.ui.detailFood

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.bumptech.glide.Glide
import com.giziguru.app.R
import com.giziguru.app.databinding.ActivityDetailFoodBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class DetailFoodActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailFoodBinding
    private val viewModel: DetailFoodViewModel by viewModels()

    @SuppressLint("StringFormatInvalid")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailFoodBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val mealId = intent.getStringExtra("itemId")
        if (mealId != null) {
            lifecycleScope.launch {
                lifecycle.repeatOnLifecycle(Lifecycle.State.CREATED) {
                    viewModel.getSingleMeals(mealId).collect { result ->
                        if (result.isSuccess) {
                            val yourMealsResponse = result.getOrNull()
                            yourMealsResponse?.let { response ->
                                val singleMeals = response.data?.getOrNull(0)
                                singleMeals?.let { meal ->
                                    binding.apply {
                                        Glide.with(this@DetailFoodActivity)
                                            .load(meal.mealImage)
                                            .placeholder(R.drawable.img_here)
                                            .into(mealImg)
                                        mealName.text = meal.mealName
                                        mealCal.text =
                                            getString(R.string.caloriess, meal.calories.toString())
                                        mealCarb.text =
                                            getString(R.string.carbss, meal.carb.toString())
                                        mealProtein.text =
                                            getString(R.string.proteins, meal.protein.toString())
                                        mealFat.text = getString(R.string.fats, meal.fat.toString())
                                        mealTag.text = getString(R.string.tags, meal.tag)
                                    }
                                }
                            }
                        } else {
                            val exception = result.exceptionOrNull()
                            exception?.printStackTrace()
                        }
                    }
                }
            }
        }
    }
}