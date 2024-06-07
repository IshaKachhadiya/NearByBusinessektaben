package com.festum.nearbybusiness.owner.Utils

import android.content.Context
import android.content.SharedPreferences
import com.festum.nearbybusiness.owner.ui.Model.Profile.ProfileResponseData
import com.festum.nearbybusiness.owner.ui.Model.getProfile.getProfileResponse
import com.google.gson.Gson

class PrefManager private constructor(context: Context) {

    companion object {
        private const val PREF_NAME = "NeraByBusinessPref"
        private const val KEY_PROFILE_DATA = "key_profile_data"
        private const val KEY_PROFILE_DATA_splash = "key_profile_data_Splash"
        @Volatile
        private var instance: PrefManager? = null

        fun getInstance(context: Context): PrefManager {
            return instance ?: synchronized(this) {
                instance ?: PrefManager(context).also { instance = it }
            }
        }
    }

    private val pref: SharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    private val editor: SharedPreferences.Editor = pref.edit()
    private val gson = Gson()

    fun savePreferencesStr(key: String, value: Boolean) {
        editor.putBoolean(key, value).apply()
    }

    fun savePreferencesStr(key: String, value: String) {
        editor.putString(key, value).apply()
    }

    fun getStringPreference(key: String): String {
        return pref.getString(key, "") ?: ""
    }

    fun loadSavedPreferences(key: String, defaultValue: Boolean): Boolean {
        return pref.getBoolean(key, defaultValue)
    }

    fun saveProfileData(profileData: ProfileResponseData.ProfileDetailData) {
        val json = gson.toJson(profileData)
        editor.putString(KEY_PROFILE_DATA, json).apply()
    }

    fun getProfileData(): ProfileResponseData.ProfileDetailData? {
        val json = pref.getString(KEY_PROFILE_DATA, null)
        return json?.let { gson.fromJson(it, ProfileResponseData.ProfileDetailData::class.java) }
    }
    fun saveProfileDatasplash(profileData: getProfileResponse.getProfileData) {
        val json = gson.toJson(profileData)
        editor.putString(KEY_PROFILE_DATA_splash, json).apply()
    }

    fun getProfileDatasplash(): getProfileResponse.getProfileData? {
        val json = pref.getString(KEY_PROFILE_DATA_splash, null)
        return json?.let { gson.fromJson(it, getProfileResponse.getProfileData::class.java) }
    }

    fun clearAllPreferences() {
        editor.clear()
        editor.commit()
    }
}
