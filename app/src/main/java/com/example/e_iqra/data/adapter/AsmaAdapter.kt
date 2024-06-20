package com.example.e_iqra.data.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.e_iqra.data.api.DataItem
import com.example.e_iqra.databinding.ItemAsmaBinding
import com.example.e_iqra.view.main.ui.home.AsmaListActivity

class AsmaAdapter(private val context: Context) : ListAdapter<DataItem, AsmaAdapter.AsmaViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AsmaViewHolder {
        val binding = ItemAsmaBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AsmaViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AsmaViewHolder, position: Int) {
        val asma = getItem(position)
        holder.bind(asma)
    }

    inner class AsmaViewHolder(private val binding: ItemAsmaBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(asma: DataItem) {
            binding.textViewArab.text = asma.arab
            binding.textViewLatin.text = asma.latin
            binding.textViewIndo.text = asma.indo
            binding.root.setOnClickListener {
                val intent = Intent(context, AsmaListActivity::class.java).apply {
                    putExtra(AsmaListActivity.EXTRA_ARAB, asma.arab)
                    putExtra(AsmaListActivity.EXTRA_LATIN, asma.latin)
                    putExtra(AsmaListActivity.EXTRA_INDO, asma.indo)
                }
                context.startActivity(intent)
            }
        }
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<DataItem>() {
            override fun areItemsTheSame(oldItem: DataItem, newItem: DataItem): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: DataItem, newItem: DataItem): Boolean {
                return oldItem == newItem
            }
        }
    }
}
