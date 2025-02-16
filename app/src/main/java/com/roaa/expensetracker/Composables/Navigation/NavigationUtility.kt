package com.roaa.expensetracker.Composables.Navigation

import android.os.Build
import android.os.Bundle
import android.os.Parcelable
import androidx.navigation.NavType
import kotlinx.serialization.KSerializer
import kotlinx.serialization.json.Json
import kotlin.reflect.KClass

class CustomNavType<T : Parcelable>(
    private val clazz: KClass<T>,
    private val serializer: KSerializer<T>
) : NavType<T>(false) {
    override fun get(bundle: Bundle, key: String): T? {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            bundle.getParcelable(key, clazz.java)
        } else {
            @Suppress("DEPRECATION")
            bundle.getParcelable(key)
        }
    }

    override fun parseValue(value: String): T {
        return Json.decodeFromString(serializer,value)
    }

    override fun put(bundle: Bundle, key: String, value: T) {
       bundle.putParcelable(key,value)
    }

    override fun serializeAsValue(value: T): String {
        return Json.encodeToString(serializer,value)
    }

}