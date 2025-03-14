package net.heidylazaro.exchangerates.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import net.heidylazaro.exchangerates.data.ExchangeRate
import net.heidylazaro.exchangerates.data.ExchangeRateRepository
import net.heidylazaro.exchangerates.data.UpdateInfo

class ExchangeRateViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: ExchangeRateRepository = ExchangeRateRepository(application)
    val exchangeRates: LiveData<List<ExchangeRate>> = repository.getExchangeRates().asLiveData()
    val updateInfo: LiveData<UpdateInfo> = repository.getLatestUpdateInfo().asLiveData()

    // LiveData para observar las tasas de cambio obtenidas del ContentProvider
    //private val _providerExchangeRates = MutableLiveData<List<ExchangeRate>>()
    //val providerExchangeRates: LiveData<List<ExchangeRate>> = _providerExchangeRates


    init {
        viewModelScope.launch {
            //Inicializa la sincronizacion
            repository.syncExchangeRates()
        }
    }

    /*fun getRatesForCurrency(currency: String): List<ExchangeRate> {
        return exchangeRates.value?.filter { it.currency == currency } ?: emptyList()
    }*/

    // Consultar tasas de cambio desde el ContentProvider
    /*fun fetchExchangeRatesFromProvider(currency: String, startDate: String, endDate: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val rates = repository.getExchangeRatesFromProvider(currency, startDate, endDate)
            _providerExchangeRates.postValue(rates) // Actualiza LiveData en el hilo principal
        }
    }*/
}
