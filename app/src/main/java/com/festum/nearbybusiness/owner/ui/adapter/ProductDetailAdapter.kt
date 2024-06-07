package com.festum.nearbybusiness.owner.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.festum.nearbybusiness.owner.R
import com.festum.nearbybusiness.owner.databinding.ItemInquiryBinding
import com.festum.nearbybusiness.owner.databinding.ItemProductBinding
import com.festum.nearbybusiness.owner.databinding.ItemProductDetailBinding
import com.festum.nearbybusiness.owner.ui.Model.Product.ProductListResponse
import com.festum.nearbybusiness.owner.ui.Model.ProductDetailModel
import com.festum.nearbybusiness.owner.ui.Model.ProductModel

class ProductDetailAdapter(mcontext: Context, private val productList: List<ProductListResponse.Data>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    lateinit var mcontext: Context

    init {
        this.mcontext = mcontext
    }


    class ViewHolder(val binding: ItemProductDetailBinding) : RecyclerView.ViewHolder(binding.root)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ViewHolder(ItemProductDetailBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun getItemCount(): Int {
       return productList.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val viewHolder=holder as ProductDetailAdapter.ViewHolder
        val inquiryModel = productList.get(position)
        viewHolder.binding.tvProductName.text = inquiryModel.vName
        viewHolder.binding.tvProductDesc.text = inquiryModel.vDescription
        viewHolder.binding.tvMrp.text = inquiryModel.vPrice
        viewHolder.binding.tvOffer.text = inquiryModel.vPrice
        Glide.with(mcontext).load(inquiryModel.arrImage[0]).placeholder(R.drawable.ic_product_placeholder)
            .into(holder.binding.ivProduct)


    }
}