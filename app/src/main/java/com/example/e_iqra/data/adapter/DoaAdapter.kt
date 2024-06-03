package com.example.e_iqra.data.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.e_iqra.R
import com.example.e_iqra.data.api.DoaResponseItem

// DoaAdapter.kt
class DoaAdapter(
    private var doaList: MutableList<DoaResponseItem>,
    private val onItemClick: (DoaResponseItem) -> Unit
) : RecyclerView.Adapter<DoaAdapter.DoaViewHolder>() {

    inner class DoaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textViewDoa: TextView = itemView.findViewById(R.id.textViewDoa)

        init {
            itemView.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    onItemClick(doaList[position])
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DoaViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_article, parent, false)
        return DoaViewHolder(view)
    }

    override fun onBindViewHolder(holder: DoaViewHolder, position: Int) {
        val currentItem = doaList[position]
        holder.textViewDoa.text = currentItem.doa
    }

    override fun getItemCount(): Int {
        return doaList.size
    }

    fun setDoaList(newDoaList: List<DoaResponseItem>) {
        doaList.clear()
        doaList.addAll(newDoaList)
        notifyDataSetChanged()
    }
}

