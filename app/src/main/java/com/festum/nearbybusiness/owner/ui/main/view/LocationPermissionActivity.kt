package com.festum.nearbybusiness.owner.ui.main.view

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.festum.nearbybusiness.owner.R
import com.festum.nearbybusiness.owner.Utils.PrefManager
import com.festum.nearbybusiness.owner.databinding.ActivityLoactionPermissionBinding
import com.festum.nearbybusiness.owner.ui.Model.getProfile.getProfileSplashResponse
import com.festum.nearbybusiness.owner.ui.ViewModel.LoginViewModel
import com.festum.nearbybusiness.owner.ui.main.view.Login.ProfileSetUpActivity
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.gson.GsonBuilder
import com.permissionx.guolindev.PermissionX

class LocationPermissionActivity : AppCompatActivity() {
    lateinit var binding: ActivityLoactionPermissionBinding

    lateinit var loginViewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoactionPermissionBinding.inflate(layoutInflater)
        setContentView(binding.root)
        loginViewModel = ViewModelProvider(this).get(LoginViewModel::class.java)

        binding.txtAllow.setOnClickListener {
            getAllPermission()
        }

    }


    fun getAllPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            PermissionX.init(this)
                .permissions(
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
                .explainReasonBeforeRequest()
                .onExplainRequestReason { scope, deniedList, beforeRequest ->
                    scope.showRequestReasonDialog(
                        deniedList,
                        getString(R.string.permissionx_needs_following_permissions_to_continue),
                        "Allow"
                    )
                }
                .onForwardToSettings { scope, deniedList ->
                    scope.showForwardToSettingsDialog(
                        deniedList,
                        getString(R.string.please_allow_following_permissions_in_settings),
                        "Allow"
                    )
                }
                .request { allGranted, grantedList, deniedList ->
                    if (!allGranted) {
                        Toast.makeText(
                            this,
                            getString(R.string.the_following_permissions_are_denied) + deniedList,
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        getLocation()
                    }
                }
        } else {
            PermissionX.init(this)
                .permissions(
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
                .explainReasonBeforeRequest()
                .onExplainRequestReason { scope, deniedList, beforeRequest ->
                    scope.showRequestReasonDialog(
                        deniedList,
                        getString(R.string.permissionx_needs_following_permissions_to_continue),
                        "Allow"
                    )
                }
                .onForwardToSettings { scope, deniedList ->
                    scope.showForwardToSettingsDialog(
                        deniedList,
                        getString(R.string.please_allow_following_permissions_in_settings),
                        "Allow"
                    )
                }
                .request { allGranted, grantedList, deniedList ->
                    if (!allGranted) {
                        Toast.makeText(
                            this,
                            getString(R.string.the_following_permissions_are_denied) + deniedList,
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        getLocation()
                    }
                }
        }
    }

    private fun getLocation() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
            && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        val fusedLocationClient: FusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(this)
        fusedLocationClient.getLastLocation()
            .addOnSuccessListener(this, object : OnSuccessListener<Location?> {
                override fun onSuccess(location: Location?) {
                    // Got last known location. In some rare situations, this can be null.
                    if (location != null) {
                        val latitude: String = location.latitude.toString()
                        val longitude: String = location.longitude.toString()
                        val isToken = PrefManager.getInstance(applicationContext).getStringPreference("isappToken")


                        checkProfile(isToken)
                        PrefManager.getInstance(applicationContext)
                            .savePreferencesStr("latitude", latitude)
                            .toString()
                        PrefManager.getInstance(applicationContext)
                            .savePreferencesStr("longitude", longitude)
                            .toString()

                        Log.e("TAGee", "latitude: $latitude")
                        Log.e("TAGee", "longitude: $longitude")


                    } else {

                        if (!(getSystemService(LOCATION_SERVICE) as LocationManager).isProviderEnabled(
                                "gps"
                            )
                        ) {
                            turnOnGps()
                        }
                    }
                }
            })
            .addOnFailureListener(this, object : OnFailureListener {
                override fun onFailure(e: Exception) {
                    // Handle failure
                    Toast.makeText(
                        applicationContext,
                        "Failed to get location: " + e.message,
                        Toast.LENGTH_SHORT
                    ).show()
                }
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

                        val intent = Intent(applicationContext, ProfileSetUpActivity::class.java)
                        startActivity(intent)

                    } else {
                        Log.e("TAGeedd", "openact: -->")

                        val intent = Intent(applicationContext, MainActivity::class.java)
                        startActivity(intent)
                        finish()
                    }

                }


            })

    }

    private fun turnOnGps() {
        this.ts_startForResult.launch(Intent("android.settings.LOCATION_SOURCE_SETTINGS"))
    }

    fun isLocationEnabled(): Boolean {
        val locationManager = getSystemService(LOCATION_SERVICE) as LocationManager
        return locationManager != null && locationManager.isProviderEnabled("gps")
    }

    var ts_startForResult = registerForActivityResult<Intent, ActivityResult>(
        ActivityResultContracts.StartActivityForResult(),
        object : ActivityResultCallback<ActivityResult?> {
            override fun onActivityResult(result: ActivityResult?) {
                if (isLocationEnabled()) {

                    return
                }
                Toast.makeText(
                    applicationContext,
                    getResources().getString(R.string.gcl_35),
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
}