package com.github.pjungmin.moneymanagement.notification

import android.app.Notification
import android.os.Bundle
import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import android.text.TextUtils
import com.github.pjungmin.moneymanagement.notification.NotificationAnalyzer.NotificationAnalyzeResult
import com.github.pjungmin.moneymanagement.notification.NotificationAnalyzer.TransactionType
import com.github.pjungmin.moneymanagement.notification.NotificationAnalyzer.TransactionType.DEPOSIT
import com.github.pjungmin.moneymanagement.notification.NotificationAnalyzer.TransactionType.WITHDRAW
import java.text.SimpleDateFormat
import java.util.*
import java.util.function.Consumer
import kotlin.collections.HashMap

class NotificationListener : NotificationListenerService() {
    private val notificationAnalyzerMap: HashMap<String, NotificationAnalyzer> = HashMap<String, NotificationAnalyzer>()

    override fun onListenerConnected() {
        super.onListenerConnected()
        notificationAnalyzerMap["com.IBK.SmartPush.app"] = object: NotificationAnalyzer {
            override fun analyze(noti: Notification?): NotificationAnalyzeResult {
                val extras = noti?.extras
                println(noti)
                println(extras)
                val lines = extras?.getString("android.text")?.split("\n")
                val data = lines?.get(0)?.split(" ")
                return NotificationAnalyzeResult(
                        getTransactionType(data?.get(0)),
                        data?.get(1)?.replace("[,원]".toRegex(), "")?.toInt(),
                        SimpleDateFormat("MM/dd HH:mm").parse(lines?.get(2)),
                        TextUtils.join(" ", data?.toTypedArray()?.copyOfRange(2, data?.size ?: 0) ?: arrayOf())
                )
            }
        }
    }

    override fun onNotificationPosted(sbn: StatusBarNotification?) {
        val result = notificationAnalyzerMap[sbn?.packageName]?.analyze(sbn?.notification)

    }
}