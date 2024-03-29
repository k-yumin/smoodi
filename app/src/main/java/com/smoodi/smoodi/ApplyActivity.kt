package com.smoodi.smoodi

import android.graphics.drawable.GradientDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.view.View
import android.widget.Button
import android.widget.CheckBox
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.widget.addTextChangedListener
import com.google.firebase.Firebase
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.firestore
import com.opencsv.CSVWriter
import com.smoodi.smoodi.data.Data
import java.io.FileOutputStream
import java.io.OutputStreamWriter

class ApplyActivity : AppCompatActivity() {

    private val db = Firebase.firestore

    private var dataAlways = mutableMapOf<String, Any>()
    private var dataNext = mutableMapOf<String, Any>()

    private var initialCheckBoxStatus = false
    private var initialButtonStatus = false
    private var buttonStatus = false

    private lateinit var cbApply: CheckBox
    private lateinit var btApply: Button
    private lateinit var tvApply: TextView

    private var grayBright: Int = 0
    private var gold: Int = 0
    private var appColor: Int = 0

    private fun Button.setBackgroundTint(color: Int) {
        val drawable: GradientDrawable = background.mutate() as GradientDrawable
        drawable.setColor(color)
        drawable.invalidateSelf()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_apply)

        /* Close Activity */
        val tvBack: TextView = findViewById(R.id.tvApplyBack)
        tvBack.setOnClickListener { finish() }

        /* Setup Views */
        setupMeal()

        cbApply = findViewById(R.id.cbApply)
        btApply = findViewById(R.id.btApply)
        tvApply = findViewById(R.id.tvApply)

        grayBright = ContextCompat.getColor(this, R.color.gray_bright)
        gold = ContextCompat.getColor(this, R.color.gold)
        appColor = ContextCompat.getColor(this, R.color.app_color)

        cbApply.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                btApply.isEnabled = false
                btApply.text = getString(R.string.apply_apply_true)
                btApply.setBackgroundTint(grayBright)

                buttonStatus = false

                if (initialCheckBoxStatus || initialButtonStatus) {
                    update(0)
                } else {
                    update(1)
                }
            } else {
                btApply.isEnabled = true
                btApply.text = getString(R.string.apply_apply)
                btApply.setBackgroundTint(appColor)

                if (initialCheckBoxStatus || initialButtonStatus) {
                    update(-1)
                } else {
                    update(0)
                }
            }
        }

        btApply.setOnClickListener {
            if (Data.isSuper()) {
                download()
            } else {
                if (buttonStatus) {
                    buttonStatus = false
                    btApply.text = getString(R.string.apply_apply)
                    btApply.setBackgroundTint(appColor)

                    if (initialCheckBoxStatus || initialButtonStatus) {
                        update(-1)
                    } else {
                        update(0)
                    }
                } else {
                    buttonStatus = true
                    btApply.text = getString(R.string.apply_apply_true)
                    btApply.setBackgroundTint(grayBright)

                    if (initialCheckBoxStatus || initialButtonStatus) {
                        update(0)
                    } else {
                        update(1)
                    }
                }
            }
        }

        if (Data.isSuper()) {
            cbApply.visibility = View.GONE
            btApply.text = getString(R.string.apply_download)
            btApply.setBackgroundTint(gold)
        }

        getSession()
    }

    private fun setupMeal() {

        val tvMon: TextView = findViewById(R.id.tvMon)
        val tvTue: TextView = findViewById(R.id.tvTue)
        val tvWed: TextView = findViewById(R.id.tvWed)
        val tvThu: TextView = findViewById(R.id.tvThu)
        val tvFri: TextView = findViewById(R.id.tvFri)

        val tvMeal: TextView = findViewById(R.id.tvApplyMeal)
        tvMeal.addTextChangedListener { text ->
            if (text.toString().isEmpty()) {
                tvMeal.text = getString(R.string.main_meal_null)
            }
        }

        val fontBlack = ResourcesCompat.getFont(this, R.font.black)
        val fontRegular = ResourcesCompat.getFont(this, R.font.regular)

        tvMon.setOnClickListener {
            tvMon.typeface = fontBlack
            tvTue.typeface = fontRegular
            tvWed.typeface = fontRegular
            tvThu.typeface = fontRegular
            tvFri.typeface = fontRegular

            tvMeal.text = Data.meals[getString(R.string.apply_mon)]
        }.also { tvMon.callOnClick() }
        tvTue.setOnClickListener {
            tvMon.typeface = fontRegular
            tvTue.typeface = fontBlack
            tvWed.typeface = fontRegular
            tvThu.typeface = fontRegular
            tvFri.typeface = fontRegular

            tvMeal.text = Data.meals[getString(R.string.apply_tue)]
        }
        tvWed.setOnClickListener {
            tvMon.typeface = fontRegular
            tvTue.typeface = fontRegular
            tvWed.typeface = fontBlack
            tvThu.typeface = fontRegular
            tvFri.typeface = fontRegular

            tvMeal.text = Data.meals[getString(R.string.apply_wed)]
        }
        tvThu.setOnClickListener {
            tvMon.typeface = fontRegular
            tvTue.typeface = fontRegular
            tvWed.typeface = fontRegular
            tvThu.typeface = fontBlack
            tvFri.typeface = fontRegular

            tvMeal.text = Data.meals[getString(R.string.apply_thu)]
        }
        tvFri.setOnClickListener {
            tvMon.typeface = fontRegular
            tvTue.typeface = fontRegular
            tvWed.typeface = fontRegular
            tvThu.typeface = fontRegular
            tvFri.typeface = fontBlack

            tvMeal.text = Data.meals[getString(R.string.apply_fri)]
        }
    }

    private fun getSession() {

        cbApply.isEnabled = false
        btApply.isEnabled = false

        db.collection(Data.KEY_APPLY)
            .get()
            .addOnSuccessListener { result ->

                var status = ""

                for (document in result) {
                    if (Data.id in document.data) {
                        status = document.id
                    }
                    when (document.id) {
                        Data.KEY_ALWAYS -> dataAlways = document.data
                        Data.KEY_NEXT -> dataNext = document.data
                    }
                }

                when (status) {
                    Data.KEY_ALWAYS -> {
                        initialCheckBoxStatus = true
                        initialButtonStatus = false
                    }
                    Data.KEY_NEXT -> {
                        initialCheckBoxStatus = false
                        initialButtonStatus = true
                    }
                }

                cbApply.isChecked = initialCheckBoxStatus
                buttonStatus = initialButtonStatus

                cbApply.isEnabled = true
                btApply.isEnabled = true

                if (!Data.isSuper()) {
                    if (cbApply.isChecked || buttonStatus) {
                        btApply.text = getString(R.string.apply_apply_true)
                        btApply.setBackgroundTint(grayBright)
                    } else {
                        btApply.text = getString(R.string.apply_apply)
                        btApply.setBackgroundTint(appColor)
                    }
                }

                update(0)

            }.addOnFailureListener {
                val toast = getString(R.string.error_server)
                Toast.makeText(this, toast, Toast.LENGTH_SHORT).show()
            }

    }

    private fun download() {

        /* Write on a csv */
        val type = Environment.DIRECTORY_DOWNLOADS
        val file = "${Environment.getExternalStoragePublicDirectory(type)}" +
                "/${Data.now}_운호고_석식_신청_명단.csv"

        val list = arrayListOf<Array<String>>()

        val header = arrayOf("학번", "이름")
        list.add(header)

        for (student in dataAlways.values) {
            val item = student.toString().split(" ")
            list.add(arrayOf(item[0], item[1]))
        }

        for (student in dataNext.values) {
            val item = student.toString().split(" ")
            list.add(arrayOf(item[0], item[1]))
        }

        val encoding = "EUC-KR"

        FileOutputStream(file).use { fos ->
            OutputStreamWriter(fos, encoding).use { osw ->
                CSVWriter(osw).use { cw ->
                    cw.writeAll(list)
                }
            }
        }

        /* Toast Success */
        val toast = getString(R.string.apply_download_true)
        Toast.makeText(this, toast, Toast.LENGTH_SHORT).show()

    }

    override fun onDestroy() {
        super.onDestroy()

        val isCheckBoxStatusChanged = initialCheckBoxStatus != cbApply.isChecked
        val isButtonStatusChanged = initialButtonStatus != buttonStatus

        if (isCheckBoxStatusChanged) {

            val docRef = db.collection(Data.KEY_APPLY).document(Data.KEY_ALWAYS)

            if (cbApply.isChecked) {
                docRef.set(mapOf(Data.id to Data.name))
                    .addOnFailureListener {
                        val toast = getString(R.string.error_server)
                        Toast.makeText(this, toast, Toast.LENGTH_SHORT).show()
                    }
            } else {
                val updates = hashMapOf<String, Any>(
                    Data.id to FieldValue.delete(),
                )
                docRef.update(updates)
                    .addOnFailureListener {
                        val toast = getString(R.string.error_server)
                        Toast.makeText(this, toast, Toast.LENGTH_SHORT).show()
                    }
            }

        }

        if (isButtonStatusChanged) {

            val docRef = db.collection(Data.KEY_APPLY).document(Data.KEY_NEXT)

            if (buttonStatus) {
                docRef.set(mapOf(Data.id to Data.name))
                    .addOnFailureListener {
                        val toast = getString(R.string.error_server)
                        Toast.makeText(this, toast, Toast.LENGTH_SHORT).show()
                    }
            } else {
                val updates = hashMapOf<String, Any>(
                    Data.id to FieldValue.delete(),
                )
                docRef.update(updates)
                    .addOnFailureListener {
                        val toast = getString(R.string.error_server)
                        Toast.makeText(this, toast, Toast.LENGTH_SHORT).show()
                    }
            }

        }
    }

    private fun update(add: Int) {
        val count = dataNext.size + dataAlways.size + add
        val total = "현재까지 ${count}명이 신청했습니다"
        tvApply.text = total
    }
}