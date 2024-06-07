package com.festum.nearbybusiness.owner.ui.Model.Product

data class ProductResponse(
    val data: ProductData,
    val iStatusCode: Int,
    val isStatus: Boolean,
    val vMessage: String
)
{
    data class ProductData(
        val __v: Int,
        val _id: String,
        val arrImage: List<String>,
        val dtCreatedAt: Long,
        val isDeleted: Boolean,
        val vDescription: String,
        val vName: String,
        val vPrice: String,
        val vShopId: String
    )
}