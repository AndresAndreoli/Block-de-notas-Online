package com.example.blocknotasonline

import android.content.Intent
import android.media.MediaRouter
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

class MainActivity : AppCompatActivity() {

    private lateinit var auth : FirebaseAuth
    private lateinit var db : FirebaseFirestore
    private lateinit var collectionReference: CollectionReference
    lateinit var noteAdapter: NoteAdapter
    lateinit var rvNotes: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()
        collectionReference = db.collection("users").document(LoginActivity.email).collection("notes")


        setUpRecyclerView()
    }

    private fun setUpRecyclerView(){
        val query: Query = collectionReference
        rvNotes = findViewById(R.id.rvNotes)

        val firestoreRecyclerOptions: FirestoreRecyclerOptions<NoteModel> = FirestoreRecyclerOptions.Builder<NoteModel>()
            .setQuery(query, NoteModel::class.java)
            .build()

        noteAdapter = NoteAdapter(firestoreRecyclerOptions, this)
        rvNotes.layoutManager = LinearLayoutManager(this)
        rvNotes.adapter = noteAdapter

        var itemTouchHelper = ItemTouchHelper(SwipeToDelete(noteAdapter, this, rvNotes))
        itemTouchHelper.attachToRecyclerView(rvNotes)
    }

    override fun onStart() {
        super.onStart()

        noteAdapter.startListening()
    }

    override fun onDestroy() {
        super.onDestroy()
        noteAdapter.stopListening()
    }

    fun callsignOut(view: View){
        signOut()
    }

    private fun signOut(){
        auth.signOut()
        startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }

    fun callAddNoteActivity(view: View){
        addNoteActivity()
    }

    private fun addNoteActivity(){
        startActivity(Intent(this, AddNote_activity::class.java))
    }

}