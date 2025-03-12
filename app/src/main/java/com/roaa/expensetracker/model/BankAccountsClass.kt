package com.roaa.expensetracker.model

import androidx.datastore.core.CorruptionException
import androidx.datastore.core.Serializer
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.roaa.expensetracker.utilities.Constants.CASH
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerializationException
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.Json
import java.io.InputStream
import java.io.OutputStream
import java.math.BigDecimal

@Serializable
@Entity(tableName = "bank_accounts")
data class BankAccountsClass(
    @PrimaryKey(autoGenerate = true)
    val bankAccountId: Long,
    @Serializable(with = BigDecimalSerializer::class)
    val initialAmount: BigDecimal,
    @Serializable(with = BigDecimalSerializer::class)
    val currentAmount: BigDecimal,
    val bankName: String,
    val cardColorNumber: Int,
    val cardIconNumber: Int,
    val accountType: String,
    var isActive: Boolean,
)


@OptIn(ExperimentalSerializationApi::class)
object BankAccountsSerializer : Serializer<BankAccountsClass> {

    override val defaultValue =
        BankAccountsClass(1, BigDecimal.ZERO, BigDecimal.ZERO, "Cash", 1, 25, CASH, false)

    override suspend fun readFrom(input: InputStream): BankAccountsClass {
        try {
            return Json.decodeFromString(
                BankAccountsClass.serializer(), input.readBytes().decodeToString()
            )
        } catch (serialization: SerializationException) {
            throw CorruptionException("Unable to read UserPrefs", serialization)
        }
    }

    override suspend fun writeTo(t: BankAccountsClass, output: OutputStream) {
        withContext(Dispatchers.IO) {
            output.write(
                Json.encodeToString(BankAccountsClass.serializer(), t)
                    .encodeToByteArray()
            )
        }
    }
}

object BigDecimalSerializer : KSerializer<BigDecimal> {
    override val descriptor: SerialDescriptor =
        PrimitiveSerialDescriptor("BigDecimal", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: BigDecimal) {
        encoder.encodeString(value.toPlainString()) // Convert BigDecimal to String
    }

    override fun deserialize(decoder: Decoder): BigDecimal {
        return BigDecimal(decoder.decodeString()) // Convert String back to BigDecimal
    }
}