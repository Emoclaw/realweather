package com.karakostas.realweather

import android.Manifest
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.fasterxml.jackson.databind.InjectableValues
import com.fasterxml.jackson.databind.node.ObjectNode
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.material.appbar.AppBarLayout
import kotlinx.android.synthetic.main.activity_main.*
import okhttp3.*
import java.io.IOException
import java.util.*
import kotlin.math.roundToInt

class MainActivity : AppCompatActivity() {
    private val client = OkHttpClient()
    private val adapter = HourlyWeatherAdapter()
    private val dailyAdapter = DailyWeatherAdapter()
    lateinit var fusedLocationClient: FusedLocationProviderClient
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        rv_weather_hourly.adapter = adapter
        rv_weather_hourly.layoutManager = LinearLayoutManager(applicationContext, LinearLayoutManager.HORIZONTAL, false)
        rv_weather_daily.adapter = dailyAdapter
        rv_weather_daily.layoutManager = LinearLayoutManager(applicationContext, LinearLayoutManager.HORIZONTAL, false)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 0)
        } else {
            fusedLocationClient.lastLocation.addOnSuccessListener(this) { location: Location? ->
                if (location != null) {
                    fetch(location)
                }
            }
        }

        val appBarLayout: AppBarLayout = findViewById(R.id.appbar_layout)
        val motionLayout: MotionLayout = findViewById(R.id.motion_layout)
        val listener = AppBarLayout.OnOffsetChangedListener { _, verticalOffset ->
            val seekPosition = -verticalOffset / appBarLayout.totalScrollRange.toFloat()
            if (seekPosition <= 50) {
                motionLayout.progress = seekPosition
            }
        }

        appBarLayout.addOnOffsetChangedListener(listener)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when (requestCode) {
            0 -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    fusedLocationClient.lastLocation.addOnSuccessListener(this) { location: Location? ->
                        if (location != null) {
                            fetch(location)
                        }
                    }
                } else {
                    finish()
                }
            }
        }
    }


    private fun fetch(location: Location) {
        val geocoder = Geocoder(this, Locale.getDefault())
        val addresses: MutableList<Address>? = geocoder.getFromLocation(location.latitude, location.longitude, 1)

        if (addresses != null) {
            main_location_textview.text = addresses[0].locality + ", " + addresses[0].countryName
        }
        val url: HttpUrl = HttpUrl.Builder().scheme("https")
            .host("api.openweathermap.org")
            .addPathSegments("data/2.5/onecall")
            .addQueryParameter("lat", location.latitude.toString())
            .addQueryParameter("lon", location.longitude.toString())
            .addQueryParameter("exclude", "minutely")
            .addQueryParameter("units", "metric")
            .addQueryParameter("appid", getString(R.string.openweather_api))
            .build()
        val request = Request.Builder()
            .url(url)
            .build()
        var hourlyWeatherList: ArrayList<Weather>
        var dailyWeatherList: ArrayList<Weather>
        var mainWeather: Weather
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                TODO("Not yet implemented")
            }

            override fun onResponse(call: Call, response: Response) {
                val injectables = InjectableValues.Std().addValue("weatherType", 0)
                val mapper = jacksonObjectMapper()
                mapper.injectableValues = injectables
                val node: ObjectNode = mapper.readValue(response.body!!.string())
                mainWeather = mapper.readValue(node.get("current").toString())
                hourlyWeatherList = mapper.readValue(node.get("hourly").toString())
                injectables.addValue("weatherType", 1)
                dailyWeatherList = mapper.readValue(node.get("daily").toString())
                runOnUiThread {
                    main_weather_temperature.text = mainWeather.degrees.roundToInt().toString() + "°C"
                    main_status_textview.text = mainWeather.subcat
                    adapter.submitList(hourlyWeatherList.take(23))
                    dailyAdapter.submitList(dailyWeatherList)
                    if (mainWeather.icon[2] == 'd') {
                        when (mainWeather.clouds) {
                            in 0..10 -> Glide.with(applicationContext).load(R.drawable.sun_yellow_large)
                                .into(main_weather_icon)
                            in 11..25 -> Glide.with(applicationContext).load(R.drawable.sun_clouds_light)
                                .into(main_weather_icon)
                            in 26..50 -> Glide.with(applicationContext).load(R.drawable.sun_clouds_scattered)
                                .into(main_weather_icon)
                            else -> Glide.with(applicationContext).load(R.drawable.clouds_broken_overcast)
                                .into(main_weather_icon)
                        }

                    } else {
                        when (mainWeather.clouds) {
                            in 0..10 -> Glide.with(applicationContext).load(R.drawable.moon).into(main_weather_icon)
                            in 11..25 -> Glide.with(applicationContext).load(R.drawable.moon_cloud_light)
                                .into(main_weather_icon)
                            in 26..50 -> Glide.with(applicationContext).load(R.drawable.moon_clouds_scattered)
                                .into(main_weather_icon)
                            else -> Glide.with(applicationContext).load(R.drawable.clouds_broken_overcast)
                                .into(main_weather_icon)
                        }

                    }
                }

            }

        })
        //Glide.with(this).load(R.drawable.clear_sky).into(weather_image)
        Glide.with(this).load(R.drawable.location_white).into(location_icon)
    }

}
