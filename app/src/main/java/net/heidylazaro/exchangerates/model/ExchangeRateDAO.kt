package net.heidylazaro.exchangerates.model

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import net.heidylazaro.exchangerates.data.ExchangeRate

@Dao
interface ExchangeRateDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRates(rates: List<ExchangeRate>)

    @Query("SELECT * FROM exchange_rates")
    fun getAllRates(): Flow<List<ExchangeRate>>
}