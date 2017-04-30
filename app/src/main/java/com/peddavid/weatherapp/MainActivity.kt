package com.peddavid.weatherapp

import android.app.job.JobInfo
import android.app.job.JobScheduler
import android.content.ComponentName
import android.content.Context
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.annotation.RequiresApi
import com.peddavid.weatherapp.model.contentprovider.WeatherContract
import org.jetbrains.anko.*
import java.util.*

class MainActivity : AppCompatActivity() {

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val jobInfo = JobInfo.Builder(0, ComponentName(this, TestJobService::class.java))
                .setPeriodic(3000)
                .build()
        val scheduler = getSystemService(Context.JOB_SCHEDULER_SERVICE) as JobScheduler
        scheduler.schedule(jobInfo)
        val layout = verticalLayout {
            val city = editText()
            button("Search City") {
                onClick { startService(intentFor<TestService>("city" to city.text)) }
            }
            button("Update") {
                onClick { testInflate() }
            }
        }
        setContentView(layout)
    }

    fun testInflate() {
        val projection = arrayOf(
                WeatherContract.CurrentWeather.CITY,
                WeatherContract.CurrentWeather.TEMP,
                WeatherContract.CurrentWeather.MAX_TEMP,
                WeatherContract.CurrentWeather.MIN_TEMP,
                WeatherContract.CurrentWeather.DESCRIPTION)
        val cursor = contentResolver.query(
                WeatherContract.CurrentWeather.CONTENT_URI,
                projection,
                null,
                null,
                null)
        while(cursor.moveToNext()) {
            val root = relativeLayout {
                val cityView = textView(cursor.getString(0)){
                    id = Random().nextInt() //TODO(PedDavid): Nice Hack
                    textSize = 20.0f
                }.lparams {
                    centerHorizontally()
                }
                val tempView = textView("${cursor.getFloat(1)}ºC") {
                    id = Random().nextInt() //TODO(PedDavid): Nice Hack
                    textSize = 20.0f
                }.lparams {
                    centerHorizontally()
                    below(cityView)         //TODO(PedDavid): Only works if view has id, so the nice hack
                }
                val maxTempView = textView("${cursor.getFloat(2)}") {
                    id = Random().nextInt() //TODO(PedDavid): Nice Hack
                    textSize = 10.0f
                }. lparams {
                    rightOf(tempView)
                    below(cityView)
                }
                textView("${cursor.getFloat(3)}") {
                    textSize = 10.0f
                }. lparams {
                    rightOf(tempView)
                    below(maxTempView)
                }
                textView(cursor.getString(4)) {
                    textSize = 15.0f
                }. lparams {
                    centerHorizontally()
                    below(tempView)
                }
            }
            setContentView(root)
        }
        cursor.close()
    }
}
