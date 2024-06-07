package com.festum.nearbybusiness.owner.ui.Api

import com.festum.nearbybusiness.owner.ui.Model.Image.ImageUrlResponse
import com.festum.nearbybusiness.owner.ui.Model.LoginResponse
import com.festum.nearbybusiness.owner.ui.Model.PostData
import com.festum.nearbybusiness.owner.ui.Model.Product.ProductListResponse
import com.festum.nearbybusiness.owner.ui.Model.Product.ProductPostData
import com.festum.nearbybusiness.owner.ui.Model.Product.ProductPostListData
import com.festum.nearbybusiness.owner.ui.Model.Product.ProductResponse
import com.festum.nearbybusiness.owner.ui.Model.Profile.ProfileResponseData
import com.festum.nearbybusiness.owner.ui.Model.ProfilePostData
import com.festum.nearbybusiness.owner.ui.Model.VerifyOTPResponse
import com.festum.nearbybusiness.owner.ui.Model.VerifyPostData
import com.festum.nearbybusiness.owner.ui.Model.getProfile.getProfileResponse
import com.festum.nearbybusiness.owner.ui.Model.getProfile.getProfileSplashResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.FieldMap
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part


interface APIInterface {

    @POST("user/login")
    fun getLoginOTP(@Body postData: PostData): Call<LoginResponse>


    @POST("user/verifyOtp")
    fun verifyOTP(@Body verifyPostData: VerifyPostData): Call<VerifyOTPResponse>


    @Multipart
    @POST("addImage/details")
    fun uploadImage(
        @Part("vImageType") vImageType: RequestBody,
        @Part vImage: MultipartBody.Part
    ): Call<ImageUrlResponse>

    @POST("user/updateUser")
    fun updateProfileData(
        @Header("Authorization") vToken: String,
        @Body profilePostData: ProfilePostData
    ): Call<ProfileResponseData>


    @GET("user/viewProfile")
    fun getProfile(@Header("Authorization") vToken: String): Call<getProfileResponse>

    @GET("user/viewProfile")
    fun getSplashProfile(@Header("Authorization") vToken: String): Call<getProfileSplashResponse>


    @POST("product/details")
    fun addProduct(@Body productPostData: ProductPostData): Call<ProductResponse>

    @POST("product/list")
    fun getProductList(@Body productPostListData: ProductPostListData): Call<ProductListResponse>





}