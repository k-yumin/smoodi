package com.smoodi.smoodi

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.widget.addTextChangedListener
import com.google.zxing.BarcodeFormat
import com.journeyapps.barcodescanner.BarcodeEncoder
import com.smoodi.smoodi.data.Data
import java.text.SimpleDateFormat
import java.util.Locale

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        /* Setup Logout Button */
        val tvReset: TextView = findViewById(R.id.tvReset)
        tvReset.setOnClickListener {

            Data().setUserData(this, "", "")

            val intent = Intent(this, LoginActivity::class.java)
            finish()
            startActivity(intent)
        }

        /* Setup Views */
        setupBarcode()
        setupDate()
        setupMeal()
        setupMenu()

        val tvIcon: TextView = findViewById(R.id.tvIcon)
        val tvName: TextView = findViewById(R.id.tvName)

        if (Data.isSuper()) {
            val gold = ContextCompat.getColor(this, R.color.gold)
            tvIcon.setTextColor(gold)
        }

        tvName.text = Data.name

        // To display open source notices
//        startActivity(Intent(this, OssLicensesMenuActivity::class.java))

    }

    private fun setupBarcode() {

        val ivBarcode: ImageView = findViewById(R.id.ivBarcode)
        val tvBarcode: TextView = findViewById(R.id.tvBarcode)

        val barcodeEncoder = BarcodeEncoder()
        val bitmap = barcodeEncoder.encodeBitmap(
            Data.id, BarcodeFormat.CODE_128, 600, 200)

        ivBarcode.setImageBitmap(bitmap)
        tvBarcode.text = Data.id

    }

    private fun setupDate() {

        val tvDate: TextView = findViewById(R.id.tvDate)

        val dateFormat1 = SimpleDateFormat("yyyyMMdd", Locale.KOREA)
        val dateFormat2 = SimpleDateFormat("yyyy. M. d.", Locale.KOREA)

        val date = dateFormat2.format(dateFormat1.parse(Data.now)!!)

        tvDate.text = date

    }

    private fun setupMeal() {

        val tvMeal1: TextView = findViewById(R.id.tvMeal1)
        val tvMeal2: TextView = findViewById(R.id.tvMeal2)
        val tvMeal3: TextView = findViewById(R.id.tvMeal3)

        val tvMeal: TextView = findViewById(R.id.tvMeal)
        tvMeal.addTextChangedListener { text ->
            if (text.toString().isEmpty()) {
                tvMeal.text = getString(R.string.main_meal_null)
            }
        }

        val fontBlack = ResourcesCompat.getFont(this, R.font.black)
        val fontRegular = ResourcesCompat.getFont(this, R.font.regular)

        tvMeal1.setOnClickListener {
            tvMeal1.typeface = fontBlack
            tvMeal2.typeface = fontRegular
            tvMeal3.typeface = fontRegular

            tvMeal.text = Data.meals[getString(R.string.main_meal1)]
        }
        tvMeal2.setOnClickListener {
            tvMeal1.typeface = fontRegular
            tvMeal2.typeface = fontBlack
            tvMeal3.typeface = fontRegular

            tvMeal.text = Data.meals[getString(R.string.main_meal2)]
        }.also { tvMeal2.callOnClick() }
        tvMeal3.setOnClickListener {
            tvMeal1.typeface = fontRegular
            tvMeal2.typeface = fontRegular
            tvMeal3.typeface = fontBlack

            tvMeal.text = Data.meals[getString(R.string.main_meal3)]
        }
    }

    private fun setupMenu() {

        val btGoRank: Button = findViewById(R.id.btGoRank)
        val btGoRate: Button = findViewById(R.id.btGoRate)
        val btGoApply: Button = findViewById(R.id.btGoApply)

        btGoRank.setOnClickListener {
            val intent = Intent(this, RankActivity::class.java)
            startActivity(intent)
        }
        btGoRate.setOnClickListener {
            val intent = Intent(this, RateActivity::class.java)
            startActivity(intent)
        }
        btGoApply.setOnClickListener {
            val intent = Intent(this, ApplyActivity::class.java)
            startActivity(intent)
        }
    }
}