package com.smoodi.smoodi

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import com.google.mlkit.vision.codescanner.GmsBarcodeScanning
import com.smoodi.smoodi.data.Data

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val btScan: Button = findViewById(R.id.btScan)
        val btSuper: Button = findViewById(R.id.btSuper)

        btScan.setOnClickListener {

            val context = this@LoginActivity

            /* Scan Barcode */
            login("test")
//            GmsBarcodeScanning.getClient(context). also {
//                it.startScan()
//                    .addOnSuccessListener { barcode ->
//                        val rawValue: String? = barcode.rawValue
//                        login(rawValue!!)
//                    }
//                    .addOnFailureListener {
//                        val toast = getString(R.string.error_api)
//                        Toast.makeText(context, toast, Toast.LENGTH_SHORT).show()
//                    }
//            }
        }

        btSuper.setOnClickListener {
            val intent = Intent(this, SuperActivity::class.java)
            startActivity(intent)
        }
    }

    private fun login(id: String) {

        val db = Firebase.firestore

        db.collection(Data.KEY_USERS).document(id)
            .get()
            .addOnSuccessListener { document ->

                if (document.data == null) {

                    Data.id = id

                    val intent = Intent(this, JoinActivity::class.java)
                    startActivity(intent)

                } else {

                    val name = document.data!![Data.KEY_NAME].toString()
                    Data().setUserData(this, id, name)

                    val intent = Intent(this, MainActivity::class.java)
                    finishAffinity()
                    startActivity(intent)

                }

            }.addOnFailureListener {
                val toast = getString(R.string.error_server)
                Toast.makeText(this, toast, Toast.LENGTH_SHORT).show()
            }

    }
}