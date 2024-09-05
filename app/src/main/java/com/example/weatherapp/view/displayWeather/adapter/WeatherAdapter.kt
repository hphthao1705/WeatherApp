package com.example.weatherapp.view.displayWeather.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherapp.R
import com.example.weatherapp.data.model.WeatherProperties
import com.example.weatherapp.databinding.InfoWeatherBinding

class WeatherAdapter : RecyclerView.Adapter<WeatherHolder>() {
    private var itemList: List<WeatherProperties> = listOf()
    fun setData(listProperties: ArrayList<WeatherProperties>) {
        itemList = listProperties
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeatherHolder {
        val binding: InfoWeatherBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.info_weather,
            parent,
            false
        )
        return WeatherHolder(binding)
    }

    override fun onBindViewHolder(holder: WeatherHolder, position: Int) {
        val post = itemList[position]
        holder.bind(post)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }
}

class WeatherHolder(val binding: InfoWeatherBinding) : RecyclerView.ViewHolder(binding.root) {
    fun bind(data: WeatherProperties) {
        binding.property = data
        binding.imgWeather.setImageResource(data.image!!)
        binding.executePendingBindings()
    }
}