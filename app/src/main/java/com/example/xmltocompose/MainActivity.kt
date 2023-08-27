package com.example.xmltocompose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.xmltocompose.ui.theme.XMLToComposeTheme
import com.example.xmltocompose.ui.theme.blue
import com.example.xmltocompose.ui.theme.pastelBlue
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize the app and load data using CoroutineScope
        // CoroutineScope is used here for better performance
        CoroutineScope(Dispatchers.IO).launch {
            DataManager.loadAssetsFromFile(applicationContext)
        }

        setContent {
            XMLToComposeTheme {
                // A surface container using the 'background' color from the theme
                Scaffold(topBar = {
                    TopAppBar(
                        title = { Text("XML To Compose") },
                        colors = TopAppBarDefaults.smallTopAppBarColors(
                            containerColor = blue,
                            titleContentColor = Color.White
                        )
                    )
                }, content = {
                    Column(
                        modifier = Modifier
                            .padding(it)
                            .fillMaxSize()
                    ) {
                        mainComposable()
                    }
                }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun mainComposable() {

    // Define mutable state variables for UI interaction
    val isExpanded = remember { mutableStateOf(false) }
    val selectedWidget = remember { mutableStateOf("Widget") }
    val equivalentComposable = remember { mutableStateOf("Composable") }

    // Column composable for the main UI content
    Column(modifier = Modifier.padding(8.dp)) {

        // Card containing the dropdown menu
        Card(
            elevation = CardDefaults.cardElevation(defaultElevation = 10.dp),
            colors = CardDefaults.cardColors(containerColor = pastelBlue),
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {

            // Box to center content within the card
            Box(modifier = Modifier.align(Alignment.CenterHorizontally)) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center

                ) {
                    Text(
                        text = "Classic Android Widget",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    // ExposedDropdownMenuBox containing TextField and DropDown
                    ExposedDropdownMenuBox(
                        expanded = isExpanded.value,
                        onExpandedChange = { isExpanded.value = !isExpanded.value }) {

                        TextField(
                            value = selectedWidget.value,
                            onValueChange = {},
                            readOnly = true,
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = isExpanded.value) },
                            modifier = Modifier.menuAnchor(),
                            colors = TextFieldDefaults.textFieldColors(containerColor = pastelBlue )
                        )

                        // ExposedDropdownMenu displaying different widgets
                        ExposedDropdownMenu(
                            expanded = isExpanded.value,
                            onDismissRequest = { isExpanded.value = false }
                        ) {
                            DataManager.data.forEach {
                                DropdownMenuItem(text = { Text(it.widget) }, onClick = {
                                    isExpanded.value = false
                                    selectedWidget.value = it.widget
                                    equivalentComposable.value = it.composable
                                })
                            }
                        }

                    }

                }
            }
        }


        Spacer(modifier = Modifier.height(8.dp))


        Image(
            painter = painterResource(id = R.drawable.ic_down), contentDescription = "Down arrow",
            modifier = Modifier.align(Alignment.CenterHorizontally),
            colorFilter = ColorFilter.tint(color = blue),
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Card displaying the equivalent composable
        Card(
            elevation = CardDefaults.cardElevation(defaultElevation = 10.dp),
            colors = CardDefaults.cardColors(containerColor = pastelBlue),
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {

            // Box to center content within the card
            Box(modifier = Modifier.align(Alignment.CenterHorizontally)) {
                Column(
                    modifier = Modifier
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "Equivalent Composable",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onBackground
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // Text displaying the selected equivalent composable
                    Text(
                        text = equivalentComposable.value,
                        style = MaterialTheme.typography.titleLarge,
                        color = blue
                    )
                }
            }
        }
    }


}


@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun GreetingPreview() {
    XMLToComposeTheme {
        Scaffold(topBar = {
            TopAppBar(
                title = { Text("XML To Compose") },
                colors = TopAppBarDefaults.smallTopAppBarColors(

                    containerColor = blue,
                    titleContentColor = Color.White
                )

            )
        }, content = {
            Column(
                modifier = Modifier
                    .padding(it)
                    .fillMaxSize()
            ) {
                mainComposable()
            }
        }
        )

    }
}