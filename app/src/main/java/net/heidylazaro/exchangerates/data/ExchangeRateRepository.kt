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
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

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
                    // Obtenemos la fecha UTC de la respuesta de la API


                    val actualUpdateTime = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault()).apply {
                    }.format(Date()).toString()

                    val ratesList = response.rates.map { ExchangeRate(
                        currency = it.key,  // Moneda
                        rate = it.value,    // Tipo de cambio
                        lastUpdateUnix = response.lastUpdateUnix,  // Fecha UTC de la respuesta
                        lastUpdateUtc = actualUpdateTime  // Fecha local de actualización
                         ) }

                    // Insertar en SQLite
                    dao.insertRates(ratesList)
                    dao.insertUpdateInfo(UpdateInfo(
                        lastUpdateUnix = response.lastUpdateUnix,
                        lastUpdateUtc = response.lastUpdateUtc,
                        nextUpdateUnix = response.nextUpdateUnix,
                        nextUpdateUtc = response.nextUpdateUtc,
                        actualUpdate = actualUpdateTime
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
