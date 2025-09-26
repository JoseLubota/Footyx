package com.example.footyxapp

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.footyxapp.databinding.SettingsActivityBinding

class SettingsActivity : AppCompatActivity() {

    private lateinit var binding: SettingsActivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        binding = SettingsActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        // Hide the default action bar since we have our own header
        supportActionBar?.hide()
        
        // Set up back button click listener
        binding.btnBack.setOnClickListener {
            finish() // Close this activity and return to the previous one
        }
        
        // Set up navigation to favorites activity
        setupFavoriteTeamNavigation()
        
        // TODO: Add click listeners for other UI elements
        // This is just the basic setup - no logic implemented yet
    }
    
    private fun setupFavoriteTeamNavigation() {
        // Find the favorite team LinearLayout and set click listener
        val favoriteTeamLayout = binding.root.findViewById<android.widget.LinearLayout>(
            R.id.layout_favorite_team
        )
        
        favoriteTeamLayout?.setOnClickListener {
            // Navigate to FavoritesActivity
            val intent = Intent(this, FavoritesActivity::class.java)
            startActivity(intent)
        }
    }
}