package de.sixbits.bitspay.main.callbacks

import androidx.recyclerview.widget.RecyclerView
import de.sixbits.bitspay.network.model.ImageListItemModel

interface OnFeedClickListener {
    fun onClick(image: ImageListItemModel)
    fun startDragging(view: RecyclerView.ViewHolder)
    fun onSharePressed(image: ImageListItemModel)
}