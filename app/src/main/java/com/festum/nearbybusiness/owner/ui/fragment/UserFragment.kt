package com.festum.nearbybusiness.owner.ui.fragment

import android.Manifest
import android.app.Activity
import android.content.Context.INPUT_METHOD_SERVICE
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.OnTouchListener
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.RadioButton
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.festum.nearbybusiness.owner.R
import com.festum.nearbybusiness.owner.Utils.PrefManager
import com.festum.nearbybusiness.owner.databinding.FragmentUserBinding
import com.festum.nearbybusiness.owner.ui.Model.Profile.ProfileResponseData
import com.festum.nearbybusiness.owner.ui.Model.ProfilePostData
import com.festum.nearbybusiness.owner.ui.Model.getProfile.getProfileResponse
import com.festum.nearbybusiness.owner.ui.ViewModel.LoginViewModel
import com.festum.nearbybusiness.owner.ui.main.view.MainActivity
import com.google.gson.GsonBuilder
import java.io.ByteArrayOutputStream
import java.io.File


class UserFragment : Fragment() {
    private lateinit var binding: FragmentUserBinding
    lateinit var loginViewModel: LoginViewModel
    lateinit var isToken: String
    var isedit: Boolean = false
    lateinit var userid: String

    var imageurl: String = ""
    lateinit var imageuri: Uri
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentUserBinding.inflate(inflater, container, false)

        loginViewModel = ViewModelProvider(requireActivity()).get(LoginViewModel::class.java)
        isToken = PrefManager.getInstance(requireActivity()).getStringPreference("isappToken")

        initViews(isToken)
        return binding.root
    }


    fun changeDataofViews() {

        binding.edUserName.isFocusableInTouchMode = false
        binding.edUserName.isLongClickable = false
        binding.edUserName.isClickable = false
        binding.edUserName.clearFocus()
        binding.edUserAddress.clearFocus()

        binding.edUserAddress.isFocusableInTouchMode = false
        binding.edUserAddress.isLongClickable = false
        binding.edUserAddress.isClickable = false

        binding.edUserNumber.isFocusableInTouchMode = false
        binding.edUserNumber.isLongClickable = false
        binding.edUserNumber.isClickable = false


        binding.ivEditImage.isClickable = false
        binding.radioButtonMale.isEnabled = false
        binding.radioButtonFemale.isEnabled = false
        binding.radioButtonOther.isEnabled = false
        binding.tvSubmitProduct.visibility = View.GONE

        binding.ivEdit.setOnClickListener {

            isedit = true


            binding.edUserNumber.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    s?.let {
                        if (it.length > 10) {
                            binding.edUserNumber.setText(it.subSequence(0, 10))
                            binding.edUserNumber.setSelection(10)
                        }
                    }
                }

                override fun afterTextChanged(s: Editable?) {}
            })


            binding.edUserName.isFocusableInTouchMode = true
            binding.edUserName.isLongClickable = true
            binding.edUserName.isClickable = true
            binding.edUserName.requestFocus()
            binding.edUserName.setSelection(binding.edUserName.length())


            val imm = activity?.getSystemService((INPUT_METHOD_SERVICE)) as InputMethodManager?
            imm!!.showSoftInput(
                binding.edUserName,
                InputMethodManager.SHOW_IMPLICIT
            )

            binding.edUserAddress.isFocusableInTouchMode = true
            binding.edUserAddress.isLongClickable = true
            binding.edUserAddress.isClickable = true



            binding.ivEditImage.isClickable = true
            binding.radioButtonMale.isEnabled = true
            binding.radioButtonFemale.isEnabled = true
            binding.radioButtonOther.isEnabled = true
            binding.tvSubmitProduct.visibility = View.VISIBLE

        }
        binding.tvSubmitProduct.setOnClickListener {
            if (isedit) {


                val radio: RadioButton =
                    requireView().findViewById(this.binding.radioGroup.checkedRadioButtonId)
                updateProfile(radio.text.toString())
                Log.e("TAGee", "isedit true: ")
            } else {
                Log.e("TAGee", "isedit false: ")

            }


        }


    }


    fun initViews(isToken: String) {
        userid = PrefManager.getInstance(this.requireContext()).getStringPreference("userId")

        checkPermissions()

        binding!!.ivEditImage.setOnClickListener {
            showImageSelectionDialog()
        }

        changeDataofViews()


        getProfiledata(isToken)
    }

    fun updateProfile(gender: String) {
        var latitudestr: String =
            PrefManager.getInstance(this.requireActivity()).getStringPreference("latitude")
        var longitudestr: String =
            PrefManager.getInstance(this.requireActivity()).getStringPreference("longitude")

        binding.progressbar.visibility = VISIBLE
        binding.tvSubmitProduct.visibility = GONE

        Log.e("TAGee", "updateProfile:gender   $gender")
        var vToken: String =
            PrefManager.getInstance(this.requireActivity()).getStringPreference("isappToken")

        val profilePostData = ProfilePostData(
            userid,
            imageurl,
            binding.edUserName.text.toString(),
            binding.edUserNumber.text.toString(),
            latitudestr,
            longitudestr,
            binding.edUserAddress.text.toString(),
            gender
        )



        loginViewModel.updateProfile(vToken, profilePostData)!!
            .observe(viewLifecycleOwner, Observer { serviceSetterGetter ->
                Log.e(
                    "TAGee",
                    "onResponse:Profile ---->" + GsonBuilder().setPrettyPrinting().create()
                        .toJson(serviceSetterGetter)
                )

                val getProfileData: ProfileResponseData.ProfileDetailData = serviceSetterGetter.data


                PrefManager.getInstance(this.requireActivity()).saveProfileData(getProfileData)


                binding.progressbar.visibility = GONE
                binding.tvSubmitProduct.visibility = VISIBLE

                Toast.makeText(
                    this.requireActivity(),
                    "Update Profile Successfully",
                    Toast.LENGTH_SHORT
                ).show()
                isedit = false
                changeDataofViews()
                Log.e("TAGee", "callAPIImagetourl: imageurl---> ${serviceSetterGetter.vMessage}")


            })

    }

    fun getProfiledata(vToken: String) {

        loginViewModel.getProflieData(vToken)!!
            .observe(viewLifecycleOwner, Observer { profiledata ->

                if (profiledata.iStatusCode == 200) {
                    Log.e(
                        "TAGee",
                        "UserFragment :Profile ---->" + GsonBuilder().setPrettyPrinting().create()
                            .toJson(profiledata)
                    )

                    val getProfileData: getProfileResponse.getProfileData = profiledata.data

                    PrefManager.getInstance(requireActivity()).saveProfileDatasplash(getProfileData)


                    if (getProfileData != null) {

                        imageurl = getProfileData.vImage


                        when (getProfileData.vGender) {
                            "Male" -> binding.radioButtonMale.isChecked = true
                            "Female" -> binding.radioButtonFemale.isChecked = true
                            "Other" -> binding.radioButtonOther.isChecked = true
                        }

                        Glide.with(requireActivity()).load(getProfileData.vImage)
                            .placeholder(R.drawable.ic_plceholder).error(R.drawable.ic_plceholder)
                            .into(binding.icUserProfile)


                        binding.edUserName.setText(getProfileData.vUserName)
                        binding.edUserNumber.setText(getProfileData.vMobile)
                        binding.edUserAddress.setText(getProfileData.vAddress)


                    }
                    Log.e("TAGee", "checkProfile: ---> ${profiledata.vMessage}")
                }

            })

    }


    private fun showImageSelectionDialog() {
        val options = arrayOf("Camera", "Gallery")
        val builder = android.app.AlertDialog.Builder(this.activity)
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
                binding.icUserProfile.setImageBitmap(imageBitmap)
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
                        binding?.icUserProfile?.setImageURI(uri)

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


    fun getImageUri(inImage: Bitmap): Uri? {
        val bytes = ByteArrayOutputStream()
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
        val path =
            MediaStore.Images.Media.insertImage(
                this.activity?.contentResolver,
                inImage,
                "Title",
                null
            )
        return Uri.parse(path)
    }

    fun getRealPathFromURI(contentUri: Uri): String? {
        val projection = arrayOf(MediaStore.Images.Media.DATA)
        val cursor = this.activity?.contentResolver?.query(contentUri, projection, null, null, null)
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
                this.requireActivity(),
                Manifest.permission.CAMERA
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            permissions.add(Manifest.permission.CAMERA)
        }
        if (ContextCompat.checkSelfPermission(
                this.requireActivity(),
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            permissions.add(Manifest.permission.READ_EXTERNAL_STORAGE)
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(
                    this.requireActivity(),
                    Manifest.permission.READ_MEDIA_IMAGES
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                permissions.add(Manifest.permission.READ_MEDIA_IMAGES)
            }
            if (ContextCompat.checkSelfPermission(
                    this.requireActivity(),
                    Manifest.permission.READ_MEDIA_VIDEO
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                permissions.add(Manifest.permission.READ_MEDIA_VIDEO)
            }
        } else {
            if (ContextCompat.checkSelfPermission(
                    this.requireActivity(),
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                permissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
            }
        }

        if (permissions.isNotEmpty()) {
            ActivityCompat.requestPermissions(this.requireActivity(), permissions.toTypedArray(), 0)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this.requireActivity(), "Permission granted", Toast.LENGTH_SHORT).show()
        } else {
//            Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show()
        }
    }

}