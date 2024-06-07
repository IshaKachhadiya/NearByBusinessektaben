package com.festum.nearbybusiness.owner.ui.main.view.Login

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.TextView.OnEditorActionListener
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.festum.nearbybusiness.owner.R
import com.festum.nearbybusiness.owner.databinding.ActivityLoginMobNumberBinding
import com.festum.nearbybusiness.owner.ui.Model.Country
import com.festum.nearbybusiness.owner.ui.Model.PostData
import com.festum.nearbybusiness.owner.ui.ViewModel.LoginViewModel
import com.festum.nearbybusiness.owner.ui.adapter.CountryAdapter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.IOException


class LoginMobNumberActivity : AppCompatActivity() {

    lateinit var binding: ActivityLoginMobNumberBinding
    lateinit var loginViewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginMobNumberBinding.inflate(layoutInflater)
        setContentView(binding.root)

        loginViewModel = ViewModelProvider(this).get(LoginViewModel::class.java)


        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                finish()
            }
        })

        init()


    }

    fun init() {
        binding.txtCountry.setOnClickListener {
            showCountryDialog()
        }




        binding.txtMob.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                s?.let {
                    if (it.length > 10) {
                        binding.txtMob.setText(it.subSequence(0, 10))
                        binding.txtMob.setSelection(10)
                    } else if (it.length == 0) {
                        binding.progressbar.visibility = View.GONE
                        binding.txtGetOtp.visibility = View.VISIBLE
                    }
                }
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        binding.txtMob.setOnEditorActionListener(OnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {

                val inputText = binding.txtMob.text.toString()

                if (inputText.isNotEmpty() && inputText.all { it.isDigit() }) {
                    if (inputText.length < 10) {
                        Toast.makeText(
                            this,
                            "Phone number must be at least 10 digits long",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {


                        val postData = PostData(inputText, "admin", "1.1", "ios", "14.1", "pro")
                        callOtpApi(postData, inputText)

                    }
                } else {

                    Toast.makeText(this, "Please enter a valid phone number", Toast.LENGTH_SHORT)
                        .show()
                }



                true
            } else false
        })


        binding.txtGetOtp.setOnClickListener {



            val inputText = binding.txtMob.text.toString()

            if (inputText.isNotEmpty() && inputText.all { it.isDigit() }) {
                if (inputText.length < 10) {
                    Toast.makeText(
                        this,
                        "Phone number must be at least 10 digits long",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    binding.progressbar.visibility = View.VISIBLE
                    binding.txtGetOtp.visibility = View.GONE


                    val postData = PostData(inputText, "admin", "1.1", "ios", "14.1", "pro")
                    callOtpApi(postData, inputText)

                }
            } else {

                Toast.makeText(this, "Please enter a valid phone number", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun callOtpApi(postData: PostData, inputText: String) {

        loginViewModel.getOTP(postData)!!
            .observe(this, Observer { serviceSetterGetter ->
                binding.progressbar.visibility = View.GONE
                binding.txtGetOtp.visibility = View.VISIBLE

                val phoneOTP = serviceSetterGetter.data.vPhoneOtp
                Toast.makeText(this, "${serviceSetterGetter.vMessage}", Toast.LENGTH_SHORT).show()

                val intent = Intent(applicationContext, VerificationCodeActivity::class.java)
                intent.putExtra("phoneOtp", "$phoneOTP")
                intent.putExtra("phoneNum", "$inputText")
                startActivity(intent)
                finish()
            })

    }

    private fun showCountryDialog() {
        val dialog = Dialog(this, R.style.CustomDialogTheme)
        val view = LayoutInflater.from(this).inflate(R.layout.dialog_country_list, null)
        dialog.setContentView(view)

        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        val countries = loadCountriesFromAssets()
        val adapter = CountryAdapter(countries) { country ->
            binding?.txtCountry?.text = "(${country.dial_code}) ${country.name}"
            dialog.dismiss()
        }

        recyclerView.adapter = adapter
        dialog.show()
    }

    private fun loadCountriesFromAssets(): List<Country> {
        val json: String?
        try {
            val inputStream = assets.open("countrycodes.json")
            json = inputStream.bufferedReader().use { it.readText() }
        } catch (ioException: IOException) {
            ioException.printStackTrace()
            return emptyList()
        }

        val listCountryType = object : TypeToken<List<Country>>() {}.type
        return Gson().fromJson(json, listCountryType)
    }


}