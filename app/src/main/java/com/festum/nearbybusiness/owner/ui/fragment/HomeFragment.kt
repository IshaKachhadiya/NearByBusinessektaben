package com.festum.nearbybusiness.owner.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.festum.nearbybusiness.owner.R
import com.festum.nearbybusiness.owner.Utils.PrefManager
import com.festum.nearbybusiness.owner.databinding.FragmentHomeBinding
import com.festum.nearbybusiness.owner.ui.Model.InquiryModel
import com.festum.nearbybusiness.owner.ui.Model.Product.ProductPostListData
import com.festum.nearbybusiness.owner.ui.Model.ProductModel
import com.festum.nearbybusiness.owner.ui.Model.Profile.ProfileResponseData
import com.festum.nearbybusiness.owner.ui.Model.getProfile.getProfileResponse
import com.festum.nearbybusiness.owner.ui.ViewModel.LoginViewModel
import com.festum.nearbybusiness.owner.ui.ViewModel.ProductViewModel
import com.festum.nearbybusiness.owner.ui.adapter.InquiryAdapter
import com.festum.nearbybusiness.owner.ui.adapter.ProductAdapter
import com.festum.nearbybusiness.owner.ui.main.view.AddProductActivity
import com.festum.nearbybusiness.owner.ui.main.view.InquiryListActivity
import com.festum.nearbybusiness.owner.ui.main.view.ProductDetailActivity
import com.google.gson.GsonBuilder


class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    lateinit var loginViewModel: LoginViewModel
    lateinit var isToken: String
    lateinit var productViewModel: ProductViewModel
    lateinit var productPostListData: ProductPostListData


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        isToken = PrefManager.getInstance(this.requireActivity()).getStringPreference("isappToken")
        getProfiledata(isToken)
        productViewModel = ViewModelProvider(this).get(ProductViewModel::class.java)

        initViews()
        return binding.root

    }


    fun getProfiledata(vToken: String) {

        loginViewModel = ViewModelProvider(this).get(LoginViewModel::class.java)


        loginViewModel.getProflieData(vToken)!!
            .observe(viewLifecycleOwner, Observer { serviceSetterGetter ->

                if (serviceSetterGetter.iStatusCode == 200) {
                    Log.e(
                        "TAGee",
                        "checkProfile:Profile ---->" + GsonBuilder().setPrettyPrinting().create()
                            .toJson(serviceSetterGetter)
                    )

                    val getProfileData: getProfileResponse.getProfileData = serviceSetterGetter.data

                    PrefManager.getInstance(this.requireActivity())
                        .saveProfileDatasplash(getProfileData)


                    if (getProfileData != null) {

                        binding.tvUsename.setText(getProfileData.vUserName)
                    }
                    Log.e("TAGee", "checkProfile: ---> ${serviceSetterGetter.vMessage}")
                }

            })

    }


    fun initViews() {

        binding.tvSeeallproduct.setOnClickListener {
            val intent = Intent(requireActivity(), ProductDetailActivity::class.java)
            startActivity(intent)
        }
        binding.tvSeeallInquiry.setOnClickListener {
            val intent = Intent(requireActivity(), InquiryListActivity::class.java)
            startActivity(intent)
        }

        binding.ivAddProduct.setOnClickListener {
            val intent = Intent(requireActivity(), AddProductActivity::class.java)
            startActivity(intent)
        }

        binding.tvAll.setOnClickListener {

            binding.tvAll.setTextColor(resources.getColor(R.color.colorWhite))
            binding.tvPending.setTextColor(resources.getColor(R.color.colorBlack))
            binding.tvOngoing.setTextColor(resources.getColor(R.color.colorBlack))
            binding.tvClosed.setTextColor(resources.getColor(R.color.colorBlack))


            binding.tvAll.setBackgroundResource(R.drawable.ic_dark_bg)
            binding.tvPending.setBackgroundResource(R.drawable.ic_light_bg)
            binding.tvOngoing.setBackgroundResource(R.drawable.ic_light_bg)
            binding.tvClosed.setBackgroundResource(R.drawable.ic_light_bg)


        }
        binding.tvPending.setOnClickListener {

            binding.tvAll.setTextColor(resources.getColor(R.color.colorBlack))
            binding.tvPending.setTextColor(resources.getColor(R.color.colorWhite))
            binding.tvOngoing.setTextColor(resources.getColor(R.color.colorBlack))
            binding.tvClosed.setTextColor(resources.getColor(R.color.colorBlack))


            binding.tvAll.setBackgroundResource(R.drawable.ic_light_bg)
            binding.tvPending.setBackgroundResource(R.drawable.ic_dark_bg)
            binding.tvOngoing.setBackgroundResource(R.drawable.ic_light_bg)
            binding.tvClosed.setBackgroundResource(R.drawable.ic_light_bg)


        }
        binding.tvOngoing.setOnClickListener {

            binding.tvAll.setTextColor(resources.getColor(R.color.colorBlack))
            binding.tvPending.setTextColor(resources.getColor(R.color.colorBlack))
            binding.tvOngoing.setTextColor(resources.getColor(R.color.colorWhite))
            binding.tvClosed.setTextColor(resources.getColor(R.color.colorBlack))


            binding.tvAll.setBackgroundResource(R.drawable.ic_light_bg)
            binding.tvPending.setBackgroundResource(R.drawable.ic_light_bg)
            binding.tvOngoing.setBackgroundResource(R.drawable.ic_dark_bg)
            binding.tvClosed.setBackgroundResource(R.drawable.ic_light_bg)


        }
        binding.tvClosed.setOnClickListener {

            binding.tvAll.setTextColor(resources.getColor(R.color.colorBlack))
            binding.tvPending.setTextColor(resources.getColor(R.color.colorBlack))
            binding.tvOngoing.setTextColor(resources.getColor(R.color.colorBlack))
            binding.tvClosed.setTextColor(resources.getColor(R.color.colorWhite))
            binding.tvAll.setBackgroundResource(R.drawable.ic_light_bg)
            binding.tvPending.setBackgroundResource(R.drawable.ic_light_bg)
            binding.tvOngoing.setBackgroundResource(R.drawable.ic_light_bg)
            binding.tvClosed.setBackgroundResource(R.drawable.ic_dark_bg)


        }

        val data = listOf<InquiryModel>(
            InquiryModel("Arman Shekh", "7894651231"),
            InquiryModel("Bhavy Gol", "7894561340"),
            InquiryModel("Aashika Savaliya", "1234567897"),
            InquiryModel("Nirav Kanani", "741410852296"),
        )

        val adapter = InquiryAdapter(data)
        binding.recyclerviewInquiry.adapter = adapter



        productPostListData= ProductPostListData("1")

        productViewModel.getProductList(productPostListData)!!.observe(viewLifecycleOwner, Observer {
            getlist ->

            val adapterProduct = ProductAdapter(requireActivity(),getlist.data)
            binding.recyclerviewProduct.adapter = adapterProduct

        })






    }


}