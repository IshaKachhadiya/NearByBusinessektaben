package com.festum.nearbybusiness.owner.ui.main.view.Login

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.RadioButton
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.festum.nearbybusiness.owner.Utils.PrefManager
import com.festum.nearbybusiness.owner.databinding.ActivityProfileSetUpBinding
import com.festum.nearbybusiness.owner.ui.Model.Profile.ProfileResponseData
import com.festum.nearbybusiness.owner.ui.Model.ProfilePostData
import com.festum.nearbybusiness.owner.ui.ViewModel.LoginViewModel
import com.festum.nearbybusiness.owner.ui.main.view.MainActivity
import com.google.gson.GsonBuilder
import java.io.ByteArrayOutputStream
import java.io.File

class ProfileSetUpActivity : AppCompatActivity() {


    lateinit var binding: ActivityProfileSetUpBinding
    lateinit var loginViewModel: LoginViewModel
    var imageurl: String = ""
    lateinit var imageuri: Uri
    lateinit var userid: String
    lateinit var usernumber: String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileSetUpBinding.inflate(layoutInflater)
        setContentView(binding.root)
        loginViewModel = ViewModelProvider(this).get(LoginViewModel::class.java)


        usernumber = PrefManager.getInstance(this).getStringPreference("userPhoneNumber")

        if (!usernumber.isEmpty() || !usernumber.isBlank()) {
            binding.edMobileNumber.setText(usernumber)
        }


        userid = PrefManager.getInstance(this).getStringPreference("userId")

        binding!!.ivProfile.setOnClickListener {
            showImageSelectionDialog()
        }
        checkPermissions()

        binding.txtSubmit.setOnClickListener {
            checkValidation()

        }

        binding.ivBack.setOnClickListener {

            onBackPressedDispatcher.onBackPressed()

        }
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                finish()
            }
        })

    }


    fun updateProfile(gender: String) {

        var latitudestr: String =
            PrefManager.getInstance(applicationContext).getStringPreference("latitude")
        var longitudestr: String =
            PrefManager.getInstance(applicationContext).getStringPreference("longitude")


        Log.e("TAGee", "updateProfile:gender   $gender")
        var vToken: String = PrefManager.getInstance(this).getStringPreference("isappToken")

        val profilePostData = ProfilePostData(
            userid,
            imageurl,
            binding.edUsername.text.toString(),
            binding.edMobileNumber.text.toString(),
            latitudestr,
            longitudestr,
            binding.edAddress.text.toString(),
            gender
        )



        loginViewModel.updateProfile(vToken, profilePostData)!!
            .observe(this, Observer { serviceSetterGetter ->
                Log.e(
                    "TAGee",
                    "onResponse:Profile ---->" + GsonBuilder().setPrettyPrinting().create()
                        .toJson(serviceSetterGetter)
                )

                val getProfileData: ProfileResponseData.ProfileDetailData = serviceSetterGetter.data


                PrefManager.getInstance(applicationContext).saveProfileData(getProfileData)


                val intent = Intent(applicationContext, MainActivity::class.java)
                startActivity(intent)

                Log.e("TAGee", "callAPIImagetourl: imageurl---> ${serviceSetterGetter.vMessage}")


            })

    }


    fun checkValidation() {
        var id: Int = binding.radioGroup.checkedRadioButtonId
        val inputText = binding.edMobileNumber.text.toString()

        when {
            imageurl.isBlank() -> {
                Toast.makeText(
                    applicationContext,
                    "Please select Profile Image",
                    Toast.LENGTH_SHORT
                ).show()
            }

            binding.edUsername.text.toString().isBlank() -> {
                Toast.makeText(
                    applicationContext,
                    "Please Enter valid user name",
                    Toast.LENGTH_SHORT
                ).show()
            }

            inputText.isBlank() || inputText.length != 10 -> {
                Toast.makeText(this, "Please enter a valid phone number", Toast.LENGTH_SHORT).show()
            }

            binding.edAddress.text.toString().isBlank() -> {
                Toast.makeText(applicationContext, "Please Enter valid Address", Toast.LENGTH_SHORT)
                    .show()
            }

            id == -1 -> { // If no radio button is selected
                Toast.makeText(
                    applicationContext,
                    "Please select an option from the radio group",
                    Toast.LENGTH_SHORT
                ).show()
            }

            else -> {
                // All validations passed
                val radio: RadioButton = findViewById(id)

                updateProfile(radio.text.toString())

                Toast.makeText(
                    applicationContext,
                    "Profile Created Successfully",
                    Toast.LENGTH_SHORT
                ).show()
                // Proceed with further processing
            }
        }
    }


    private fun showImageSelectionDialog() {
        val options = arrayOf("Camera", "Gallery")
        val builder = android.app.AlertDialog.Builder(this)
        builder.setTitle("Select Image Source")
        builder.setItems(options) { dialog, which ->
            when (which) {
                0 -> openCamera()
                1 -> openGallery()
            }
        }
        builder.show()
    }

    var resultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                // There are no request codes
                val data: Intent? = result.data
                val imageBitmap = data?.extras?.get("data") as Bitmap
                binding.ivProfile.setImageBitmap(imageBitmap)
                imageuri = getImageUri(imageBitmap)!!
                val vProfileImage = getRealPathFromURI(imageuri)
                val imageFile: File = File(vProfileImage)
                callAPIImagetourl(imageFile)
            }
        }

    private fun openCamera() {
        val intent1 = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        resultLauncher.launch(intent1)
    }


    var galleryluancher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                // There are no request codes
                val data: Intent? = result.data
                val selectedUri: Uri? = data?.data
                selectedUri?.let { uri ->
                    // Check if it's an image or video
                    if (uri.toString().contains("image")) {
                        // Handle image
                        binding?.ivProfile?.setImageURI(uri)

                        val vProfileImage = getRealPathFromURI(uri)
                        val imageFile: File = File(vProfileImage)

                        Log.e("TAGee", "onActivityResult: imageFile---- $imageFile")

                        callAPIImagetourl(imageFile)


                    }
                }
            }
        }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        intent.type = "image/* video/*"
        galleryluancher.launch(intent)
    }

    /*
        override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
            super.onActivityResult(requestCode, resultCode, data)
            if (resultCode == Activity.RESULT_OK) {
                when (requestCode) {
                    cameraRequestCode -> {
                        val imageBitmap = data?.extras?.get("data") as Bitmap
                        binding.ivProfile.setImageBitmap(imageBitmap)
                        imageuri = getImageUri(imageBitmap)!!
                        val vProfileImage = getRealPathFromURI(imageuri)
                        val imageFile: File = File(vProfileImage)
                        callAPIImagetourl(imageFile)
                    }

                    galleryRequestCode -> {
                        val selectedUri: Uri? = data?.data
                        selectedUri?.let { uri ->
                            // Check if it's an image or video
                            if (uri.toString().contains("image")) {
                                // Handle image
                                binding?.ivProfile?.setImageURI(uri)

                                val vProfileImage = getRealPathFromURI(uri)
                                val imageFile: File = File(vProfileImage)

                                Log.e("TAGee", "onActivityResult: imageFile---- $imageFile")

                                callAPIImagetourl(imageFile)


                            }
                        }
                    }
                }
            }
        }
    */

    fun getImageUri(inImage: Bitmap): Uri? {
        val bytes = ByteArrayOutputStream()
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
        val path =
            MediaStore.Images.Media.insertImage(
                applicationContext.contentResolver,
                inImage,
                "Title",
                null
            )
        return Uri.parse(path)
    }

    fun getRealPathFromURI(contentUri: Uri): String? {
        val projection = arrayOf(MediaStore.Images.Media.DATA)
        val cursor = contentResolver.query(contentUri, projection, null, null, null)
        return if (cursor != null) {
            val columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
            cursor.moveToFirst()
            val filePath = cursor.getString(columnIndex)
            cursor.close()
            filePath
        } else {
            contentUri.path
        }
    }


    fun callAPIImagetourl(file: File) {

        val imgpref = "http://192.168.1.70:7600/"

        loginViewModel.imagetoURL(file)!!
            .observe(this, Observer { urldata ->

                imageurl = imgpref + urldata.data.image

                Log.e("TAGee", "callAPIImagetourl: imageurl---> $imageurl")

            })


    }

    private fun checkPermissions() {
        val permissions = mutableListOf<String>()
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.CAMERA
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            permissions.add(Manifest.permission.CAMERA)
        }
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            permissions.add(Manifest.permission.READ_EXTERNAL_STORAGE)
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.READ_MEDIA_IMAGES
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                permissions.add(Manifest.permission.READ_MEDIA_IMAGES)
            }
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.READ_MEDIA_VIDEO
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                permissions.add(Manifest.permission.READ_MEDIA_VIDEO)
            }
        } else {
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                permissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
            }
        }

        if (permissions.isNotEmpty()) {
            ActivityCompat.requestPermissions(this, permissions.toTypedArray(), 0)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "Permission granted", Toast.LENGTH_SHORT).show()
        } else {
//            Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show()
        }
    }

}