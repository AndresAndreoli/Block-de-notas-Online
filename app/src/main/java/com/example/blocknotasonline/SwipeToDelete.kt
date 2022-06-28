package com.example.blocknotasonline

import android.app.Activity
import android.graphics.Canvas
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FieldValue.delete
import com.google.firebase.firestore.util.FileUtil.delete
import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator
import java.nio.file.Files.delete


class SwipeToDelete(var adapter: NoteAdapter, activity: Activity, view: RecyclerView): ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {

    var activity = activity
    var view = view

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        TODO("Not yet implemented")
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        adapter.deleteItem(viewHolder.adapterPosition, view)
    }

    override fun onChildDraw(
        c: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean
    ) {
        RecyclerViewSwipeDecorator.Builder(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
            .addBackgroundColor(ContextCompat.getColor(activity, R.color.delete))
            .addActionIcon(R.drawable.ic_baseline_delete_24)
            .create()
            .decorate()

        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
    }
}