package com.github.pjungmin.moneymanagement.util

import android.content.Context
import androidx.core.app.NotificationManagerCompat

const val PREF_NAME = "bankingData"
const val DATA_KEY = "dataKey"
const val MAPPER_PREFIX = "mapper_"

fun isPermissionAllowed(ctx : Context, packageName : String) = NotificationManagerCompat.getEnabledListenerPackages(ctx).contains(packageName)