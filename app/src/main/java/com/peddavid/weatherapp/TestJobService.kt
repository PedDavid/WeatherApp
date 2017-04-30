package com.peddavid.weatherapp

import android.app.job.JobParameters
import android.app.job.JobService
import android.os.Build
import android.support.annotation.RequiresApi
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import org.jetbrains.anko.toast

@RequiresApi(Build.VERSION_CODES.LOLLIPOP)
class TestJobService : JobService(), AnkoLogger {
    override fun onStartJob(params: JobParameters?): Boolean {
        toast("onStartJob ${params?.jobId}")
        return false
    }
    override fun onStopJob(params: JobParameters?): Boolean {
        toast("onStopJob ${params?.jobId}")
        return false
    }
}