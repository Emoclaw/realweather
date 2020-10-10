package com.karakostas.realweather

import com.fasterxml.jackson.annotation.JsonAlias
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

@JsonIgnoreProperties(ignoreUnknown = true)
class Weather(@JsonProperty("temp") var degrees: Float, var sunset: Long, var sunrise: Long, @JsonProperty("dt") var time: Long) {


}