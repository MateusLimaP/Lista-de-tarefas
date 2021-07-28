package com.mateuslima.listaafazeres.adapter

import android.graphics.Color
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import javax.inject.Inject

class ItemTouchHelperAdapter(
    private val listener: ItemTouchListener
) : ItemTouchHelper.Callback() {


    override fun isLongPressDragEnabled(): Boolean {
        return false
    }

    override fun isItemViewSwipeEnabled(): Boolean {
        return true
    }

    override fun clearView(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder) {
        super.clearView(recyclerView, viewHolder)
        viewHolder.itemView.setBackgroundColor(android.R.attr.colorBackground)
    }

    override fun onSelectedChanged(viewHolder: RecyclerView.ViewHolder?, actionState: Int) {
        super.onSelectedChanged(viewHolder, actionState)
        if (actionState == ItemTouchHelper.ACTION_STATE_DRAG)
            viewHolder?.itemView?.setBackgroundColor(Color.LTGRAY)
    }

    override fun getMovementFlags(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder): Int {
        val dragFlags = ItemTouchHelper.UP or ItemTouchHelper.DOWN
        val swipeFlags = ItemTouchHelper.START or ItemTouchHelper.END
        return makeMovementFlags(dragFlags, swipeFlags)
    }

    override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder,
                        target: RecyclerView.ViewHolder): Boolean{
       listener.onItemMoved(viewHolder.adapterPosition, target.adapterPosition)
       return true
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, directions: Int) {
        listener.onItemSwipe(viewHolder.adapterPosition)
    }

    interface ItemTouchListener{
        fun onItemMoved(fromPosition: Int, toPosition: Int)
        fun onItemSwipe(position: Int)
    }
}