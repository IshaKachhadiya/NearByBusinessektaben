package com.festum.nearbybusiness.owner.ui.Repository

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.festum.nearbybusiness.owner.ui.Api.RetrofitClient
import com.festum.nearbybusiness.owner.ui.Model.LoginResponse
import com.festum.nearbybusiness.owner.ui.Model.PostData
import com.festum.nearbybusiness.owner.ui.Model.Product.ProductListResponse
import com.festum.nearbybusiness.owner.ui.Model.Product.ProductPostData
import com.festum.nearbybusiness.owner.ui.Model.Product.ProductPostListData
import com.festum.nearbybusiness.owner.ui.Model.Product.ProductResponse
import com.festum.nearbybusiness.owner.ui.Model.getProfile.getProfileSplashResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

object ProductRepository {

    val addProduct = MutableLiveData<ProductResponse>()
    val listProduct = MutableLiveData<ProductListResponse>()

    fun addProduct(productPostData: ProductPostData): MutableLiveData<ProductResponse> {


        val call = RetrofitClient.apiService.addProduct(productPostData)

        call.enqueue(object : Callback<ProductResponse> {
            override fun onFailure(call: Call<ProductResponse>, t: Throwable) {
                Log.e("ProductRepository : ", t.message.toString())
            }

            override fun onResponse(
                call: Call<ProductResponse>,
                response: Response<ProductResponse>
            ) {
                Log.e("ProductRepository : ", response.body().toString())

                val data = response.body()
                addProduct.value =
                    data?.let {
                        ProductResponse(
                            it.data,
                            data.iStatusCode,
                            data.isStatus,
                            data.vMessage
                        )
                    }


            }
        })

        return addProduct
    }
    fun getProductList(productPostListData: ProductPostListData): MutableLiveData<ProductListResponse> {


        val call = RetrofitClient.apiService.getProductList(productPostListData)

        call.enqueue(object : Callback<ProductListResponse> {
            override fun onFailure(call: Call<ProductListResponse>, t: Throwable) {
                Log.e("getProductList : ", t.message.toString())
            }

            override fun onResponse(
                call: Call<ProductListResponse>,
                response: Response<ProductListResponse>
            ) {
//                Log.e("getProductList : ", response.body().toString())

                val data = response.body()
                listProduct.value =
                    data?.let {
                        ProductListResponse(
                            it.data,
                            data.iCount,
                            data.iStatusCode,
                            data.isStatus,
                            data.vMessage
                        )
                    }


            }
        })

        return listProduct
    }


}