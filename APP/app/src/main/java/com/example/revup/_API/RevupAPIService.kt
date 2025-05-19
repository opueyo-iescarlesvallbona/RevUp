package com.example.revup._API

import com.example.revup._DATACLASS.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
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
    @GET("/api/PostsByMemberId/")
    suspend fun getPostsByMemberId(
        @Query("memberId") memberId: Int
    ): Response<MutableList<Post>>

    @GET("/api/PostsByLocationId/")
    suspend fun getPostsByLocation(
        @Query("location_id") locationId: Int
    ): Response<MutableList<Post>>

    @GET("/api/PostsByLikes/")
    suspend fun getPostsByLikes(): Response<MutableList<Post>>

    @GET("/api/PostById/")
    suspend fun getPostsById(
        @Query("id") id: Int
    ): Response<Post>

    @Multipart
    @POST("/api/Post/")
    suspend fun postPost(
        @Part image: MultipartBody.Part?,
        @Part("post") post: RequestBody): Response<Post>

    @Multipart
    @PUT("/api/Post/")
    suspend fun putPost(
        @Part image: MultipartBody.Part?,
        @Part("post") post: RequestBody): Response<Post>

    @DELETE("/api/Post/")
    suspend fun deletePost(@Query("id") id: Int): Response<Boolean>

    @POST("/api/PostLike/")
    suspend fun postLike(@Query("memberId") memberId: Int, @Query("postId") postId: Int): Response<Post>

    @DELETE("/api/PostUnLike/")
    suspend fun postUnLike(@Query("memberId") memberId: Int, @Query("postId") postId: Int): Response<Post>

    @GET("/api/PostsByMemberFriends/")
    suspend fun getPostsByFriends(
        @Query("memberId") memberId: Int
    ): Response<MutableList<Post>>

    @GET("/api/PostIsLikedByMember/")
    suspend fun getPostIsLikedByMember(
        @Query("memberId") memberId: Int,
        @Query("postId") postId: Int
    ): Response<Boolean>

    @GET("/api/PostTypeById/")
    suspend fun getPostTypeById(
        @Query("id") id: Int
    ): Response<PostType>

    @GET("/api/PostTypeByName/")
    suspend fun getPostTypeByName(
        @Query("name") name: String
    ): Response<PostType>
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
        @Part("member") member: RequestBody): Response<Member>

    @Multipart
    @POST("/api/Member/")
    suspend fun postMember(
        @Part image: MultipartBody.Part?,
        @Part("member") member: RequestBody): Response<Member>

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

    @GET("/api/MemberFriends/")
    suspend fun getFriends(
        @Query("id") id: Int
    ): Response<MutableList<Member>>

    @GET("/api/ClubMembers/")
    suspend fun getMembersByClub(@Query("clubId") clubId: Int): Response<MutableList<Member>>

    @GET("/api/MemberClubRoleById/")
    suspend fun getMemberClubRoleById(@Query("clubId") clubId: Int, @Query("memberId") memberId: Int): Response<MemberClubRole>

    @GET("/api/MemberClubRoles/")
    suspend fun getMemberClubRoles(): Response<MutableList<MemberClubRole>>

    @GET("/api/MemberRelationsByMemberId/")
    suspend fun getMemberRelationsByMemberId(@Query("id") id: Int): Response<MutableList<MemberRelation>>

    @PUT("/api/MemberRelation/")
    suspend fun putMemberRelation(@Body memberRelation: MemberRelation): Response<MemberRelation>

    @POST("/api/MemberRelation/")
    suspend fun postMemberRelation(@Body memberRelation: MemberRelation): Response<MemberRelation>

    @DELETE("/api/MemberRelation/")
    suspend fun deleteMemberRelation(@Query("memberId1") memberId1: Int, @Query("memberId2") memberId2: Int): Response<Boolean>

    @GET("/api/RelationStateById/")
    suspend fun getRelationStateById(
        @Query("id") id: Int
    ): Response<RelationState>

    @GET("/api/RelationStateByName/")
    suspend fun getRelationStateByName(
        @Query("name") name: String
    ): Response<RelationState>
    //endregion

    //region COMMENTS
    @POST("/api/Comment/")
    suspend fun postComment(@Body comment: PostComment): Response<Boolean>

    @DELETE("/api/Comment/")
    suspend fun deleteComment(@Query("id") id: Int): Response<Boolean>

    @GET("/api/CommentsByPostId/")
    suspend fun getCommentsByPostId(
        @Query("postId") postId: Int
    ): Response<MutableList<PostComment>>
    //endregion

    //region CARS
    @Multipart
    @POST("/api/Car/")
    suspend fun postCar(
        @Part image: MultipartBody.Part?,
        @Part("car") car: RequestBody
    ): Response<Car>

    @GET("/api/Cars/")
    suspend fun getCarsByMember(
        @Query("memberId") memberId: Int
    ): Response<MutableList<Car>>

    @Multipart
    @PUT("/api/Car/")
    suspend fun putCar(
        @Part("car") car: RequestBody,
        @Part image: MultipartBody.Part?
        ): Response<Car>

    @GET("/api/Brands/")
    suspend fun getBrands(): Response<MutableList<Brand>>

    @GET("/api/Brand/")
    suspend fun getBrand(
        @Query("id") brandId: Int
    ): Response<Brand>

    @GET("/api/Models/")
    suspend fun getModels(): Response<MutableList<Model>>

    @GET("/api/Model/")
    suspend fun getModelById(@Query("id") id: Int): Response<Model>

    @DELETE("/api/Car/")
    suspend fun deleteCar(@Query("id") id: Int): Response<Boolean>

    @GET("/api/ModelsByBrand/")
    suspend fun getModelsByBrandId(@Query("brandId") brandId: Int): Response<MutableList<Model>>

    @GET("/api/BrandModelCheck/")
    suspend fun getBrandModelCheck(@Query("brandName") brandName: String, @Query("modelName") modelName: String): Response<Boolean>
    //endregion

    //region EVENTS
    @Multipart
    @POST("/api/Event/")
    suspend fun postEvent(
        @Part image: MultipartBody.Part?,
        @Part("clubEvent") clubEvent: RequestBody
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

    @Multipart
    @PUT("/api/Event/")
    suspend fun putEvent(
        @Part image: MultipartBody.Part?,
        @Part("clubEvent") clubEvent: RequestBody): Response<ClubEvent>

    @GET("/api/EventStateById/")
    suspend fun getEventStateById(
        @Query("id") id: Int
    ): Response<EventState>

    @GET("/api/EventStates/")
    suspend fun getEventStates(): Response<MutableList<EventState>>
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

    @PUT("/api/Route/")
    suspend fun putRoute(
        @Body route: Route): Response<Route>

    @GET("/api/TerrainTypeById/")
    suspend fun getTerrainTypeById(
        @Query("id") id: Int
    ): Response<TerrainType>

    @GET("/api/TerrainTypeByName/")
    suspend fun getTerrainTypeByName(
        @Query("name") name: String
    ): Response<TerrainType>
    //endregion

    //region GENDER
    @GET("/api/Genders/")
    suspend fun getGenders(): Response<List<Gender>>

    @GET("/api/Gender/")
    suspend fun getGenderById(@Query("id") id: Int): Response<Gender>
    //endregion

    // region CLUBS
    @GET("/api/MemberClubs/")
    suspend fun getClubsByMember(
        @Query("id") id: Int
    ): Response<MutableList<Club>>

    @GET("/api/Clubs/")
    suspend fun getClubsByName(
        @Query("clubName") clubName: String
    ): Response<MutableList<Club>>

    @GET("/api/ClubById/")
    suspend fun getClubById(
        @Query("id") clubId: Int
    ): Response<Club>

    @Multipart
    @POST("/api/Club/")
    suspend fun postClub(
        @Part image: MultipartBody.Part?,
        @Part("club") club: RequestBody): Response<Club>

    @Multipart
    @PUT("/api/Club/")
    suspend fun putClub(
        @Part image: MultipartBody.Part?,
        @Part("club") club: RequestBody): Response<Club>

    @POST("/api/MemberClub/")
    suspend fun postMemberClub(
        @Body memberClub: MemberClub): Response<MemberClub>

    @PUT("/api/MemberClub/")
    suspend fun putMemberClub(
        @Body memberClub: MemberClub): Response<MemberClub>

    @DELETE("/api/MemberClub/")
    suspend fun deleteMemberClub(@Query("memberId") memberId: Int, @Query("clubId") clubId: Int): Response<Boolean>
    // endregion

    // region LOCATION
    @GET("/api/LocationById/")
    suspend fun getLocationById(@Query("id") id: Int): Response<MemberLocation>

    @GET("/api/LocationByCountry/")
    suspend fun getLocationsByCountry(@Query("country") countryName: String): Response<MutableList<MemberLocation>>

    @GET("/api/LocationByMunicipality/")
    suspend fun getLocationsByMunicipality(@Query("municipality") municipality: String): Response<MutableList<MemberLocation>>

    @GET("/api/LocationByCcaa/")
    suspend fun getLocationsByCcaa(@Query("ccaa") ccaa: String): Response<MutableList<MemberLocation>>

    @GET("/api/Locations/")
    suspend fun getAllLocations(): Response<MutableList<MemberLocation>>

    @POST("/api/Location/")
    suspend fun postLocation(@Body location: MemberLocation): Response<MemberLocation>
    // endregion


    // MESSAGES
    @GET("/api/OldMessages/")
    suspend fun getOldMessages(@Query("senderId") senderId: Int, @Query("receiverId") receiverId: Int): Response<MutableList<Message>>
}