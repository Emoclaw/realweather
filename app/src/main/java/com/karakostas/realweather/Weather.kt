package com.karakostas.realweather

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.databind.annotation.JsonDeserialize

@JsonDeserialize(using = WeatherDeserializer::class)
@JsonIgnoreProperties(ignoreUnknown = true)
class Weather() {

    var degrees: Double = 0.0
    var sunset: Long = 0
    var sunrise: Long = 0
    var time: Long = 0
    var clouds: Int = 0
    lateinit var cat: String
    lateinit var subcat: String
    lateinit var icon: String

}

