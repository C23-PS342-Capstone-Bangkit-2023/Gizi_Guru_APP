package com.giziguru.app.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.transition.AutoTransition
import android.transition.TransitionManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.constraintlayout.widget.Group
import androidx.recyclerview.widget.RecyclerView
import com.giziguru.app.R
import com.giziguru.app.data.remote.response.dashboard.HistoryResponse
import com.giziguru.app.databinding.HistoryCardBinding

@SuppressLint("NotifyDataSetChanged")
class HistoryAdapter(private val historyList: MutableList<HistoryResponse>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var isEmptyList: Boolean = false
    private val expandedPositions: MutableSet<Int> = HashSet()

    fun setHistoryList(historyList: List<HistoryResponse>) {
        this.historyList.clear()
        this.historyList.addAll(historyList)
        isEmptyList = historyList.isEmpty()
        notifyDataSetChanged()
    }

    inner class HistoryViewHolder(
        private val binding: HistoryCardBinding,
        private val context: Context = binding.root.context,
    ) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(history: HistoryResponse?, position: Int) {
            if (history != null) {
                binding.apply {
                    val dataHistory = history.data?.firstOrNull()
                    historyName.text = dataHistory?.title
                    historyDate.text = dataHistory?.historyDate
                    totalCal.text =
                        context.getString(R.string.kcal, dataHistory?.totalCalories.toString())
                    carbsValue.text =
                        context.getString(R.string.gr, dataHistory?.totalCarbs.toString())
                    proteinValue.text =
                        context.getString(R.string.gr, dataHistory?.totalProtein.toString())
                    fatValue.text = context.getString(R.string.gr, dataHistory?.totalFat.toString())
                    dropdownIcon.setImageResource(
                        if (expandedPositions.contains(position))
                            android.R.drawable.arrow_up_float
                        else
                            android.R.drawable.arrow_down_float
                    )
                    cardGroup.visibility = if (expandedPositions.contains(position))
                        View.VISIBLE
                    else
                        View.GONE
                }

                binding.dropdownIcon.setOnClickListener {
                    if (expandedPositions.contains(position)) {
                        expandedPositions.remove(position)
                        collapseCard(binding.cardGroup, binding.dropdownIcon)
                    } else {
                        expandedPositions.add(position)
                        expandCard(binding.cardGroup, binding.dropdownIcon)
                    }
                }
            } else {
                binding.root.visibility = View.GONE
            }
        }

        private fun expandCard(cardGroup: Group, arrow: ImageView) {
            TransitionManager.beginDelayedTransition(binding.baseCardView, AutoTransition())
            cardGroup.visibility = View.VISIBLE
            arrow.setImageResource(android.R.drawable.arrow_up_float)
        }

        private fun collapseCard(cardGroup: Group, arrow: ImageView) {
            TransitionManager.beginDelayedTransition(binding.baseCardView, AutoTransition())
            cardGroup.visibility = View.GONE
            arrow.setImageResource(android.R.drawable.arrow_down_float)
        }
    }

    override fun getItemCount(): Int {
        return if (isEmptyList) 1 else historyList.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            VIEW_TYPE_HISTORY -> {
                val binding = HistoryCardBinding.inflate(inflater, parent, false)
                HistoryViewHolder(binding)
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
            is HistoryViewHolder -> {
                val history = historyList[position]
                holder.bind(history, position)
            }

            is EmptyViewHolder -> {
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (isEmptyList) VIEW_TYPE_EMPTY else VIEW_TYPE_HISTORY
    }

    inner class EmptyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    companion object {
        private const val VIEW_TYPE_HISTORY = 0
        private const val VIEW_TYPE_EMPTY = 1
    }
}

