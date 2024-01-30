package com.smoodi.smoodi

import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import androidx.appcompat.app.AppCompatActivity
import com.smoodi.smoodi.data.Data
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.jsoup.Jsoup
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.TemporalAdjusters

class IntroActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_intro)

        Data().getUserData(this)

        val sleep = 1_500L

        object : CountDownTimer(sleep, sleep) {

            val context = this@IntroActivity

            override fun onTick(millisUntilFinished: Long) {
                runBlocking {
                    launch(Dispatchers.IO) { getMealData() }
                }
            }

            override fun onFinish() {

                Intent(
                    context,
                    if (Data.id.isEmpty()) {
                        LoginActivity::class.java
                    } else {
                        MainActivity::class.java
                    }
                ).also {
                    finish()
                    startActivity(it)
                }

            }
        }.start()
    }

    private fun getMealData() {

        /* Setup Query Strings */
        val formatter = DateTimeFormatter.ofPattern("yyyyMMdd")

        val now = LocalDate.now() // LocalDate.of()
        val formattedNow = now.format(formatter)

        val nextMonday = now.with(TemporalAdjusters.next(DayOfWeek.MONDAY))
        val formattedNextMonday = nextMonday.format(formatter)

        val nextFriday = nextMonday.plusDays(4)
        val formattedNextFriday = nextFriday.format(formatter)

        val meals = mutableMapOf<String, String>()

        val api = "https://open.neis.go.kr/hub/mealServiceDietInfo"
        val queryStrings = mutableMapOf(
            "KEY" to BuildConfig.API_KEY,
            "Type" to "xml",
            "pIndex" to "1",
            "pSize" to "100",
            "ATPT_OFCDC_SC_CODE" to "M10",
            "SD_SCHUL_CODE" to "8000091",
            "MLSV_YMD" to formattedNow,
        )

        /* Get Today's Meal */
        val nowMeal = Jsoup.connect(api).data(queryStrings).get()

        for (row in nowMeal.select("row")) {

            val dishList = arrayListOf<String>()

            for (dish in row.select("DDISH_NM").text()
                .replace("<br/>", "\n")
                .split("\n")) {
                dishList.add(removeAllergyMark(dish))
            }

            val mealName = row.select("MMEAL_SC_NM").text()
            meals[mealName] = dishList.joinToString("\n")
        }

        /* Get Next Week's Meal */
        queryStrings.remove("MLSV_YMD")
        queryStrings["MLSV_FROM_YMD"] = formattedNextMonday
        queryStrings["MLSV_TO_YMD"] = formattedNextFriday

        val nextMeal = Jsoup.connect(api).data(queryStrings).get()

        for (row in nextMeal.select("row")) {

            val mealName = row.select("MMEAL_SC_NM").text()

            if (mealName == getString(R.string.main_meal3)) {

                val dishList = arrayListOf<String>()

                for (dish in row.select("DDISH_NM").text()
                    .replace("<br/>", "\n")
                    .split("\n")) {
                    dishList.add(removeAllergyMark(dish))
                }

                val date = row.select("MLSV_YMD").text()
                meals[getDayOfWeek(date)] = dishList.joinToString("\n")
            }
        }

        /* Save Data on Data Class temporarily */
        Data.meals = meals
        Data.now = formattedNow

    }

    private fun getDayOfWeek(date: String): String {
        val formatter = DateTimeFormatter.ofPattern("yyyyMMdd")
        val localDate = LocalDate.parse(date, formatter)
        val dayOfWeek = localDate.dayOfWeek
        return when (dayOfWeek!!) {
            DayOfWeek.MONDAY -> getString(R.string.apply_mon)
            DayOfWeek.TUESDAY -> getString(R.string.apply_tue)
            DayOfWeek.WEDNESDAY -> getString(R.string.apply_wed)
            DayOfWeek.THURSDAY -> getString(R.string.apply_thu)
            DayOfWeek.FRIDAY -> getString(R.string.apply_fri)
            else -> ""
        }
    }

    private fun removeAllergyMark(dish: String): String {
        val allergyMarkStart = dish.indexOf("(")
        return if (allergyMarkStart != -1) {
            dish.substring(0, allergyMarkStart-1)
        } else dish
    }
}