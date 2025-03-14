package net.heidylazaro.exchangerates.ui.theme

import android.annotation.SuppressLint
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.livedata.observeAsState
import net.heidylazaro.exchangerates.viewmodel.ExchangeRateViewModel


@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun ExchangeRateScreen(/*navController: NavController, */viewModel: ExchangeRateViewModel) {
    val rates by viewModel.exchangeRates.observeAsState(initial = emptyList())
    val updateInfo by viewModel.updateInfo.observeAsState()

    Scaffold(topBar = { TopAppBar(title = { Text("Tasas de Cambio") }) }) {
        Column(modifier = Modifier.padding(16.dp)) {
            updateInfo?.let {
                Text("Última Actualización: ${it.lastUpdateUtc}", style = MaterialTheme.typography.body2)
                Text("Próxima Actualización: ${it.nextUpdateUtc}", style = MaterialTheme.typography.body2)
            }
            Spacer(modifier = Modifier.height(16.dp))
            LazyColumn {
                items(rates) { rate ->
                    Card(
                        modifier = Modifier.fillMaxWidth().padding(8.dp)
                       /* .clickable {
                        // Navegar a la pantalla de la gráfica pasando los datos
                        navController.navigate("chart/${rate.currency}")
                    }*/
                        ,
                        elevation = 4.dp
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text("Moneda: ${rate.currency}", style = MaterialTheme.typography.h6)
                            Text("Tasa: ${rate.rate}", style = MaterialTheme.typography.body1)
                        }
                    }
                }
            }
        }
    }
}

/*@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun ExchangeRateChartScreen(currency: String, rates: List<ExchangeRate>, viewModel: ExchangeRateViewModel) {
    val sortedRates = rates.sortedBy { it.lastUpdateUnix } // Ordena por fecha ascendente

    val latestRate = sortedRates.lastOrNull()?.rate ?: "No disponible"  // Último valor de la tasa

    val exchangeRates by viewModel.providerExchangeRates.observeAsState(emptyList())

    // Llamar a la función para obtener datos del ContentProvider
    viewModel.fetchExchangeRatesFromProvider("USD", "2024-03-01", "2024-03-10")

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Gráfico de $currency") })
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Text("Moneda: $currency", style = MaterialTheme.typography.h6)
            Text("Tasa Actual: $latestRate", style = MaterialTheme.typography.body1)
            Spacer(modifier = Modifier.height(8.dp))
            Text("Rango de fechas: ${rates.firstOrNull()?.lastUpdateUtc} - ${rates.lastOrNull()?.lastUpdateUtc}")

            Spacer(modifier = Modifier.height(16.dp))


            ExchangeRateGraph()
        }
    }
}

@Composable
fun AppNavigation(viewModel: ExchangeRateViewModel) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "exchangeRates") {
        composable("exchangeRates") {
            ExchangeRateScreen(navController, viewModel)
        }
        composable("chart/{currency}") { backStackEntry ->
            val currency = backStackEntry.arguments?.getString("currency") ?: ""
            val rates = viewModel.getRatesForCurrency(currency)
            ExchangeRateChartScreen(currency, rates, viewModel)
        }
    }
}

@Composable
fun ExchangeRateGraph(rates: List<ExchangeRate>){
    if (rates.isEmpty()) return

    val maxRate = rates.maxOfOrNull { it.rate } ?: return
    val minRate = rates.minOfOrNull { it.rate } ?: return

    Canvas(modifier = Modifier.fillMaxSize()) {
        val width = size.width
        val height = size.height

        val scaleX = width / rates.size
        val scaleY = height / (maxRate - minRate)

        rates.forEachIndexed { index, rate ->
            val x = index * scaleX
            val y = (maxRate - rate.rate) * scaleY
            if (index > 0) {
                val prevX = (index - 1) * scaleX
                val prevY = (maxRate - rates[index - 1].rate) * scaleY
                drawLine(
                    start = Offset(prevX, prevY),
                    end = Offset(x, y),
                    color = Color.Blue,
                    strokeWidth = 2f
                )
            }
        }
    }
}*/







