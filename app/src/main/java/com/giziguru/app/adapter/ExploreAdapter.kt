package com.giziguru.app.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.giziguru.app.R
import com.giziguru.app.data.remote.response.meal.DataMeals
import com.giziguru.app.databinding.ItemExploreBinding
import com.giziguru.app.ui.detailFood.DetailFoodActivity

@SuppressLint("NotifyDataSetChanged")
class ExploreAdapter(
    private var filteredList: MutableList<DataMeals> = mutableListOf(),
    private var originalList: MutableList<DataMeals> = mutableListOf(),
) : RecyclerView.Adapter<ExploreAdapter.ViewHolder>() {

    fun filter(query: String?) {
        filteredList.clear()
        if (query.isNullOrEmpty()) {
            filteredList.addAll(originalList) // Use originalList instead of exploreList
        } else {
            val filterPattern = query.lowercase().trim()
            filteredList.addAll(originalList.filter { data ->
                data.mealName?.lowercase()?.contains(filterPattern) == true
            })
        }
        notifyDataSetChanged()
    }

    fun setOriginalList(originalList: List<DataMeals>) {
        this.originalList.clear()
        this.originalList.addAll(originalList)
        updateFilteredList()
    }

    private fun updateFilteredList() {
        filteredList.clear()
        filteredList.addAll(originalList)
        notifyDataSetChanged()
    }

    class ViewHolder(
        private val binding: ItemExploreBinding,
        private val filteredList: List<DataMeals>,
        private val context: Context = binding.root.context,
    ) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val explore = filteredList[position]
                    val intent = Intent(context, DetailFoodActivity::class.java)
                    intent.putExtra(
                        "itemId",
                        explore.mealId
                    )
                    context.startActivity(intent)
                }
            }
        }

        fun bind(explore: DataMeals) {
            binding.apply {
                Glide.with(itemView)
                    .load(explore.mealImage)
                    .into(exploreImage)
                rvFoodName.text = explore.mealName
                rvFoodValue.text = context.getString(R.string.kcal, explore.calories.toString())
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemExploreBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding, filteredList)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val explore = filteredList[position]
        holder.bind(explore)
    }

    override fun getItemCount(): Int {
        return filteredList.size
    }
}