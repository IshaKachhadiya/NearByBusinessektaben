package com.festum.nearbybusiness.owner.ui.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.festum.nearbybusiness.owner.R
import com.festum.nearbybusiness.owner.ui.Model.Country


class CountryAdapter(
    private val countries: List<Country>,
    private val onItemClick: (Country) -> Unit
) : RecyclerView.Adapter<CountryAdapter.CountryViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CountryViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_country, parent, false)
        return CountryViewHolder(view, onItemClick)
    }

    override fun onBindViewHolder(holder: CountryViewHolder, position: Int) {
        val country = countries[position]
        holder.bind(country)
    }
    override fun getItemCount() = countries.size

    class CountryViewHolder(itemView: View, private val onItemClick: (Country) -> Unit) :
        RecyclerView.ViewHolder(itemView) {
        fun bind(country: Country) {
            val countryNameTextView: TextView = itemView.findViewById(R.id.countryNameTextView)
            val countryCodeTextView: TextView = itemView.findViewById(R.id.countryCodeTextView)

            countryCodeTextView.text = country.dial_code
            Log.e("TAG", "bind: "+country.dial_code )
            countryNameTextView.text = country.name
            itemView.setOnClickListener { onItemClick(country) }
        }
    }
}