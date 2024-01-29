package com.smoodi.smoodi

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import com.smoodi.smoodi.data.Data

class SuperActivity : AppCompatActivity() {

    private lateinit var tvSuper: TextView
    private lateinit var etSuper: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_super)

        tvSuper = findViewById(R.id.tvSuper)
        etSuper = findViewById(R.id.etSuper)

        val maxTries = 5
        var tries = 0

        etSuper.addTextChangedListener { text ->
            val passcode = text.toString()

            if (passcode.length == 6) {

                etSuper.isEnabled = false

                if (tries > maxTries) {
                    tvSuper.text = getString(R.string.super_over)
                    return@addTextChangedListener
                }

                login(passcode)
                tries++
            }

        }

    }

    private fun login(passcode: String) {

        val db = Firebase.firestore

        val id = Data.KEY_SUPER

        db.collection(Data.KEY_USERS).document(id)
            .get()
            .addOnSuccessListener { document ->

                val data = document.data!!

                if (data[Data.KEY_PASSCODE] == passcode) {

                    val name = data[Data.KEY_NAME].toString()
                    Data().setUserData(this, id, name)

                    val intent = Intent(this, MainActivity::class.java)
                    finishAffinity()
                    startActivity(intent)

                } else {
                    etSuper.setText("")
                    etSuper.isEnabled = true
                    tvSuper.text = getString(R.string.super_wrong)
                }

            }.addOnFailureListener {
                val toast = getString(R.string.error_server)
                Toast.makeText(this, toast, Toast.LENGTH_SHORT).show()
            }

    }
}