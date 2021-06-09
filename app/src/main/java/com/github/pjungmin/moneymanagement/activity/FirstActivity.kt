package com.github.pjungmin.moneymanagement.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.github.pjungmin.moneymanagement.R
import com.github.pjungmin.moneymanagement.databinding.FirstBinding
import com.github.pjungmin.moneymanagement.util.isPermissionAllowed

class FirstActivity : AppCompatActivity() {
    lateinit var binding : FirstBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.first)
        binding = DataBindingUtil.setContentView(this, R.layout.first)
        if (!isPermissionAllowed(this, packageName)) startActivity(Intent(this, PermissionConfirmActivity::class.java))
        else startActivity(Intent(this, MainScreenActivity::class.java))
    }

    override fun onResume() {
        super.onResume()
        if(isPermissionAllowed(this, packageName)) binding.textView.text = "권한 허용 확인됨! ㅎㅎ"
    }
}