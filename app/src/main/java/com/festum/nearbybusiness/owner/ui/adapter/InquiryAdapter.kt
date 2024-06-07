package com.festum.nearbybusiness.owner.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.festum.nearbybusiness.owner.databinding.ItemInquiryBinding
import com.festum.nearbybusiness.owner.ui.Model.InquiryModel

class InquiryAdapter(private val minquiryList: List<InquiryModel>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {


    class ViewHolder(val binding: ItemInquiryBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ItemInquiryBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val viewHolder=holder as ViewHolder
        val inquiryModel = minquiryList.get(position)
        viewHolder.binding.tvUsername.text = inquiryModel.userName
        viewHolder.binding.tvPhoneNum.text = inquiryModel.phoneNumber
    }

    override fun getItemCount(): Int {
       return minquiryList.size
    }


}