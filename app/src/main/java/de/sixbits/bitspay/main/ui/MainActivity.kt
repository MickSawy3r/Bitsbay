package de.sixbits.bitspay.main.ui

import android.os.Bundle
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.integration.recyclerview.RecyclerViewPreloader
import com.bumptech.glide.util.ViewPreloadSizeProvider
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import de.sixbits.bitspay.databinding.ActivityMainBinding
import de.sixbits.bitspay.main.adapters.SearchResultRecyclerAdapter
import de.sixbits.bitspay.main.callbacks.OnImageClickListener
import de.sixbits.bitspay.main.view_model.MainViewModel
import de.sixbits.bitspay.network.model.ImageListItemModel
import org.jetbrains.annotations.TestOnly

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), OnImageClickListener {

    private lateinit var mainViewModel: MainViewModel

    // UI Links
    private lateinit var binding: ActivityMainBinding
    private lateinit var searchRecyclerAdapter: SearchResultRecyclerAdapter

    // State Variable
    private var loading = true
    private var canLoadMorePages = true


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mainViewModel = ViewModelProvider(this).get(MainViewModel::class.java)

        initListerners()

        initViews()
        initRecyclerView()
    }

    private fun initListerners() {

        // Handle data response
        mainViewModel.searchImagesLiveData.observe(this, {
            searchRecyclerAdapter.switchItems(it)
        })

        mainViewModel.pagerLiveData.observe(this, {
            if (it.isNotEmpty()) {
                searchRecyclerAdapter.addItemsToCurrent(it)
            } else {
                canLoadMorePages = false
            }
        })

        mainViewModel.errorLiveData.observe(this, {
            Snackbar.make(binding.root, it, Snackbar.LENGTH_SHORT).show()
        })

        // Handle Loading Events
        mainViewModel.loadingLiveData.observe(this, {
            if (it) {
                binding.pbLoadingSearchResult.visibility = VISIBLE
                binding.rvSearchResult.visibility = GONE
            } else {
                binding.pbLoadingSearchResult.visibility = GONE
                binding.rvSearchResult.visibility = VISIBLE
            }
        })
    }

    private fun initViews() {
        binding.etSearchBar.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                binding.pbLoadingSearchResult.visibility = VISIBLE
                binding.rvSearchResult.visibility = GONE
                mainViewModel.searchFor(binding.etSearchBar.query.toString())
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })
        binding.etSearchBar.setQuery("Fruits", true)
    }

    private fun initRecyclerView() {
        val activeLayoutManager = LinearLayoutManager(this)

        binding.rvSearchResult.layoutManager = activeLayoutManager

        // For Preloading images
        val searchRecyclerRequestBuilder = Glide
            .with(this)
            .asDrawable()
        val preloadSizeProvider = ViewPreloadSizeProvider<ImageListItemModel>()

        // initially we have no items
        searchRecyclerAdapter = SearchResultRecyclerAdapter(
            listOf(),
            searchRecyclerRequestBuilder,
            this
        )

        // For Preloading  images
        val preLoader = RecyclerViewPreloader(
            Glide.with(this),
            searchRecyclerAdapter,
            preloadSizeProvider,
            4
        )

        // Attach the adapter
        binding.rvSearchResult.adapter = searchRecyclerAdapter
        searchRecyclerAdapter.stateRestorationPolicy =
            RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY

        // attaching the preLoader
        binding.rvSearchResult.addOnScrollListener(preLoader)

        // Attach Infinite Scroll
        binding.rvSearchResult.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if (dy > 0) { //check for scroll down
                    val visibleItemCount = activeLayoutManager.childCount
                    val totalItemCount = activeLayoutManager.itemCount
                    val pastVisiblesItems = activeLayoutManager.findFirstVisibleItemPosition()

                    // To save network requests
                    if (loading && canLoadMorePages &&
                        (visibleItemCount + pastVisiblesItems) >= totalItemCount
                    ) {
                        mainViewModel.requestMoreImage()
                    }
                }
            }
        })
    }

    override fun onClick(id: Int) {
        MaterialAlertDialogBuilder(this)
            .setMessage("Are you sure you want to view this entry?")
            .setPositiveButton("Yes") { _, _ ->

            }
            .setNegativeButton("No") { _, _ ->
                // Respond to positive button press
            }
            .show()
    }

    @TestOnly
    fun setTestViewModel(testViewModel: MainViewModel) {
        mainViewModel = testViewModel
    }
}