package net.heidylazaro.exchangerates.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import net.heidylazaro.exchangerates.data.ExchangeRateRepository

class ExchangeRateViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = ExchangeRateRepository(application)
    val exchangeRates = repository.getExchangeRates().asLiveData()
}