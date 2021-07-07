package de.sixbits.bitspay.main.adapters

import android.annotation.SuppressLint
import android.graphics.drawable.Drawable
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestBuilder
import com.bumptech.glide.load.engine.DiskCacheStrategy
import de.sixbits.bitspay.databinding.TrashListItemBinding
import de.sixbits.bitspay.main.callbacks.OnTrashClickListener
import de.sixbits.bitspay.network.model.ImageListItemModel
import kotlin.math.abs

private const val TAG = "TrashRecyclerAdapter"

class TrashRecyclerAdapter constructor(
    private var searchResult: List<ImageListItemModel>,
    private val requestBuilder: RequestBuilder<Drawable>,
    private val onTrashClickListener: OnTrashClickListener,
) : RecyclerView.Adapter<TrashRecyclerAdapter.TrashRecyclerViewHolder>() {

    var swipeStartX: Float = -1f

    class TrashRecyclerViewHolder(val binding: TrashListItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): TrashRecyclerViewHolder = TrashRecyclerViewHolder(
        TrashListItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
    )

    @SuppressLint("ClickableViewAccessibility")
    override fun onBindViewHolder(holder: TrashRecyclerViewHolder, position: Int) {
        requestBuilder
            .load(searchResult[position].thumbnail)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .into(holder.binding.ivTrash)

        holder.binding.cardImage.setOnTouchListener { _, event ->
            Log.d(TAG, "onCreateViewHolder: Touch ${event.action}")
            if (event.action == MotionEvent.ACTION_DOWN) {
                swipeStartX = event.x
                onTrashClickListener.startSwipe(holder)
            }
            if (event.action == MotionEvent.ACTION_CANCEL) {
                if (swipeStartX < 0) {
                    return@setOnTouchListener false
                }

                Log.d(TAG, "onBindViewHolder: dx ${abs(event.x - swipeStartX)}")

                val absX = abs(event.x)
                val absLastX = abs(swipeStartX)
                swipeStartX = -1f

                if (abs(absX - absLastX) > 36) {
                    onTrashClickListener.endSwipe(searchResult[position])
                }
            }
            return@setOnTouchListener true
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

    fun deleteAt(index: Int) {
        Log.d(TAG, "deleteAt: Deleting in Adapter")
        onTrashClickListener.onDelete(searchResult[index])
    }
}
