package com.example.weatherapp.view.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherapp.R
import com.example.weatherapp.databinding.ItemCityBinding
import com.example.weatherapp.utils.WeatherUtils
import com.example.weatherapp.viewmodel.uiViewModel.CityUIViewModel

class CityAdapter(): RecyclerView.Adapter<CityAdapter.MyViewHolder>(){
    private var onClickListener: OnClickListener? = null
    private var itemList: List<CityUIViewModel> = emptyList()

    fun setDataList(list: List<CityUIViewModel>) {
        this.itemList = list
        notifyDataSetChanged()
    }

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
        fun onClick(city:CityUIViewModel)
    }
    inner class MyViewHolder(val binding:ItemCityBinding):RecyclerView.ViewHolder(binding.root)
    {
        fun bind(data: CityUIViewModel)
        {
            binding.city = data
            Log.d("Thao Ho", data.image)
            binding.backgroundImage.setImageBitmap(WeatherUtils.getBitmapFromURL(data.image))
            binding.executePendingBindings()
        }
    }
    fun setOnClickListener(listener: OnClickListener)
    {
        this.onClickListener = listener
    }
}