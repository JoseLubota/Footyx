package com.example.footyxapp

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
        
        // TODO: Add click listeners for other UI elements
        // This is just the basic setup - no logic implemented yet
    }
}