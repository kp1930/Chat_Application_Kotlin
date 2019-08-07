package com.example.chatapplication.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.chatapplication.R
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        textViewSignUp.setOnClickListener {
            val intent = Intent(this@LoginActivity, RegisterActivity::class.java)
            startActivity(intent)
        }

        buttonLogIn.setOnClickListener {
            login()
        }
    }

    private fun login() {
        val etEmailL = editTextEmailL.text.toString()
        val etPasswordL = editTextPasswordL.text.toString()

        if (etEmailL.isEmpty() || !etEmailL.contains('@') || !etEmailL.contains(".com")) {
            editTextEmailL.requestFocus()
            editTextEmailL.error = "Please enter valid email"
        }
        if (etPasswordL.isEmpty()) {
            editTextPasswordL.requestFocus()
            editTextPasswordL.error = "Please enter valid password"
        }

        FirebaseAuth.getInstance().signInWithEmailAndPassword(etEmailL, etPasswordL)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val intent = Intent(this@LoginActivity, LatestMessageActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(intent)
                } else {
                    Toast.makeText(this@LoginActivity, "Please enter valid email or password", Toast.LENGTH_LONG).show()
                }
            }
            .addOnFailureListener {
                Toast.makeText(this@LoginActivity, "Please enter valid email or password", Toast.LENGTH_LONG).show()
            }
    }
}