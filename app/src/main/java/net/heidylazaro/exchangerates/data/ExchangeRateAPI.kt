package net.heidylazaro.exchangerates.data

import net.heidylazaro.exchangerates.model.ExchangeRateResponse
import retrofit2.http.GET

interface ExchangeRateApi {
    @GET("v6/494660e3c219dce679d3ec7c/latest/MXN")
    suspend fun getExchangeRates(): ExchangeRateResponse
}