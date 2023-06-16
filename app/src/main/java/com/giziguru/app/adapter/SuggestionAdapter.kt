package com.giziguru.app.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.giziguru.app.R
import com.giziguru.app.data.remote.response.dashboard.SuggestionResponse
import com.giziguru.app.databinding.SuggestionCardBinding

@SuppressLint("NotifyDataSetChanged")
class SuggestionAdapter(private val suggestionList: MutableList<SuggestionResponse>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var isEmptyList: Boolean = false

    fun setSuggestionList(suggestionList: List<SuggestionResponse>) {
        this.suggestionList.clear()
        this.suggestionList.addAll(suggestionList)
        isEmptyList = suggestionList.isEmpty()
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            VIEW_TYPE_SUGGESTION -> {
                val binding = SuggestionCardBinding.inflate(inflater, parent, false)
                SuggestionViewHolder(binding)
            }

            VIEW_TYPE_EMPTY -> {
                val emptyView = inflater.inflate(R.layout.item_no_data, parent, false)
                EmptyViewHolder(emptyView)
            }

            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is SuggestionViewHolder -> {
                val suggestion = suggestionList[position]
                holder.bind(suggestion)
            }

            is EmptyViewHolder -> {
            }
        }
    }

    override fun getItemCount(): Int {
        return if (isEmptyList) 1 else suggestionList.size
    }

    override fun getItemViewType(position: Int): Int {
        return if (isEmptyList) VIEW_TYPE_EMPTY else VIEW_TYPE_SUGGESTION
    }

    inner class SuggestionViewHolder(
        private val binding: SuggestionCardBinding,
        private val context: Context = binding.root.context,
    ) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(suggestion: SuggestionResponse) {
            val dataSuggestion = suggestion.data?.firstOrNull()
            binding.apply {
                if (dataSuggestion?.mealImage != null) {
                    Glide.with(itemView)
                        .load(dataSuggestion.mealImage)
                        .into(mealsImage)
                } else {
                    mealsImage.visibility = View.GONE
                }
                mealsName.text = dataSuggestion?.mealName
                mealsTag.text = dataSuggestion?.tag
            }
        }
    }

    inner class EmptyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    companion object {
        private const val VIEW_TYPE_EMPTY = 0
        private const val VIEW_TYPE_SUGGESTION = 1
    }
}
