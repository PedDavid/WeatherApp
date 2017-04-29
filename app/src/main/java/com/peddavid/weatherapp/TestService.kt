package com.peddavid.weatherapp

import android.app.Service
import android.content.ContentValues
import android.content.Intent
import android.os.IBinder
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.peddavid.weatherapp.model.contentprovider.WeatherContract
import com.peddavid.weatherapp.model.openweathermap.CurrentWeather
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.info
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

class TestService : Service(), AnkoLogger
{
    companion object {
        val mapper: ObjectMapper = jacksonObjectMapper()
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
    }
    override fun onBind(intent: Intent?): IBinder? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)
        val city = intent?.extras?.get("city") ?: "Lisbon"
        doAsync {
            val url = URL("http://api.openweathermap.org/data/2.5/weather?q=${city}&units=metric&appid=bbf66afdde0edc717b3a6631c27611cc")
            val con = url.openConnection() as HttpURLConnection
            val input = BufferedReader(InputStreamReader(con.inputStream)).use{ it.readText() }
            val currentWeather = mapper.readValue<CurrentWeather>(input)
            info { currentWeather }
            val values = ContentValues().apply {
                put(WeatherContract.CurrentWeather._ID, 1)
                put(WeatherContract.CurrentWeather.CITY, currentWeather.name)
                put(WeatherContract.CurrentWeather.TEMP, currentWeather.main.temp)
                put(WeatherContract.CurrentWeather.MIN_TEMP, currentWeather.main.minTemp)
                put(WeatherContract.CurrentWeather.MAX_TEMP, currentWeather.main.maxTemp)
                put(WeatherContract.CurrentWeather.WIND_SPEED, currentWeather.wind.speed)
                put(WeatherContract.CurrentWeather.ICON, currentWeather.weather[0].icon)
                put(WeatherContract.CurrentWeather.DESCRIPTION, currentWeather.weather[0].description)
            }
            contentResolver.insert(WeatherContract.CurrentWeather.CONTENT_URI, values)
            stopSelf(startId)
        }
        return START_NOT_STICKY
    }
}
