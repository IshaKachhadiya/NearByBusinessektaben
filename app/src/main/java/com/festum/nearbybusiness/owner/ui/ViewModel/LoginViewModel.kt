package com.festum.nearbybusiness.owner.ui.ViewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.festum.nearbybusiness.owner.ui.Model.Image.ImageUrlResponse
import com.festum.nearbybusiness.owner.ui.Model.LoginResponse
import com.festum.nearbybusiness.owner.ui.Model.PostData
import com.festum.nearbybusiness.owner.ui.Model.Profile.ProfileResponseData
import com.festum.nearbybusiness.owner.ui.Model.ProfilePostData
import com.festum.nearbybusiness.owner.ui.Model.VerifyOTPResponse
import com.festum.nearbybusiness.owner.ui.Model.VerifyPostData
import com.festum.nearbybusiness.owner.ui.Model.getProfile.getProfileResponse
import com.festum.nearbybusiness.owner.ui.Model.getProfile.getProfileSplashResponse
import com.festum.nearbybusiness.owner.ui.Repository.LoginRepository
import kotlinx.coroutines.launch
import java.io.File

class LoginViewModel : ViewModel() {

    var servicesLiveData: MutableLiveData<LoginResponse>? = null
    var verifyotpLiveData: MutableLiveData<VerifyOTPResponse>? = null
    var imagtourlLiveData: MutableLiveData<ImageUrlResponse>? = null
    var updateProfileLiveData: MutableLiveData<ProfileResponseData>? = null
    var getProfileLiveData: MutableLiveData<getProfileResponse>? = null
    var getProfileSplashLiveData: MutableLiveData<getProfileSplashResponse>? = null

     fun getOTP(postData: PostData) : LiveData<LoginResponse>? {
        servicesLiveData = LoginRepository.getOTPapiCall(postData)
        return servicesLiveData
    }

    fun updateProfile(vToken:String,profilePostData: ProfilePostData) : LiveData<ProfileResponseData>? {
        updateProfileLiveData = LoginRepository.updateProfileData(vToken,profilePostData)
        return updateProfileLiveData
    }
     fun verifyOTP(verifyPostData: VerifyPostData) : LiveData<VerifyOTPResponse>? {
         verifyotpLiveData = LoginRepository.verifyotpData(verifyPostData)
        return verifyotpLiveData
    }
     fun imagetoURL(file: File) : LiveData<ImageUrlResponse>? {
         imagtourlLiveData = LoginRepository.imageToURl(file)
        return imagtourlLiveData
    }
    fun imagetoURL_product(file: File) : LiveData<ImageUrlResponse>? {
         imagtourlLiveData = LoginRepository.imagetoURL_product(file)
        return imagtourlLiveData
    }

    fun getProflieData(vToken:String) : LiveData<getProfileResponse>? {
        getProfileLiveData = LoginRepository.getProfileData(vToken)
        return getProfileLiveData
    }
    fun getProflieSplashData(vToken:String) : LiveData<getProfileSplashResponse>? {
        getProfileSplashLiveData = LoginRepository.getProfileSplashData(vToken)
        return getProfileSplashLiveData
    }





}