package de.sixbits.bitspay.main.callbacks

import de.sixbits.bitspay.network.model.ImageListItemModel

interface OnImageClickListener {
    fun onClick(image: ImageListItemModel)
}