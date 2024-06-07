package com.festum.nearbybusiness.owner.ui.main.view

import android.R.attr.delay
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.festum.nearbybusiness.owner.databinding.ActivityAddProductBinding
import com.festum.nearbybusiness.owner.ui.Model.Image.ImageUrlResponse
import com.festum.nearbybusiness.owner.ui.Model.Product.ProductPostData
import com.festum.nearbybusiness.owner.ui.ViewModel.LoginViewModel
import com.festum.nearbybusiness.owner.ui.ViewModel.ProductViewModel
import com.festum.nearbybusiness.owner.ui.adapter.ProductImageAdapter
import com.google.gson.GsonBuilder
import java.io.File
import java.util.Timer
import java.util.TimerTask


class AddProductActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddProductBinding
    lateinit var productViewModel: ProductViewModel
    lateinit var productPostData: ProductPostData
    lateinit var loginViewModel: LoginViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddProductBinding.inflate(layoutInflater)
        setContentView(binding.root)

        productViewModel = ViewModelProvider(this).get(ProductViewModel::class.java)
        loginViewModel = ViewModelProvider(this).get(LoginViewModel::class.java)

        binding.recyclerviewProductImages.visibility = View.GONE
        binding.relImg.visibility = View.VISIBLE
        initview()
    }

    fun addProduct() {

        var name: String = binding.icEdittextProductName.text.toString()
        var details = binding.icEdittextProductDetails.text.toString()
        var price = binding.icEdittextProductPrice.text.toString()


        if (name.isEmpty() || name.isBlank()) {
            Toast.makeText(this, "Please enter a product name", Toast.LENGTH_SHORT).show()

        } else if (details.isEmpty() || details.isBlank()) {
            Toast.makeText(this, "Please enter a product details", Toast.LENGTH_SHORT).show()

        } else if (price.isEmpty() || price.isBlank()) {
            Toast.makeText(this, "Please enter a product price", Toast.LENGTH_SHORT).show()

        } else if (fileNameList.isEmpty() || fileNameList.size == 0) {
            Toast.makeText(this, "Please select product image", Toast.LENGTH_SHORT).show()

        } else {

            productPostData = ProductPostData("1", name, details, price, fileNameList)
            Log.e("PhotoPicker", "callAPIImagetourl: File---> ${fileNameList.size}")

            productViewModel.addProduct(productPostData)?.observe(this, Observer { getproduct ->

                val data = getproduct.data

                Log.e("TAGee", "addProduct: ${GsonBuilder().setPrettyPrinting().create()}")
                Toast.makeText(this, "Product Added Successfully", Toast.LENGTH_SHORT).show()

                finish()


            })
        }


    }

    val fileNameList = mutableListOf<String>()
    var index: Int = 0

    private val pickMultipleMedia =
        registerForActivityResult(ActivityResultContracts.PickMultipleVisualMedia(10)) { uris ->
            // Callback is invoked after the user selects media items or closes the photo picker
            if (uris.isNotEmpty()) {
                Log.d("PhotoPicker", "Number of items selected: ${uris.size}")

                val files = uris.mapNotNull { uri ->
                    val path = getRealPathFromURI(uri)
                    if (path != null) File(path) else null
                }





                binding.relImg.visibility = View.GONE
                binding.recyclerviewProductImages.visibility = View.VISIBLE

                val adapter = ProductImageAdapter(applicationContext, files)
                binding.recyclerviewProductImages.adapter = adapter

                while (index < files.size) {
                    callAPIImagetourl(files, index)
                    index++
                }


            } else {
                Log.d("PhotoPicker", "No media selected")
            }
        }

    fun callAPIImagetourl(files: List<File>, index: Int) {
        val currentFile = files[index]
        loginViewModel.imagetoURL_product(currentFile)!!.observe(this, Observer { urldata ->
            if (urldata.iStatusCode == 200) {
                addDatatoArraylist(urldata)
            } else {
                Log.e("PhotoPicker", "Error: urldata is null")
            }
        })
    }


    fun addDatatoArraylist(imageUrlResponse: ImageUrlResponse) {

        val imgpref = "http://192.168.1.70:7600/"

        val imageUrl = imgpref + imageUrlResponse.data.image
        if (!fileNameList.contains(imageUrl)) {
            fileNameList.add(imageUrl)
            Log.e("PhotoPicker", "imageUrl----- $imageUrl")

        }


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

    fun initview() {

        binding.ivProductImg.setOnClickListener {
            pickMultipleMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))

        }

        binding.ivBack.setOnClickListener {

            onBackPressedDispatcher.onBackPressed()

        }
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                finish()
            }
        })
        binding.tvSubmitProduct.setOnClickListener {
            addProduct()
        }


    }
}

