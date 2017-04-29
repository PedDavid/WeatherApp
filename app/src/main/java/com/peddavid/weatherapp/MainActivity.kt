package com.peddavid.weatherapp

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.peddavid.weatherapp.model.contentprovider.WeatherContract
import org.jetbrains.anko.*
import java.util.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
