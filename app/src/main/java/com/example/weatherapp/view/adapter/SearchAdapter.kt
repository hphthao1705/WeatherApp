package com.example.weatherapp.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherapp.R
import com.example.weatherapp.data.model.Data
import com.example.weatherapp.databinding.ItemSearchBinding

class SearchAdapter(private var itemList: List<Data>): RecyclerView.Adapter<SearchAdapter.MyViewHolder>() {
    private var onClickListener: OnClickListener? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding:ItemSearchBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.item_search,
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
        fun onClick(city:Data)
    }
    inner class MyViewHolder(val binding: ItemSearchBinding):RecyclerView.ViewHolder(binding.root)
    {
        fun bind(data: Data)
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