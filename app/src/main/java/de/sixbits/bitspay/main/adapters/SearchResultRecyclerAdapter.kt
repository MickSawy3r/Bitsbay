package de.sixbits.bitspay.main.adapters

import android.content.Context
import android.content.Intent
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestBuilder
import com.bumptech.glide.load.engine.DiskCacheStrategy
import de.sixbits.bitspay.databinding.RowImagesListBinding
import de.sixbits.bitspay.main.callbacks.OnImageClickListener
import de.sixbits.bitspay.network.model.ImageListItemModel


private const val TAG = "SearchResultRecyclerAda"

class SearchResultRecyclerAdapter constructor(
    private var searchResult: List<ImageListItemModel>,
    private val requestBuilder: RequestBuilder<Drawable>,
    private val onImageClickListener: OnImageClickListener
) :
    RecyclerView.Adapter<SearchResultRecyclerAdapter.SearchResultRecyclerViewHolder>() {

    class SearchResultRecyclerViewHolder(val binding: RowImagesListBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): SearchResultRecyclerViewHolder {
        val vh = SearchResultRecyclerViewHolder(
            RowImagesListBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

        vh.binding.cardImage.setOnLongClickListener {
            onImageClickListener.startDragging(vh)
            return@setOnLongClickListener true
        }

        return vh
    }

    override fun onBindViewHolder(holder: SearchResultRecyclerViewHolder, position: Int) {
        holder.binding.tvImageItemUsername.text = searchResult[position].username
        holder.binding.tvImageItemTags.text = searchResult[position].tags
        requestBuilder
            .load(searchResult[position].thumbnail)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .into(holder.binding.ivImageItemThumbnail)

        holder.binding.cardImage.setOnClickListener {
            onImageClickListener.onClick(searchResult[position])
        }

        holder.binding.ivDelete.setOnClickListener {
        }
    }

    override fun getItemCount(): Int = searchResult.size

    fun switchItems(searchResult: List<ImageListItemModel>) {
        Log.d(TAG, "switchItems: Switching Items ${searchResult.size}")
        this.searchResult = searchResult
        notifyDataSetChanged()
    }

    fun addItemsToCurrent(searchResult: List<ImageListItemModel>) {
        this.searchResult = this.searchResult + searchResult
        notifyDataSetChanged()
    }

    fun swapItems(startIndex: Int, endIndex: Int) {
        val result = searchResult.toMutableList()
        val temp = result[startIndex]
        result[startIndex] = result[endIndex]
        result[endIndex] = temp
        searchResult = result
    }

    private fun getShareIntent(context: Context, path: String) {
        val shareIntent = Intent(Intent.ACTION_SEND)
        shareIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_DOCUMENT)

        shareIntent.type = "image/*"

        val uri = Uri.fromFile(context.getFileStreamPath(path))
        shareIntent.putExtra(Intent.EXTRA_STREAM, uri)
        onImageClickListener.onSharePressed(shareIntent)
    }
}