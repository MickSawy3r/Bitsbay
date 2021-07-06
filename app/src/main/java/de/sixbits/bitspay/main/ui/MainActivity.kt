package de.sixbits.bitspay.main.ui

import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.commit
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import de.sixbits.bitspay.R
import de.sixbits.bitspay.databinding.ActivityMainBinding
import de.sixbits.bitspay.main.ui.fragments.FeedFragment
import de.sixbits.bitspay.main.ui.fragments.TrashFragment
import de.sixbits.bitspay.main.view_model.SharedViewModel
import java.io.File


private const val TAG = "MainActivity"

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var imageUri: Uri
    private lateinit var sharedViewModel: SharedViewModel

    // UI Links
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        sharedViewModel = ViewModelProvider(this).get(SharedViewModel::class.java)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.sharedViewModel = sharedViewModel

        setupListener()

        sharedViewModel.scheduleNotificationIfNotExists()
    }

    private fun setupListener() {
        sharedViewModel.activePageLiveData.observe(this, {
            when (it) {
                SharedViewModel.ActiveFragment.FEED -> supportFragmentManager.commit {
                    replace(binding.mainContainer.id, FeedFragment { viewModelStore })
                }
                SharedViewModel.ActiveFragment.TRASH -> supportFragmentManager.commit {
                    replace(binding.mainContainer.id, TrashFragment())
                }
                else -> {
                    Snackbar.make(binding.root, "Unknown Move Action", Snackbar.LENGTH_SHORT).show()
                }
            }

        })

        binding.bottomNavigation.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.nav_feed -> {
                    sharedViewModel.showFeed()
                    return@setOnItemSelectedListener true
                }
                R.id.nav_trash -> {
                    sharedViewModel.showTrash()
                    return@setOnItemSelectedListener true
                }
                R.id.nav_camera -> {
                    val photoFile = File.createTempFile(
                        "IMG_",
                        ".jpg",
                        baseContext.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
                    )

                    imageUri = FileProvider.getUriForFile(
                        baseContext,
                        "${baseContext.packageName}.provider",
                        photoFile
                    )
                    takePicture.launch(imageUri)
                    return@setOnItemSelectedListener true
                }
                else -> {
                    return@setOnItemSelectedListener false
                }
            }
        }
    }

    private val takePicture =
        registerForActivityResult(ActivityResultContracts.TakePicture()) { success: Boolean ->
            if (success) {
                Log.d(TAG, "takeImageAndSaveIt: ")
                sharedViewModel.saveImage(imageUri)
            }
        }

}