package com.example.chatapplication.activities

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
import com.example.chatapplication.R
import com.example.chatapplication.models.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_register.*
import java.util.*

class RegisterActivity : AppCompatActivity() {

    private var selectedImageUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        textViewSignIn.setOnClickListener{
            val intent = Intent(this@RegisterActivity, LoginActivity::class.java)
            startActivity(intent)
        }

        buttonImage.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, 0)
        }

        buttonSignUp.setOnClickListener {
            register()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode ==  0 && resultCode == Activity.RESULT_OK && data!= null) {
            selectedImageUri = data.data
            val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, selectedImageUri)

            circularImageView.setImageBitmap(bitmap)
            buttonImage.alpha = 0f
        }
    }

    private fun register() {
        val etUsernameR = editTextUsernameR.text.toString()
        val etEmailR = editTextEmailR.text.toString()
        val etPasswordR = editTextPasswordR.text.toString()

        if (etUsernameR.isEmpty()) {
            editTextUsernameR.requestFocus()
            editTextUsernameR.error = "Please enter valid email"
        }
        if (etEmailR.isEmpty() || !etEmailR.contains('@') || !etEmailR.contains(".com")) {
            editTextEmailR.requestFocus()
            editTextEmailR.error = "Please enter valid email"
        }
        if (etPasswordR.isEmpty()) {
            editTextPasswordR.requestFocus()
            editTextPasswordR.error = "Please enter valid password"
        }

        FirebaseAuth.getInstance().createUserWithEmailAndPassword(etEmailR, etPasswordR)
            .addOnCompleteListener{
                if (!it.isSuccessful) return@addOnCompleteListener
                uploadImage()
            }
            .addOnFailureListener {
                Toast.makeText(this@RegisterActivity, "Please enter username or email or password", Toast.LENGTH_LONG).show()
            }
    }

    private fun uploadImage() {
        if (selectedImageUri == null) return

        val fileName = UUID.randomUUID().toString()
        val ref = FirebaseStorage.getInstance().getReference("images/$fileName")

        ref.putFile(selectedImageUri!!)
            .addOnSuccessListener {
                ref.downloadUrl.addOnSuccessListener {
                    saveUserToFirebase(it.toString())
                }
            }
            .addOnFailureListener{
                Toast.makeText(this@RegisterActivity, it.toString(), Toast.LENGTH_LONG).show()
            }
    }

    private fun saveUserToFirebase(profileImageUrl:String) {
        val uid = FirebaseAuth.getInstance().uid?: ""
        val ref = FirebaseDatabase.getInstance().getReference("/users/$uid")

        val user = User(uid, editTextUsernameR.text.toString(), profileImageUrl)

        ref.setValue(user)
            .addOnSuccessListener {
                val intent = Intent(this@RegisterActivity, LatestMessageActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
            }
            .addOnFailureListener {
                Toast.makeText(this@RegisterActivity, it.toString(), Toast.LENGTH_LONG).show()
            }
    }
}