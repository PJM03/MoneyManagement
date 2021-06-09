package com.github.pjungmin.moneymanagement.notification

import android.app.Notification
import com.github.pjungmin.moneymanagement.data.NotificationAnalyzeResult
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

    enum class TransactionType{
        DEPOSIT, WITHDRAW;

    }

}