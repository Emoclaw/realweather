package com.karakostas.realweather

import android.os.Bundle
import android.util.DisplayMetrics
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.fasterxml.jackson.databind.node.ObjectNode
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.google.android.material.appbar.AppBarLayout
import kotlinx.android.synthetic.main.activity_main.*
import okhttp3.*
import java.io.IOException
import kotlin.math.roundToInt

class MainActivity : AppCompatActivity() {
private val client = OkHttpClient()
private val adapter = WeatherAdapter()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        rv_weather_hourly.adapter = adapter
        rv_weather_hourly.layoutManager = LinearLayoutManager(applicationContext, LinearLayoutManager.HORIZONTAL, false)
        val url :HttpUrl = HttpUrl.Builder().scheme("https")
            .host("api.openweathermap.org")
            .addPathSegments("data/2.5/onecall")
            .addQueryParameter("lat", 40.629269.toString())
            .addQueryParameter("lon", 22.947412.toString())
            .addQueryParameter("exclude", "minutely")
            .addQueryParameter("units", "metric")
            .addQueryParameter("appid", getString(R.string.openweather_api))
            .build()
        val request = Request.Builder()
            .url(url)
            .build()
        var hourlyWeatherList: ArrayList<Weather>
        var mainWeather: Weather
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                TODO("Not yet implemented")
            }

            override fun onResponse(call: Call, response: Response) {
                val mapper = jacksonObjectMapper()
                val node: ObjectNode = mapper.readValue(response.body!!.string())

                mainWeather = mapper.readValue(node.get("current").toString())
                hourlyWeatherList = mapper.readValue(node.get("hourly").toString())
                runOnUiThread {
                    main_weather_temperature.text = mainWeather.degrees.roundToInt().toString() + "°C"
                    adapter.submitList(hourlyWeatherList)
                    if (System.currentTimeMillis() < mainWeather.sunset) {
                        Glide.with(applicationContext).load(R.drawable.sun_yellow_gradient).into(main_weather_icon)
                    } else {
                        Glide.with(applicationContext).load(R.drawable.moon).into(main_weather_icon)
                    }
                }

            }

        })

        //Glide.with(this).load(R.drawable.clear_sky).into(weather_image)

        Glide.with(this).load(R.drawable.location_white).into(location_icon)

        val appBarLayout: AppBarLayout = findViewById(R.id.appbar_layout)
        val motionLayout: MotionLayout = findViewById(R.id.motion_layout)
        val listener = AppBarLayout.OnOffsetChangedListener { _, verticalOffset ->
            val seekPosition = -verticalOffset / appBarLayout.totalScrollRange.toFloat()
            if (seekPosition<=50) {
                motionLayout.progress = seekPosition
            }
        }

        appBarLayout.addOnOffsetChangedListener(listener)
        }


}
