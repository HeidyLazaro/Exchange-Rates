package net.heidylazaro.exchangerates.worker

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import net.heidylazaro.exchangerates.data.ExchangeRateRepository
//Corrutine worker es para ejecutar las tareas en segundpo plano
class CurrencySyncWorker(context: Context, params: WorkerParameters) :
    CoroutineWorker(context, params) {
//Worker para sincroniza la bd
    override suspend fun doWork(): Result {
        return try {
            //Consulta el metodo para sincronizar la bd
            val repository = ExchangeRateRepository(applicationContext)
            repository.syncExchangeRates()
            Result.success()
        } catch (e: Exception) {
            Result.retry()
        }
    }
}