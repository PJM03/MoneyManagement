package com.github.pjungmin.moneymanagement.data

import com.github.pjungmin.moneymanagement.notification.NotificationAnalyzer
import java.util.*

data class NotificationAnalyzeResult(val type: NotificationAnalyzer.TransactionType?, val value: Int?, val date: Date, val from: String)