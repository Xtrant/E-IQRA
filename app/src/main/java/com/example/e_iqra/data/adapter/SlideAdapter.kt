package com.example.e_iqra.data.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.e_iqra.databinding.ItemSliderBinding

class SlideAdapter(
    private val slides: List<String>,
    private val descriptions: List<String>,
    private val buttonTexts: List<String>,
    private val onButtonClick: (position: Int) -> Unit
) : RecyclerView.Adapter<SlideAdapter.SlideViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SlideViewHolder {
        val binding = ItemSliderBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SlideViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SlideViewHolder, position: Int) {
        holder.bind(slides[position], descriptions[position], buttonTexts[position], position)
    }

    override fun getItemCount(): Int = slides.size

    inner class SlideViewHolder(private val binding: ItemSliderBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(title: String, description: String, buttonText: String, position: Int) {
            binding.tvTitle.text = title
            binding.tvDescription.text = description
            binding.btnView.text = buttonText
            binding.btnView.setOnClickListener {
                onButtonClick(position)
            }
        }
    }
}

