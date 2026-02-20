package com.example.run.presentation.active_run.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import androidx.core.app.TaskStackBuilder
import androidx.core.content.getSystemService
import androidx.core.net.toUri
import com.example.core.presentation.designsystem.R
import com.example.presentation.ui.formatted
import com.example.presentation.ui.toFormattedPace
import com.example.run.domain.RunningTracker
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.koin.android.ext.android.inject

class ActiveRunService : Service() {
    private val notiManager by lazy {
        getSystemService<NotificationManager>()!!
    }

    private val runningTracker by inject<RunningTracker>()

    private var serviceScope = CoroutineScope(Dispatchers.Main + SupervisorJob())

    private val baseNoti by lazy {
        NotificationCompat.Builder(applicationContext, CHANNEL_ID).setSmallIcon(R.drawable.logo)
            .setContentTitle(
                getString(R.string.active_run)
            )
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (intent?.action) {
            ACTION_START -> {
                val activityClass = intent.getStringExtra(EXTRA_ACTIVITY_CLASS)
                    ?: throw IllegalArgumentException("No activity class provided")

                // Create a class reference based on specific name
                start(Class.forName(activityClass))
            }

            ACTION_STOP -> {
                stop()
            }
        }
        return START_STICKY
    }

    private fun start(activityClass: Class<*>) {
        if (!isServiceActive) {
            isServiceActive = true
            createNotiChanel()

            val activityIntent = Intent(applicationContext, activityClass).apply {
                data = "gumrun://active_run".toUri()
                addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
            }

            // Pending intent as a deep link
            val pendingIntent = TaskStackBuilder.create(applicationContext).run {
                addNextIntentWithParentStack(activityIntent)
                getPendingIntent(0, PendingIntent.FLAG_IMMUTABLE)
            }

            val notification =
                baseNoti.setContentText("00:00:00").setContentIntent(pendingIntent).build()

            // Id can be anything except 0
            startForeground(1, notification)
            updateNotification()
        }
    }

    private fun updateNotification() {
        runningTracker.elapsedTime.onEach { elapsedTime ->
            val noti = baseNoti.setContentText(elapsedTime.formatted()).build()

            notiManager.notify(1, noti)
        }.launchIn(serviceScope)
    }

    private fun stop() {
        stopSelf()
        isServiceActive = false
        serviceScope.cancel()

        // Assign new service scope when the app is closed, the service is still active but is not used anymore
        serviceScope = CoroutineScope(Dispatchers.Main + SupervisorJob())

    }

    private fun createNotiChanel() {
        if (Build.VERSION.SDK_INT >= 26) {
            val channel = NotificationChannel(
                CHANNEL_ID, getString(R.string.active_run),
                NotificationManager.IMPORTANCE_DEFAULT
            )

            notiManager.createNotificationChannel(channel)
        }
    }

    companion object {
        var isServiceActive = false
        private const val CHANNEL_ID = "active_run"

        private const val ACTION_START = "start"
        private const val ACTION_STOP = "stop"
        private const val EXTRA_ACTIVITY_CLASS = "activity_class"


        fun createStartIntent(context: Context, activityClass: Class<*>): Intent {
            return Intent(context, ActiveRunService::class.java).apply {
                action = ACTION_START
                putExtra(EXTRA_ACTIVITY_CLASS, activityClass.name)
            }
        }

        fun createStopIntent(context: Context): Intent {
            return Intent(context, ActiveRunService::class.java).apply {
                action = ACTION_STOP
            }
        }
    }
}
