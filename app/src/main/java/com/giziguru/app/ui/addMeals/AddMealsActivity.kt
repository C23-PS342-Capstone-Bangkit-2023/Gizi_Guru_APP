package com.giziguru.app.ui.addMeals

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.giziguru.app.adapter.YourMealsAdapter
import com.giziguru.app.data.remote.response.meal.YourMealsResponse
import com.giziguru.app.databinding.ActivityAddMealsBinding
import com.giziguru.app.ui.main.MainActivity
import com.giziguru.app.ui.singleExplore.ExploreSingleActivity
import com.giziguru.app.utils.Constanta
import com.giziguru.app.utils.Helper
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.util.Calendar

@Suppress("DEPRECATION")
@AndroidEntryPoint
class AddMealsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddMealsBinding
    private var token: String = ""

    private val viewModel: AddMealsViewModel by viewModels()
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: YourMealsAdapter
    private var yourMealsResponse: YourMealsResponse? = null

    private val TITLE_KEY = "title"
    private val DATE_KEY = "date"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddMealsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.addMealsDate.setOnClickListener {
            showDatePickerDialog()
        }

        binding.explore.setOnClickListener {
            val intent = Intent(this, ExploreSingleActivity::class.java)
            startActivity(intent)
        }


        binding.camera.setOnClickListener {
            Toast.makeText(this, "Coming Soon", Toast.LENGTH_SHORT).show()
        }

        recyclerView = binding.chooseMealsRecycler
        adapter = YourMealsAdapter()
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        yourMealsResponse = intent.getParcelableExtra("yourMealsResponse")
        if (yourMealsResponse != null) {
            yourMealsResponse?.let { adapter.setData(listOf(it)) }
        }

        binding.btnAddMeals.setOnClickListener {
            doAddMeals()
        }

        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.CREATED) {
                viewModel.getAuthToken().collect { authToken ->
                    if (!authToken.isNullOrEmpty()) token = authToken
                }
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(TITLE_KEY, binding.addMealsTitle.text.toString())
        outState.putString(DATE_KEY, binding.addMealsDate.text.toString())
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        val savedTitle = savedInstanceState.getString(TITLE_KEY)
        val savedDate = savedInstanceState.getString(DATE_KEY)
        binding.addMealsTitle.setText(savedTitle)
        binding.addMealsDate.setText(savedDate)
    }

    private fun doAddMeals() {
        val title = binding.addMealsTitle
        val date = binding.addMealsDate
        val mealId = yourMealsResponse?.meals_id
        val serve = yourMealsResponse?.serve

        var isValid = true

        if (title.text.isNullOrEmpty()) {
            title.error = "Title is required"
            isValid = false
        } else if (title.text!!.length < 6) {
            title.error = "Title must be at least 6 characters long"
            isValid = false
        }

        if (date.text.isNullOrEmpty()) {
            date.error = "Date is required"
            isValid = false
        }

        if (isValid) {
            val meals = Constanta.AddMeals(
                title = title.text.toString(),
                history_date = date.text.toString(),
                details = listOf(
                    Constanta.MealDetail(
                        meal_id = mealId.toString(),
                        serve = serve ?: 0
                    )
                )
            )
            lifecycleScope.launch {
                lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                    viewModel.addMeals(meals)
                        .collect { response ->
                            response.onSuccess {
                                showSnackbar("Success add meals")
                                setResult(RESULT_OK)
                                finish()
                            }
                            response.onFailure {
                                showSnackbar("Failed add meals")
                            }
                        }
                }
            }

            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }

    private fun showSnackbar(message: String) {
        Snackbar.make(
            binding.root,
            message,
            Snackbar.LENGTH_SHORT
        ).show()
    }


    private fun showDatePickerDialog() {
        val currentDate = Calendar.getInstance()
        val year = currentDate.get(Calendar.YEAR)
        val month = currentDate.get(Calendar.MONTH)
        val day = currentDate.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog =
            DatePickerDialog(this, { _, selectedYear, selectedMonth, selectedDayOfMonth ->
                val selectedDate = Calendar.getInstance().apply {
                    set(selectedYear, selectedMonth, selectedDayOfMonth)
                }
                val formattedDate = Helper.Utils.formatDateTime(selectedDate)
                binding.addMealsDate.setText(formattedDate)
            }, year, month, day)

        datePickerDialog.show()
    }
}