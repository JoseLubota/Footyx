package com.example.footyxapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.footyxapp.databinding.ActivityFavoritesBinding

class FavoritesActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFavoritesBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        binding = ActivityFavoritesBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        // Hide the default action bar since we have our own header
        supportActionBar?.hide()
        
        // Set up back button click listener
        binding.btnBack.setOnClickListener {
            finish() // Close this activity and return to the previous one (Settings)
        }
        
        // TODO: Add click listeners for add team/player buttons and implement RecyclerViews
        // This is just the basic navigation setup - no favorites logic implemented yet
    }
}
