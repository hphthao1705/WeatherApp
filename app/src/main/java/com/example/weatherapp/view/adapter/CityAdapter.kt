package com.example.weatherapp.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherapp.R
import com.example.weatherapp.data.model.City
import com.example.weatherapp.data.model.Data
import com.example.weatherapp.databinding.ItemCityBinding

class CityAdapter (private var itemList: List<Data>): RecyclerView.Adapter<CityAdapter.MyViewHolder>(){
    private var onClickListener: OnClickListener? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding:ItemCityBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.item_city,
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
    inner class MyViewHolder(val binding:ItemCityBinding):RecyclerView.ViewHolder(binding.root)
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