package com.example.xmltocompose

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.ClickableText
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.xmltocompose.ui.theme.XMLToComposeTheme
import com.example.xmltocompose.ui.theme.blue
import com.example.xmltocompose.ui.theme.pastelBlue
import com.example.xmltocompose.viewmodel.MainViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Check internet connection
        if (!isInternetAvailable(applicationContext)) {
            Toast.makeText(this, "No internet connection", Toast.LENGTH_SHORT).show()
            return
        }

        // Load data using CoroutineScope
        // CoroutineScope is used here for better performance
        CoroutineScope(Dispatchers.IO).launch {
            DataManager.loadAssetsFromUrl()
        }

        setContent {
            XMLToComposeTheme {
                // A surface container using the 'background' color from the theme
                Scaffold(topBar = {
                    TopAppBar(
                        title = { Text(stringResource(id = R.string.xml_to_compose)) },
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

                        MainComposable()
                    }
                }
                )
            }
        }
    }
}

@Composable
fun MainComposable(viewModel: MainViewModel = viewModel()) {
    val context = LocalContext.current

    //observe changes in ViewModel properties and automatically update the Composable UI
    val isExpanded by viewModel.isExpanded.observeAsState(false)
    val selectedWidget by viewModel.selectedWidget.observeAsState("Widget")
    val equivalentComposable by viewModel.equivalentComposable.observeAsState("Composable")
    val composableSyntax by viewModel.composableSyntax.observeAsState("")
    val composableLink by viewModel.composableLink.observeAsState("")

    // Column composable for the main UI content
    Column(modifier = Modifier.padding(8.dp)) {

        // Card containing the dropdown menu
        DropDownMenuCard(
            viewModel,
            isExpanded,
            selectedWidget
        )

        Spacer(modifier = Modifier.height(8.dp))
        Image(
            painter = painterResource(id = R.drawable.ic_down),
            contentDescription = stringResource(
                id = R.string.down_arrow
            ),
            modifier = Modifier.align(Alignment.CenterHorizontally),
            colorFilter = ColorFilter.tint(color = blue),
        )
        Spacer(modifier = Modifier.height(8.dp))

        // Card displaying the equivalent composable Component
        CardEquivalentComposableComponentName(equivalentComposable)
        
        // Card displaying the equivalent composable Syntax
        CardEquivalentComposableSyntax(composableSyntax)
        
        // Card displaying the equivalent composable Resource
        CardEquivalentComposableResourceToLearn(composableLink, context)

    }


}

@Composable
private fun CardEquivalentComposableResourceToLearn(
    composableLink: String,
    context: Context
) {
    Card(
        elevation = CardDefaults.cardElevation(defaultElevation = 10.dp),
        colors = CardDefaults.cardColors(containerColor = pastelBlue),
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth()
    ) {

        // Box to center content within the card
        Column(
            modifier = Modifier
                .padding(16.dp),
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = stringResource(id = R.string.resource_to_learn),
                style = MaterialTheme.typography.titleMedium,
                color = if (isSystemInDarkTheme()) MaterialTheme.colorScheme.onSecondary else MaterialTheme.colorScheme.onBackground,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))

            // Text displaying the selected equivalent composable
            ClickableText(
                text = AnnotatedString(composableLink),
                onClick = {
                    // Open the link when clicked
                    val uri = Uri.parse(composableLink)
                    val intent = Intent(Intent.ACTION_VIEW, uri)
                    context.startActivity(intent)
                },
                style = TextStyle(
                    color = blue,
                    textDecoration = TextDecoration.Underline
                )
            )
        }
    }
}

@Composable
private fun CardEquivalentComposableSyntax(composableSyntax: String) {
    Card(
        elevation = CardDefaults.cardElevation(defaultElevation = 10.dp),
        colors = CardDefaults.cardColors(containerColor = pastelBlue),
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth()
    ) {

        // Box to center content within the card
        Column(
            modifier = Modifier
                .padding(16.dp),
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = stringResource(id = R.string.syntax),
                style = MaterialTheme.typography.titleMedium,
                color = if (isSystemInDarkTheme()) MaterialTheme.colorScheme.onSecondary else MaterialTheme.colorScheme.onBackground,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))

            // Text displaying the selected equivalent composable
            Text(
                text = composableSyntax,
                style = MaterialTheme.typography.bodyMedium,
                color = Color.DarkGray
            )
        }
    }
}

@Composable
private fun CardEquivalentComposableComponentName(equivalentComposable: String) {
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
                    text = stringResource(id = R.string.equivalent_composable),
                    style = MaterialTheme.typography.titleMedium,
                    color = if (isSystemInDarkTheme()) MaterialTheme.colorScheme.onSecondary else MaterialTheme.colorScheme.onBackground
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Text displaying the selected equivalent composable
                Text(
                    text = equivalentComposable,
                    style = MaterialTheme.typography.titleLarge,
                    color = blue
                )
            }
        }
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun DropDownMenuCard(
    viewModel: MainViewModel,
    isExpanded: Boolean,
    selectedWidget: String,
) {
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
                    text = stringResource(id = R.string.classic_android_widget),
                    style = MaterialTheme.typography.titleMedium,
                    color = if (isSystemInDarkTheme()) MaterialTheme.colorScheme.onSecondary else MaterialTheme.colorScheme.onBackground
                )
                Spacer(modifier = Modifier.height(8.dp))

                // ExposedDropdownMenuBox containing TextField and DropDown
                ExposedDropdownMenuBox(
                    expanded = isExpanded,
                    onExpandedChange = {
                        viewModel.updateExpandedValue(!isExpanded)
                    }) {

                    TextField(
                        value = selectedWidget,
                        onValueChange = {},
                        readOnly = true,
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = isExpanded) },
                        modifier = Modifier.menuAnchor(),
                        textStyle = TextStyle(
                            color = if (isSystemInDarkTheme()) Color.Black else MaterialTheme.colorScheme.onBackground,
                            fontSize = 17.sp
                        ),
                        colors = TextFieldDefaults.textFieldColors(containerColor = pastelBlue)
                    )

                    // ExposedDropdownMenu displaying different widgets
                    ExposedDropdownMenu(
                        expanded = isExpanded,
                        onDismissRequest = {
                            viewModel.updateExpandedValue(false)
                        }
                    ) {
                        DataManager.data.forEach {
                            DropdownMenuItem(text = { Text(it.widget) }, onClick = {
                                //update viewModel Property
                                //Example : isExpanded.selectedWidget,equivalentComposable,composableSyntax,composableLink
                                viewModel.updateExpandedValue(false)
                                viewModel.updateSelectedWidget(it.widget)
                                viewModel.updateEquivalentComposable(it.composable)
                                viewModel.updateComposableSyntax(it.syntax)
                                viewModel.updateComposableLink(it.link)
                            })
                        }
                    }

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
                title = { Text(stringResource(id = R.string.xml_to_compose)) },
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
                MainComposable()
            }
        }
        )

    }
}

private fun isInternetAvailable(context: Context): Boolean {
    val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val activeNetworkInfo = connectivityManager.activeNetworkInfo
    return activeNetworkInfo?.isConnectedOrConnecting ?: false
}
