package com.example.blocknotasonline

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.firestore.FirebaseFirestore

class AddNote_activity : AppCompatActivity() {

    private var db = FirebaseFirestore.getInstance()
    private lateinit var etAddTitle : EditText
    private lateinit var etAddContent : EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_note)

        etAddTitle = findViewById(R.id.etAddTitle)
        etAddContent = findViewById(R.id.etAddContent)
    }

    fun callAddNote(view: View){
        if (etAddTitle.text.isNotEmpty() && etAddContent.text.isNotEmpty()){
            addNote()
        } else {
            Toast.makeText(this, "Complete fields", Toast.LENGTH_SHORT).show()
        }
    }

    private fun addNote(){
        db.collection("users").document(LoginActivity.email).collection("notes").add(hashMapOf(
            "title" to etAddTitle.text.toString(),
            "content" to etAddContent.text.toString(),
            "checked" to false
        )).addOnCompleteListener{
            if (it.isSuccessful){
                Toast.makeText(this, "Note added", Toast.LENGTH_SHORT).show()
            }
        }

        startActivity(Intent(this, MainActivity::class.java))
    }
}