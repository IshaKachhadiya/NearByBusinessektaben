package com.festum.nearbybusiness.owner.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.festum.nearbybusiness.owner.databinding.ItemProductImageBinding
import java.io.File

class ProductImageAdapter(mcontext: Context, private val productimglist: List<File>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    lateinit var mcontext: Context

    init {
        this.mcontext = mcontext
    }

    class ViewHolder(val binding: ItemProductImageBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemProductImageBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val viewHolder = holder as ViewHolder
        val inquiryModel = productimglist.get(position).absoluteFile
        Glide.with(mcontext).load(inquiryModel)
            .into(holder.binding.img)

    }

    override fun getItemCount(): Int {
        return productimglist.size
    }


}