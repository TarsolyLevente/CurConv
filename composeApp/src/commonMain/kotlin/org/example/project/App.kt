package org.example.project

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Alignment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun App() {
    var fromCurrency by remember { mutableStateOf("") }
    var toCurrency by remember { mutableStateOf("") }
    var amount by remember { mutableStateOf("") }
    var result by remember { mutableStateOf(0.0) }
    var currencies by remember { mutableStateOf(listOf<String>()) }
    var showError by remember { mutableStateOf(false) }
    var filterText by remember { mutableStateOf("") }
    var amountError by remember { mutableStateOf<String?>(null) }
    val scope = CoroutineScope(Dispatchers.Main)

    LaunchedEffect(Unit) {
        try {
            currencies = CurrencyApiService.getCurrencies()
        } catch (e: Exception) {
            showError = true
        }
    }

    val filteredCurrencies = currencies.filter { it.contains(filterText, ignoreCase = true) }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            horizontalArrangement = Arrangement.Center,
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                var expanded by remember { mutableStateOf(false) }
                Box {
                    Text(fromCurrency.ifEmpty { "Select Currency" }, modifier = Modifier.clickable { expanded = true })
                    DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                        TextField(
                            value = filterText,
                            onValueChange = { filterText = it },
                            placeholder = { Text("Filter") },
                            modifier = Modifier.padding(8.dp)
                        )
                        filteredCurrencies.forEach { currency ->
                            DropdownMenuItem(onClick = {
                                fromCurrency = currency
                                expanded = false
                            }) {


                                Text(text = currency)
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                TextField(
                    value = amount,
                    onValueChange = {
                        amount = it
                        amountError = null
                    },
                    modifier = Modifier
                        .padding(8.dp)
                        .border(1.dp, MaterialTheme.colors.onSurface)
                        .width(100.dp),
                    isError = amountError != null
                )
                if (amountError != null) {
                    Text(amountError!!, color = MaterialTheme.colors.error)
                }
            }

            Spacer(modifier = Modifier.width(8.dp))

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                var expandedTo by remember { mutableStateOf(false) }
                Box {
                    Text(toCurrency.ifEmpty { "Select Currency" }, modifier = Modifier.clickable { expandedTo = true })
                    DropdownMenu(expanded = expandedTo, onDismissRequest = { expandedTo = false }) {
                        TextField(
                            value = filterText,
                            onValueChange = { filterText = it },
                            placeholder = { Text("Filter") },
                            modifier = Modifier.padding(8.dp)
                        )
                        filteredCurrencies.forEach { currency ->
                            DropdownMenuItem(onClick = {
                                toCurrency = currency
                                expandedTo = false
                            }) {
                                Text(text = currency)
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                TextField(
                    value = result.toString(),
                    onValueChange = {},
                    modifier = Modifier
                        .padding(8.dp)
                        .border(1.dp, MaterialTheme.colors.onSurface)
                        .width(100.dp),
                    enabled = false,
                )
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        if (showError)
            Text("Unexpected error", color = MaterialTheme.colors.error)
        Spacer(modifier = Modifier.height(8.dp))

        // Convert Button
        Button(onClick = {
            if (amount.toDoubleOrNull() == null) {
                amountError = "Invalid input"
            } else {
                scope.launch {
                    try {
                        result = CurrencyApiService.convertCurrency(fromCurrency, toCurrency, amount)
                        showError = false
                    } catch (e: Exception) {
                        showError = true
                    }
                }
            }
        }) {
            Text("Convert")
        }
    }
}