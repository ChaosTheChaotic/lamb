package com.chaosthechaotic.lamb

import android.content.Context
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import android.util.Base64
import java.nio.charset.StandardCharsets
import java.security.KeyStore
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.GCMParameterSpec

class LambSS(context: Context) {
    private val shprfs = context.getSharedPreferences("secure_prefs", Context.MODE_PRIVATE)
    private val kst = KeyStore.getInstance("AndroidKeyStore").apply { load(null) }

    companion object {
        private const val KEY = "lamb_croco_pass"
        private const val AKST = "AndroidKeyStore"
        private const val EKEY = "lamb_croco_epass"
    }

    private fun getOrCreateKey(): SecretKey {
        if (!kst.containsAlias(KEY)) {
            val keyGen = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, AKST)
            keyGen.init(
                KeyGenParameterSpec
                    .Builder(
                        KEY, KeyProperties.PURPOSE_DECRYPT or KeyProperties.PURPOSE_DECRYPT
                    )
                    .setBlockModes(
                        KeyProperties.BLOCK_MODE_GCM
                    )
                    .setEncryptionPaddings(
                        KeyProperties.ENCRYPTION_PADDING_NONE
                    )
                    .setRandomizedEncryptionRequired(false)
                    .build()
            )
            return keyGen.generateKey()
        }
        return kst.getKey(KEY, null) as SecretKey
    }

    fun encryptStore(pwd: String) {
        val cipher = Cipher.getInstance("AES/GCM/NoPadding")
        cipher.init(Cipher.ENCRYPT_MODE, getOrCreateKey())
        val enc = cipher.doFinal(pwd.toByteArray(StandardCharsets.UTF_8))

        val comb = cipher.iv + enc
        shprfs.edit().putString(EKEY, Base64.encodeToString(comb, Base64.DEFAULT)).apply()
    }

    fun decryptPwd(): String? {
        val comb = Base64.decode(shprfs.getString(EKEY, null), Base64.DEFAULT) ?: return null
        val iv = comb.copyOfRange(0, 12) // GCM IV len is 12 bytes
        val enc = comb.copyOfRange(12, comb.size)

        val cipher = Cipher.getInstance("AES/GCM/NoPadding")
        val spec = GCMParameterSpec(128, iv)
        cipher.init(Cipher.DECRYPT_MODE, getOrCreateKey(), spec)
        val dec = cipher.doFinal(enc)
        return String(dec, StandardCharsets.UTF_8)
    }
}