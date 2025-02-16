package net.heidylazaro.exchangerates.model

import com.google.gson.annotations.SerializedName

data class ExchangeRateResponse(
    @SerializedName("base_code") val baseCode: String,
    @SerializedName("conversion_rates") val rates: Map<String, Double>
)
