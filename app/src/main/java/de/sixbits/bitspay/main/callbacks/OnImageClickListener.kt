package de.sixbits.bitspay.main.callbacks

import android.content.Intent
import android.net.Uri
import androidx.recyclerview.widget.RecyclerView
import de.sixbits.bitspay.network.model.ImageListItemModel

interface OnImageClickListener {
    fun onClick(image: ImageListItemModel)
    fun startDragging(view: RecyclerView.ViewHolder)
    fun onSharePressed(image: ImageListItemModel)
    fun onDelete(image: ImageListItemModel)
}