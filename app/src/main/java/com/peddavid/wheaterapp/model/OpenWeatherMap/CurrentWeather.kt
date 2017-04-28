package com.peddavid.wheaterapp.model.OpenWeatherMap

import android.os.Parcel
import android.os.Parcelable
import com.fasterxml.jackson.annotation.JsonProperty

data class CurrentWeather(
        val coord: Coordinates,
        val weather: List<Weather>,
        val base: String,
        val main: Main,
        val wind: Wind,
        val clouds: Cloud,
        val rain: Rain?,
        val snow: Snow?,
        val dt: Int,
        val sys: Sys,
        val id: Int,
        val name: String,
        val cod: Int
) : Parcelable
{
    companion object
    {
        @JvmField @Suppress("unused")
        val CREATOR = object : Parcelable.Creator<CurrentWeather> {
            override fun createFromParcel(source: Parcel) = CurrentWeather(source)
            override fun newArray(size: Int): Array<CurrentWeather?> = arrayOfNulls(size)
        }
    }
    constructor(source: Parcel) : this(
            source.readParcelable<Coordinates>(Coordinates::class.java.classLoader),
            mutableListOf<Weather>().apply { source.readTypedList(this, Weather.CREATOR) },
            source.readString(),
            source.readParcelable<Main>(Main::class.java.classLoader),
            source.readParcelable<Wind>(Wind::class.java.classLoader),
            source.readParcelable<Cloud>(Cloud::class.java.classLoader),
            source.readParcelable<Rain>(Rain::class.java.classLoader),
            source.readParcelable<Snow>(Snow::class.java.classLoader),
            source.readInt(),
            source.readParcelable<Sys>(Sys::class.java.classLoader),
            source.readInt(),
            source.readString(),
            source.readInt()
    )
    override fun writeToParcel(dest: Parcel?, flags: Int) {
        dest?.apply {
            writeParcelable(coord, flags)
            writeTypedList(weather)
            writeString(base)
            writeParcelable(main, flags)
            writeParcelable(wind, flags)
            writeParcelable(clouds, flags)
            writeParcelable(rain, flags)
            writeParcelable(snow, flags)
            writeInt(dt)
            writeParcelable(sys, flags)
            writeInt(id)
            writeString(name)
            writeInt(cod)
        }
    }
    override fun describeContents() = 0

    data class Coordinates(
            val lon: Float,
            val lat: Float
    ) : Parcelable
    {
        companion object
        {
            @JvmField @Suppress("unused")
            val CREATOR = object : Parcelable.Creator<Coordinates> {
                override fun createFromParcel(source: Parcel) = Coordinates(source)
                override fun newArray(size: Int): Array<Coordinates?> = arrayOfNulls(size)
            }
        }
        constructor(source: Parcel) : this(source.readFloat(), source.readFloat())
        override fun writeToParcel(dest: Parcel?, flags: Int) {
            dest?.apply {
                writeFloat(lon)
                writeFloat(lat)
            }
        }
        override fun describeContents() = 0
    }

    data class Weather(
            val id: Int,
            val main: String,
            val description: String,
            val icon: String
    ) : Parcelable
    {
        companion object
        {
            @JvmField @Suppress("unused")
            val CREATOR = object : Parcelable.Creator<Weather> {
                override fun createFromParcel(source: Parcel) = Weather(source)
                override fun newArray(size: Int): Array<Weather?> = arrayOfNulls(size)
            }
        }
        constructor(source: Parcel) : this(
                source.readInt(),
                source.readString(),
                source.readString(),
                source.readString()
        )
        override fun writeToParcel(dest: Parcel?, flags: Int) {
            dest?.apply {
                writeInt(id)
                writeString(main)
                writeString(description)
                writeString(icon)
            }
        }
        override fun describeContents() = 0
    }

    data class Main(
            val temp: Float,
            val pressure: Float,                                //hPa
            val humidity: Int,                                  //%
            @JsonProperty("temp_min") val minTemp: Float,
            @JsonProperty("temp_max") val maxTemp: Float,
            @JsonProperty("sea_level")  val seaLevel: Float,    //hPa
            @JsonProperty("grnd_level") val groundLevel: Float  //hPa
    ) : Parcelable
    {
        companion object
        {
            @JvmField @Suppress("unused")
            val CREATOR = object : Parcelable.Creator<Main> {
                override fun createFromParcel(source: Parcel) = Main(source)
                override fun newArray(size: Int): Array<Main?> = arrayOfNulls(size)
            }
        }
        constructor(source: Parcel) : this(
                source.readFloat(),
                source.readFloat(),
                source.readInt(),
                source.readFloat(),
                source.readFloat(),
                source.readFloat(),
                source.readFloat()
        )
        override fun writeToParcel(dest: Parcel?, flags: Int) {
            dest?.apply {
                writeFloat(temp)
                writeFloat(pressure)
                writeInt(humidity)
                writeFloat(minTemp)
                writeFloat(maxTemp)
                writeFloat(seaLevel)
                writeFloat(groundLevel)
            }
        }
        override fun describeContents() = 0
    }

    data class Wind(
            val speed: Float,
            val deg: Float
    ) : Parcelable
    {
        companion object
        {
            @JvmField @Suppress("unused")
            val CREATOR = object : Parcelable.Creator<Wind> {
                override fun createFromParcel(source: Parcel) = Wind(source)
                override fun newArray(size: Int): Array<Wind?> = arrayOfNulls(size)
            }
        }
        constructor(source: Parcel) : this(source.readFloat(), source.readFloat())
        override fun writeToParcel(dest: Parcel?, flags: Int) {
            dest?.apply {
                writeFloat(speed)
                writeFloat(deg)
            }
        }
        override fun describeContents() = 0
    }

    data class Cloud(
            val all: Float                                        //%
    ) : Parcelable
    {
        companion object
        {
            @JvmField @Suppress("unused")
            val CREATOR = object : Parcelable.Creator<Cloud> {
                override fun createFromParcel(source: Parcel) = Cloud(source)
                override fun newArray(size: Int): Array<Cloud?> = arrayOfNulls(size)
            }
        }
        constructor(source: Parcel) : this(source.readFloat())
        override fun writeToParcel(dest: Parcel?, flags: Int) {
            dest?.apply {
                writeFloat(all)
            }
        }
        override fun describeContents() = 0
    }

    data class Rain(
            @JsonProperty("3h") val volume: Float                //Volume in Last 3 hours
    ) : Parcelable
    {
        companion object
        {
            @JvmField @Suppress("unused")
            val CREATOR = object : Parcelable.Creator<Rain> {
                override fun createFromParcel(source: Parcel) = Rain(source)
                override fun newArray(size: Int): Array<Rain?> = arrayOfNulls(size)
            }
        }
        constructor(source: Parcel) : this(source.readFloat())
        override fun writeToParcel(dest: Parcel?, flags: Int) {
            dest?.apply {
                writeFloat(volume)
            }
        }
        override fun describeContents() = 0
    }

    data class Snow(
            @JsonProperty("3h") val volume: Float                 //Volume in Last 3 hours
    ) : Parcelable
    {
        companion object
        {
            @JvmField @Suppress("unused")
            val CREATOR = object : Parcelable.Creator<Snow> {
                override fun createFromParcel(source: Parcel) = Snow(source)
                override fun newArray(size: Int): Array<Snow?> = arrayOfNulls(size)
            }
        }
        constructor(source: Parcel) : this(source.readFloat())
        override fun writeToParcel(dest: Parcel?, flags: Int) {
            dest?.apply {
                writeFloat(volume)
            }
        }
        override fun describeContents() = 0
    }

    data class Sys(
            val type: Int,
            val id: Int,
            val message: Float,
            val country: String,
            val sunrise: Long,
            val sunset: Long
    ) : Parcelable
    {
        companion object
        {
            @JvmField @Suppress("unused")
            val CREATOR = object : Parcelable.Creator<Sys> {
                override fun createFromParcel(source: Parcel) = Sys(source)
                override fun newArray(size: Int): Array<Sys?> = arrayOfNulls(size)
            }
        }
        constructor(source: Parcel) : this(
                source.readInt(),
                source.readInt(),
                source.readFloat(),
                source.readString(),
                source.readLong(),
                source.readLong()
        )
        override fun writeToParcel(dest: Parcel?, flags: Int) {
            dest?.apply {
                writeInt(type)
                writeInt(id)
                writeFloat(message)
                writeString(country)
                writeLong(sunrise)
                writeLong(sunset)
            }
        }
        override fun describeContents() = 0
    }
}