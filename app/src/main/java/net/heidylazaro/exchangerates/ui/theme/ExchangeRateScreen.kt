package net.heidylazaro.exchangerates.ui.theme

import android.annotation.SuppressLint
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
fun ExchangeRateScreen(viewModel: ExchangeRateViewModel) {
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
                        modifier = Modifier.fillMaxWidth().padding(8.dp),
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
