package app.ecommercemedical.utils

import android.app.NotificationManager
import android.content.Context
import androidx.core.app.NotificationCompat
import app.ecommercemedical.R

fun showSimpleNotification(context: Context, content: String?) {
    val builder = NotificationCompat.Builder(context, "myapp_1")
        .setSmallIcon(R.drawable.ic_launcher_foreground)
        .setContentTitle("MyApp Notification")
        .setContentText(content)
        .setPriority(NotificationCompat.PRIORITY_DEFAULT)

    val notificationManager =
        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    notificationManager.notify(1, builder.build())
}