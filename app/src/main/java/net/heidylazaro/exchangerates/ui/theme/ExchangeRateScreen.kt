package net.heidylazaro.exchangerates.ui.theme

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import net.heidylazaro.exchangerates.data.ExchangeRate
import net.heidylazaro.exchangerates.viewmodel.ExchangeRateViewModel


@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun ExchangeRateScreen(navController: NavController, viewModel: ExchangeRateViewModel) {
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
                        .clickable {
                        // Navegar a la pantalla de la gráfica pasando los datos
                        navController.navigate("chart/${rate.currency}")
                            //Toast("")
                    },
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

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun ExchangeRateChartScreen(currency: String, rates: List<ExchangeRate>) {
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
            Spacer(modifier = Modifier.height(8.dp))
            Text("Rango de fechas: ${rates.firstOrNull()?.lastUpdateUtc} - ${rates.lastOrNull()?.lastUpdateUtc}")

            Spacer(modifier = Modifier.height(16.dp))

            // Aquí irá la función para dibujar el gráfico
            //ExchangeRateGraph(rates)
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
            ExchangeRateChartScreen(currency, rates)
        }
    }
}







