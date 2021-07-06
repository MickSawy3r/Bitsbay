package de.sixbits.bitspay.main.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import de.sixbits.bitspay.R
import de.sixbits.bitspay.databinding.FragmentTrashBinding
import de.sixbits.bitspay.main.adapters.SearchResultRecyclerAdapter
import de.sixbits.bitspay.main.callbacks.OnImageClickListener
import de.sixbits.bitspay.main.view_model.TrashViewModel
import de.sixbits.bitspay.network.model.ImageListItemModel
import org.jetbrains.annotations.TestOnly

@AndroidEntryPoint
class TrashFragment : Fragment(), OnImageClickListener {
    private lateinit var uiBinding: FragmentTrashBinding
    private lateinit var trashViewModel: TrashViewModel

    private lateinit var searchRecyclerAdapter: SearchResultRecyclerAdapter

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
            searchRecyclerAdapter.switchItems(it)
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
            this,
            true
        )

        // Attach the adapter
        uiBinding.rvTrashList.adapter = searchRecyclerAdapter
    }

    override fun onClick(image: ImageListItemModel) {
        MaterialAlertDialogBuilder(requireContext())
            .setMessage("Are you sure you want to delete this entry?")
            .setPositiveButton("Yes") { _, _ ->
                trashViewModel.deleteItem(image)
            }
            .setNegativeButton("No") { _, _ ->
                // Respond to negative button press
            }
            .show()
    }

    override fun startDragging(view: RecyclerView.ViewHolder) {
    }

    override fun onSharePressed(image: ImageListItemModel) {
    }

    override fun onDelete(image: ImageListItemModel) {
        trashViewModel.deleteItem(image)
    }

    @TestOnly
    fun setTestViewModel(testViewModel: TrashViewModel) {
        trashViewModel = testViewModel
    }
}