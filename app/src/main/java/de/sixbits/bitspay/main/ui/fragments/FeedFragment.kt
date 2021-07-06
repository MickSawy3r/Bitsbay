package de.sixbits.bitspay.main.ui.fragments

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.bumptech.glide.Glide
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import de.sixbits.bitspay.R
import de.sixbits.bitspay.databinding.FragmentFeedBinding
import de.sixbits.bitspay.main.adapters.SearchResultRecyclerAdapter
import de.sixbits.bitspay.main.callbacks.OnImageClickListener
import de.sixbits.bitspay.main.ui.helpers.DragHelper
import de.sixbits.bitspay.main.ui.helpers.DragHelper.itemTouchHelper
import de.sixbits.bitspay.main.view_model.FeedViewModel
import de.sixbits.bitspay.main.view_model.SharedViewModel
import de.sixbits.bitspay.network.model.ImageListItemModel
import org.jetbrains.annotations.TestOnly

private const val TAG = "FeedFragment"

@AndroidEntryPoint
class FeedFragment @JvmOverloads constructor(
    private val parentStoreOwner: ViewModelStoreOwner? = null
) : Fragment(), OnImageClickListener {

    private lateinit var uiBinding: FragmentFeedBinding
    private lateinit var feedViewModel: FeedViewModel
    private lateinit var sharedViewModel: SharedViewModel

    private lateinit var searchRecyclerAdapter: SearchResultRecyclerAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        uiBinding = DataBindingUtil.inflate(
            layoutInflater,
            R.layout.fragment_feed,
            container,
            false
        )

        feedViewModel = ViewModelProvider(this).get(FeedViewModel::class.java)
        if (parentStoreOwner != null) {
            sharedViewModel = ViewModelProvider(parentStoreOwner).get(SharedViewModel::class.java)
        }

        feedViewModel.init()

        initListeners()
        initViews()
        initRecyclerView()

        return uiBinding.root
    }

    private fun initListeners() {
        if (this::sharedViewModel.isInitialized) {
            sharedViewModel.changeLiveData.observe(viewLifecycleOwner, {
                feedViewModel.getAll()
            })
        }

        // Handle data response
        feedViewModel.loadingLiveData.observe(viewLifecycleOwner, {
            uiBinding.refreshLayout.isRefreshing = it
        })

        feedViewModel.searchImagesLiveData.observe(viewLifecycleOwner, {
            if (it.isEmpty()) {
                uiBinding.rvSearchResult.visibility = View.INVISIBLE
                uiBinding.lottie.visibility = View.VISIBLE
            } else {
                searchRecyclerAdapter.switchItems(it)
            }
        })

        feedViewModel.errorLiveData.observe(viewLifecycleOwner, {
            Snackbar.make(uiBinding.root, it, Snackbar.LENGTH_SHORT).show()
        })

        feedViewModel.gridModeLiveData.observe(viewLifecycleOwner, {
            Log.d(TAG, "gridViewMode: $it")
            if (it == FeedViewModel.GridMode.MASONRY) {
                uiBinding.rvSearchResult.layoutManager =
                    StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
            } else {
                uiBinding.rvSearchResult.layoutManager = GridLayoutManager(context, 2)
            }
        })
    }

    private fun initViews() {
        uiBinding.fabFeed.setOnClickListener {
            feedViewModel.switchViewMode()
        }
        uiBinding.etSearchBar.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                uiBinding.rvSearchResult.visibility = View.GONE
                feedViewModel.searchFor(uiBinding.etSearchBar.query.toString())
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })
    }

    private fun initRecyclerView() {
        // For Preloading images
        val searchRecyclerRequestBuilder = Glide
            .with(this)
            .asDrawable()

        // initially we have no items
        searchRecyclerAdapter = SearchResultRecyclerAdapter(
            listOf(),
            searchRecyclerRequestBuilder,
            this
        )

        // Attach the adapter
        uiBinding.rvSearchResult.adapter = searchRecyclerAdapter
        uiBinding.refreshLayout.isEnabled = false

        itemTouchHelper.attachToRecyclerView(uiBinding.rvSearchResult)
    }

    override fun onClick(image: ImageListItemModel) {
        MaterialAlertDialogBuilder(requireContext())
            .setMessage("Are you sure you want to delete this entry?")
            .setPositiveButton("Yes") { _, _ ->
                feedViewModel.trashImage(image)
            }
            .setNegativeButton("No") { _, _ ->
                // Respond to positive button press
            }
            .show()
    }

    override fun startDragging(view: RecyclerView.ViewHolder) {
        itemTouchHelper.startDrag(view)
    }

    override fun onSharePressed(image: ImageListItemModel) {
        val shareIntent = Intent(Intent.ACTION_SEND)
        shareIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_DOCUMENT)

        shareIntent.type = "image/*"

        val uri = Uri.fromFile(requireContext().getFileStreamPath(image.thumbnail))
        shareIntent.putExtra(Intent.EXTRA_STREAM, uri)

        activity?.startActivity(shareIntent)
    }

    override fun onDelete(image: ImageListItemModel) {
    }

    @TestOnly
    fun setTestViewModel(testViewModel: FeedViewModel) {
        feedViewModel = testViewModel
    }
}