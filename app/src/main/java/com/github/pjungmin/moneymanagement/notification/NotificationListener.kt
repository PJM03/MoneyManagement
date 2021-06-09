package com.github.pjungmin.moneymanagement.notification

import android.app.Notification
import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import android.text.TextUtils
import com.github.pjungmin.moneymanagement.data.NotificationAnalyzeResult
import com.github.pjungmin.moneymanagement.util.DATA_KEY
import com.github.pjungmin.moneymanagement.util.PREF_NAME
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.HashMap

class NotificationListener : NotificationListenerService() {
    private val notificationAnalyzerMap: HashMap<String, NotificationAnalyzer> = HashMap<String, NotificationAnalyzer>()
    private lateinit var gson: Gson

    override fun onListenerConnected() {
        super.onListenerConnected()
        gson = GsonBuilder().create()
        notificationAnalyzerMap["com.IBK.SmartPush.app"] = object: NotificationAnalyzer {
            override fun analyze(noti: Notification?): NotificationAnalyzeResult {
                val extras = noti?.extras
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
//        testData()
    }

    fun testData() {
        val pref = getSharedPreferences(PREF_NAME, MODE_PRIVATE)
        val editor = pref.edit()
        val data = pref.getStringSet(DATA_KEY, LinkedHashSet<String>())
        for(i in 1..10) {
            data?.add(gson.toJson(NotificationAnalyzeResult(
                when(Random().nextInt(2)) {
                    1 -> NotificationAnalyzer.TransactionType.DEPOSIT
                    else -> NotificationAnalyzer.TransactionType.WITHDRAW
                },
                Random().nextInt(100000),
                Date(System.currentTimeMillis() - Random().nextInt(60000*24*365)),
                "테스트$i"
            )))
        }
        println(data)
        editor.putStringSet(DATA_KEY, data)
        editor.commit()
    }

    override fun onNotificationPosted(sbn: StatusBarNotification?) {
        val result = notificationAnalyzerMap[sbn?.packageName]?.analyze(sbn?.notification)
        result?.run {
            val pref = getSharedPreferences(PREF_NAME, MODE_PRIVATE)
            val editor = pref.edit()
            val data = pref.getStringSet(DATA_KEY, LinkedHashSet<String>())
            data?.add(gson.toJson(this))
            println(data)
            editor.putStringSet(DATA_KEY, data)
            editor.commit()
        }
    }
}