package com.example.practicamapes_oscarpueyocasas.API

import com.example.revup._DATACLASS.Municipality
import com.google.gson.GsonBuilder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import kotlin.coroutines.CoroutineContext

class MunicipalitiesCrudAPI : CoroutineScope {
    private var job = Job()

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    var urlApi = "http://172.16.24.184/"

    private fun getClient(): OkHttpClient {
        var loggin = HttpLoggingInterceptor()
        loggin.setLevel(HttpLoggingInterceptor.Level.BODY)

        return OkHttpClient.Builder().addInterceptor(loggin).build()
    }

    private fun getRetrofit(): Retrofit {
        val gson = GsonBuilder().setLenient().create()

        return Retrofit.Builder().baseUrl(urlApi).client(getClient())
            .addConverterFactory(GsonConverterFactory.create(gson)).build()
    }

    fun getMunicipisByName(nom: String): List<Municipality>?{
        var resposta : Response<List<Municipality>>? = null
        runBlocking {
            val cor = launch {
                resposta = getRetrofit().create(MunicipalitiesAPIService::class.java).getMunicipalitiesByName(nom)
            }
            cor.join()
        }
        if (resposta!!.isSuccessful)
            return resposta!!.body()
        else
            return null
    }

    fun getMunicipisByCord(latitud: Double, longitud: Double): List<Municipality>?{
        var resposta : Response<List<Municipality>>? = null
        runBlocking {
            val cor = launch {
                resposta = getRetrofit().create(MunicipalitiesAPIService::class.java).getMunicipalitiesByCord(latitud, longitud)
            }
            cor.join()
        }
        if (resposta!!.isSuccessful)
            return resposta!!.body()
        else
            return null
    }

    fun getAllMunicipalities(): List<Municipality>?{
        var resposta : Response<List<Municipality>>? = null
        runBlocking {
            val cor = launch {
                resposta = getRetrofit().create(MunicipalitiesAPIService::class.java).getAllMunicipalities()
            }
            cor.join()
        }
        if (resposta!!.isSuccessful)
            return resposta!!.body()
        else
            return null
    }

}