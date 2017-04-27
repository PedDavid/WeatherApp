package com.peddavid.wheaterapp

import android.app.Service
import android.content.Intent
import android.os.IBinder
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
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
        val mapper = jacksonObjectMapper()
        init {
            mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
        }
    }
    override fun onBind(intent: Intent?): IBinder? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)
        val city = intent?.extras?.get("city") ?: "Lisbon"
        doAsync {
            Thread.sleep(3000)
            val url = URL("http://api.openweathermap.org/data/2.5/weather?q=${city}&units=metric&appid=c1acb32ea454f0837464e058f608471d")
            val con = url.openConnection() as HttpURLConnection
            val input = BufferedReader(InputStreamReader(con.inputStream)).use{ it.readText() }
            val currentWeather = mapper.readValue<CurrentWeather>(input)
            info { currentWeather }
            stopSelf(startId)
        }
        return START_NOT_STICKY
    }
}

data class CurrentWeather(
        val weather: List<Weather>
)

data class Weather(
        val id: Int,
        val main: String,
        val description: String,
        val icon: String
)
