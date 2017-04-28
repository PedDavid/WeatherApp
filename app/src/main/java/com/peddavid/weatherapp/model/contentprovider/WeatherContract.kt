package com.peddavid.weatherapp.model.contentprovider

import android.content.ContentProvider
import android.content.ContentValues
import android.content.UriMatcher
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.net.Uri
import android.provider.BaseColumns

import android.content.ContentResolver.CURSOR_DIR_BASE_TYPE
import android.content.ContentResolver.CURSOR_ITEM_BASE_TYPE

class WeatherContract : ContentProvider()
{
    companion object
    {
        const val AUTHORITY = "com.peddavid.weatherapp"
        val AUTHORITY_URI: Uri = Uri.parse("content://$AUTHORITY")

        const val PATH_CURRENT_WEATHER = "currweather"

        /**
         * IDS
         */
        private const val CURRENT_WEATHER = 100
        private const val CURRENT_WEATHER_ID = 101

        val uriMatcher = UriMatcher(UriMatcher.NO_MATCH).apply {
            addURI(AUTHORITY, PATH_CURRENT_WEATHER, CURRENT_WEATHER)
            addURI(AUTHORITY, "$PATH_CURRENT_WEATHER/#", CURRENT_WEATHER_ID)
        }
    }

    class CurrentWeather : BaseColumns
    {
        companion object
        {
            val TABLE_NAME = "CURRENT_WEATHER"
            val CONTENT_URI: Uri = Uri.withAppendedPath(AUTHORITY_URI, PATH_CURRENT_WEATHER)

            val CONTENT_TYPE = "$CURSOR_DIR_BASE_TYPE/$CONTENT_URI"
            val CONTENT_ITEM_TYPE = "$CURSOR_ITEM_BASE_TYPE/$CONTENT_URI"

            const val _ID = BaseColumns._ID
            const val CITY = "CITY"
            const val TEMP = "TEMPERATURE"
            const val MIN_TEMP = "MINIMUM_TEMPERATURE"
            const val MAX_TEMP = "MAXIMUM_TEMPERATURE"
            const val WIND_SPEED = "WIND_SPEED"
            const val ICON = "ICON"
        }
    }

    private inner class SQLiteHelper(name: String = "weather.db", version: Int = 1)
        : SQLiteOpenHelper(this@WeatherContract.context, name, null, version)
    {
        override fun onCreate(db: SQLiteDatabase?) {
            db?.apply {
                createWeatherTable(db)
            }
        }

        private fun createWeatherTable(db: SQLiteDatabase) =
                db.execSQL("""CREATE TABLE IF NOT EXISTS ${CurrentWeather.TABLE_NAME} (
                    ${CurrentWeather._ID} INTEGER NOT NULL,
                    ${CurrentWeather.CITY} TEXT NOT NULL,
                    ${CurrentWeather.TEMP} REAL NOT NULL,
                    ${CurrentWeather.MIN_TEMP} REAL NOT NULL,
                    ${CurrentWeather.MAX_TEMP} REAL NOT NULL,
                    ${CurrentWeather.WIND_SPEED} REAL NOT NULL,
                    ${CurrentWeather.ICON} TEXT NOT NULL)""")

        private fun dropTable(db: SQLiteDatabase, tableName: String) =
                db.execSQL("DROP TABLE IF EXISTS $tableName")

        override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
            db?.apply {
                dropTable(db, CurrentWeather.TABLE_NAME)
                createWeatherTable(db);
            }
        }
    }

    private lateinit var dbHelper: SQLiteHelper

    override fun onCreate(): Boolean {
        dbHelper = SQLiteHelper()
        return true
    }

    override fun insert(uri: Uri?, values: ContentValues?): Uri {
        val db = dbHelper.writableDatabase
        db.insert(CurrentWeather.TABLE_NAME, null, values)
        return Uri.EMPTY
    }

    override fun query(uri: Uri?, projection: Array<out String>?, selection: String?, selectionArgs: Array<out String>?, sortOrder: String?): Cursor {
        val db = dbHelper.readableDatabase
        return db.query(CurrentWeather.TABLE_NAME, projection, selection, null, null, null, null)
    }

    override fun update(uri: Uri?, values: ContentValues?, selection: String?, selectionArgs: Array<out String>?): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun delete(uri: Uri?, selection: String?, selectionArgs: Array<out String>?): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getType(uri: Uri?) = when(uriMatcher.match(uri)) {
        CURRENT_WEATHER -> CurrentWeather.CONTENT_TYPE
        CURRENT_WEATHER_ID -> CurrentWeather.CONTENT_ITEM_TYPE
        else -> throw UnsupportedOperationException("Unknown uri: $uri")
    }
}