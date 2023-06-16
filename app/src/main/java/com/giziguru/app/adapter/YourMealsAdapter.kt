package com.giziguru.app.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.giziguru.app.R
import com.giziguru.app.data.remote.response.meal.YourMealsResponse
import com.giziguru.app.databinding.YourMealsBinding

class YourMealsAdapter : RecyclerView.Adapter<YourMealsAdapter.ViewHolder>() {
    private val dataList: MutableList<YourMealsResponse> = mutableListOf()

    @SuppressLint("NotifyDataSetChanged")
    fun setData(dataList: List<YourMealsResponse>) {
        this.dataList.clear()
        this.dataList.addAll(dataList)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = YourMealsBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = dataList[position]
        holder.bind(data)
    }

    override fun getItemCount(): Int {
        return minOf(dataList.size, 3)
    }

    inner class ViewHolder(
        private val binding: YourMealsBinding,
        private val context: Context = binding.root.context,
    ) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: YourMealsResponse) {
            binding.rvFoodName.text = data.food_name
            binding.rvFoodNameValue.text =
                context.getString(R.string.kcal, data.food_name_value.toString())
            binding.rvServe.text = data.serve.toString()
        }
    }
}
