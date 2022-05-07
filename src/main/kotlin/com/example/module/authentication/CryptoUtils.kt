package com.example.module.authentication

import io.ktor.util.*
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec
import javax.naming.ConfigurationException

object CryptoUtils {
    private var hmac: Mac? = null

    fun initKey(keyToSet: String?) {
        if (keyToSet == null) throw ConfigurationException("没key")
        hmac = Mac.getInstance("HmacSHA256")
        val safeKey = keyToSet.toByteArray()
        val keyObject = SecretKeySpec(keyToSet.toByteArray(), "HmacSHA256")
        hmac!!.init(keyObject)
    }

    fun encryptPassword(passwordMessage: String): String {
        return hmac!!.doFinal(passwordMessage.toByteArray()).encodeBase64()
    }

}