package com.festum.nearbybusiness.owner.ui.main.view

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.festum.nearbybusiness.owner.Utils.PrefManager
import com.festum.nearbybusiness.owner.databinding.ActivitySplashBinding
import com.festum.nearbybusiness.owner.ui.Model.getProfile.getProfileSplashResponse
import com.festum.nearbybusiness.owner.ui.ViewModel.LoginViewModel
import com.festum.nearbybusiness.owner.ui.main.view.Login.LoginMobNumberActivity
import com.festum.nearbybusiness.owner.ui.main.view.Login.ProfileSetUpActivity
import com.google.gson.GsonBuilder

class SplashActivity : AppCompatActivity() {

    lateinit var activity: Activity
    lateinit var binding: ActivitySplashBinding
    lateinit var loginViewModel: LoginViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)
        activity = this
        val isToken = PrefManager.getInstance(this).getStringPreference("isappToken")
        val isIntro = PrefManager.getInstance(this).loadSavedPreferences("isOnBondingShow", false)

        loginViewModel = ViewModelProvider(this).get(LoginViewModel::class.java)


        Log.e("TAGee", "onCreate: isToken--- > $isToken")

        if (!isIntro) {
            val intent = Intent(applicationContext, OnboardingActivity::class.java)
            startActivity(intent)
            finish()
        } else if (isToken.isBlank() && isToken.isEmpty()) {
            val intent = Intent(applicationContext, LoginMobNumberActivity::class.java)
            startActivity(intent)
            finish()
        } else {
            checkProfile(isToken)
        }


    }

    fun isLocationPermissionGranted(context: Context): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        } else {
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        }
    }

    fun checkProfile(vToken: String) {
        Log.e("TAGeedd", "checkProfile: -->")


        loginViewModel.getProflieSplashData(vToken)!!
            .observe(this, Observer { serviceSetterGetter ->

                if (serviceSetterGetter.iStatusCode == 200) {

                    Log.e(
                        "TAGee",
                        "checkProfile:Profile ---->" + GsonBuilder().setPrettyPrinting().create()
                            .toJson(serviceSetterGetter)
                    )

                    val getProfileData: getProfileSplashResponse.getProfileSplashData =
                        serviceSetterGetter.data

                    /*   PrefManager.getInstance(applicationContext)
                           .saveProfileDatasplash(getProfileData)
   */

                    if (getProfileData.vUserName.isEmpty() || getProfileData.vUserName.isBlank()
                        || getProfileData.vAddress.isEmpty() || getProfileData.vAddress.isBlank() ||
                        getProfileData.vUserLat.isEmpty() || getProfileData.vUserLat.isBlank()
                        || getProfileData.vUserLong.isEmpty() || getProfileData.vUserLong.isBlank()
                        || getProfileData.vMobile.isEmpty() || getProfileData.vMobile.isBlank() ||
                        getProfileData.vImage.isEmpty() || getProfileData.vImage.isBlank()
                    ) {

                        if (!isLocationPermissionGranted(applicationContext)) {
                            val intent =
                                Intent(applicationContext, LocationPermissionActivity::class.java)
                            startActivity(intent)
                            finish()
                        } else {
                            val intent =
                                Intent(applicationContext, ProfileSetUpActivity::class.java)
                            startActivity(intent)
                        }


                    } else {
                        Log.e("TAGeedd", "openact: -->")

                        val intent = Intent(applicationContext, MainActivity::class.java)
                        startActivity(intent)
                        activity.finish()
                    }

                }


            })

    }
}