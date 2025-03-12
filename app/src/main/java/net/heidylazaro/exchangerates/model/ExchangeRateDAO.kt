package net.heidylazaro.exchangerates.model

import android.database.Cursor
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import net.heidylazaro.exchangerates.data.ExchangeRate
import net.heidylazaro.exchangerates.data.UpdateInfo

@Dao
interface ExchangeRateDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRates(rates: List<ExchangeRate>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUpdateInfo(updateInfo: UpdateInfo)

    @Query("SELECT * FROM exchange_rates")
    fun getAllRates(): Flow<List<ExchangeRate>>

    @Query("SELECT * FROM update_info ORDER BY id DESC LIMIT 1")
    fun getLatestUpdateInfo(): Flow<UpdateInfo>

    @Query("SELECT * FROM exchange_rates")
    fun queryAllRates(): Cursor

    /*
    @Query("SELECT * FROM exchange_rates WHERE currency = :currency AND lastUpdateUnix BETWEEN :startDate AND :endDate")
    fun queryRatesByCurrency(currency: String, startDate: Long, endDate: Long): Cursor
    */
    /*@Query("SELECT lastUpdateUnix, rate FROM exchange_rates WHERE currency = :currency AND lastUpdateUnix BETWEEN :startDate AND :endDate ORDER BY lastUpdateUnix ASC")
    fun queryRatesByCurrency(currency: String, startDate: Long, endDate: Long): Cursor*/

    @Query("SELECT currency, lastUpdateUnix, rate FROM exchange_rates WHERE currency = :currency AND lastUpdateUnix >= :startDate AND lastUpdateUnix <= :endDate")
    fun queryRatesByCurrency(currency: String, startDate: Long, endDate: Long): Cursor

}