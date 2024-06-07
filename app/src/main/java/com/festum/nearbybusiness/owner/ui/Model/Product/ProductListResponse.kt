package com.festum.nearbybusiness.owner.ui.Model.Product

data class ProductListResponse(
    val data: List<Data>,
    val iCount: Int,
    val iStatusCode: Int,
    val isStatus: Boolean,
    val vMessage: String
)
{
    data class Data(
        val _id: String,
        val arrImage: List<String>,
        val vDescription: String,
        val vName: String,
        val vPrice: String,
        val vShopId: String
    )
}