package net.heidylazaro.exchangerates.model

import com.google.gson.annotations.SerializedName

data class ExchangeRateResponse(
    @SerializedName("result") val result: String,
    @SerializedName("time_last_update_unix") val lastUpdateUnix: Long,
    @SerializedName("time_last_update_utc") val lastUpdateUtc: String,
    @SerializedName("time_next_update_unix") val nextUpdateUnix: Long,
    @SerializedName("time_next_update_utc") val nextUpdateUtc: String,
    @SerializedName("base_code") val baseCode: String,
    @SerializedName("conversion_rates") val rates: Map<String, Double>
)