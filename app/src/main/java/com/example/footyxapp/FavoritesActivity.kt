package com.example.footyxapp

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.footyxapp.data.manager.FavoritesManager
import com.example.footyxapp.data.model.PlayerData
import com.example.footyxapp.databinding.ActivityFavoritesBinding
import com.example.footyxapp.ui.favorites.dialog.TeamSearchDialogFragment
import com.example.footyxapp.ui.favorites.dialog.PlayerSearchDialogFragment

class FavoritesActivity : AppCompatActivity() {

    //°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°//

    private lateinit var binding: ActivityFavoritesBinding
    private lateinit var favoritesManager: FavoritesManager

    //°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°//

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        binding = ActivityFavoritesBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        // Hide the default action bar since we have our own header
        supportActionBar?.hide()
        
        // Initialize favorites manager
        favoritesManager = FavoritesManager.getInstance(this)
        
        // Set up listeners
        setupClickListeners()
        
        // Load favorite team
        loadFavoriteTeam()
    }

    //°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°//

    override fun onResume() {
        super.onResume()
        // Refresh favorites when returning to this activity
        loadFavoriteTeam()
        loadFavoritePlayer()
    }

    //°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°//

    private fun setupClickListeners() {
        binding.btnBack.setOnClickListener {
            finish()
        }
        
        binding.btnAddTeam.setOnClickListener {
            showTeamSearchDialog()
        }
        
        binding.btnAddPlayer.setOnClickListener {
            showPlayerSearchDialog()
        }
    }

    //°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°//

    private fun loadFavoriteTeam() {
        val favorite = favoritesManager.getFavoriteTeam()
        
        if (favorite != null) {
            // Show favorite team card
            binding.recyclerViewFavoriteTeams.visibility = View.VISIBLE
            binding.tvNoTeams.visibility = View.GONE
            
            // Display team info
            binding.txtFavoriteTeamName.text = favorite.teamData.team.name
            binding.txtFavoriteTeamCountry.text = favorite.teamData.team.country
            binding.txtFavoriteLeague.text = "${favorite.leagueName} (${favorite.season})"
            
            Glide.with(this)
                .load(favorite.teamData.team.logo)
                .placeholder(R.drawable.ic_team_placeholder)
                .error(R.drawable.ic_team_placeholder)
                .into(binding.imgFavoriteTeamLogo)
            
            // Show change button instead of add button
            binding.btnAddTeam.text = "Change Team"
            
            // Set up remove button
            binding.btnRemoveFavoriteTeam.setOnClickListener {
                showRemoveTeamDialog()
            }
        } else {
            // Show empty state
            binding.recyclerViewFavoriteTeams.visibility = View.GONE
            binding.tvNoTeams.visibility = View.VISIBLE
            binding.btnAddTeam.text = getString(R.string.favorites_add_team)
        }
    }

    //°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°//

    private fun loadFavoritePlayer() {
        val favorite = favoritesManager.getFavoritePlayer()
        
        if (favorite != null) {
            // Show favorite player card
            binding.layoutFavoritePlayer.visibility = View.VISIBLE
            binding.tvNoPlayers.visibility = View.GONE
            
            // Display player info
            val player = favorite.playerData.player
            binding.txtFavoritePlayerName.text = player.name
            binding.txtFavoritePlayerNationality.text = player.nationality
            binding.txtFavoritePlayerSeason.text = "Season ${favorite.season}"
            
            Glide.with(this)
                .load(player.photo)
                .placeholder(R.drawable.player_w)
                .error(R.drawable.player_w)
                .into(binding.imgFavoritePlayerPhoto)
            
            // Show change button instead of add button
            binding.btnAddPlayer.text = "Change Player"
            
            // Set up remove button
            binding.btnRemoveFavoritePlayer.setOnClickListener {
                showRemovePlayerDialog()
            }
        } else {
            // Show empty state
            binding.layoutFavoritePlayer.visibility = View.GONE
            binding.tvNoPlayers.visibility = View.VISIBLE
            binding.btnAddPlayer.text = getString(R.string.favorites_add_player)
        }
    }

    //°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°//

    private fun showPlayerSearchDialog() {
        val dialog = PlayerSearchDialogFragment.newInstance { playerData, season ->
            saveFavoritePlayer(playerData, season)
        }
        dialog.show(supportFragmentManager, PlayerSearchDialogFragment.TAG)
    }

    //°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°//

    private fun saveFavoritePlayer(playerData: PlayerData, season: Int) {
        favoritesManager.saveFavoritePlayer(playerData, season)
        
        Toast.makeText(
            this,
            "${playerData.player.name} set as your favorite player!",
            Toast.LENGTH_SHORT
        ).show()
        
        loadFavoritePlayer()
    }

    //°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°//

    private fun showRemovePlayerDialog() {
        val favorite = favoritesManager.getFavoritePlayer() ?: return
        
        AlertDialog.Builder(this)
            .setTitle("Remove Favorite Player")
            .setMessage("Are you sure you want to remove ${favorite.playerData.player.name}?")
            .setPositiveButton("Remove") { _, _ ->
                removeFavoritePlayer()
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    //°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°//

    private fun removeFavoritePlayer() {
        favoritesManager.clearFavoritePlayer()
        Toast.makeText(
            this,
            "Favorite player removed",
            Toast.LENGTH_SHORT
        ).show()
        loadFavoritePlayer()
    }

    //°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°//

    private fun showTeamSearchDialog() {
        val dialog = TeamSearchDialogFragment.newInstance { teamData, leagueData, season ->
            saveFavoriteTeam(teamData, leagueData, season)
        }
        dialog.show(supportFragmentManager, TeamSearchDialogFragment.TAG)
    }

    //°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°//

    private fun saveFavoriteTeam(
        teamData: com.example.footyxapp.data.model.TeamData,
        leagueData: com.example.footyxapp.data.model.TeamLeagueData,
        season: Int
    ) {
        favoritesManager.saveFavoriteTeam(
            teamData,
            leagueData.league.id,
            leagueData.league.name,
            season
        )
        
        Toast.makeText(
            this,
            "${teamData.team.name} set as your favorite team!",
            Toast.LENGTH_SHORT
        ).show()
        
        loadFavoriteTeam()
    }

    //°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°//

    private fun showRemoveTeamDialog() {
        val favorite = favoritesManager.getFavoriteTeam() ?: return
        
        AlertDialog.Builder(this)
            .setTitle("Remove Favorite Team")
            .setMessage("Are you sure you want to remove ${favorite.teamData.team.name}?")
            .setPositiveButton("Remove") { _, _ ->
                removeFavoriteTeam()
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    //°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°//

    private fun removeFavoriteTeam() {
        favoritesManager.clearFavoriteTeam()
        Toast.makeText(
            this,
            "Favorite team removed",
            Toast.LENGTH_SHORT
        ).show()
        loadFavoriteTeam()
    }

    //°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°//

}
