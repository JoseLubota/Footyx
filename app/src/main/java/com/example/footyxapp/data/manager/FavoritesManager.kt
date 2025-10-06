package com.example.footyxapp.data.manager

import android.content.Context
import android.content.SharedPreferences
import com.example.footyxapp.data.model.TeamData
import com.google.gson.Gson

data class FavoriteTeamWithLeague(
    val teamData: TeamData,
    val leagueId: Int,
    val leagueName: String,
    val season: Int
)

class FavoritesManager private constructor(context: Context) {
    
    private val sharedPreferences: SharedPreferences = 
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    private val gson = Gson()
    
    companion object {
        private const val PREFS_NAME = "FootyXFavorites"
        private const val KEY_FAVORITE_TEAM = "favorite_team"
        
        @Volatile
        private var instance: FavoritesManager? = null
        
        fun getInstance(context: Context): FavoritesManager {
            return instance ?: synchronized(this) {
                instance ?: FavoritesManager(context.applicationContext).also { instance = it }
            }
        }
    }
    
    // Save favorite team with league
    fun saveFavoriteTeam(teamData: TeamData, leagueId: Int, leagueName: String, season: Int) {
        val favorite = FavoriteTeamWithLeague(teamData, leagueId, leagueName, season)
        val json = gson.toJson(favorite)
        sharedPreferences.edit().putString(KEY_FAVORITE_TEAM, json).apply()
    }
    
    // Get favorite team
    fun getFavoriteTeam(): FavoriteTeamWithLeague? {
        val json = sharedPreferences.getString(KEY_FAVORITE_TEAM, null) ?: return null
        return try {
            gson.fromJson(json, FavoriteTeamWithLeague::class.java)
        } catch (e: Exception) {
            null
        }
    }
    
    // Check if a favorite team exists
    fun hasFavoriteTeam(): Boolean {
        return getFavoriteTeam() != null
    }
    
    // Clear favorite team
    fun clearFavoriteTeam() {
        sharedPreferences.edit().remove(KEY_FAVORITE_TEAM).apply()
    }
}
