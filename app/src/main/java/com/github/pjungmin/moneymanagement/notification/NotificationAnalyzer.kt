package com.github.pjungmin.moneymanagement.notification

import android.app.Notification
import java.util.*

interface NotificationAnalyzer {
    fun analyze(noti: Notification?) : NotificationAnalyzeResult

    fun getTransactionType(s : String?): TransactionType? {
        return when(s) {
            "[입급]" -> TransactionType.DEPOSIT
            "[출금]" -> TransactionType.WITHDRAW
            else -> null
        }
    }

    data class NotificationAnalyzeResult(val type: TransactionType?, val value: Int?, val date: Date, val from: String)
    enum class TransactionType{
        DEPOSIT, WITHDRAW;

    }

}