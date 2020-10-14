package com.karakostas.realweather

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonDeserializer
import com.fasterxml.jackson.databind.JsonNode

@JsonIgnoreProperties(ignoreUnknown = true)
class WeatherDeserializer() : JsonDeserializer<Weather>() {
    override fun deserialize(p: JsonParser, ctxt: DeserializationContext?): Weather {
        val weatherNode: JsonNode = p.codec.readTree(p)

        val categoryNode: JsonNode = weatherNode.get("weather")[0]
        val weather = Weather()
        weather.degrees = weatherNode.get("temp").asDouble()
        weather.time = weatherNode.get("dt").asLong()
        if (weatherNode.get("sunset") != null) weather.sunset = weatherNode.get("sunset").asLong()
        weather.cat = categoryNode.get("main").asText()
        weather.icon = categoryNode.get("icon").asText()
        weather.clouds = weatherNode.get("clouds").asInt()
        weather.subcat = categoryNode.get("description").asText()
        return weather
    }
}

