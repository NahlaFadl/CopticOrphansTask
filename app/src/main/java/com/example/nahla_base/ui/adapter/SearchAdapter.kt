package com.example.firebaseapp.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.firebaseapp.databinding.ItemRepositoryBinding
import com.example.firebaseapp.data.remote.dto.Items
import com.example.firebaseapp.data.remote.dto.RepositoriesData

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