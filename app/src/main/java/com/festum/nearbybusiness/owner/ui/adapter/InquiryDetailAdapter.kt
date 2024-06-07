package com.festum.nearbybusiness.owner.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.festum.nearbybusiness.owner.databinding.ItemInquiryBinding
import com.festum.nearbybusiness.owner.databinding.ItemInquiryListBinding
import com.festum.nearbybusiness.owner.ui.Model.InquiryDetailModel
import com.festum.nearbybusiness.owner.ui.Model.InquiryModel

class InquiryDetailAdapter(private val minquiryList: List<InquiryDetailModel>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {


    class ViewHolder(val binding: ItemInquiryListBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ItemInquiryListBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val viewHolder=holder as ViewHolder
        val inquiryModel = minquiryList.get(position)
        viewHolder.binding.tvUsername.text = inquiryModel.userName
        viewHolder.binding.tvPhoneNum.text = inquiryModel.productNumber
        viewHolder.binding.tvProductName.text = inquiryModel.productName
        viewHolder.binding.tvLoaction.text = inquiryModel.productLocation


    }

    override fun getItemCount(): Int {
       return minquiryList.size
    }


}