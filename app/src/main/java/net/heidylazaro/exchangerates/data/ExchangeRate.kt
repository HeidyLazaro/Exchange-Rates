package net.heidylazaro.exchangerates.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "exchange_rates")
data class ExchangeRate(
    @PrimaryKey val currency: String,
    val rate: Double
)

@Entity(tableName = "update_info")
data class UpdateInfo(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val lastUpdateUnix: Long,
    val lastUpdateUtc: String,
    val nextUpdateUnix: Long,
    val nextUpdateUtc: String
)