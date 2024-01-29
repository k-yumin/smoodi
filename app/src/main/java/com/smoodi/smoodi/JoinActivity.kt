package com.smoodi.smoodi

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import com.smoodi.smoodi.data.Data

class JoinActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_join)

        val tvId: TextView = findViewById(R.id.tvId)
        val etNumber: EditText = findViewById(R.id.etNumber)
        val etName: EditText = findViewById(R.id.etName)
        val btJoin: Button = findViewById(R.id.btJoin)

        tvId.text = Data.id

        btJoin.setOnClickListener {
            btJoin.isEnabled = false

            val name = "${etNumber.text} ${etName.text}"
            join(Data.id, name)
        }
    }

    private fun join(id: String, name: String) {

        val db = Firebase.firestore

        db.collection(Data.KEY_USERS).document(id)
            .set(mapOf(Data.KEY_NAME to name))
            .addOnSuccessListener {

                Data().setUserData(this, id, name)

                val intent = Intent(this, MainActivity::class.java)
                finishAffinity()
                startActivity(intent)

            }
            .addOnFailureListener {
                val toast = getString(R.string.error_server)
                Toast.makeText(this, toast, Toast.LENGTH_SHORT).show()
            }

    }
}