package com.example.workshophub.utils

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.workshophub.R
import com.example.workshophub.databinding.ItemWorkshopBinding
import com.example.workshophub.models.Workshop
import com.example.workshophub.utils.Constant.IMAGE_URL

class WorkshopAdapter(private val clickListener: WorkshopClickListener) : ListAdapter<Workshop, WorkshopAdapter.WorkshopViewHolder>(WorkshopComparator()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WorkshopViewHolder {
        return WorkshopViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: WorkshopViewHolder, position: Int) {
        holder.bind(getItem(position), clickListener)
    }

    class WorkshopViewHolder(private val binding: ItemWorkshopBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(workshop: Workshop, listener: WorkshopClickListener) {
            binding.clickListener = listener
            binding.workshop = workshop
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): WorkshopViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ItemWorkshopBinding.inflate(layoutInflater,parent,false)
                return WorkshopViewHolder(binding)
            }
        }
    }

    class WorkshopComparator : DiffUtil.ItemCallback<Workshop>() {
        override fun areItemsTheSame(oldItem: Workshop, newItem: Workshop): Boolean {
            return oldItem.wid == newItem.wid
        }

        override fun areContentsTheSame(oldItem: Workshop, newItem: Workshop): Boolean {
            return oldItem.equals(newItem)
        }
    }

    interface WorkshopClickListener {
        fun onItemClick(workshop: Workshop)
    }
}