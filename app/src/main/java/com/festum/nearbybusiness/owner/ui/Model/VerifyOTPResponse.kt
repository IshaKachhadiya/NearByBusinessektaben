package com.festum.nearbybusiness.owner.ui.Model

data class VerifyOTPResponse(
    val `data`: VerifyOTPData,
    val iStatusCode: Int,
    val isStatus: Boolean,
    val vMessage: String
) {
    data class VerifyOTPData(
        val __v: Int,
        val _id: String,
        val dtCreatedAt: Long,
        val isAdmin: Boolean,
        val isDeleted: Boolean,
        val vLoginToken: String,
        val vMobile: String,
        val vMobileOtp: String,
        val vType: String,
        val vUserName: String
    )
}

