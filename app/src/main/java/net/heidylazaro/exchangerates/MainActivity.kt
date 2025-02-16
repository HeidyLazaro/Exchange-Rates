package net.heidylazaro.exchangerates

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.work.*
import net.heidylazaro.exchangerates.ui.theme.ExchangeRateScreen
import net.heidylazaro.exchangerates.viewmodel.ExchangeRateViewModel
import net.heidylazaro.exchangerates.worker.CurrencySyncWorker
import java.util.concurrent.TimeUnit

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Programar WorkManager para sincronizar cada hora
        val workRequest = PeriodicWorkRequestBuilder<CurrencySyncWorker>(1, TimeUnit.HOURS).build()
        WorkManager.getInstance(this).enqueueUniquePeriodicWork(
            "currency_sync",
            ExistingPeriodicWorkPolicy.KEEP,
            workRequest
        )

        setContent {
            val viewModel: ExchangeRateViewModel = viewModel()
            ExchangeRateScreen(viewModel)
        }
    }
}