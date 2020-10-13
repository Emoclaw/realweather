package com.karakostas.realweather

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.roundToInt

class WeatherAdapter() :
    ListAdapter<Weather, WeatherAdapter.ViewHolder>(WeatherDiffCallBack()) {
    private lateinit var mContext : Context
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            mContext = parent.context
            val view: View = layoutInflater.inflate(R.layout.weather, parent, false)
            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val weather: Weather = getItem(position)
            holder.degreeTextView.text = weather.degrees.roundToInt().toString() + "°C"
            val cal: Calendar = Calendar.getInstance()
            val tz: TimeZone = cal.timeZone
            val sdf = SimpleDateFormat("HH:mm")
            sdf.timeZone = tz
            val localTime = sdf.format(Date(weather.time * 1000))
            holder.timeTextView.text = localTime
            //TODO: expand icons (clouds, rain, snow etc)
            if (weather.icon[2] == 'd')
                Glide.with(mContext).load(R.drawable.sun_yellow_large).into(holder.weatherImageView)
            else
                Glide.with(mContext).load(R.drawable.moon).into(holder.weatherImageView)

        }



    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val degreeTextView:TextView = itemView.findViewById(R.id.weather_textview)
            val timeTextView:TextView = itemView.findViewById(R.id.time_textview)
            val weatherImageView: ImageView = itemView.findViewById(R.id.weather_imageview)
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