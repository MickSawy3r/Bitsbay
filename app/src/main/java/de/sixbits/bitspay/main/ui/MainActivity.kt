package de.sixbits.bitspay.main.ui

import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.AdapterView
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.commit
import androidx.lifecycle.ViewModelProvider
import dagger.hilt.android.AndroidEntryPoint
import de.sixbits.bitspay.R
import de.sixbits.bitspay.databinding.ActivityMainBinding
import de.sixbits.bitspay.main.ui.fragments.FeedFragment
import de.sixbits.bitspay.main.view_model.SharedViewModel

private const val TAG = "MainActivity"

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var sharedViewModel: SharedViewModel

    // UI Links
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        sharedViewModel = ViewModelProvider(this).get(SharedViewModel::class.java)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.sharedViewModel = sharedViewModel
        binding.bottomNavigation.setOnItemSelectedListener{
            when(it.itemId) {
                R.id.nav_feed -> {
                    sharedViewModel.showFeed()
                    return@setOnItemSelectedListener true
                }
                R.id.nav_trash -> {
                    sharedViewModel.showTrash()
                    return@setOnItemSelectedListener true
                }
                else -> {
                    return@setOnItemSelectedListener false
                }
            }
        }

        sharedViewModel.activePageLiveData.observe(this, {
            supportFragmentManager.commit {
                replace(binding.mainContainer.id, it)
            }
        })
    }
}