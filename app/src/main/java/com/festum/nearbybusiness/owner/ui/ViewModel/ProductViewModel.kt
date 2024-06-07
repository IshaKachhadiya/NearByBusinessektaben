package com.festum.nearbybusiness.owner.ui.ViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.festum.nearbybusiness.owner.ui.Model.LoginResponse
import com.festum.nearbybusiness.owner.ui.Model.PostData
import com.festum.nearbybusiness.owner.ui.Model.Product.ProductListResponse
import com.festum.nearbybusiness.owner.ui.Model.Product.ProductPostData
import com.festum.nearbybusiness.owner.ui.Model.Product.ProductPostListData
import com.festum.nearbybusiness.owner.ui.Model.Product.ProductResponse
import com.festum.nearbybusiness.owner.ui.Repository.LoginRepository
import com.festum.nearbybusiness.owner.ui.Repository.ProductRepository

class ProductViewModel :ViewModel() {

    var productLiveData: MutableLiveData<ProductResponse>? = null
    var productlistLiveData: MutableLiveData<ProductListResponse>? = null

    fun addProduct(productPostData: ProductPostData) : LiveData<ProductResponse>? {
        productLiveData = ProductRepository.addProduct(productPostData)
        return productLiveData
    }
   fun getProductList(productPostListData: ProductPostListData) : LiveData<ProductListResponse>? {
       productlistLiveData = ProductRepository.getProductList(productPostListData)
        return productlistLiveData
    }


}