package com.example.revup._API

import android.content.Context
import android.util.Log
import com.example.revup._DATACLASS.*
import com.google.gson.GsonBuilder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.w3c.dom.Comment
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import kotlin.coroutines.CoroutineContext

class RevupCrudAPI : CoroutineScope {
    //region Retrofit
    private var job = Job()

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    var urlApi = "http://172.16.24.136:5178/"

    private fun getClient(context: Context): OkHttpClient {
        var loggin = HttpLoggingInterceptor()
        loggin.setLevel(HttpLoggingInterceptor.Level.BODY)

        return OkHttpClient.Builder().addInterceptor(loggin).addInterceptor(AuthInterceptor(context)).build()

        val client = OkHttpClient.Builder()
            .addInterceptor(AuthInterceptor(context))
            .build()
    }

    private fun getRetrofit(context: Context): Retrofit {
        val gson = GsonBuilder().setLenient().create()

        return Retrofit.Builder().baseUrl(urlApi).client(getClient(context))
            .addConverterFactory(GsonConverterFactory.create(gson)).build()
    }
    //endregion

    //region POSTS
    fun getPostsByLocation(memberLocation: MemberLocation, context: Context): MutableList<Post>?{
        var resposta : Response<MutableList<Post>>? = null
        runBlocking {
            val cor = launch {
                resposta = getRetrofit(context).create(RevupAPIService::class.java).getPostsByLocation(memberLocation)
            }
            cor.join()
        }
        if (resposta!!.isSuccessful)
            return resposta!!.body()
        else
            return null
    }

    fun getPostsByLikes(context: Context): MutableList<Post>?{
        var resposta : Response<MutableList<Post>>? = null
        runBlocking {
            val cor = launch {
                resposta = getRetrofit(context).create(RevupAPIService::class.java).getPostsByLikes()
            }
            cor.join()
        }
        if (resposta!!.isSuccessful)
            return resposta!!.body()
        else
            return null
    }

    fun postPost(post: Post, context: Context): Boolean{
        var afegit: Boolean = false
        runBlocking {
            var resposta : Response<Post>? = null
            val cor= launch {
                resposta = getRetrofit(context).create(RevupAPIService::class.java).postPost(null, post)
            }
            cor.join()
            if (resposta!!.isSuccessful)
                afegit = true
            else
                afegit = false
        }
        return afegit
    }

    fun putPost(post: Post, context: Context): Boolean{
        var modificat: Boolean = false
        runBlocking {
            var resposta : Response<Post>? = null
            val cor = launch {
                resposta = getRetrofit(context).create(RevupAPIService::class.java).putPost(null, post)
            }
            cor.join()
            if (resposta!!.isSuccessful)
                modificat = true
            else
                modificat = false
        }
        return modificat
    }

    fun deletePost(postId: Int, context: Context): Boolean{
        var esborrat: Boolean = false
        runBlocking {
            var resposta: Response<Boolean>? = null
            val cor = launch {
                resposta = getRetrofit(context).create(RevupAPIService::class.java).deletePost(postId)
            }
            cor.join()
            if (resposta!!.isSuccessful)
                esborrat = true
            else
                esborrat = false
        }
        return esborrat
    }

    fun postLike(memberId: Int, postId: Int, context: Context): Boolean{
        var afegit: Boolean = false
        runBlocking {
            var resposta : Response<Post>? = null
            val cor= launch {
                resposta = getRetrofit(context).create(RevupAPIService::class.java).postLike(memberId, postId)
            }
            cor.join()
            if (resposta!!.isSuccessful)
                afegit = true
            else
                afegit = false
        }
        return afegit
    }

    fun postUnLike(memberId: Int, postId: Int, context: Context): Boolean{
        var afegit: Boolean = false
        runBlocking {
            var resposta : Response<Post>? = null
            val cor= launch {
                resposta = getRetrofit(context).create(RevupAPIService::class.java).postUnLike(memberId, postId)
            }
            cor.join()
            if (resposta!!.isSuccessful)
                afegit = true
            else
                afegit = false
        }
        return afegit
    }
    //endregion

    //region MEMBERS
    fun getMemberById(memberId: Int, context: Context): Member?{
        var resposta : Response<Member>? = null
        runBlocking {
            val cor = launch {
                resposta = getRetrofit(context).create(RevupAPIService::class.java).getMemberById(memberId)
            }
            cor.join()
        }
        if (resposta!!.isSuccessful)
            return resposta!!.body()
        else
            return null
    }

    fun putMember(member: Member, context: Context): Boolean{
        var modificat: Boolean = false
        runBlocking {
            var resposta : Response<Member>? = null
            val cor = launch {
                resposta = getRetrofit(context).create(RevupAPIService::class.java).putMember(null, member)
            }
            cor.join()
            if (resposta!!.isSuccessful)
                modificat = true
            else
                modificat = false
        }
        return modificat
    }

    fun postMember(member: Member, context: Context): Boolean{
        var afegit: Boolean = false
        runBlocking {
            var resposta : Response<Member>? = null
            val cor= launch {
                resposta = getRetrofit(context).create(RevupAPIService::class.java).postMember(member)
            }
            cor.join()
            if (resposta!!.isSuccessful)
                afegit = true
            else
                afegit = false
        }
        return afegit
    }

    fun login(memberName: String, password: String, context: Context): String?{
        var resposta : Response<String>? = null
        runBlocking {
            val cor = launch {
                resposta =
                    getRetrofit(context).create(RevupAPIService::class.java).login(memberName, password)
            }
        }
        Log.i("resposta", resposta.toString())
        if (resposta!!.isSuccessful)
            return resposta!!.body()
        else
            return null
    }

    fun getMembersByMemberName(memberName: String, context: Context): MutableList<Member>?{
        var resposta : Response<MutableList<Member>>? = null
        runBlocking {
            val cor = launch {
                resposta = getRetrofit(context).create(RevupAPIService::class.java).getMembersByMemberName(memberName)
            }
            cor.join()
        }
        if (resposta!!.isSuccessful)
            return resposta!!.body()
        else
            return null
    }

    fun getMemberByMemberName(memberName: String, context: Context): Member?{
        var resposta : Response<Member>? = null
        runBlocking {
            val cor = launch {
                resposta = getRetrofit(context).create(RevupAPIService::class.java).getMemberByMemberName(memberName)
            }
            cor.join()
        }
        if (resposta!!.isSuccessful)
            return resposta!!.body()
        else
            return null
    }

    fun getMemberExist(memberName: String, context: Context): Boolean?{
        var resposta : Response<Boolean>? = null
        runBlocking {
            val cor = launch {
                resposta = getRetrofit(context).create(RevupAPIService::class.java).getMemberExist(memberName)
            }
            cor.join()
        }
        if (resposta!!.isSuccessful)
            return resposta!!.body()
        else
            return null
    }

    fun getMembersByCarName(carName: String, context: Context): MutableList<Member>?{
        var resposta : Response<MutableList<Member>>? = null
        runBlocking {
            val cor = launch {
                resposta = getRetrofit(context).create(RevupAPIService::class.java).getMembersByCarName(carName)
            }
            cor.join()
        }
        if (resposta!!.isSuccessful)
            return resposta!!.body()
        else
            return null
    }

    fun getFriends(memberId: Int, context: Context): MutableList<Member>?{
        var resposta : Response<MutableList<Member>>? = null
        runBlocking {
            val cor = launch {
                resposta = getRetrofit(context).create(RevupAPIService::class.java).getFriends(memberId)
            }
            cor.join()
        }
        if (resposta!!.isSuccessful)
            return resposta!!.body()
        else
            return null
    }
    //endregion

    //region COMMENTS
    fun postComments(post_comment: PostComment, context: Context): Boolean{
        var afegit: Boolean = false
        runBlocking {
            var resposta : Response<Comment>? = null
            val cor= launch {
                resposta = getRetrofit(context).create(RevupAPIService::class.java).postComment(post_comment)
            }
            cor.join()
            if (resposta!!.isSuccessful)
                afegit = true
            else
                afegit = false
        }
        return afegit
    }

    fun deleteComment(post_commentId: Int, context: Context): Boolean{
        var esborrat: Boolean = false
        runBlocking {
            var resposta: Response<Boolean>? = null
            val cor = launch {
                resposta = getRetrofit(context).create(RevupAPIService::class.java).deleteComment(post_commentId)
            }
            cor.join()
            if (resposta!!.isSuccessful)
                esborrat = true
            else
                esborrat = false
        }
        return esborrat
    }
    //endregion

    //region CARS
    fun postCar(car: Car, context: Context): Boolean{
        var afegit: Boolean = false
        runBlocking {
            var resposta : Response<Car>? = null
            val cor= launch {
                resposta = getRetrofit(context).create(RevupAPIService::class.java).postCar(null, car)
            }
            cor.join()
            if (resposta!!.isSuccessful)
                afegit = true
            else
                afegit = false
        }
        return afegit
    }

    fun getCarsByMember(memberId: Int, context: Context): MutableList<Car>?{
        var resposta : Response<MutableList<Car>>? = null
        runBlocking {
            val cor = launch {
                resposta = getRetrofit(context).create(RevupAPIService::class.java).getCarsByMember(memberId)
            }
            cor.join()
        }
        if (resposta!!.isSuccessful)
            return resposta!!.body()
        else
            return null
    }

    fun putCar(car: Car, context: Context): Boolean{
        var modificat: Boolean = false
        runBlocking {
            var resposta : Response<Car>? = null
            val cor = launch {
                resposta = getRetrofit(context).create(RevupAPIService::class.java).putCar(null, car)
            }
            cor.join()
            if (resposta!!.isSuccessful)
                modificat = true
            else
                modificat = false
        }
        return modificat
    }

    fun getBrands(context: Context): MutableList<Brand>?{
        var resposta : Response<MutableList<Brand>>? = null
        runBlocking {
            val cor = launch {
                resposta = getRetrofit(context).create(RevupAPIService::class.java).getBrands()
            }
            cor.join()
        }
        if (resposta!!.isSuccessful)
            return resposta!!.body()
        else
            return null
    }

    fun getModels(context: Context): MutableList<Model>?{
        var resposta : Response<MutableList<Model>>? = null
        runBlocking {
            val cor = launch {
                resposta = getRetrofit(context).create(RevupAPIService::class.java).getModels()
            }
            cor.join()
        }
        if (resposta!!.isSuccessful)
            return resposta!!.body()
        else
            return null
    }
    //endregion

    //region EVENTS
    fun postEvent(clubEvent: ClubEvent, context: Context): Boolean{
        var afegit: Boolean = false
        runBlocking {
            var resposta : Response<ClubEvent>? = null
            val cor= launch {
                resposta = getRetrofit(context).create(RevupAPIService::class.java).postEvent(null, clubEvent)
            }
            cor.join()
            if (resposta!!.isSuccessful)
                afegit = true
            else
                afegit = false
        }
        return afegit
    }

    fun getAllEventsByClub(clubId: Int, context: Context): MutableList<ClubEvent>?{
        var resposta : Response<MutableList<ClubEvent>>? = null
        runBlocking {
            val cor = launch {
                resposta = getRetrofit(context).create(RevupAPIService::class.java).getAllEventsByClub(clubId)
            }
            cor.join()
        }
        if (resposta!!.isSuccessful)
            return resposta!!.body()
        else
            return null
    }

    fun getEvent(eventId: Int, context: Context): ClubEvent?{
        var resposta : Response<ClubEvent>? = null
        runBlocking {
            val cor = launch {
                resposta = getRetrofit(context).create(RevupAPIService::class.java).getEvent(eventId)
            }
            cor.join()
        }
        if (resposta!!.isSuccessful)
            return resposta!!.body()
        else
            return null
    }
    //endregion

    //region ROUTE
    fun postRoute(route: Route, context: Context): Boolean{
        var afegit: Boolean = false
        runBlocking {
            var resposta : Response<Route>? = null
            val cor= launch {
                resposta = getRetrofit(context).create(RevupAPIService::class.java).postRoute(route)
            }
            cor.join()
            if (resposta!!.isSuccessful)
                afegit = true
            else
                afegit = false
        }
        return afegit
    }

    fun getAllRoutesByMember(memberId: Int, context: Context): MutableList<Route>?{
        var resposta : Response<MutableList<Route>>? = null
        runBlocking {
            val cor = launch {
                resposta = getRetrofit(context).create(RevupAPIService::class.java).getAllRoutesByMember(memberId)
            }
            cor.join()
        }
        if (resposta!!.isSuccessful)
            return resposta!!.body()
        else
            return null
    }

    fun getRoute(routeId: Int, context: Context): Route?{
        var resposta : Response<Route>? = null
        runBlocking {
            val cor = launch {
                resposta = getRetrofit(context).create(RevupAPIService::class.java).getRoute(routeId)
            }
            cor.join()
        }
        if (resposta!!.isSuccessful)
            return resposta!!.body()
        else
            return null
    }
    //endregion

    class AuthInterceptor(private val context: Context) : Interceptor {
        override fun intercept(chain: Interceptor.Chain): okhttp3.Response {
            val prefs = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
            val token = prefs.getString("token", null)

            val requestBuilder = chain.request().newBuilder()
            if (!token.isNullOrEmpty()) {
                requestBuilder.addHeader("Authorization", "Bearer $token")
            }

            return chain.proceed(requestBuilder.build())
        }
    }
}