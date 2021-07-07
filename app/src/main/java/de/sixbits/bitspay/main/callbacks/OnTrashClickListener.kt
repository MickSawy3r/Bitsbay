package de.sixbits.bitspay.main.callbacks

import androidx.recyclerview.widget.RecyclerView
import de.sixbits.bitspay.network.model.ImageListItemModel

interface OnTrashClickListener {
    fun onDelete(image: ImageListItemModel)
    fun startSwipe(view: RecyclerView.ViewHolder)
    fun endSwipe(image: ImageListItemModel)
}