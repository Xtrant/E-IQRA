package com.example.e_iqra.data.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.e_iqra.databinding.ItemSliderBinding

class SlideAdapter(
    private val descriptions: List<String>,
    private val buttonTexts: List<String>,
    private val slideImages: List<Int>,
    private val onButtonClick: (position: Int) -> Unit
) : RecyclerView.Adapter<SlideAdapter.SlideViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SlideViewHolder {
        val binding = ItemSliderBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SlideViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SlideViewHolder, position: Int) {
        holder.bind(descriptions[position], buttonTexts[position], slideImages[position], position)
    }

    override fun getItemCount(): Int = descriptions.size

    inner class SlideViewHolder(private val binding: ItemSliderBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(description: String, buttonText: String, imageRes: Int, position: Int) {
            binding.tvDescription.text = description
            binding.btnView.text = buttonText
            binding.imageView.setImageResource(imageRes)

            binding.btnView.setOnClickListener {
                onButtonClick(position)
            }
        }
    }
}
