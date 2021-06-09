package com.github.pjungmin.moneymanagement.activity

import android.content.Intent
import android.os.Bundle
import android.os.Process
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.github.pjungmin.moneymanagement.R
import com.github.pjungmin.moneymanagement.databinding.PermissionConfirmBinding
import com.github.pjungmin.moneymanagement.util.isPermissionAllowed

class PermissionConfirmActivity : AppCompatActivity() {
    lateinit var binding : PermissionConfirmBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.permission_confirm)

        binding = DataBindingUtil.setContentView(this, R.layout.permission_confirm)

        binding.openSetting.setOnClickListener {
            startActivity(Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS"))
        }
    }

    override fun onResume() {
        super.onResume()
        if(isPermissionAllowed(this, packageName)) finish()
    }

//    override fun onBackPressed() {
//        if(!isPermissionAllowed(this, packageName)) {
//            moveTaskToBack(true)
//            finishAndRemoveTask()
//            Process.killProcess(Process.myPid())
//        }
//    }
}