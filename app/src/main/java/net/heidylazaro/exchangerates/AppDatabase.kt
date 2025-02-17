package net.heidylazaro.exchangerates

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import net.heidylazaro.exchangerates.data.ExchangeRate
import net.heidylazaro.exchangerates.data.UpdateInfo
import net.heidylazaro.exchangerates.model.ExchangeRateDao

@Database(entities = [ExchangeRate::class, UpdateInfo::class], version = 2)
abstract class AppDatabase : RoomDatabase() {
    abstract fun exchangeRateDao(): ExchangeRateDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "exchange_rate_db"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}