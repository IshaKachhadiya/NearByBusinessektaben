package com.festum.nearbybusiness.owner.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.festum.nearbybusiness.owner.R
import com.festum.nearbybusiness.owner.databinding.ItemInquiryBinding
import com.festum.nearbybusiness.owner.databinding.ItemProductBinding
import com.festum.nearbybusiness.owner.ui.Model.Product.ProductListResponse
import com.festum.nearbybusiness.owner.ui.Model.ProductModel

class ProductAdapter(mcontext: Context, val productList: List<ProductListResponse.Data>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    lateinit var mcontext: Context

    init {
        this.mcontext = mcontext
    }


    class ViewHolder(val binding: ItemProductBinding) : RecyclerView.ViewHolder(binding.root)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ViewHolder(ItemProductBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun getItemCount(): Int {
       return productList.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val viewHolder=holder as ProductAdapter.ViewHolder
        val inquiryModel = productList.get(position)
        viewHolder.binding.tvProductName.text = inquiryModel.vName
        viewHolder.binding.tvProductmrp.text = inquiryModel.vPrice
        viewHolder.binding.tvProductPrice.text = inquiryModel.vDescription

        Glide.with(mcontext).load(inquiryModel.arrImage[0]).placeholder(R.drawable.ic_product_placeholder)
            .into(holder.binding.ivProduct)

    }
}