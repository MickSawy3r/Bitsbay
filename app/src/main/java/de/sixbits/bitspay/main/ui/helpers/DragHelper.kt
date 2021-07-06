package de.sixbits.bitspay.main.ui.helpers

import android.content.Context
import android.graphics.Canvas
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.ItemTouchHelper.DOWN
import androidx.recyclerview.widget.ItemTouchHelper.UP
import androidx.recyclerview.widget.ItemTouchHelper.START
import androidx.recyclerview.widget.ItemTouchHelper.END
import androidx.recyclerview.widget.RecyclerView
import de.sixbits.bitspay.main.adapters.SearchResultRecyclerAdapter

object DragHelper {
    val itemTouchHelper by lazy {
        val simpleItemTouchCallback =
            object : ItemTouchHelper.SimpleCallback(
                UP or
                        DOWN or
                        START or
                        END, 0
            ) {

                override fun onMove(
                    recyclerView: RecyclerView,
                    viewHolder: RecyclerView.ViewHolder,
                    target: RecyclerView.ViewHolder
                ): Boolean {

                    val adapter = recyclerView.adapter as SearchResultRecyclerAdapter
                    val from = viewHolder.bindingAdapterPosition
                    val to = target.bindingAdapterPosition
                    adapter.swapItems(from, to)
                    adapter.notifyItemMoved(from, to)

                    return true
                }

                override fun onSwiped(
                    viewHolder: RecyclerView.ViewHolder,
                    direction: Int
                ) {
                    val adapter = viewHolder.bindingAdapter as SearchResultRecyclerAdapter
                    adapter.deleteAt(viewHolder.bindingAdapterPosition)
                }
            }

        ItemTouchHelper(simpleItemTouchCallback)
    }
}