package com.karakostas.realweather

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.roundToInt

class HourlyWeatherAdapter() :
    ListAdapter<Weather, HourlyWeatherAdapter.ViewHolder>(WeatherDiffCallBack()) {
    private lateinit var mContext: Context
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        mContext = parent.context
        val view: View = layoutInflater.inflate(R.layout.hourly_weather_layout, parent, false)
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
        if (weather.icon[2] == 'd') {
            when (weather.clouds) {
                in 0..10 -> Glide.with(mContext).load(R.drawable.sun_yellow_large).into(holder.weatherImageView)
                in 11..25 -> Glide.with(mContext).load(R.drawable.sun_clouds_light).into(holder.weatherImageView)
                in 26..50 -> Glide.with(mContext).load(R.drawable.sun_clouds_scattered).into(holder.weatherImageView)
                else -> Glide.with(mContext).load(R.drawable.clouds_broken_overcast).into(holder.weatherImageView)
            }

        } else {
            when (weather.clouds) {
                in 0..10 -> Glide.with(mContext).load(R.drawable.moon).into(holder.weatherImageView)
                in 11..25 -> Glide.with(mContext).load(R.drawable.moon_cloud_light).into(holder.weatherImageView)
                in 26..50 -> Glide.with(mContext).load(R.drawable.moon_clouds_scattered).into(holder.weatherImageView)
                else -> Glide.with(mContext).load(R.drawable.clouds_broken_overcast).into(holder.weatherImageView)
            }

        }

    }


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val degreeTextView: TextView = itemView.findViewById(R.id.weather_textview)
        val timeTextView: TextView = itemView.findViewById(R.id.time_textview)
        val weatherImageView: ImageView = itemView.findViewById(R.id.weather_imageview)
    }

    class WeatherDiffCallBack : DiffUtil.ItemCallback<Weather?>() {
        override fun areItemsTheSame(oldItem: Weather, newItem: Weather): Boolean {
            return false
        }

        override fun areContentsTheSame(oldItem: Weather, newItem: Weather): Boolean {
            return false
        }
    }
}