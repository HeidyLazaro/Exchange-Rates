package net.heidylazaro.exchangerates

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import net.heidylazaro.exchangerates.data.ExchangeRate
import net.heidylazaro.exchangerates.data.UpdateInfo
import net.heidylazaro.exchangerates.model.ExchangeRateDao

@Database(entities = [ExchangeRate::class, UpdateInfo::class], version = 3)
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
                )//addMigrations(MIGRATION_2_3).
                    //fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }

        /*private val MIGRATION_2_3 = object : Migration(2, 3) {
            override fun migrate(database: SupportSQLiteDatabase) {
                // Agregar la nueva columna con un valor por defecto
                database.execSQL("ALTER TABLE update_info ADD COLUMN actualUpdateTime INTEGER NOT NULL DEFAULT 0")
            }
        }*/
    }
}