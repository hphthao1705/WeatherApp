package com.example.weatherapp.view.fragment


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.example.weatherapp.R
import com.example.weatherapp.databinding.FragmentSearchBinding

class FragmentSearch : Fragment(), SearchView.OnQueryTextListener, MenuItem.OnActionExpandListener {
    private lateinit var binding: FragmentSearchBinding
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding= DataBindingUtil.inflate(inflater, R.layout.fragment_search, container, false)
        val view: View = binding.root
        return view
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.searchbar2.setOnClickListener{
            Toast.makeText(context,"ok",Toast.LENGTH_SHORT).show()
        }
        initControls()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {

    }
    private fun initControls()
    {
    }

    override fun onQueryTextSubmit(p0: String?): Boolean {
        TODO("Not yet implemented")
    }

    override fun onQueryTextChange(p0: String?): Boolean {
        Toast.makeText(context,"ok",Toast.LENGTH_SHORT).show()
        return true
    }

    override fun onMenuItemActionExpand(p0: MenuItem): Boolean {
        TODO("Not yet implemented")
    }

    override fun onMenuItemActionCollapse(p0: MenuItem): Boolean {
        TODO("Not yet implemented")
    }
}