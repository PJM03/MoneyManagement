package com.github.pjungmin.moneymanagement.activity

import android.graphics.Color
import android.os.Bundle
import android.os.Process
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.animation.Easing.EasingOption.EaseInOutBack
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.utils.ColorTemplate
import com.github.mikephil.charting.utils.ColorTemplate.*
import com.github.pjungmin.moneymanagement.R
import com.github.pjungmin.moneymanagement.data.NotificationAnalyzeResult
import com.github.pjungmin.moneymanagement.databinding.HistoryListViewBinding
import com.github.pjungmin.moneymanagement.databinding.MainScreenBinding
import com.github.pjungmin.moneymanagement.notification.NotificationAnalyzer.TransactionType.DEPOSIT
import com.github.pjungmin.moneymanagement.notification.NotificationAnalyzer.TransactionType.WITHDRAW
import com.github.pjungmin.moneymanagement.util.DATA_KEY
import com.github.pjungmin.moneymanagement.util.MAPPER_PREFIX
import com.github.pjungmin.moneymanagement.util.PREF_NAME
import com.google.gson.GsonBuilder
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.LinkedHashSet

class MainScreenActivity : AppCompatActivity() {
    private val decimalFormat = DecimalFormat("#,###")
    private val dateFormat = SimpleDateFormat("yyyy/MM/dd HH:mm:ss")
    private val gson = GsonBuilder().create()
    private lateinit var binding: MainScreenBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.main_screen)
        binding = DataBindingUtil.setContentView(this, R.layout.main_screen)

        val pref = getSharedPreferences(PREF_NAME, MODE_PRIVATE)
        val dataSet = pref.getStringSet(DATA_KEY, LinkedHashSet())!!.map{ gson.fromJson(it, NotificationAnalyzeResult::class.java) }
        val dataList = dataSet.sortedBy { it.date }.reversed()

        /*Test*/
//        val edit = pref.edit()
//        for(i in 1 until 10 step 2) {
//            edit.putString(MAPPER_PREFIX + "테스트$i", "테스트" + (i+1))
//        }
//        edit.commit()
        /*Test*/

        val incomeTotal = dataList.filter { it.type == DEPOSIT }.sumBy { it.value!! }
        val spendingTotal = dataList.filter { it.type == WITHDRAW }.sumBy { it.value!! }

        binding.totalIncomeValue.text = decimalFormat.format(incomeTotal)
        binding.totalSpendingValue.text = decimalFormat.format(spendingTotal)

        val adapter = HistoryViewAdapter(dataList)
        binding.historyList.adapter = adapter
        println("wow: " + dataList.groupBy { pref.getString(MAPPER_PREFIX + it.from, it.from) })
        val chartData = dataList.filter{it.type == WITHDRAW}.groupBy{pref.getString(MAPPER_PREFIX + it.from, it.from)}.map {
            PieEntry(it.value.sumBy{ s -> s.value!! }.toFloat(), it.key)
        }.sortedBy { it.value }
        val spendingTop = chartData.slice(0..4).toMutableList()
        val etc = chartData.slice(5 until chartData.size).sumBy{ it.value.toInt() }
        spendingTop.add(PieEntry(etc.toFloat(), "기타"))
        val colorItems = ArrayList<Int>()
        listOf(VORDIPLOM_COLORS, JOYFUL_COLORS, COLORFUL_COLORS, LIBERTY_COLORS, PASTEL_COLORS).forEach{ arr ->
            arr.forEach { colorItems.add(it) }
        }
        val pieData = PieData(PieDataSet(spendingTop, "").apply {
            colors = listOf(Color.RED, Color.YELLOW, Color.GREEN, Color.BLUE, Color.MAGENTA)
            valueTextColor = ContextCompat.getColor(this@MainScreenActivity, R.color.main)
            valueTextSize = 16f
        })
        binding.pieChart.apply {
            data = pieData
            description.isEnabled = false
            isRotationEnabled = false
            setHoleColor(Color.TRANSPARENT)
            setEntryLabelColor(Color.BLACK)
            animateY(1400, EaseInOutBack)
            animate()
        }
    }

    override fun onBackPressed() {
        moveTaskToBack(true)
        finishAndRemoveTask()
        Process.killProcess(Process.myPid())
    }

    inner class HistoryViewAdapter(private val list: List<NotificationAnalyzeResult>): BaseAdapter() {
        override fun getCount() = list.size

        override fun getItem(position: Int) = list[position]

        override fun getItemId(position: Int) = position.toLong()

        override fun getView(position: Int, view: View?, parent: ViewGroup?): View {
            var convertView: View? = view
            if(view == null) convertView = LayoutInflater.from(parent?.context).inflate(R.layout.history_list_view, parent, false)
            val historyBinding: HistoryListViewBinding = DataBindingUtil.bind(convertView!!)!!
            val item = list[position]
            historyBinding.transactionTypeImage.setImageResource(when(item.type) {
                DEPOSIT -> R.drawable.ic_add_48
                else -> R.drawable.ic_remove_48
            })
            historyBinding.fromText.text = item.from
            historyBinding.value.text = decimalFormat.format(item.value)
            historyBinding.date.text = dateFormat.format(item.date)
            return convertView
        }
    }
}