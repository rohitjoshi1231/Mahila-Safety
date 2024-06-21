package com.domingo.mahila_saftey

import android.content.Intent
import android.net.Uri
import android.view.Gravity
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.domingo.mahila_saftey.ui.modules.News
import org.jsoup.Jsoup
import java.net.URLEncoder
import java.util.Calendar
import java.util.Locale

class Repository {

    fun makeCall(number: String, type: String, message: String, callback: (Intent) -> Unit) {
        when (type) {
            "call" -> {
                val intent = Intent(Intent.ACTION_CALL).apply {
                    data = Uri.parse("tel:$number")
                }
                callback(intent)
            }

            "whatsapp" -> {

                val intent = Intent(Intent.ACTION_VIEW)
                intent.data = Uri.parse(
                    "whatsapp://send?phone=$number&text=${
                        URLEncoder.encode(
                            message, "UTF-8"
                        )
                    }"
                )
                callback(intent)
            }
        }
    }

    fun getCurrentTime(): String {
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault())
        val day = c.get(Calendar.DAY_OF_MONTH)
        val hour = c.get(Calendar.HOUR_OF_DAY)
        val minute = c.get(Calendar.MINUTE)
        val second = c.get(Calendar.SECOND)
        val amOrPm = c.get(Calendar.AM_PM)
        val period = if (amOrPm == Calendar.AM) "AM" else "PM"

        return "$day/$month/$year - $hour:$minute:$second $period"
    }


    fun scrapeNews(url: String): List<News> {
        val newsList = mutableListOf<News>()
        val doc = Jsoup.connect(url).get()
        val elements = doc.select(".src_itm-ttl")
        val descriptions = doc.select(".src_itm-txt")
        val imgClass = doc.select(".img_brd")
        val imgTags = imgClass.select("img")

        val minSize = minOf(elements.size, descriptions.size, imgTags.size)

        for (i in 0 until minSize) {
            val linkTitle = elements[i].text()
            val description = descriptions.getOrNull(i)?.text() ?: ""
            val href = elements[i].select("a[href]").attr("href")
            val src = imgTags[i].attr("src")

            val data = News(linkTitle, description, src, href)
            newsList.add(data)
        }
        return newsList
    }

}