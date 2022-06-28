package com.example.blocknotasonline

import android.app.Activity
import android.content.Intent
import android.graphics.Paint.STRIKE_THRU_TEXT_FLAG
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot

class NoteAdapter(options: FirestoreRecyclerOptions<NoteModel>, activity: Activity) : FirestoreRecyclerAdapter<NoteModel, NoteAdapter.NotesViewHolder>(options) {

    var activity = activity

    class NotesViewHolder (itemView: View): RecyclerView.ViewHolder(itemView){
        var titleNote = itemView.findViewById<TextView>(R.id.tvTitle)
        var contentNote = itemView.findViewById<TextView>(R.id.tvContent)
        var checkedNote = itemView.findViewById<CheckBox>(R.id.cbChecked)
        var container = itemView.findViewById<ConstraintLayout>(R.id.clContainer)
    }

    private fun toggleStrikeThrough(tvTitle: TextView,tvContent: TextView, isChecked: Boolean) {
        if(isChecked) {
            tvContent.paintFlags = tvContent.paintFlags or STRIKE_THRU_TEXT_FLAG
            tvTitle.paintFlags = tvTitle.paintFlags or STRIKE_THRU_TEXT_FLAG
        } else {
            tvContent.paintFlags = tvContent.paintFlags and STRIKE_THRU_TEXT_FLAG.inv()
            tvTitle.paintFlags = tvTitle.paintFlags and STRIKE_THRU_TEXT_FLAG.inv()
        }
    }

    fun deleteItem(pos: Int, view: RecyclerView){
        //getSnapshots().getSnapshot(pos).getReference().delete()
        handleDeleteitem(getSnapshots().getSnapshot(pos), view)
    }

    private fun handleDeleteitem(snapshot: DocumentSnapshot, view: RecyclerView){

        var documentReference : DocumentReference = snapshot.reference
        var title = snapshot.get("title")
        var content = snapshot.get("content")

        snapshot.getReference().delete() // Borro el documento

        Snackbar.make(view, "Item deleted", Snackbar.LENGTH_LONG)
            .setAction("Undo") {
                documentReference.set(hashMapOf(
                    "title" to title,
                    "content" to content
                ))
            }
            .show()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotesViewHolder {
        return NotesViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_view, parent, false))
    }

    override fun onBindViewHolder(holder: NotesViewHolder, position: Int, model: NoteModel) {
        val notaDocument: DocumentSnapshot = getSnapshots().getSnapshot(holder.absoluteAdapterPosition)
        val id: String = notaDocument.getId()

        holder.titleNote.text = model.title
        holder.contentNote.text = model.content
        holder.checkedNote.isChecked = model.checked

        toggleStrikeThrough(holder.titleNote ,holder.contentNote, model.checked)

        holder.checkedNote.setOnCheckedChangeListener{_, isChecked ->
            toggleStrikeThrough(holder.titleNote, holder.contentNote, isChecked)
        }

        holder.container.setOnLongClickListener {
            var intent = Intent(activity, ModifyNote_Activity::class.java)
            intent.putExtra("noteId", id)
            activity.startActivity(intent)
            true
            //required that you return a boolean to notify if you actually consumed the event
        }
    }



}