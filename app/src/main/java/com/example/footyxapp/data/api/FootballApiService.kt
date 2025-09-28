package com.example.footyxapp.data.api

import com.example.footyxapp.data.model.PlayerResponse
import com.example.footyxapp.data.model.PlayerSearchResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface FootballApiService {
    
    @GET("players")
    suspend fun getPlayer(
        @Header("X-RapidAPI-Key") apiKey: String,
        @Header("X-RapidAPI-Host") host: String = "v3.football.api-sports.io",
        @Query("id") playerId: Int,
        @Query("season") season: Int
    ): Response<PlayerResponse>
    
    @GET("players/profiles")
    suspend fun searchPlayers(
        @Header("X-RapidAPI-Key") apiKey: String,
        @Header("X-RapidAPI-Host") host: String = "v3.football.api-sports.io",
        @Query("search") searchQuery: String
    ): Response<PlayerSearchResponse>
}
