package com.festum.nearbybusiness.owner.ui.main.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import com.festum.nearbybusiness.owner.R
import com.festum.nearbybusiness.owner.databinding.ActivityInquiryListBinding
import com.festum.nearbybusiness.owner.ui.Model.InquiryDetailModel
import com.festum.nearbybusiness.owner.ui.adapter.InquiryDetailAdapter
import com.festum.nearbybusiness.owner.ui.fragment.HomeFragment

class InquiryListActivity : AppCompatActivity() {

    private lateinit var binding: ActivityInquiryListBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityInquiryListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initviews()


    }

    fun initviews() {

        binding.ivBack.setOnClickListener {

            onBackPressedDispatcher.onBackPressed()

        }
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                finish()
            }
        })

        val inquiryDeatils = listOf<InquiryDetailModel>(
            InquiryDetailModel(
                "Product : Man Clothes",
                "Location : Surat",
                "Paras Patel",
                "Mobile No. : +91 90120 10230"
            ),
            InquiryDetailModel(
                "Product : Man Clothes",
                "Location : Surat",
                "Paras Patel",
                "Mobile No. : +91 90120 10230"
            ),
            InquiryDetailModel(
                "Product : Man Clothes",
                "Location : Surat",
                "Paras Patel",
                "Mobile No. : +91 90120 10230"
            ),
            InquiryDetailModel(
                "Product : Man Clothes",
                "Location : Surat",
                "Paras Patel",
                "Mobile No. : +91 90120 10230"
            ),
            InquiryDetailModel(
                "Product : Man Clothes",
                "Location : Surat",
                "Paras Patel",
                "Mobile No. : +91 90120 10230"
            ),
            InquiryDetailModel(
                "Product : Man Clothes",
                "Location : Surat",
                "Paras Patel",
                "Mobile No. : +91 90120 10230"
            ),
            InquiryDetailModel(
                "Product : Man Clothes",
                "Location : Surat",
                "Paras Patel",
                "Mobile No. : +91 90120 10230"
            )

        )

        val inquiryAdapter = InquiryDetailAdapter(inquiryDeatils)
        binding.recyclerviewInquiryList.adapter = inquiryAdapter


    }


}