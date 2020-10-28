package com.mobcom.paintly

import retrofit2.Call
import retrofit2.http.*


interface Api{
    
    @GET("users/show/{email}")
    fun getUser(@Path("email") email: String): Call<UserGet>

    @GET("imageresults/show/{username")
    fun getGallery(@Path("username") username: String): Call<List<GalleryGet>>

    @FormUrlEncoded
    @POST("users/create")
    fun createUser(@Field(
        "username") username: String,
        @Field("password") password: String,
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("edit_freq") edit_freq: Int,
        @Field("share_freq") share_freq: Int,
    ): Call<UserCreate>

    @FormUrlEncoded
    @PUT("users/update")
    fun updateUser(
        @Field("username") username: String,
        @Field("password") password: String,
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("edit_freq") edit_freq: Int,
        @Field("share_freq") share_freq: Int,
    ): Call<UserUpdateResponse>

    @DELETE("users/delete")
    fun deleteUsers(): Call<ArrayList<UserDeleteResponse>>

}