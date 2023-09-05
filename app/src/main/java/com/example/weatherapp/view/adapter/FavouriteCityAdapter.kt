package com.example.weatherapp.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherapp.R
import com.example.weatherapp.data.local.entities.Search
import com.example.weatherapp.data.model.Data
import com.example.weatherapp.databinding.ItemFavouritecityBinding
import com.example.weatherapp.databinding.ItemSearchBinding

class FavouriteCityAdapter(private var itemList: List<Search>): RecyclerView.Adapter<FavouriteCityAdapter.MyViewHolder>() {
    private var onClickListener: OnClickListener? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding: ItemFavouritecityBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.item_favouritecity,
            parent, false)
        return MyViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val post = itemList[position]
        holder.bind(post)
        holder.itemView.setOnClickListener{
            onClickListener?.let {
                onClickListener!!.onClick(post)
            }
        }
    }
    interface OnClickListener {
        fun onClick(city:Search)
    }
    inner class MyViewHolder(val binding: ItemFavouritecityBinding):RecyclerView.ViewHolder(binding.root)
    {
        fun bind(data: Search)
        {
            binding.city = data
            binding.executePendingBindings()
        }
    }
    fun setOnClickListener(listener: OnClickListener)
    {
        this.onClickListener = listener
    }
}