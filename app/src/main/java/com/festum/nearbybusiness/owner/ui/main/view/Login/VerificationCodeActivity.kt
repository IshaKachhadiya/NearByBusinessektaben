package com.festum.nearbybusiness.owner.ui.main.view.Login

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.CountDownTimer
import android.text.Editable
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.festum.nearbybusiness.owner.Utils.PrefManager
import com.festum.nearbybusiness.owner.databinding.ActivityVerificationCodeBinding
import com.festum.nearbybusiness.owner.ui.Model.PostData
import com.festum.nearbybusiness.owner.ui.Model.VerifyPostData
import com.festum.nearbybusiness.owner.ui.Model.getProfile.getProfileSplashResponse
import com.festum.nearbybusiness.owner.ui.ViewModel.LoginViewModel
import com.festum.nearbybusiness.owner.ui.main.view.LocationPermissionActivity
import com.festum.nearbybusiness.owner.ui.main.view.MainActivity
import com.google.gson.GsonBuilder


class VerificationCodeActivity : AppCompatActivity() {

    lateinit var binding: ActivityVerificationCodeBinding
    lateinit var phonenum: String
    lateinit var phoneOTP: String
    lateinit var loginViewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVerificationCodeBinding.inflate(layoutInflater)
        setContentView(binding!!.root)

        phoneOTP = intent.getStringExtra("phoneOtp").toString()
        phonenum = intent.getStringExtra("phoneNum").toString()

        loginViewModel = ViewModelProvider(this).get(LoginViewModel::class.java)


        initviews(phonenum)
        countTimer()
    }

    fun countTimer() {

        binding.tvResendOtp.isEnabled = false
        binding.llSendAgain.alpha = 0.7f
        binding.llSendAgain.isEnabled = false
        binding.llSendAgain.visibility = View.INVISIBLE

        object : CountDownTimer(20000, 1000) {

            override fun onTick(millisUntilFinished: Long) {
                binding.tvResendOtp.setText("Resend OTP in " + millisUntilFinished / 1000 + " Seconds")
            }

            override fun onFinish() {
                binding.llSendAgain.visibility = View.VISIBLE


                binding.llSendAgain.alpha = 1f
                binding.tvResendOtp.isEnabled = true
                binding.llSendAgain.isEnabled = true
                binding.tvResendOtp.setText("Resend OTP in 20 Seconds")


                binding.llSendAgain.setOnClickListener {

                    binding.llSendAgain.visibility = View.INVISIBLE
                    object : CountDownTimer(20000, 1000) {

                        override fun onTick(millisUntilFinished: Long) {
                            binding.tvResendOtp.setText("Resend OTP in " + millisUntilFinished / 1000 + " Seconds")

                        }

                        override fun onFinish() {
                            binding.llSendAgain.visibility = View.VISIBLE


                            binding.llSendAgain.alpha = 1f
                            binding.tvResendOtp.isEnabled = true
                            binding.llSendAgain.isEnabled = true
                            binding.tvResendOtp.setText("Resend OTP in 20 Seconds")


                        }
                    }.start()
                    Log.e("TAGee", "callOtpApiAgain: eee ===>  ")

                    callOtpApiAgain(phonenum)

                }


            }
        }.start()


    }


    fun initviews(phonenum: String) {

        binding.llNumberChange.setOnClickListener {
            startActivity(Intent(applicationContext, LoginMobNumberActivity::class.java))
            finish()
        }
        binding.llSendAgain.setOnClickListener {
            Log.e("TAGee", "callOtpApiAgain: ddd ===>  ")

            callOtpApiAgain(phonenum)
        }


        var txt = binding!!.pinView.text


        val pinText = Editable.Factory.getInstance().newEditable(phoneOTP)
        binding.pinView.text = pinText



        binding.tvPhoneNum.text = formatPhoneNumber(phonenum, "+ 91")

        binding!!.txtVerify.setOnClickListener {

            if (binding.pinView.text?.isEmpty() == true) {
                Toast.makeText(this, "isempty", Toast.LENGTH_SHORT).show()

            } else {
                verifyotpdata()


            }

        }
    }


    fun formatPhoneNumber(phoneNumber: String, countryCode: String = "+91"): String {

        if (phoneNumber.length != 10) {
            throw IllegalArgumentException("Phone number must be 10 digits long")
        }

        val firstPart = phoneNumber.substring(0, 5)
        val maskedPart = "*****"
        return "$countryCode $firstPart $maskedPart"
    }


    fun callOtpApiAgain(phonenum: String) {

        Log.e("TAGee", "callOtpApiAgain:===>  ")
        val postData = PostData(phonenum, "admin", "1.1", "ios", "14.1", "pro")

        loginViewModel.getOTP(postData)!!
            .observe(this, Observer { serviceSetterGetter ->


                val phoneOTPvalue = serviceSetterGetter.data.vPhoneOtp


                phoneOTP = phoneOTPvalue

                val pinText = Editable.Factory.getInstance().newEditable(phoneOTPvalue)
                binding.pinView.text = pinText

                /*
                                Toast.makeText(this, "${serviceSetterGetter.data.vPhoneOtp}", Toast.LENGTH_SHORT)
                                    .show()
                */

                verifyotpdata()

            })

    }

    fun verifyotpdata() {

        val verifyPostData = VerifyPostData(phonenum, phoneOTP, "admin")
        loginViewModel.verifyOTP(verifyPostData)!!
            .observe(this, Observer { verifydata ->

                val phoneOTPvalue = verifydata.data.vMobileOtp


                phoneOTP = phoneOTPvalue
                val pinText = Editable.Factory.getInstance().newEditable(phoneOTPvalue)
                binding.pinView.text = pinText

                Toast.makeText(this, "${verifydata.data.vMobileOtp}", Toast.LENGTH_SHORT)
                    .show()

                PrefManager.getInstance(this)
                    .savePreferencesStr("isappToken", verifydata.data.vLoginToken)
                PrefManager.getInstance(this)
                    .savePreferencesStr("userPhoneNumber", verifydata.data.vMobile)
                PrefManager.getInstance(this).savePreferencesStr("userId", verifydata.data._id)

                checkProfile(verifydata.data.vLoginToken)

               /* if (!isLocationPermissionGranted(applicationContext)) {
                    val intent = Intent(applicationContext, LocationPermissionActivity::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    val intent = Intent(applicationContext, ProfileSetUpActivity::class.java)
                    startActivity(intent)
                }*/

            })


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
                        finish()
                    }

                }


            })

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

}