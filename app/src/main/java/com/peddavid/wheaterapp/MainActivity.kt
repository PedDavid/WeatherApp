package com.peddavid.wheaterapp

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import org.jetbrains.anko.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val layout = verticalLayout {
            val city = editText()
            button("Search City") {
                onClick { startService(intentFor<TestService>("city" to city.text)) }
            }
        }
        setContentView(layout)
    }
}
