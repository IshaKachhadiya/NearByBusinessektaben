package com.festum.nearbybusiness.owner.ui.fragment

import android.app.Dialog
import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.festum.nearbybusiness.owner.R
import com.festum.nearbybusiness.owner.Utils.PrefManager
import com.festum.nearbybusiness.owner.databinding.FragmentSettingBinding
import com.festum.nearbybusiness.owner.ui.main.view.Login.LoginMobNumberActivity


class SettingFragment : Fragment() {

    private lateinit var binding: FragmentSettingBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSettingBinding.inflate(inflater, container, false)
        initViews()
        return binding.root
    }

    private fun initViews() {
        binding.llLogout.setOnClickListener {
            logoutilaog()
        }

    }


    fun logoutilaog() {
        val dialog = Dialog(this.requireActivity())
        dialog.setContentView(R.layout.log_out_dialog)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(0))
        dialog.findViewById<View>(R.id.tvYes).setOnClickListener {
            PrefManager.getInstance(this.requireContext()).clearAllPreferences()
            val intent = Intent(this.requireContext(), LoginMobNumberActivity::class.java)
            startActivity(intent)
            dialog.dismiss()
        }
        dialog.findViewById<View>(R.id.tvNo).setOnClickListener { dialog.dismiss() }
        dialog.window!!.setLayout(-1, -2)
        dialog.show()
    }


}