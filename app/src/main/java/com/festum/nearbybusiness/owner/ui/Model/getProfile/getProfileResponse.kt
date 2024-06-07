package com.festum.nearbybusiness.owner.ui.Model.getProfile

data class getProfileResponse(
    val data: getProfileData,
    val iStatusCode: Int,
    val isStatus: Boolean,
    val vMessage: String
) {
    data class getProfileData(
        val __v: Int,
        val _id: String,
        val dtCreatedAt: Long,
        val isAdmin: Boolean,
        val isDeleted: Boolean,
        val vAddress: String,
        val vImage: String,
        val vLoginToken: String,
        val vMobile: String,
        val vMobileOtp: String,
        val vName: String,
        val vType: String,
        val vUserLat: String,
        val vUserLong: String,
        val vUserName: String,
        val vGender: String
    )
}