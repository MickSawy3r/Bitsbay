package de.sixbits.bitspay.main.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
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
import de.sixbits.bitspay.main.view_model.FeedViewModel
import de.sixbits.bitspay.network.model.ImageListItemModel
import org.jetbrains.annotations.TestOnly

private const val TAG = "FeedFragment"

@AndroidEntryPoint
class FeedFragment : Fragment(), OnImageClickListener {

    private lateinit var uiBinding: FragmentFeedBinding
    private lateinit var feedViewModel: FeedViewModel

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

        Log.d(TAG, "onCreateView: ")

        feedViewModel = ViewModelProvider(this).get(FeedViewModel::class.java)

        feedViewModel.init()

        initListeners()

        initViews()
        initRecyclerView()

        return uiBinding.root
    }

    private fun initListeners() {
        // Handle data response
        feedViewModel.loadingLiveData.observe(viewLifecycleOwner, {
            uiBinding.refreshLayout.isRefreshing = it
        })

        feedViewModel.searchImagesLiveData.observe(viewLifecycleOwner, {
            searchRecyclerAdapter.switchItems(it)
        })

        feedViewModel.errorLiveData.observe(viewLifecycleOwner, {
            Snackbar.make(uiBinding.root, it, Snackbar.LENGTH_SHORT).show()
        })
    }

    private fun initViews() {
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
        val activeLayoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)

        uiBinding.rvSearchResult.layoutManager = activeLayoutManager

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
        searchRecyclerAdapter.stateRestorationPolicy =
            RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY
    }

    @TestOnly
    fun setTestViewModel(testViewModel: FeedViewModel) {
        feedViewModel = testViewModel
    }

    override fun onClick(image: ImageListItemModel) {

        MaterialAlertDialogBuilder(requireContext())
            .setMessage("Are you sure you want to delete this entry?")
            .setPositiveButton("Yes") { _, _ ->

            }
            .setNegativeButton("No") { _, _ ->
                // Respond to positive button press
            }
            .show()
    }
}