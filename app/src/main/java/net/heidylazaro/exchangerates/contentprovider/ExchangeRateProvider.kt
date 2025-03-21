package net.heidylazaro.exchangerates.contentprovider
import android.annotation.SuppressLint
import android.content.ContentProvider
import android.content.ContentValues
import android.content.UriMatcher
import android.database.Cursor
import android.net.Uri
import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import net.heidylazaro.exchangerates.AppDatabase
import net.heidylazaro.exchangerates.model.ExchangeRateDao
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone

class ExchangeRateProvider : ContentProvider() {

    companion object {
        const val AUTHORITY = "net.heidylazaro.exchangerates.contentprovider"
        const val CODE_RATE_BY_CURRENCY = 2

        private val uriMatcher = UriMatcher(UriMatcher.NO_MATCH).apply {
            addURI(AUTHORITY, "exchange_rates/*/*/*", CODE_RATE_BY_CURRENCY)
        }
    }

    private lateinit var databaseDao: ExchangeRateDao

    override fun onCreate(): Boolean {
        databaseDao = AppDatabase.getDatabase(context!!).exchangeRateDao()
        return true
    }

    override fun query(
        uri: Uri,
        projection: Array<out String>?,
        selection: String?,
        selectionArgs: Array<out String>?,
        sortOrder: String?
    ): Cursor? {
        return runBlocking(Dispatchers.IO) {
            when (uriMatcher.match(uri)) {
                CODE_RATE_BY_CURRENCY -> {
                    val segments = uri.pathSegments
                    Log.d("ExchangeRateProvider", "Consulta recibida: $segments")

                    if (segments.size == 4) {
                        val currency = segments[1]

                        // Extraer las fechas desde la URI
                        val startDateString = segments[2]
                        val endDateString = segments[3]

                        // Usar SimpleDateFormat para convertir las fechas a milisegundos (timestamp)
                        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                        // Forzar la zona horaria a UTC
                        dateFormat.timeZone = TimeZone.getTimeZone("UTC")

                        val startDate = try {
                            (dateFormat.parse(startDateString)?.time ?: 0L) / 1000  // Convertir a segundos
                        } catch (e: ParseException) {
                            Log.e("ExchangeRateProvider", "Error parsing start date: $startDateString", e)
                            0L
                        }

                        val endDate = try {
                            (dateFormat.parse(endDateString)?.time ?: 0L) / 1000  // Convertir a segundos
                        } catch (e: ParseException) {
                            Log.e("ExchangeRateProvider", "Error parsing end date: $endDateString", e)
                            0L
                        }

                        // Imprimir las fechas convertidas para depuración
                        Log.d("ExchangeRateProvider", "Start date: $startDate, End date: $endDate")

                        Log.d("ExchangeRateProvider", "Moneda: $currency, Inicio: $startDate, Fin: $endDate")

                        val cursor = databaseDao.queryRatesByCurrency(currency, startDate, endDate)
                        Log.d("ExchangeRateProvider", "Cursor obtenido: ${cursor?.count}")

                        cursor
                    } else {
                        Log.e("ExchangeRateProvider", "Formato de URI incorrecto: $uri")
                        null
                    }
                }
                else -> {
                    Log.e("ExchangeRateProvider", "URI desconocida: $uri")
                    null
                }
            }
        }
    }

    override fun getType(uri: Uri): String? = "vnd.android.cursor.dir/vnd.$AUTHORITY.exchange_rates"

    override fun insert(uri: Uri, values: ContentValues?): Uri? = throw UnsupportedOperationException("Insert not supported")
    override fun update(uri: Uri, values: ContentValues?, selection: String?, selectionArgs: Array<String>?): Int = 0
    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<String>?): Int = 0
}
