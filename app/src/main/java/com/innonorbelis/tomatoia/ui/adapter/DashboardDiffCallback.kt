package com.innonorbelis.tomatoia.ui.adapter

import androidx.recyclerview.widget.DiffUtil
import com.innonorbelis.tomatoia.domain.model.DashboardItem

class DashboardDiffCallback : DiffUtil.ItemCallback<DashboardItem>() {

    override fun areItemsTheSame(
        oldItem: DashboardItem,
        newItem: DashboardItem
    ): Boolean {
        return oldItem::class == newItem::class
    }

    override fun areContentsTheSame(
        oldItem: DashboardItem,
        newItem: DashboardItem
    ): Boolean {
        return oldItem == newItem
    }
}