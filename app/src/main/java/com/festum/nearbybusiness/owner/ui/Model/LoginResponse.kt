package com.festum.nearbybusiness.owner.ui.Model

data class LoginResponse(
    val `data`: Data,
    val iStatusCode: Int,
    val isStatus: Boolean,
    val vMessage: String


){
    data class Data(
        val vPhoneOtp: String
    )
}


