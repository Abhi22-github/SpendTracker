package com.roaa.expensetracker.Model.UiDateModels

import androidx.datastore.core.CorruptionException
import com.roaa.expensetracker.Utilities.UtilityModalClass.defaultCurrency
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import java.io.InputStream
import java.io.OutputStream

@Serializable
data class CurrencyClass(
    val countryName: String,
    val currencyCode: String,
    val currencyDisplayName:String,
    val currencySymbol: String,
    val flag: String
)

object CurrencyClassSerializer : androidx.datastore.core.Serializer<CurrencyClass> {
     override  val defaultValue = defaultCurrency

     override suspend fun readFrom(input: InputStream): CurrencyClass {
        try {
            return Json.decodeFromString(
                CurrencyClass.serializer(), input.readBytes().decodeToString()
            )
        } catch (serialization: SerializationException) {
            throw CorruptionException("Unable to read UserPrefs", serialization)
        }
    }

     override suspend fun writeTo(t: CurrencyClass, output: OutputStream) {
        withContext(Dispatchers.IO) {
            output.write(
                Json.encodeToString(CurrencyClass.serializer(), t)
                    .encodeToByteArray()
            )
        }
    }
}
