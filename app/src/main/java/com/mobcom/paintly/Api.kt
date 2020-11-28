package com.mobcom.paintly

import retrofit2.Call
import retrofit2.http.*


interface Api{
    
    @GET("users/show/{email}")
    fun getUser(@Path("email") email: String): Call<UserData>

    @FormUrlEncoded
    @POST("users/create")
    fun createUser(@Field(
        "username") username: String,
        @Field("password") password: String,
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("quota_today") quota_today: Int,
        @Field("edit_freq") edit_freq: Int,
        @Field("share_freq") share_freq: Int,
    ): Call<UserData>

    @FormUrlEncoded
    @PUT("users/update/{email}")
    fun updateUser(@Path("email") email: String,
        @Field("name") name: String,
        @Field("email") new_email: String,
        @Field("photo") photo: String
    ): Call<UserData>

    @FormUrlEncoded
    @PUT("users/update/{email}")
    fun updateUserEditFreq(@Path("email") email: String,
                   @Field("edit_freq") edit_freq: Int,
    ): Call<UserData>

    @FormUrlEncoded
    @PUT("users/update/{email}")
    fun updateQuota(@Path("email") email: String,
                    @Field("quota_today") quota_today: Int,
    ): Call<UserData>

    @GET("imagestyles")
    fun getStyles(): Call<List<StyleData>>

    @GET("imageresults/show/{user_email}")
    fun getGallery(@Path("user_email") user_email: String): Call<List<GalleryData>>

    @FormUrlEncoded
    @POST("/imageresults/create")
    fun addResult(@Field("user_email") user_email: String,
                  @Field("style_id") style_id: String,
                  @Field("content_image") content_image: String,
                   @Field("file_result") file_result: String,
    ): Call<GalleryData>

    @DELETE("users/delete/{email}")
    fun deleteUser(@Path("email") email: String, @Body userData: UserData): Call<UserData>


}
