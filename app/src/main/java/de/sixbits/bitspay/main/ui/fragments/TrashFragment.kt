package de.sixbits.bitspay.main.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import de.sixbits.bitspay.R
import de.sixbits.bitspay.databinding.FragmentTrashBinding
import de.sixbits.bitspay.main.adapters.TrashRecyclerAdapter
import de.sixbits.bitspay.main.callbacks.OnTrashClickListener
import de.sixbits.bitspay.main.view_model.TrashViewModel
import de.sixbits.bitspay.network.model.ImageListItemModel
import org.jetbrains.annotations.TestOnly

private const val TAG = "TrashFragment"

@AndroidEntryPoint
class TrashFragment : Fragment(), OnTrashClickListener {
    private lateinit var uiBinding: FragmentTrashBinding
    private lateinit var trashViewModel: TrashViewModel
    private lateinit var trashRecyclerAdapter: TrashRecyclerAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        uiBinding = DataBindingUtil.inflate(
            layoutInflater,
            R.layout.fragment_trash,
            container,
            false
        )

        trashViewModel = ViewModelProvider(this).get(TrashViewModel::class.java)

        initListeners()
        initRecyclerView()

        trashViewModel.getItems()

        return uiBinding.root
    }

    private fun initListeners() {
        trashViewModel.errorLiveData.observe(viewLifecycleOwner, {
            Snackbar.make(uiBinding.root, it, Snackbar.LENGTH_SHORT).show()
        })
        trashViewModel.imagesLiveData.observe(viewLifecycleOwner, {
            if (it.isEmpty()) {
                uiBinding.rvTrashList.visibility = View.GONE
                uiBinding.lottie.visibility = View.VISIBLE
            } else {
                uiBinding.lottie.visibility = View.GONE
                uiBinding.rvTrashList.visibility = View.VISIBLE
                trashRecyclerAdapter.switchItems(it)
            }
        })
    }

    private fun initRecyclerView() {
        // For Preloading images
        val searchRecyclerRequestBuilder = Glide
            .with(this)
            .asDrawable()

        // initially we have no items
        trashRecyclerAdapter = TrashRecyclerAdapter(
            listOf(),
            searchRecyclerRequestBuilder,
            this,
        )

        // Attach the adapter
        uiBinding.rvTrashList.adapter = trashRecyclerAdapter

        trashTouchHelper.attachToRecyclerView(uiBinding.rvTrashList)
    }

    override fun onDelete(image: ImageListItemModel) {
        Log.d(TAG, "onDelete: ")
        trashViewModel.deleteItem(image)
    }

    override fun startSwipe(view: RecyclerView.ViewHolder) {
        Log.d(TAG, "startSwipe: ")
        trashTouchHelper.startDrag(view)
    }

    override fun endSwipe(image: ImageListItemModel) {
        trashViewModel.deleteItem(image)
    }

    @TestOnly
    fun setTestViewModel(testViewModel: TrashViewModel) {
        trashViewModel = testViewModel
    }

    val trashTouchHelper by lazy {
        val simpleSwapItemTouchCallback =
            object : ItemTouchHelper.SimpleCallback(
                ItemTouchHelper.START or
                        ItemTouchHelper.END, 0
            ) {
                override fun onMove(
                    recyclerView: RecyclerView,
                    viewHolder: RecyclerView.ViewHolder,
                    target: RecyclerView.ViewHolder
                ): Boolean {
                    val adapter = recyclerView.adapter as TrashRecyclerAdapter
                    val index = target.bindingAdapterPosition
                    Log.d(TAG, "onMove: Trash Item")
                    adapter.deleteAt(index)
                    adapter.notifyDataSetChanged()
                    return true
                }

                override fun onSwiped(
                    viewHolder: RecyclerView.ViewHolder,
                    direction: Int
                ) {
                    Log.d(TAG, "onSwiped: Trash Item")
                    val adapter = viewHolder.bindingAdapter as TrashRecyclerAdapter
                    adapter.deleteAt(viewHolder.bindingAdapterPosition)
                }
            }

        ItemTouchHelper(simpleSwapItemTouchCallback)
    }
}