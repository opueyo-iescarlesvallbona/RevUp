package com.example.practicamapes_oscarpueyocasas.API

import com.example.revup._DATACLASS.Municipality
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface MunicipalitiesAPIService {
    @GET("/municipis/")
    suspend fun getMunicipalitiesByName(
        @Query("nompoblacio") nompoblacio: String
    ): Response<List<Municipality>>

    @GET("/municipis/")
    suspend fun getMunicipalitiesByCord(
        @Query("latitud") latitud: Double,
        @Query("longitud") longitud: Double
    ): Response<List<Municipality>>

    @GET("/municipis/")
    suspend fun getAllMunicipalities(
    ): Response<List<Municipality>>
}