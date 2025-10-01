package com.example.footyxapp

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.credentials.ClearCredentialStateRequest
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import androidx.credentials.exceptions.ClearCredentialException
import androidx.lifecycle.lifecycleScope
import androidx.media3.common.util.Log
import androidx.media3.common.util.UnstableApi
import com.example.footyxapp.LoginActivity
import com.example.footyxapp.databinding.SettingsActivityBinding
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import kotlinx.coroutines.launch

class SettingsActivity : AppCompatActivity() {

    private lateinit var binding: SettingsActivityBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var credentialManager: CredentialManager
    private lateinit var logoutButton: Button

    //Google Sign-In Option & Request
    private val googleIdOption = GetGoogleIdOption.Builder()
        .setServerClientId("538104946492-rnfeh3iac5pl60mioig7va4h2l01osp9.apps.googleusercontent.com")
        .setFilterByAuthorizedAccounts(false)
        .build()

    // Create the credential manager request
    private val credentialRequest = GetCredentialRequest.Builder()
        .addCredentialOption(googleIdOption)
        .build()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = SettingsActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //---------------------------------------------------------------------------------------------------------------------------------------//
        // Initialize Views
        //---------------------------------------------------------------------------------------------------------------------------------------//
        logoutButton = findViewById(R.id.btn_logout)


        // Initiate Firebase Variables
        auth = Firebase.auth
        credentialManager = CredentialManager.create(this)

        // Hide the default action bar since we have our own header
        supportActionBar?.hide()

        // Set up back button click listener
        binding.btnBack.setOnClickListener {
            finish() // Close this activity and return to the previous one
        }

        // Set up navigation to favorites activity
        setupFavoriteTeamNavigation()

        //
        logoutButton.setOnClickListener {
            signOut()
            startActivity(Intent(this, LoginActivity::class.java))
        }
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

    // SignOut
    @androidx.annotation.OptIn(UnstableApi::class)
    private fun signOut() {
        // Firebase sign out
        auth.signOut()
        // When a user signs out, clear the current user credential state from all credential credential providers
        lifecycleScope.launch {
            try {
                val clearRequest = ClearCredentialStateRequest()
                credentialManager.clearCredentialState(clearRequest)
                val sharedPreferences = getSharedPreferences("user_prefs", MODE_PRIVATE)
                sharedPreferences.edit().remove("user_uid").apply()
                Toast.makeText(this@SettingsActivity, "Logging Out", Toast.LENGTH_SHORT).show()
            } catch (e: ClearCredentialException) {
                Toast.makeText(this@SettingsActivity, "UPUI Error", Toast.LENGTH_SHORT).show()
                Log.e(TAG, "Couldn't clear user credentials: ${e.localizedMessage}")
            }

        }
    }
}