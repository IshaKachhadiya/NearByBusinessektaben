package com.festum.nearbybusiness.owner.ui.main.view

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.festum.nearbybusiness.owner.R
import com.festum.nearbybusiness.owner.Utils.PrefManager
import com.festum.nearbybusiness.owner.databinding.ActivityMainBinding
import com.festum.nearbybusiness.owner.ui.Model.Profile.ProfileResponseData
import com.festum.nearbybusiness.owner.ui.Model.getProfile.getProfileResponse
import com.festum.nearbybusiness.owner.ui.ViewModel.LoginViewModel
import com.festum.nearbybusiness.owner.ui.fragment.HomeFragment
import com.festum.nearbybusiness.owner.ui.fragment.SettingFragment
import com.festum.nearbybusiness.owner.ui.fragment.UserFragment
import com.festum.nearbybusiness.owner.ui.main.view.Login.ProfileSetUpActivity
import com.google.gson.GsonBuilder


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    lateinit var loginViewModel: LoginViewModel
    lateinit var isToken: String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        loginViewModel = ViewModelProvider(this).get(LoginViewModel::class.java)
        isToken = PrefManager.getInstance(this).getStringPreference("isappToken")

        initViews()


    }

    fun getProfiledata(vToken: String) {

        loginViewModel.getProflieData(vToken)!!
            .observe(this, Observer { serviceSetterGetter ->

                if (serviceSetterGetter.iStatusCode == 200) {
                    Log.e(
                        "TAGee",
                        "checkProfile:Profile ---->" + GsonBuilder().setPrettyPrinting().create()
                            .toJson(serviceSetterGetter)
                    )

                    val getProfileData: getProfileResponse.getProfileData = serviceSetterGetter.data

                    PrefManager.getInstance(applicationContext)
                        .saveProfileDatasplash(getProfileData)


                    if (getProfileData != null) {
                        Glide.with(applicationContext).load(getProfileData.vImage)
                            .into(binding.ivUserProfile)
                        binding.tvUser.setText(getProfileData.vUserName)
                    }
                    Log.e("TAGee", "checkProfile: ---> ${serviceSetterGetter.vMessage}")
                }

            })

    }


    fun initViews() {

        getProfiledata(isToken)

        supportFragmentManager.beginTransaction().replace(R.id.main_frame, HomeFragment())
            .commit()
        binding.ivHome.setImageResource(R.drawable.ic_home_select)
        binding.ivSetting.setImageResource(R.drawable.ic_setting_unselect)
        binding.tvUser.setTextColor(getResources().getColor(R.color.colorAppLight))
        binding.ivAdminIndicator.visibility = View.INVISIBLE


        binding.ivHome.setOnClickListener {
            supportFragmentManager.beginTransaction().replace(R.id.main_frame, HomeFragment())
                .commit()
            binding.ivHome.setImageResource(R.drawable.ic_home_select)
            binding.ivSetting.setImageResource(R.drawable.ic_setting_unselect)
            binding.tvUser.setTextColor(getResources().getColor(R.color.colorAppLight))
            binding.ivAdminIndicator.visibility = View.INVISIBLE

        }
        binding.ivSetting.setOnClickListener {
            supportFragmentManager.beginTransaction().replace(R.id.main_frame, SettingFragment())
                .commit()
            binding.ivHome.setImageResource(R.drawable.ic_home_unselect)
            binding.ivSetting.setImageResource(R.drawable.ic_setting_select)
            binding.tvUser.setTextColor(getResources().getColor(R.color.colorAppLight))
            binding.ivAdminIndicator.visibility = View.INVISIBLE
        }
        binding.ivUser.setOnClickListener {
            supportFragmentManager.beginTransaction().replace(R.id.main_frame, UserFragment())
                .commit()
            binding.ivHome.setImageResource(R.drawable.ic_home_unselect)
            binding.ivSetting.setImageResource(R.drawable.ic_setting_unselect)
            binding.tvUser.setTextColor(getResources().getColor(R.color.colorApp))
            binding.ivAdminIndicator.visibility = View.VISIBLE
            binding.ivAdminIndicator.setImageResource(R.drawable.ic_line)

        }

        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                val currentFragment = supportFragmentManager.findFragmentById(R.id.main_frame)
                if (currentFragment is HomeFragment) {
                    finish()
                } else {
                    binding.ivHome.setImageResource(R.drawable.ic_home_select)
                    binding.ivSetting.setImageResource(R.drawable.ic_setting_unselect)
                    binding.tvUser.setTextColor(getResources().getColor(R.color.colorAppLight))
                    binding.ivAdminIndicator.visibility = View.INVISIBLE
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.main_frame, HomeFragment())
                        .commit()
                }
            }
        })


    }




}