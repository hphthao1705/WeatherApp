package com.example.weatherapp.view.fragment

import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.example.weatherapp.R
import com.example.weatherapp.databinding.FragmentCityBinding
import com.example.weatherapp.databinding.FragmentLoadingBinding
import kotlinx.coroutines.processNextEventInCurrentThread

class FragmentLoading : Fragment() {
    private lateinit var binding: FragmentLoadingBinding
    val handler:Handler = Handler()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= DataBindingUtil.inflate(inflater, R.layout.fragment_loading, container, false)
        val view: View = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //binding.progressBar.setProgress(3000,true)
        //binding.progressBar.postDelayed(Runnable {  },5000)
        //handler.postDelayed(Runnable { },5000)
    }
}