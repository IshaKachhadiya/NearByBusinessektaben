package com.festum.nearbybusiness.owner.ui.main.view

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.viewpager.widget.ViewPager
import com.festum.nearbybusiness.owner.R
import com.festum.nearbybusiness.owner.Utils.PrefManager
import com.festum.nearbybusiness.owner.databinding.ActivityOnboardingBinding
import com.festum.nearbybusiness.owner.ui.main.view.Login.LoginMobNumberActivity


class OnboardingActivity : AppCompatActivity() {

    private lateinit var viewPager: ViewPager
    private lateinit var layouts: IntArray
    var binding: ActivityOnboardingBinding? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOnboardingBinding.inflate(layoutInflater)
        setContentView(binding!!.root)

        val isOnBondingShow =
            PrefManager.getInstance(this).loadSavedPreferences("isOnBondingShow", true)
        if (!isOnBondingShow) {
            lunchSplash()
        }
        setLocalData()

    }

    private fun setLocalData() {
        layouts = intArrayOf(
            R.layout.intro_one,
            R.layout.intro_two,
            R.layout.intro_three
        )
        viewPager = findViewById(R.id.cp_pager)
        val myViewPagerAdapter = PagerAdapter()
        viewPager.adapter = myViewPagerAdapter
        viewPager.currentItem = 0

        viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
            }

            override fun onPageSelected(position: Int) {

            }

            override fun onPageScrollStateChanged(state: Int) {}
        })
    }


    inner class PagerAdapter : androidx.viewpager.widget.PagerAdapter() {
        private var layoutInflater: LayoutInflater? = null

        lateinit var img_next: ImageView


        override fun instantiateItem(container: ViewGroup, position: Int): Any {
            layoutInflater = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

            val view = layoutInflater!!.inflate(layouts[position], container, false)
            container.addView(view)

            img_next = view.findViewById(R.id.iv_next1)
            img_next.setOnClickListener {

                if (position == 2) {
                    lunchSplash()
                } else {
                    viewPager.currentItem = position + 1

                }

            }

            return view
        }

        override fun getCount(): Int {
            return layouts.size
        }

        override fun isViewFromObject(view: View, obj: Any): Boolean {
            return view === obj
        }

        override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
            val view = `object` as View
            container.removeView(view)

        }
    }

    fun lunchSplash() {

       /* if (!isLocationPermissionGranted(applicationContext)) {
            val intent = Intent(applicationContext, LocationPermissionActivity::class.java)
            startActivity(intent)
            finish()
        } else {*/
            PrefManager.getInstance(this).savePreferencesStr("isOnBondingShow", true)
            startActivity(Intent(this, LoginMobNumberActivity::class.java))
//        }


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