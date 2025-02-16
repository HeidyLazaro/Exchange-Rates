package net.heidylazaro.exchangerates.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "exchange_rates")
data class ExchangeRate(
    @PrimaryKey val currency: String,
    val rate: Double
)