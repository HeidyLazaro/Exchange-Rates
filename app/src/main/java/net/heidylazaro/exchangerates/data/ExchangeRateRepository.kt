package net.heidylazaro.exchangerates.data

import android.content.Context
import android.util.Log
import net.heidylazaro.exchangerates.data.ExchangeRate
import net.heidylazaro.exchangerates.data.ExchangeRateApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import net.heidylazaro.exchangerates.AppDatabase
import net.heidylazaro.exchangerates.model.ExchangeRateDao
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ExchangeRateRepository(context: Context) {

    private val api: ExchangeRateApi
    private val dao: ExchangeRateDao

    init {
        //Obtiene la bd insertandto el contexto de la aplicacion
        val db = AppDatabase.getDatabase(context)
        //Instanciar la bd
        dao = db.exchangeRateDao()


        val retrofit = Retrofit.Builder()
            .baseUrl("https://v6.exchangerate-api.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        api = retrofit.create(ExchangeRateApi::class.java)
    }

    suspend fun syncExchangeRates() {
        withContext(Dispatchers.IO) {
            try {
                val response = api.getExchangeRates()
                Log.d("API_RESPONSE", "Datos obtenidos: ${response.rates}")
                if (response.rates.isNotEmpty()) {
                    val ratesList = response.rates.map { ExchangeRate(it.key, it.value) }

                    // Insertar en SQLite
                    dao.insertRates(ratesList)
                    dao.insertUpdateInfo(UpdateInfo(
                        lastUpdateUnix = response.lastUpdateUnix,
                        lastUpdateUtc = response.lastUpdateUtc,
                        nextUpdateUnix = response.nextUpdateUnix,
                        nextUpdateUtc = response.nextUpdateUtc
                    ))

                    Log.d("DATABASE", "Datos insertados en SQLite")
                } else {
                    Log.e("API_RESPONSE", "La API no devolvió datos")
                }
            } catch (e: Exception) {
                Log.e("API_RESPONSE", "Error al obtener datos de la API", e)
            }
        }
    }

    fun getExchangeRates() = dao.getAllRates()
    fun getLatestUpdateInfo() = dao.getLatestUpdateInfo()
}
