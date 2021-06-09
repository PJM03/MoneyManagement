package com.github.pjungmin.moneymanagement.activity

import android.os.Bundle
import android.os.Process
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.github.pjungmin.moneymanagement.R
import com.github.pjungmin.moneymanagement.databinding.MainScreenBinding
import com.github.pjungmin.moneymanagement.util.isPermissionAllowed

class MainScreenActivity : AppCompatActivity() {
    lateinit var binding: MainScreenBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_screen)
        binding = DataBindingUtil.setContentView(this, R.layout.main_screen)


    }

    override fun onBackPressed() {
        moveTaskToBack(true)
        finishAndRemoveTask()
        Process.killProcess(Process.myPid())
    }
}