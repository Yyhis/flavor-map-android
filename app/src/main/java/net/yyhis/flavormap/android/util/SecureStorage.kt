package net.yyhis.flavormap.android.util

import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey

class SecureStorage(context: Context) {

    private val preferencesName = "encrypted_pref"

    private val encryptedPreferences: SharedPreferences

    init {
        val masterKey = MasterKey.Builder(context)
            .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
            .build();

        encryptedPreferences = EncryptedSharedPreferences.create(
            context,
            preferencesName,
            masterKey,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
    }

    fun save(key: String, value: String) {
        encryptedPreferences.edit().apply {
            putString(key, value)
            apply()
        }
    }

    fun get(key: String): String? {
        return encryptedPreferences.getString(key, null)
    }

    fun delete(key: String) {
        encryptedPreferences.edit().apply {
            remove(key)
            apply()
        }
    }

    fun saveToken(token: String) {
        encryptedPreferences.edit().putString("accessToken", token).apply()
    }

    fun isTokenPresent(): Boolean {
        return encryptedPreferences.getString("accessToken", null) != null
    }

    fun getToken(): String? {
        return encryptedPreferences.getString("accessToken", null)
    }

    fun clearToken() {
        encryptedPreferences.edit().remove("accessToken").apply()
    }
}
