package com.example.revup._API

import com.example.revup._DATACLASS.*
import okhttp3.MultipartBody
import org.w3c.dom.Comment
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Query

interface RevupAPIService {
    //region POSTS
    @GET("/api/PostsByLocation/")
    suspend fun getPostsByLocation(
        @Body memberLocation: MemberLocation
    ): Response<MutableList<Post>>

    @GET("/api/PostsByLikes/")
    suspend fun getPostsByLikes(): Response<MutableList<Post>>

    @Multipart
    @POST("/api/Post/")
    suspend fun postPost(
        @Part image: MultipartBody.Part?,
        @Body post: Post): Response<Post>

    @Multipart
    @PUT("/api/Post/")
    suspend fun putPost(
        @Part image: MultipartBody.Part?,
        @Body post: Post): Response<Post>

    @DELETE("/api/Post/")
    suspend fun deletePost(@Query("id") id: Int): Response<Boolean>

    @POST("/api/PostLike/")
    suspend fun postLike(@Query("memberId") memberId: Int, @Query("postId") postId: Int): Response<Post>

    @DELETE("/api/PostUnLike/")
    suspend fun postUnLike(@Query("memberId") memberId: Int, @Query("postId") postId: Int): Response<Post>
    //endregion

    //region MEMBERS
    @GET("/api/MemberById/")
    suspend fun getMemberById(
        @Query("id") id: Int
    ): Response<Member>

    @Multipart
    @PUT("/api/Member/")
    suspend fun putMember(
        @Part image: MultipartBody.Part?,
        @Body member: Member): Response<Member>

    //@Multipart
    @POST("/api/Member/")
    suspend fun postMember(
//        @Part image: MultipartBody.Part?,
        @Body member: Member): Response<Member>

    @GET("/api/login/")
    suspend fun login(@Query("memberName") memberName: String, @Query ("password") password: String): Response<String>

    @GET("/api/Members/")
    suspend fun getMembersByMemberName(
        @Query("memberName") memberName: String
    ): Response<MutableList<Member>>

    @GET("/api/MemberByName/")
    suspend fun getMemberByMemberName(
        @Query("memberName") memberName: String
    ): Response<Member>

    @GET("/api/MemberExists/")
    suspend fun getMemberExist(
        @Query("memberName") memberName: String
    ): Response<Boolean>

    @GET("/api/CheckPassword/")
    suspend fun getCheckPassword(
        @Query("memberName") memberName: String,
        @Query("password") password: String
    ): Response<Boolean>

    @GET("/api/Members/")
    suspend fun getMembersByCarName(
        @Query("carName") carName: String
    ): Response<MutableList<Member>>

    @GET("/api/Friends/")
    suspend fun getFriends(
        @Query("id") id: Int
    ): Response<MutableList<Member>>
    //endregion

    //region COMMENTS
    @POST("/api/Comment/")
    suspend fun postComment(@Body comment: PostComment): Response<Comment>

    @DELETE("/api/Comment/")
    suspend fun deleteComment(@Query("id") id: Int): Response<Boolean>
    //endregion

    //region CARS
    @Multipart
    @POST("/api/Car/")
    suspend fun postCar(
        @Part image: MultipartBody.Part?,
        @Body car: Car
    ): Response<Car>

    @GET("/api/Cars/")
    suspend fun getCarsByMember(
        @Query("memberId") memberId: Int
    ): Response<MutableList<Car>>

    @Multipart
    @PUT("/api/Car/")
    suspend fun putCar(
        @Part image: MultipartBody.Part?,
        @Body car: Car): Response<Car>

    @GET("/api/Brands/")
    suspend fun getBrands(): Response<MutableList<Brand>>

    @GET("/api/Models/")
    suspend fun getModels(): Response<MutableList<Model>>
    //endregion

    //region EVENTS
    @Multipart
    @POST("/api/Event/")
    suspend fun postEvent(
        @Part image: MultipartBody.Part?,
        @Body clubEvent: ClubEvent
    ): Response<ClubEvent>

    @GET("/api/Events/")
    suspend fun getAllEventsByClub(
        @Query("clubId") clubId: Int
    ): Response<MutableList<ClubEvent>>

    @GET("/api/Event/")
    suspend fun getEvent(
        @Query("id") id: Int
    ): Response<ClubEvent>

    @DELETE("/api/Event/")
    suspend fun deleteEvent(@Query("id") id: Int): Response<Boolean>
    //endregion

    //region ROUTES
    @POST("/api/Route/")
    suspend fun postRoute(@Body route: Route): Response<Route>

    @GET("/api/RoutesByMember/")
    suspend fun getAllRoutesByMember(
        @Query("id") id: Int
    ): Response<MutableList<Route>>

    @GET("/api/Route/")
    suspend fun getRoute(
        @Query("id") id: Int
    ): Response<Route>

    @DELETE("/api/Route/")
    suspend fun deleteRoute(@Query("id") id: Int): Response<Boolean>
    //endregion

    //region GENDER
    @GET("/api/Genders/")
    suspend fun getGenders(): Response<List<Gender>>
    //endregion
}