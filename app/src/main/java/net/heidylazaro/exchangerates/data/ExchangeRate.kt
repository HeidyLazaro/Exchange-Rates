package net.heidylazaro.exchangerates.data

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

/*@Entity(tableName = "exchange_rates")
data class ExchangeRate(
    @PrimaryKey val currency: String,
    val rate: Double,
    val lastUpdateUtc: String,       // Nueva columna para la fecha en UTC
    val actualUpdateTime: String
)*/

@Entity(
    tableName = "exchange_rates",
    indices = [Index(value = ["currency"]), Index(value = ["lastUpdateUnix"])]
)
data class ExchangeRate(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,  // id autogenerado para las filas
    val currency: String,  // Moneda
    val rate: Double,      // Tasa de cambio
    val lastUpdateUnix: Long, // Fecha de la última actualización (en Unix)
    val lastUpdateUtc: String  // Fecha de la última actualización (en formato UTC)
)


@Entity(tableName = "update_info")
data class UpdateInfo(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val lastUpdateUnix: Long,
    val lastUpdateUtc: String,
    val nextUpdateUnix: Long,
    val nextUpdateUtc: String,
    val actualUpdate: String
)