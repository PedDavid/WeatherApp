package com.peddavid.weatherapp

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.peddavid.weatherapp.model.contentprovider.WeatherContract
import org.jetbrains.anko.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val layout = verticalLayout {
            val cityView = textView()
            val tempView = textView()
            val city = editText()
            button("Search City") {
                onClick { startService(intentFor<TestService>("city" to city.text)) }
            }
            button("Update") {
                onClick {
                    val projection = arrayOf(
                            WeatherContract.CurrentWeather.CITY,
                            WeatherContract.CurrentWeather.TEMP)
                    val cursor = contentResolver.query(
                            WeatherContract.CurrentWeather.CONTENT_URI,
                            projection,
                            null,
                            null,
                            null)
                    while(cursor.moveToNext()) {
                        cityView.text = cursor.getString(0)
                        tempView.text = cursor.getFloat(1).toString()
                    }
                    cursor.close()
                }
            }
        }
        setContentView(layout)
    }
}
