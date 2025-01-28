package com.roaa.expensetracker.Model

import androidx.datastore.core.CorruptionException
import androidx.datastore.core.Serializer
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.roaa.expensetracker.Utilities.Constants.CASH
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import java.io.InputStream
import java.io.OutputStream

@Serializable
@Entity(tableName = "bank_accounts")
data class BankAccountsClass(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val initialAmount: Long,
    val currentAmount: Long,
    val bankName: String,
    val cardColorNumber: Int,
    val cardIconNumber: Int,
    val accountType: String,
)


@OptIn(ExperimentalSerializationApi::class)
object BankAccountsSerializer : Serializer<BankAccountsClass> {

    override val defaultValue = BankAccountsClass(1, 0, 0, "Cash", 1, 25, CASH)

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