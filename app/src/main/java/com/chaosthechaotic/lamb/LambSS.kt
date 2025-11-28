package com.chaosthechaotic.lamb

import android.content.Context
import java.security.KeyStore

class LambSS(context: Context) {
    // Start adding shared prefs from android key store
    private val shP = context.getSharedPreferences("secure_prefs", Context.MODE_PRIVATE)
    private val kst = KeyStore.getInstance("AndroidKeyStore").apply { load(null) } }
}