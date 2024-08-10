package com.example.nahla_base.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.nahla_base.data.remote.dto.Items
import com.example.nahla_base.data.remote.dto.RepositoriesData
import com.example.nahla_base.databinding.ItemRepositoryBinding

class SearchAdapter : RecyclerView.Adapter<SearchAdapter.MyHolder>() {
    var list: MutableList<Items> = mutableListOf()

    class MyHolder(val binding: ItemRepositoryBinding) : RecyclerView.ViewHolder(binding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder {
        return MyHolder(
            ItemRepositoryBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: MyHolder, position: Int) {
        holder.binding.tvRepoName.text = list[position].name
    }

    fun setData(list: MutableList<Items>){
        this.list=list
        notifyDataSetChanged()
    }
}