package com.example.e_iqra.data.adapter

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.e_iqra.R
import com.example.e_iqra.data.api.DoaResponseItem
import com.example.e_iqra.databinding.ItemArticleBinding
import com.example.e_iqra.view.main.ui.home.detail.DoaDetailFragment

class DoaAdapter(private val context: Context) : ListAdapter<DoaResponseItem, DoaAdapter.DoaViewHolder>(DIFF_CALLBACK) {

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<DoaResponseItem>() {
            override fun areItemsTheSame(oldItem: DoaResponseItem, newItem: DoaResponseItem): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: DoaResponseItem, newItem: DoaResponseItem): Boolean {
                return oldItem == newItem
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DoaViewHolder {
        val binding = ItemArticleBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DoaViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DoaViewHolder, position: Int) {
        val doaItem = getItem(position)
        holder.bind(doaItem)
    }

    inner class DoaViewHolder(private val binding: ItemArticleBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(doaItem: DoaResponseItem) {
            binding.textViewDoa.text = doaItem.doa
            binding.root.setOnClickListener {
                val bundle = Bundle().apply {
                    putString(DoaDetailFragment.EXTRA_DOA, doaItem.doa)
                    putString(DoaDetailFragment.EXTRA_AYAT, doaItem.ayat)
                    putString(DoaDetailFragment.EXTRA_LATIN, doaItem.latin)
                    putString(DoaDetailFragment.EXTRA_ARTINYA, doaItem.artinya)
                }
                it.findNavController().navigate(R.id.action_navigation_home_to_doaDetailFragment, bundle)
            }
        }
    }
}
