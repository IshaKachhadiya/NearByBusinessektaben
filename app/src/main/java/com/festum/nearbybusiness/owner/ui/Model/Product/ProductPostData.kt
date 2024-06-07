package com.festum.nearbybusiness.owner.ui.Model.Product

data class ProductPostData(
    val vShopId: String,
    val vName: String,
    val vDescription: String,
    val vPrice: String,
    val arrImage: List<String>
    )