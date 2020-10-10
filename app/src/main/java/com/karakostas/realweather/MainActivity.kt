package com.karakostas.realweather

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.google.android.material.appbar.AppBarLayout
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

private val adapter = WeatherAdapter()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        rv_weather_hourly.adapter = adapter
        rv_weather_hourly.layoutManager = LinearLayoutManager(applicationContext,LinearLayoutManager.HORIZONTAL,false)
        val list = ArrayList<Weather>()
        list.add(Weather(25))
        list.add(Weather(25))
        list.add(Weather(25))
        list.add(Weather(25))
        list.add(Weather(25))
        list.add(Weather(25))
        list.add(Weather(25))
        list.add(Weather(25))
        list.add(Weather(25))
        list.add(Weather(25))
        list.add(Weather(25))
        list.add(Weather(25))
        list.add(Weather(257))
        list.add(Weather(235))
        adapter.submitList(list)

        Glide.with(this).load(R.drawable.clear_sky).into(weather_image)

        val appBarLayout: AppBarLayout = findViewById(R.id.appbar_layout)
        val motionLayout: MotionLayout = findViewById(R.id.motion_layout)
        val listener = AppBarLayout.OnOffsetChangedListener { _, verticalOffset ->
            val seekPosition = -verticalOffset / appBarLayout.totalScrollRange.toFloat()
            motionLayout.progress = seekPosition
        }

        appBarLayout.addOnOffsetChangedListener(listener)
        }


}
