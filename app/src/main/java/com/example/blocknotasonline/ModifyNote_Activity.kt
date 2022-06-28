package com.example.blocknotasonline

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import com.google.firebase.firestore.FirebaseFirestore

class ModifyNote_Activity : AppCompatActivity() {

    private lateinit var noteId: String
    private lateinit var etTitle: EditText
    private lateinit var etContent: EditText
    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_modify_note)

        db = FirebaseFirestore.getInstance()
        noteId = getIntent().getStringExtra("noteId").toString()

        etTitle = findViewById(R.id.etModifyTitle)
        etContent = findViewById(R.id.etModifyContent)

        mostrarDatos()
    }

    private fun mostrarDatos(){
        db.collection("users").document(LoginActivity.email).collection("notes").document(noteId).get()
            .addOnSuccessListener{
                etTitle.setText(it.get("title") as String)
                etContent.setText(it.get("content") as String)
            }
    }

    fun callModifyNote(view: View){
        modifyNote()
    }

    private fun modifyNote(){
        db.collection("users").document(LoginActivity.email).collection("notes").document(noteId).set(
            hashMapOf(
                "title" to etTitle.text.toString(),
                "content" to etContent.text.toString()
            )
        )
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}

