package com.example.chatapplication.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.chatapplication.R
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        textViewSignUp.setOnClickListener{
            val intent = Intent(this@LoginActivity, RegisterActivity::class.java)
            startActivity(intent)
        }

        buttonLogIn.setOnClickListener {
            val etEmailL = editTextEmailL.text.toString()
            val etPasswordL = editTextPasswordL.text.toString()

            if (etEmailL.isEmpty() || !etEmailL.contains('@') || !etEmailL.contains(".com")) {
                editTextEmailL.requestFocus()
                editTextEmailL.error = "Please enter valid email"
            }
            if (etPasswordL.isEmpty()) {
                editTextPasswordL.requestFocus()
                editTextEmailL.error = "Please enter valid password"
            }

            FirebaseAuth.getInstance().signInWithEmailAndPassword(etEmailL, etPasswordL)
                .addOnCompleteListener {
                    val intent = Intent(this@LoginActivity, LatestMessageActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(intent)
                }
                .addOnFailureListener {
                    Toast.makeText(this@LoginActivity, it.toString(), Toast.LENGTH_LONG).show()
                }
        }
    }
}