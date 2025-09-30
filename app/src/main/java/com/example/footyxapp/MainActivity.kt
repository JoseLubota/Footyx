package com.example.footyxapp

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.inputmethod.EditorInfo
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.footyxapp.databinding.ActivityMainBinding
import com.example.footyxapp.ui.common.SearchableFragment

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    
    // Debouncing for search
    private val searchHandler = Handler(Looper.getMainLooper())
    private var searchRunnable: Runnable? = null
    private val SEARCH_DELAY_MS = 800L // Wait 800ms after user stops typing

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment_activity_main) as NavHostFragment

        navController = navHostFragment.navController

        binding.navView.setupWithNavController(navController)
        
        // Set up click listeners for toolbar buttons
        setupToolbarButtons()
        
        // Set up global search functionality
        setupGlobalSearch()
    }
    
    private fun setupToolbarButtons() {
        // Leaderboard button click listener
        binding.buttonLeaderboard.setOnClickListener {
            val intent = Intent(this, LeaderboardActivity::class.java)
            startActivity(intent)
        }
        
        // Settings button click listener
        binding.buttonSettings.setOnClickListener {
            val intent = Intent(this, SettingsActivity::class.java)
            startActivity(intent)
        }
    }
    
    private fun setupGlobalSearch() {
        binding.searchEditText.addTextChangedListener { text ->
            val query = text.toString().trim()
            
            // Cancel previous search if user is still typing
            searchRunnable?.let { searchHandler.removeCallbacks(it) }
            
            // Schedule new search with delay
            searchRunnable = Runnable {
                handleGlobalSearch(query)
            }
            
            if (query.isNotEmpty() && query.length >= 2) {
                searchHandler.postDelayed(searchRunnable!!, SEARCH_DELAY_MS)
            } else {
                // Clear results immediately if query is too short
                handleGlobalSearch("")
            }
        }
        
        binding.searchEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                val query = binding.searchEditText.text.toString().trim()
                // Cancel any pending search and execute immediately
                searchRunnable?.let { searchHandler.removeCallbacks(it) }
                handleGlobalSearch(query)
                true
            } else {
                false
            }
        }
    }
    
    private fun handleGlobalSearch(query: String) {
        // Get the current fragment
        val currentFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment_activity_main)
            ?.childFragmentManager?.fragments?.firstOrNull()
        
        // If the current fragment implements SearchableFragment, delegate search to it
        if (currentFragment is SearchableFragment) {
            if (query.isNotEmpty()) {
                currentFragment.onSearch(query)
            } else {
                currentFragment.clearSearchResults()
            }
        } else {
        }
    }
}