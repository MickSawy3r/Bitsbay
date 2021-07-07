package de.sixbits.bitspay.main.adapters

import android.graphics.drawable.Drawable
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestBuilder
import com.bumptech.glide.load.engine.DiskCacheStrategy
import de.sixbits.bitspay.databinding.RowImagesListBinding
import de.sixbits.bitspay.main.callbacks.OnFeedClickListener
import de.sixbits.bitspay.network.model.ImageListItemModel


private const val TAG = "SearchResultRecyclerAda"

class FeedRecyclerAdapter constructor(
    private var searchResult: List<ImageListItemModel>,
    private val requestBuilder: RequestBuilder<Drawable>,
    private val onFeedClickListener: OnFeedClickListener
) : RecyclerView.Adapter<FeedRecyclerAdapter.SearchResultRecyclerViewHolder>() {

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
            onFeedClickListener.startDragging(vh)
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
            onFeedClickListener.onClick(searchResult[position])
        }

        holder.binding.ivDelete.setOnClickListener {
            onFeedClickListener.onSharePressed(image = searchResult[position])
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
}