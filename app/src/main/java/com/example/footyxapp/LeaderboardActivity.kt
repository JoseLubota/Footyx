package com.example.footyxapp

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.footyxapp.databinding.ActivityLeaderboardBinding

class LeaderboardActivity : AppCompatActivity() {
    
    private lateinit var binding: ActivityLeaderboardBinding
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        
        binding = ActivityLeaderboardBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        // Hide the default action bar since we have our own header
        supportActionBar?.hide()
        
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        
        // Set up back button click listener
        binding.btnBack.setOnClickListener {
            finish() // Close this activity and return to the previous one
        }
    }
}