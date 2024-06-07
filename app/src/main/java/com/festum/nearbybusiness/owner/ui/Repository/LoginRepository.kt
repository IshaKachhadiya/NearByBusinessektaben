package com.festum.nearbybusiness.owner.ui.Repository

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.festum.nearbybusiness.owner.ui.Api.RetrofitClient
import com.festum.nearbybusiness.owner.ui.Api.RetrofitClient.apiService
import com.festum.nearbybusiness.owner.ui.Model.Image.ImageUrlResponse
import com.festum.nearbybusiness.owner.ui.Model.LoginResponse
import com.festum.nearbybusiness.owner.ui.Model.PostData
import com.festum.nearbybusiness.owner.ui.Model.Profile.ProfileResponseData
import com.festum.nearbybusiness.owner.ui.Model.ProfilePostData
import com.festum.nearbybusiness.owner.ui.Model.VerifyOTPResponse
import com.festum.nearbybusiness.owner.ui.Model.VerifyPostData
import com.festum.nearbybusiness.owner.ui.Model.getProfile.getProfileResponse
import com.festum.nearbybusiness.owner.ui.Model.getProfile.getProfileSplashResponse
import com.google.gson.GsonBuilder
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

object LoginRepository {

    val serviceSetterGetter = MutableLiveData<LoginResponse>()
    val verifyotp = MutableLiveData<VerifyOTPResponse>()
    val imageurl = MutableLiveData<ImageUrlResponse>()
    val updateProfile = MutableLiveData<ProfileResponseData>()
    val getProfile = MutableLiveData<getProfileResponse>()
    val getSplashProfile = MutableLiveData<getProfileSplashResponse>()

    fun getOTPapiCall(postData: PostData): MutableLiveData<LoginResponse> {


        val call = RetrofitClient.apiService.getLoginOTP(postData)

        call.enqueue(object : Callback<LoginResponse> {
            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                // TODO("Not yet implemented")
                Log.e("LoginRepository : ", t.message.toString())
            }

            override fun onResponse(
                call: Call<LoginResponse>,
                response: Response<LoginResponse>
            ) {
                // TODO("Not yet implemented")
                Log.e("LoginRepository : ", response.body().toString())

                val data = response.body()

                val msg = data!!.vMessage

                serviceSetterGetter.value =
                    LoginResponse(data.data, data.iStatusCode, data.isStatus, data.vMessage)
            }
        })

        return serviceSetterGetter
    }


    fun verifyotpData(verifyPostData: VerifyPostData): MutableLiveData<VerifyOTPResponse> {

        val call = RetrofitClient.apiService.verifyOTP(verifyPostData)

        call.enqueue(object : Callback<VerifyOTPResponse> {
            override fun onResponse(
                p0: Call<VerifyOTPResponse>,
                response: Response<VerifyOTPResponse>
            ) {
                Log.e("verifyotpData : ", response.body().toString())

                val data = response.body()

                if (data != null) {
                    verifyotp.value =
                        VerifyOTPResponse(data.data, data.iStatusCode, data.isStatus, data.vMessage)
                }

            }

            override fun onFailure(p0: Call<VerifyOTPResponse>, t: Throwable) {
                Log.e("verifyotpData : ", t.message.toString())

            }

        })
        return verifyotp
    }


    fun imagetoURL_product(file: File): MutableLiveData<ImageUrlResponse> {


        val vImageType = RequestBody.create("text/plain".toMediaTypeOrNull(), "yourImageType")
        val requestFile = RequestBody.create("image/*".toMediaTypeOrNull(), file)
        val body = MultipartBody.Part.createFormData("vImage", file.name, requestFile)
        val call = RetrofitClient.apiService.uploadImage(vImageType, body)

        call.enqueue(object : Callback<ImageUrlResponse> {
            override fun onResponse(
                call: Call<ImageUrlResponse>,
                response: Response<ImageUrlResponse>
            ) {
                if (response.isSuccessful) {
                    val data = response.body()
                    Log.e("imageToURl", "onResponse: " + GsonBuilder().setPrettyPrinting().create()
                            .toJson(response.body()))
                        if (data != null) {
                            imageurl.value =response.body()
                              /*  ImageUrlResponse(
                                    data.data,
                                    data.iStatusCode,
                                    data.isStatus,
                                    data.vMessage
                                )*/
                        }
                }
            }

            override fun onFailure(call: Call<ImageUrlResponse>, t: Throwable) {
                Log.e("imageToURl", "onFailure: " +t.message)

            }
        })
        return imageurl
    }
    fun imageToURl(file: File): MutableLiveData<ImageUrlResponse> {


        val vImageType = RequestBody.create("text/plain".toMediaTypeOrNull(), "yourImageType")
        val requestFile = RequestBody.create("image/*".toMediaTypeOrNull(), file)
        val body = MultipartBody.Part.createFormData("vImage", file.name, requestFile)
        val call = RetrofitClient.apiService.uploadImage(vImageType, body)

        call.enqueue(object : Callback<ImageUrlResponse> {
            override fun onResponse(
                call: Call<ImageUrlResponse>,
                response: Response<ImageUrlResponse>
            ) {
                if (response.isSuccessful) {
                    val data = response.body()
                    Log.e("imageToURl", "onResponse: " + GsonBuilder().setPrettyPrinting().create()
                            .toJson(response.body()))
                        if (data != null) {
                            imageurl.value =response.body()
                              /*  ImageUrlResponse(
                                    data.data,
                                    data.iStatusCode,
                                    data.isStatus,
                                    data.vMessage
                                )*/
                        }
                }
            }

            override fun onFailure(call: Call<ImageUrlResponse>, t: Throwable) {
                Log.e("imageToURl", "onFailure: " +t.message)

            }
        })
        return imageurl
    }
    fun updateProfileData(vToken:String,profilePostData: ProfilePostData): MutableLiveData<ProfileResponseData> {
        Log.e(
            "TAGee",
            "updateProfileData:Profile ---->" + GsonBuilder().setPrettyPrinting().create()
                .toJson(profilePostData)
        )
        Log.e(
            "TAGee",
            "updateProfileData:vToken ---->" + vToken
        )

        val call = RetrofitClient.apiService.updateProfileData(vToken,profilePostData)

        call.enqueue(object : Callback<ProfileResponseData> {
            override fun onFailure(call: Call<ProfileResponseData>, t: Throwable) {
                Log.e("updateProfileData fail : ", t.message.toString())
            }

            override fun onResponse(
                call: Call<ProfileResponseData>,
                response: Response<ProfileResponseData>
            ) {
                Log.e("updateProfileData response : ", response.body().toString())

                val data = response.body()

                if (data != null) {
                    updateProfile.value =
                        ProfileResponseData(data.data, data.iStatusCode, data.isStatus, data.vMessage)
                }
            }
        })

        return updateProfile
    }


    fun getProfileData(vToken:String): MutableLiveData<getProfileResponse> {


        val call = RetrofitClient.apiService.getProfile(vToken)

        call.enqueue(object : Callback<getProfileResponse> {
            override fun onFailure(call: Call<getProfileResponse>, t: Throwable) {
                // TODO("Not yet implemented")
                Log.e("getProfileData :onFailure ", t.message.toString())
            }

            override fun onResponse(
                call: Call<getProfileResponse>,
                response: Response<getProfileResponse>
            ) {
                // TODO("Not yet implemented")
                Log.e("getProfileData onResponse : ", GsonBuilder().setPrettyPrinting().create()
                    .toJson(response.body()))

                val data = response.body()

                if (data != null) {
                    getProfile.value =
                        getProfileResponse(data.data, data.iStatusCode, data.isStatus, data.vMessage)
                }
            }
        })

        return getProfile
    }
    fun getProfileSplashData(vToken:String): MutableLiveData<getProfileSplashResponse> {


        val call = RetrofitClient.apiService.getSplashProfile(vToken)

        call.enqueue(object : Callback<getProfileSplashResponse> {
            override fun onFailure(call: Call<getProfileSplashResponse>, t: Throwable) {
                // TODO("Not yet implemented")
                Log.e("getProfileData :onFailure ", t.message.toString())
            }

            override fun onResponse(
                call: Call<getProfileSplashResponse>,
                response: Response<getProfileSplashResponse>
            ) {
                // TODO("Not yet implemented")
                Log.e("getProfileData onResponse : ", GsonBuilder().setPrettyPrinting().create()
                    .toJson(response.body()))

                val data = response.body()

                if (data != null) {
                    getSplashProfile.value =
                        getProfileSplashResponse(data.data, data.iStatusCode, data.isStatus, data.vMessage)
                }
            }
        })

        return getSplashProfile
    }


}