package com.festum.nearbybusiness.owner.ui.main.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.festum.nearbybusiness.owner.R
import com.festum.nearbybusiness.owner.databinding.ActivityMainBinding
import com.festum.nearbybusiness.owner.databinding.ActivityProductDetailBinding
import com.festum.nearbybusiness.owner.ui.Model.Product.ProductPostListData
import com.festum.nearbybusiness.owner.ui.Model.ProductDetailModel
import com.festum.nearbybusiness.owner.ui.ViewModel.ProductViewModel
import com.festum.nearbybusiness.owner.ui.adapter.ProductAdapter
import com.festum.nearbybusiness.owner.ui.adapter.ProductDetailAdapter

class ProductDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProductDetailBinding
    lateinit var productViewModel: ProductViewModel
    lateinit var productPostListData: ProductPostListData


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProductDetailBinding.inflate(layoutInflater)
        val view = binding.root

        setContentView(view)

        productViewModel = ViewModelProvider(this).get(ProductViewModel::class.java)

        initViews()
    }


    fun initViews(){

        binding.ivAddProduct.setOnClickListener {
            val intent = Intent(this, AddProductActivity::class.java)
            startActivity(intent)
        }

        binding.ivBack.setOnClickListener {

            onBackPressedDispatcher.onBackPressed()

        }
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                finish()
            }
        })


      /*  val productdetaildata = listOf<ProductDetailModel>(
            ProductDetailModel("Man T-Shirt","MRP₹2,599","Offer Price MRP₹2,399","If ‘cotton-made’ is a feature, then ‘comfort’ is the provided benefit. A list of features makes up the product. Conversely, benefits are what render the product useful in customers’ eyes."),
            ProductDetailModel("Man T-Shirt","MRP₹2,599","Offer Price MRP₹2,399","If ‘cotton-made’ is a feature, then ‘comfort’ is the provided benefit. A list of features makes up the product. Conversely, benefits are what render the product useful in customers’ eyes."),
            ProductDetailModel("Man T-Shirt","MRP₹2,599","Offer Price MRP₹2,399","If ‘cotton-made’ is a feature, then ‘comfort’ is the provided benefit. A list of features makes up the product. Conversely, benefits are what render the product useful in customers’ eyes."),
            ProductDetailModel("Man T-Shirt","MRP₹2,599","Offer Price MRP₹2,399","If ‘cotton-made’ is a feature, then ‘comfort’ is the provided benefit. A list of features makes up the product. Conversely, benefits are what render the product useful in customers’ eyes."),
            ProductDetailModel("Man T-Shirt","MRP₹2,599","Offer Price MRP₹2,399","If ‘cotton-made’ is a feature, then ‘comfort’ is the provided benefit. A list of features makes up the product. Conversely, benefits are what render the product useful in customers’ eyes."),
            ProductDetailModel("Man T-Shirt","MRP₹2,599","Offer Price MRP₹2,399","If ‘cotton-made’ is a feature, then ‘comfort’ is the provided benefit. A list of features makes up the product. Conversely, benefits are what render the product useful in customers’ eyes."),
            ProductDetailModel("Man T-Shirt","MRP₹2,599","Offer Price MRP₹2,399","If ‘cotton-made’ is a feature, then ‘comfort’ is the provided benefit. A list of features makes up the product. Conversely, benefits are what render the product useful in customers’ eyes.")
        )

        val adpterPorductDetail=ProductDetailAdapter(productdetaildata)
        binding.recyclerviewProductDetail.adapter=adpterPorductDetail*/




        productPostListData= ProductPostListData("1")

        productViewModel.getProductList(productPostListData)!!.observe(this, Observer {
                getlist ->

            val adapterProduct = ProductDetailAdapter(this,getlist.data)
            binding.recyclerviewProductDetail.adapter = adapterProduct

        })



    }




}