package com.mobcom.paintly

import retrofit2.Call
import retrofit2.http.*

interface Api{
    @GET("users/show")
    fun getUsers(): Call<ArrayList<UserGetResponse>>

    @FormUrlEncoded
    @POST("users/create")
    fun createUser(
        @Field("username") username: String,
        @Field("password") password: String,
        @Field("name") name: String,
        @Field("email") email: String,
    ): Call<UserCreateResponse>

    @FormUrlEncoded
    @PUT("users/update")
    fun updateUser(
        @Field("username") username: String,
        @Field("password") password: String,
        @Field("name") name: String,
        @Field("email") email: String,
    ): Call<UserUpdateResponse>

    @DELETE("users/delete")
    fun deleteUsers(): Call<ArrayList<UserDeleteResponse>>
}