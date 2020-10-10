package com.karakostas.realweather

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

class WeatherAdapter() :
    ListAdapter<Weather, WeatherAdapter.ViewHolder>(WeatherDiffCallBack()) {
    private var mContext : Context? = null
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            mContext = parent.context
            val view: View = layoutInflater.inflate(R.layout.weather, parent, false)
            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val weather: Weather = getItem(position)
            holder.degreeTextView?.text = weather.degrees.toString()
        }



    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val degreeTextView:TextView? = itemView.findViewById(R.id.weather_textview)

        }

    class WeatherDiffCallBack: DiffUtil.ItemCallback<Weather?>() {
        override fun areItemsTheSame(oldItem: Weather, newItem: Weather): Boolean {
            return false
        }

        override fun areContentsTheSame(oldItem: Weather, newItem: Weather): Boolean {
            return false
        }
    }
}