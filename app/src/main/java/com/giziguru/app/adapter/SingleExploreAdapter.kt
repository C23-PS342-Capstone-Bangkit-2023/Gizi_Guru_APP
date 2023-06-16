package com.giziguru.app.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.giziguru.app.R
import com.giziguru.app.data.remote.response.meal.DataMeals
import com.giziguru.app.databinding.SingleExploreMealsBinding

@SuppressLint("NotifyDataSetChanged")
class SingleExploreAdapter(
    private val exploreSingleList: MutableList<DataMeals>,
    private val onItemClickListener: OnItemClickListener,
    private var filteredList: MutableList<DataMeals> = mutableListOf(),
) : RecyclerView.Adapter<SingleExploreAdapter.ViewHolder>() {

    interface OnItemClickListener {
        fun onItemClick(singleExplore: DataMeals)
    }

    private var selectedItemPosition: Int = RecyclerView.NO_POSITION

    fun filter(query: String?) {
        filteredList.clear()
        if (query.isNullOrEmpty()) {
            filteredList.addAll(exploreSingleList)
        } else {
            val filterPattern = query.lowercase().trim()
            filteredList.addAll(exploreSingleList.filter { data ->
                data.mealName?.lowercase()?.contains(filterPattern) == true
            })
        }
        notifyDataSetChanged()
    }

    fun setExplorerList(exploreList: List<DataMeals>) {
        exploreSingleList.clear()
        exploreSingleList.addAll(exploreList)
        updateFilteredList()
    }

    private fun updateFilteredList() {
        filteredList.clear()
        filteredList.addAll(exploreSingleList)
        notifyDataSetChanged()
    }

    inner class ViewHolder(
        private val binding: SingleExploreMealsBinding,
        private val onItemClickListener: OnItemClickListener,
        private val filteredList: List<DataMeals>,
        private val context: Context = binding.root.context,
    ) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    selectItem(position)
                    val singleExplore = filteredList[position]
                    onItemClickListener.onItemClick(singleExplore)
                }
            }
        }

        fun bind(singleExplore: DataMeals) {
            binding.apply {
                Glide.with(itemView)
                    .load(singleExplore.mealImage)
                    .into(rvFoodImage)
                rvFoodName.text = singleExplore.mealName
                rvFoodNameValue.text =
                    context.getString(R.string.kcal, singleExplore.calories.toString())

                if (adapterPosition == selectedItemPosition) {
                    selectionIndicator.visibility = View.VISIBLE
                } else {
                    selectionIndicator.visibility = View.GONE
                }
            }
        }
    }

    private fun selectItem(position: Int) {
        val previouslySelectedItemPosition = selectedItemPosition
        selectedItemPosition = position
        notifyItemChanged(previouslySelectedItemPosition)
        notifyItemChanged(selectedItemPosition)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            SingleExploreMealsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding, onItemClickListener, filteredList)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val singleExplore = filteredList[position]
        holder.bind(singleExplore)
    }

    override fun getItemCount(): Int {
        return filteredList.size
    }
}