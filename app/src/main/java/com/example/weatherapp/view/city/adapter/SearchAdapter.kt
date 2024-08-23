package com.example.weatherapp.view.city.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherapp.R
import com.example.weatherapp.databinding.ItemSearchBinding
import com.example.weatherapp.view.city.uiViewModel.CityUIViewModel

class SearchAdapter() : RecyclerView.Adapter<SearchAdapter.MyViewHolder>() {
    private var onClickListener: OnClickListener? = null
    private var itemList: List<CityUIViewModel> = emptyList()

    fun setDataList(list: List<CityUIViewModel>) {
        this.itemList = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding: ItemSearchBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.item_search,
            parent, false
        )
        return MyViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val post = itemList[position]
        holder.bind(post)
        holder.itemView.setOnClickListener {
            onClickListener?.let {
                onClickListener!!.onClick(post)
            }
        }
    }

    interface OnClickListener {
        fun onClick(city: CityUIViewModel)
    }

    inner class MyViewHolder(val binding: ItemSearchBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: CityUIViewModel) {
            binding.city = data
            binding.executePendingBindings()
        }
    }

    fun setOnClickListener(listener: OnClickListener) {
        this.onClickListener = listener
    }
}