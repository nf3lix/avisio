package com.avisio.dashboard.usecase.crud_box.common

import android.annotation.TargetApi
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.util.Log.DEBUG
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.avisio.dashboard.R
import com.avisio.dashboard.common.data.model.box.AvisioBox
import com.avisio.dashboard.common.data.transfer.getBoxObject
import com.avisio.dashboard.common.persistence.card.CardRepository
import com.google.gson.internal.bind.util.ISO8601Utils.format
import kotlinx.android.synthetic.main.activity_box_detail.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.lang.String.format
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*


class BoxDetailActivity : AppCompatActivity() {

    private lateinit var box: AvisioBox
    private lateinit var cardRepository: CardRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        cardRepository = CardRepository(application)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_box_detail)
        box = intent.getBoxObject()!!
        actionBarSetup()

        box_creation_date_detail_view_content.text =  dateToString()

        GlobalScope.launch {
            setBoxCardCount()
            setOpenCardsCount()
        }
    }

    private fun dateToString(): String {
        val dateFormatter = SimpleDateFormat("dd.MM.yyyy")
        Log.d("TEST", box.createDate.toString())
        Log.d("TEST2", dateFormatter.format(box.createDate))
        return dateFormatter.format(box.createDate)
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private fun actionBarSetup() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val actionBar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.box_detail_app_bar)
            actionBar?.title = box.name
        }
    }

    private suspend fun setBoxCardCount() {
        box_card_count_detail_view_content.text = cardRepository.getCardsByBox(box.id).count().toString()
    }

    private fun setOpenCardsCount() {
        val allItems = cardRepository.getCardsByBoxIdWithSMDetails(box.id)
        var count = 0
        for(item in allItems) {
            if(item.smCardItem == null) {
                count++
                continue
            }
            if(item.smCardItem.dueDate.time < System.currentTimeMillis()) {
                count++
            }
        }
        box_card_open_detail_view_content.text = count.toString()
    }

}