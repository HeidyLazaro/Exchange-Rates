package net.heidylazaro.exchangerates.worker

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import net.heidylazaro.exchangerates.data.ExchangeRateRepository

class CurrencySyncWorker(context: Context, params: WorkerParameters) :
    CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        return try {
            val repository = ExchangeRateRepository(applicationContext)
            repository.syncExchangeRates()
            Result.success()
        } catch (e: Exception) {
            Result.retry()
        }
    }
}