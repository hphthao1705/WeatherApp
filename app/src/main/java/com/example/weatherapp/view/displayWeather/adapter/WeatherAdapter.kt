package com.example.weatherapp.view.displayWeather.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherapp.R
import com.example.weatherapp.data.model.WeatherProperties
import com.example.weatherapp.databinding.InfoWeatherBinding

class WeatherAdapter(private var itemList: List<WeatherProperties>):RecyclerView.Adapter<WeatherAdapter.MyViewHolder>() {
    fun setData(list_properties:ArrayList<WeatherProperties>)
    {
        this.itemList = list_properties
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding:InfoWeatherBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.context),R.layout.info_weather,parent,false)
        return MyViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val post = itemList[position]
        holder.bind(post)
    }
    inner class MyViewHolder(val binding: InfoWeatherBinding): RecyclerView.ViewHolder(binding.root)
    {
        fun bind(data: WeatherProperties)
        {
            binding.property = data
            binding.imgWeather.setImageResource(data.image!!)
            binding.executePendingBindings()
        }
    }
}