package com.example.util

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.example.MainActivity
import com.example.R

object NotificationHelper {
    const val CHANNEL_BOOKINGS = "futurus_bookings_channel"
    const val CHANNEL_ACCOUNT = "futurus_account_channel"

    fun ensureChannels(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as? NotificationManager
                ?: return

            val bookingsChannel = NotificationChannel(
                CHANNEL_BOOKINGS,
                "Agendamentos e Cuidados",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Alertas de novos agendamentos, confirmações e atualizações de passeios"
                enableVibration(true)
            }

            val accountChannel = NotificationChannel(
                CHANNEL_ACCOUNT,
                "Conta, Pets e Mensagens",
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = "Mensagens de cuidadores, cadastro de pets e selo de verificação"
            }

            manager.createNotificationChannel(bookingsChannel)
            manager.createNotificationChannel(accountChannel)
        }
    }

    fun hasNotificationPermission(context: Context): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
        } else {
            true
        }
    }

    fun postSystemNotification(
        context: Context,
        notificationId: Int,
        title: String,
        body: String,
        isBookingAlert: Boolean = true
    ) {
        if (!hasNotificationPermission(context)) return
        ensureChannels(context)

        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
        }
        val pendingIntent = PendingIntent.getActivity(
            context,
            notificationId,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val channelId = if (isBookingAlert) CHANNEL_BOOKINGS else CHANNEL_ACCOUNT
        val priority = if (isBookingAlert) {
            NotificationCompat.PRIORITY_HIGH
        } else {
            NotificationCompat.PRIORITY_DEFAULT
        }

        val notification = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(R.drawable.ic_notification_paw)
            .setColor(0xFF5D13E6.toInt())
            .setContentTitle(title)
            .setContentText(body)
            .setStyle(NotificationCompat.BigTextStyle().bigText(body))
            .setPriority(priority)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .build()

        try {
            NotificationManagerCompat.from(context).notify(notificationId, notification)
        } catch (_: SecurityException) {
            // Permission revoked at runtime
        }
    }
}
