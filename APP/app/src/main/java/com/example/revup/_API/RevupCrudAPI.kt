package com.example.revup._API

import android.content.Context
import android.net.Uri
import android.provider.ContactsContract
import android.util.Log
import android.view.Display
import com.example.revup._DATACLASS.*
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
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
    fun getPostsByMemberId(memberId: Int, context: Context): MutableList<Post>?{
        var resposta : Response<MutableList<Post>>? = null
        runBlocking {
            val cor = launch {
                resposta = getRetrofit(context).create(RevupAPIService::class.java).getPostsByMemberId(memberId)
            }
            cor.join()
        }
        if (resposta!!.isSuccessful)
            return resposta!!.body()
        else
            return null
    }

    fun getPostsByLocation(memberLocationId: Int, context: Context): MutableList<Post>?{
        var resposta : Response<MutableList<Post>>? = null
        runBlocking {
            val cor = launch {
                resposta = getRetrofit(context).create(RevupAPIService::class.java).getPostsByLocation(memberLocationId)
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

    fun getPostById(postId: Int, context: Context): Post?{
        var resposta : Response<Post>? = null
        runBlocking {
            val cor = launch {
                resposta = getRetrofit(context).create(RevupAPIService::class.java).getPostsById(postId)
            }
            cor.join()
        }
        if (resposta!!.isSuccessful)
            return resposta!!.body()
        else
            return null
    }

    fun postPost(post: Post, image_path: Uri?, context: Context): Boolean{
        var afegit: Boolean = false
        var body: MultipartBody.Part? = null
        if (image_path != null){
            var file = uriToFile(context, image_path)
            val requestFile = file.asRequestBody("image/*".toMediaTypeOrNull())
            body = MultipartBody.Part.createFormData("image", file.name, requestFile)
        }
        runBlocking {
            var resposta : Response<Post>? = null
            val cor= launch {
                resposta = getRetrofit(context).create(RevupAPIService::class.java).postPost(body, post)
            }
            cor.join()
            if (resposta!!.isSuccessful)
                afegit = true
            else
                afegit = false
        }
        return afegit
    }

    fun putPost(post: Post, image_path: Uri?, context: Context): Boolean{
        var modificat: Boolean = false
        var body: MultipartBody.Part? = null
        if (image_path != null) {
            var file = uriToFile(context, image_path)
            val requestFile = file.asRequestBody("image/*".toMediaTypeOrNull())
            body = MultipartBody.Part.createFormData("image", file.name, requestFile)
        }
        runBlocking {
            var resposta : Response<Post>? = null
            val cor = launch {
                resposta = getRetrofit(context).create(RevupAPIService::class.java).putPost(body, post)
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

    fun getPostsByFriends(memberId: Int, context: Context): MutableList<Post>?{
        var resposta : Response<MutableList<Post>>? = null
        runBlocking {
            val cor = launch {
                resposta = getRetrofit(context).create(RevupAPIService::class.java).getPostsByFriends(memberId)
            }
            cor.join()
        }
        if (resposta!!.isSuccessful)
            return resposta!!.body()
        else
            return null
    }

    fun getPostIsLikedByMember(memberId: Int, postId: Int, context: Context): Boolean?{
        var resposta : Response<Boolean>? = null
        runBlocking {
            val cor = launch {
                resposta = getRetrofit(context).create(RevupAPIService::class.java).getPostIsLikedByMember(memberId, postId)
            }
            cor.join()
        }
        if (resposta!!.isSuccessful)
            return resposta!!.body()
        else
            return null
    }

    fun getPostTypeById(postTypeId: Int, context: Context): PostType?{
        var resposta : Response<PostType>? = null
        runBlocking {
            val cor = launch {
                resposta = getRetrofit(context).create(RevupAPIService::class.java).getPostTypeById(postTypeId)
            }
            cor.join()
        }
        if (resposta!!.isSuccessful)
            return resposta!!.body()
        else
            return null
    }

    fun getPostTypeByName(postTypeName: String, context: Context): PostType?{
        var resposta : Response<PostType>? = null
        runBlocking {
            val cor = launch {
                resposta = getRetrofit(context).create(RevupAPIService::class.java).getPostTypeByName(postTypeName)
            }
            cor.join()
        }
        if (resposta!!.isSuccessful)
            return resposta!!.body()
        else
            return null
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

    fun putMember(member: Member, image_path: Uri?, context: Context): Boolean{
        var modificat: Boolean = false
        var body: MultipartBody.Part? = null
        if (image_path != null) {
            var file = uriToFile(context, image_path)
            val requestFile = file.asRequestBody("image/*".toMediaTypeOrNull())
            body = MultipartBody.Part.createFormData("image", file.name, requestFile)
        }
        runBlocking {
            var resposta : Response<Member>? = null
            val cor = launch {
                resposta = getRetrofit(context).create(RevupAPIService::class.java).putMember(body, member)
            }
            cor.join()
            if (resposta!!.isSuccessful)
                modificat = true
            else
                modificat = false
        }
        return modificat
    }

    fun postMember(member: Member, image_path: Uri?, context: Context): Boolean{
        var afegit: Boolean = false
        var body: MultipartBody.Part? = null
        if (image_path != null) {
            var file = uriToFile(context, image_path)
            val requestFile = file.asRequestBody("image/*".toMediaTypeOrNull())
            body = MultipartBody.Part.createFormData("image", file.name, requestFile)
        }
        runBlocking {
            var resposta : Response<Member>? = null
            val cor= launch {
                resposta = getRetrofit(context).create(RevupAPIService::class.java).postMember(body, member)
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

    fun getMembersByClub(clubId: Int, context: Context): MutableList<Member>?{
        var resposta : Response<MutableList<Member>>? = null
        runBlocking {
            val cor = launch {
                resposta = getRetrofit(context).create(RevupAPIService::class.java).getMembersByClub(clubId)
            }
            cor.join()
        }
        if (resposta!!.isSuccessful)
            return resposta!!.body()
        else
            return null
    }

    fun getMemberClubRoleById(clubId: Int, memberId: Int, context: Context): MemberClubRole?{
        var resposta : Response<MemberClubRole>? = null
        runBlocking {
            val cor = launch {
                resposta = getRetrofit(context).create(RevupAPIService::class.java).getMemberClubRoleById(clubId, memberId)
            }
            cor.join()
        }
        if (resposta!!.isSuccessful)
            return resposta!!.body()
        else
            return null
    }

    fun getMemberClubRoles(clubId: Int, memberId: Int, context: Context): MutableList<MemberClubRole>?{
        var resposta : Response<MutableList<MemberClubRole>>? = null
        runBlocking {
            val cor = launch {
                resposta = getRetrofit(context).create(RevupAPIService::class.java).getMemberClubRoles()
            }
            cor.join()
        }
        if (resposta!!.isSuccessful)
            return resposta!!.body()
        else
            return null
    }

    fun getMemberRelationsByMemberId(memberId: Int, context: Context): MutableList<MemberRelation>?{
        var resposta : Response<MutableList<MemberRelation>>? = null
        runBlocking {
            val cor = launch {
                resposta = getRetrofit(context).create(RevupAPIService::class.java).getMemberRelationsByMemberId(memberId)
            }
            cor.join()
        }
        if (resposta!!.isSuccessful)
            return resposta!!.body()
        else
            return null
    }

    fun putMemberRelation(memberRelation: MemberRelation, context: Context): Boolean{
        var modificat: Boolean = false
        runBlocking {
            var resposta : Response<MemberRelation>? = null
            val cor = launch {
                resposta = getRetrofit(context).create(RevupAPIService::class.java).putMemberRelation(memberRelation)
            }
            cor.join()
            if (resposta!!.isSuccessful)
                modificat = true
            else
                modificat = false
        }
        return modificat
    }

    fun postMemberRelation(memberRelation: MemberRelation, context: Context): Boolean{
        var afegit: Boolean = false
        runBlocking {
            var resposta : Response<MemberRelation>? = null
            val cor= launch {
                resposta = getRetrofit(context).create(RevupAPIService::class.java).postMemberRelation(memberRelation)
            }
            cor.join()
            if (resposta!!.isSuccessful)
                afegit = true
            else
                afegit = false
        }
        return afegit
    }

    fun deleteMemberRelation(memberId1: Int, memberId2: Int, context: Context): Boolean{
        var esborrat: Boolean = false
        runBlocking {
            var resposta: Response<Boolean>? = null
            val cor = launch {
                resposta = getRetrofit(context).create(RevupAPIService::class.java).deleteMemberRelation(memberId1, memberId2)
            }
            cor.join()
            if (resposta!!.isSuccessful)
                esborrat = true
            else
                esborrat = false
        }
        return esborrat
    }

    fun getRelationStateById(relationStateId: Int, context: Context): RelationState?{
        var resposta : Response<RelationState>? = null
        runBlocking {
            val cor = launch {
                resposta = getRetrofit(context).create(RevupAPIService::class.java).getRelationStateById(relationStateId)
            }
            cor.join()
        }
        if (resposta!!.isSuccessful)
            return resposta!!.body()
        else
            return null
    }

    fun getRelationStateByName(relationStateName: String, context: Context): RelationState?{
        var resposta : Response<RelationState>? = null
        runBlocking {
            val cor = launch {
                resposta = getRetrofit(context).create(RevupAPIService::class.java).getRelationStateByName(relationStateName)
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
            var resposta : Response<Boolean>? = null
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

    fun getCommentsByPostId(postId: Int, context: Context): MutableList<PostComment>?{
        var resposta : Response<MutableList<PostComment>>? = null
        runBlocking {
            val cor = launch {
                resposta = getRetrofit(context).create(RevupAPIService::class.java).getCommentsByPostId(postId)
            }
            cor.join()
        }
        if (resposta!!.isSuccessful)
            return resposta!!.body()
        else
            return null
    }
    //endregion

    //region CARS
    fun postCar(car: Car, image_path: Uri?, context: Context): Boolean{
        var afegit: Boolean = false
        var body: MultipartBody.Part? = null
        if (image_path != null) {
            var file = uriToFile(context, image_path)
            val requestFile = file.asRequestBody("image/*".toMediaTypeOrNull())
            body = MultipartBody.Part.createFormData("image", file.name, requestFile)
        }
        runBlocking {
            var resposta : Response<Car>? = null
            val cor= launch {
                resposta = getRetrofit(context).create(RevupAPIService::class.java).postCar(body, car)
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

    fun putCar(car: Car, image_path: Uri?, context: Context): Boolean{
        var modificat: Boolean = false
        var body: MultipartBody.Part? = null
        if (image_path != null) {
            var file = uriToFile(context, image_path)
            val requestFile = file.asRequestBody("image/*".toMediaTypeOrNull())
            body = MultipartBody.Part.createFormData("image", file.name, requestFile)
        }
        val carJson = Gson().toJson(car)
        val carRequestBody = carJson.toRequestBody("application/json".toMediaTypeOrNull())
        runBlocking {
            var resposta : Response<Car>? = null
            val cor = launch {
                resposta = getRetrofit(context).create(RevupAPIService::class.java).putCar(carRequestBody, body)
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

    fun getBrand(brandId: Int, context: Context): Brand?{
        var resposta : Response<Brand>? = null
        runBlocking {
            val cor = launch {
                resposta = getRetrofit(context).create(RevupAPIService::class.java).getBrand(brandId)
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

    fun getModelById(modelId: Int, context: Context): Model?{
        var resposta : Response<Model>? = null
        runBlocking {
            val cor = launch {
                resposta = getRetrofit(context).create(RevupAPIService::class.java).getModelById(modelId)
            }
            cor.join()
        }
        if (resposta!!.isSuccessful)
            return resposta!!.body()
        else
            return null
    }

    fun deleteCar(carId: Int, context: Context): Boolean{
        var esborrat: Boolean = false
        runBlocking {
            var resposta: Response<Boolean>? = null
            val cor = launch {
                resposta = getRetrofit(context).create(RevupAPIService::class.java).deleteCar(carId)
            }
            cor.join()
            if (resposta!!.isSuccessful)
                esborrat = true
            else
                esborrat = false
        }
        return esborrat
    }

    fun getModelsByBrandId(brandId: Int, context: Context): MutableList<Model>?{
        var resposta : Response<MutableList<Model>>? = null
        runBlocking {
            val cor = launch {
                resposta = getRetrofit(context).create(RevupAPIService::class.java).getModelsByBrandId(brandId)
            }
            cor.join()
        }
        if (resposta!!.isSuccessful)
            return resposta!!.body()
        else
            return null
    }

    fun getBrandModelCheck(brandName: String, modelName: String, context: Context): Boolean?{
        var resposta : Response<Boolean>? = null
        runBlocking {
            val cor = launch {
                resposta = getRetrofit(context).create(RevupAPIService::class.java).getBrandModelCheck(brandName, modelName)
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
    fun postEvent(clubEvent: ClubEvent, image_path: Uri?, context: Context): Boolean{
        var afegit: Boolean = false
        var body: MultipartBody.Part? = null
        if (image_path != null) {
            var file = uriToFile(context, image_path)
            val requestFile = file.asRequestBody("image/*".toMediaTypeOrNull())
            body = MultipartBody.Part.createFormData("image", file.name, requestFile)
        }
        runBlocking {
            var resposta : Response<ClubEvent>? = null
            val cor= launch {
                resposta = getRetrofit(context).create(RevupAPIService::class.java).postEvent(body, clubEvent)
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

    fun deleteEvent(event_id: Int, context: Context): Boolean{
        var esborrat: Boolean = false
        runBlocking {
            var resposta: Response<Boolean>? = null
            val cor = launch {
                resposta = getRetrofit(context).create(RevupAPIService::class.java).deleteEvent(event_id)
            }
            cor.join()
            if (resposta!!.isSuccessful)
                esborrat = true
            else
                esborrat = false
        }
        return esborrat
    }

    fun putEvent(clubEvent: ClubEvent, image_path: Uri?, context: Context): Boolean{
        var modificat: Boolean = false
        var body: MultipartBody.Part? = null
        if (image_path != null) {
            var file = uriToFile(context, image_path)
            val requestFile = file.asRequestBody("image/*".toMediaTypeOrNull())
            body = MultipartBody.Part.createFormData("image", file.name, requestFile)
        }
        runBlocking {
            var resposta : Response<ClubEvent>? = null
            val cor = launch {
                resposta = getRetrofit(context).create(RevupAPIService::class.java).putEvent(body, clubEvent)
            }
            cor.join()
            if (resposta!!.isSuccessful)
                modificat = true
            else
                modificat = false
        }
        return modificat
    }

    fun getEventStateById(eventStateId: Int, context: Context): EventState?{
        var resposta : Response<EventState>? = null
        runBlocking {
            val cor = launch {
                resposta = getRetrofit(context).create(RevupAPIService::class.java).getEventStateById(eventStateId)
            }
            cor.join()
        }
        if (resposta!!.isSuccessful)
            return resposta!!.body()
        else
            return null
    }

    fun getEventStates(context: Context): MutableList<EventState>?{
        var resposta : Response<MutableList<EventState>>? = null
        runBlocking {
            val cor = launch {
                resposta = getRetrofit(context).create(RevupAPIService::class.java).getEventStates()
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

    fun deleteRoute(route_id: Int, context: Context): Boolean{
        var esborrat: Boolean = false
        runBlocking {
            var resposta: Response<Boolean>? = null
            val cor = launch {
                resposta = getRetrofit(context).create(RevupAPIService::class.java).deleteRoute(route_id)
            }
            cor.join()
            if (resposta!!.isSuccessful)
                esborrat = true
            else
                esborrat = false
        }
        return esborrat
    }

    fun putRoute(route: Route, context: Context): Boolean{
        var modificat: Boolean = false
        runBlocking {
            var resposta : Response<Route>? = null
            val cor = launch {
                resposta = getRetrofit(context).create(RevupAPIService::class.java).putRoute(route)
            }
            cor.join()
            if (resposta!!.isSuccessful)
                modificat = true
            else
                modificat = false
        }
        return modificat
    }

    fun getTerrainTypeById(terrainTypeId: Int, context: Context): TerrainType?{
        var resposta : Response<TerrainType>? = null
        runBlocking {
            val cor = launch {
                resposta = getRetrofit(context).create(RevupAPIService::class.java).getTerrainTypeById(terrainTypeId)
            }
            cor.join()
        }
        if (resposta!!.isSuccessful)
            return resposta!!.body()
        else
            return null
    }

    fun getTerrainTypeByName(terrainTypeName: String, context: Context): TerrainType?{
        var resposta : Response<TerrainType>? = null
        runBlocking {
            val cor = launch {
                resposta = getRetrofit(context).create(RevupAPIService::class.java).getTerrainTypeByName(terrainTypeName)
            }
            cor.join()
        }
        if (resposta!!.isSuccessful)
            return resposta!!.body()
        else
            return null
    }
    //endregion

    //region GENDERS
    fun getAllGenders(context: Context): List<Gender>?{
        var resposta : Response<List<Gender>>? = null
        runBlocking {
            val cor = launch {
                resposta = getRetrofit(context).create(RevupAPIService::class.java).getGenders()
            }
            cor.join()
        }
        if (resposta!!.isSuccessful)
            return resposta!!.body()
        else
            return null
    }

    fun getGenderById(genderId: Int, context: Context): Gender?{
        var resposta : Response<Gender>? = null
        runBlocking {
            val cor = launch {
                resposta = getRetrofit(context).create(RevupAPIService::class.java).getGenderById(genderId)
            }
            cor.join()
        }
        if (resposta!!.isSuccessful)
            return resposta!!.body()
        else
            return null
    }
    //endregion

    //region CLUBS
    fun getClubsByMember(memberId: Int, context: Context): MutableList<Club>?{
        var resposta : Response<MutableList<Club>>? = null
        runBlocking {
            val cor = launch {
                resposta = getRetrofit(context).create(RevupAPIService::class.java).getClubsByMember(memberId)
            }
            cor.join()
        }
        if (resposta!!.isSuccessful)
            return resposta!!.body()
        else
            return null
    }

    fun getClubsByName(clubName: String, context: Context): MutableList<Club>?{
        var resposta : Response<MutableList<Club>>? = null
        runBlocking {
            val cor = launch {
                resposta = getRetrofit(context).create(RevupAPIService::class.java).getClubsByName(clubName)
            }
            cor.join()
        }
        if (resposta!!.isSuccessful)
            return resposta!!.body()
        else
            return null
    }

    fun getClubById(clubId: Int, context: Context): Club?{
        var resposta : Response<Club>? = null
        runBlocking {
            val cor = launch {
                resposta = getRetrofit(context).create(RevupAPIService::class.java).getClubById(clubId)
            }
            cor.join()
        }
        if (resposta!!.isSuccessful)
            return resposta!!.body()
        else
            return null
    }

    fun postClub(club: Club, image_path: Uri?, context: Context): Boolean{
        var afegit: Boolean = false
        var body: MultipartBody.Part? = null
        if (image_path != null) {
            var file = uriToFile(context, image_path)
            val requestFile = file.asRequestBody("image/*".toMediaTypeOrNull())
            body = MultipartBody.Part.createFormData("image", file.name, requestFile)
        }
        runBlocking {
            var resposta : Response<Club>? = null
            val cor= launch {
                resposta = getRetrofit(context).create(RevupAPIService::class.java).postClub(body, club)
            }
            cor.join()
            if (resposta!!.isSuccessful)
                afegit = true
            else
                afegit = false
        }
        return afegit
    }

    fun putClub(club: Club, image_path: Uri?, context: Context): Boolean{
        var modificat: Boolean = false
        var body: MultipartBody.Part? = null
        if (image_path != null) {
            var file = uriToFile(context, image_path)
            val requestFile = file.asRequestBody("image/*".toMediaTypeOrNull())
            body = MultipartBody.Part.createFormData("image", file.name, requestFile)
        }
        runBlocking {
            var resposta : Response<Club>? = null
            val cor = launch {
                resposta = getRetrofit(context).create(RevupAPIService::class.java).putClub(body, club)
            }
            cor.join()
            if (resposta!!.isSuccessful)
                modificat = true
            else
                modificat = false
        }
        return modificat
    }

    fun postMemberClub(memberClub: MemberClub, context: Context): Boolean{
        var afegit: Boolean = false
        runBlocking {
            var resposta : Response<MemberClub>? = null
            val cor= launch {
                resposta = getRetrofit(context).create(RevupAPIService::class.java).postMemberClub(memberClub)
            }
            cor.join()
            if (resposta!!.isSuccessful)
                afegit = true
            else
                afegit = false
        }
        return afegit
    }

    fun putMemberClub(memberClub: MemberClub, context: Context): Boolean{
        var modificat: Boolean = false
        runBlocking {
            var resposta : Response<MemberClub>? = null
            val cor = launch {
                resposta = getRetrofit(context).create(RevupAPIService::class.java).putMemberClub(memberClub)
            }
            cor.join()
            if (resposta!!.isSuccessful)
                modificat = true
            else
                modificat = false
        }
        return modificat
    }

    fun deleteMemberClub(memberId: Int, clubId: Int, context: Context): Boolean{
        var esborrat: Boolean = false
        runBlocking {
            var resposta: Response<Boolean>? = null
            val cor = launch {
                resposta = getRetrofit(context).create(RevupAPIService::class.java).deleteMemberClub(memberId, clubId)
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

    // reion LOCATION
    fun getLocationById(locationId: Int, context: Context): MemberLocation?{
        var resposta : Response<MemberLocation>? = null
        runBlocking {
            val cor = launch {
                resposta = getRetrofit(context).create(RevupAPIService::class.java).getLocationById(locationId)
            }
            cor.join()
        }
        if (resposta!!.isSuccessful)
            return resposta!!.body()
        else
            return null
    }

    fun getLocationsByCountry(countryName: String, context: Context): MutableList<MemberLocation>?{
        var resposta : Response<MutableList<MemberLocation>>? = null
        runBlocking {
            val cor = launch {
                resposta = getRetrofit(context).create(RevupAPIService::class.java).getLocationsByCountry(countryName)
            }
            cor.join()
        }
        if (resposta!!.isSuccessful)
            return resposta!!.body()
        else
            return null
    }

    fun getLocationsByMunicipality(municipality: String, context: Context): MutableList<MemberLocation>?{
        var resposta : Response<MutableList<MemberLocation>>? = null
        runBlocking {
            val cor = launch {
                resposta = getRetrofit(context).create(RevupAPIService::class.java).getLocationsByMunicipality(municipality)
            }
            cor.join()
        }
        if (resposta!!.isSuccessful)
            return resposta!!.body()
        else
            return null
    }

    fun getLocationsByCcaa(ccaa: String, context: Context): MutableList<MemberLocation>?{
        var resposta : Response<MutableList<MemberLocation>>? = null
        runBlocking {
            val cor = launch {
                resposta = getRetrofit(context).create(RevupAPIService::class.java).getLocationsByCcaa(ccaa)
            }
            cor.join()
        }
        if (resposta!!.isSuccessful)
            return resposta!!.body()
        else
            return null
    }

    fun getAllLocations(context: Context): MutableList<MemberLocation>?{
        var resposta : Response<MutableList<MemberLocation>>? = null
        runBlocking {
            val cor = launch {
                resposta = getRetrofit(context).create(RevupAPIService::class.java).getAllLocations()
            }
            cor.join()
        }
        if (resposta!!.isSuccessful)
            return resposta!!.body()
        else
            return null
    }

    fun postLocation(location: MemberLocation, context: Context): Boolean{
        var afegit: Boolean = false
        runBlocking {
            var resposta : Response<MemberLocation>? = null
            val cor= launch {
                resposta = getRetrofit(context).create(RevupAPIService::class.java).postLocation(location)
            }
            cor.join()
            if (resposta!!.isSuccessful)
                afegit = true
            else
                afegit = false
        }
        return afegit
    }
    // endregion

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