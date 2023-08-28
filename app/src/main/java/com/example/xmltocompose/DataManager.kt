package com.example.xmltocompose

import android.content.Context
import com.example.xmltocompose.models.WidgetInfo
import com.google.gson.Gson
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL


object DataManager {

    // The data array to store WidgetInfo objects
    var data = emptyArray<WidgetInfo>()

    // Function to load data from a JSON file in assets folder
    fun loadAssetsFromUrl() {

        val response = fetchJsonFromUrl()

        // Initialize Gson to parse JSON data
        val gson = Gson()

        // Parse JSON data and populate the data array
        data = gson.fromJson(response, Array<WidgetInfo>::class.java)

        // Sort the data array by widget name in alphabetical order (A to Z)
        data = data.sortedBy { it.widget }.toTypedArray()
    }

    // Function to load data from a JSON file in assets folder

    private fun fetchJsonFromUrl(): String {
        val connection =
            URL("https://raw.githubusercontent.com/bhoomi0104/XML-To-Compose/master/assets/widgets.json").openConnection() as HttpURLConnection
        connection.requestMethod = "GET"
        connection.connect()

        val responseCode = connection.responseCode
        if (responseCode == HttpURLConnection.HTTP_OK) {
            val reader = BufferedReader(InputStreamReader(connection.inputStream))
            val response = StringBuilder()
            var line: String?
            while (reader.readLine().also { line = it } != null) {
                response.append(line)
            }
            reader.close()
            return response.toString()
        } else {
            throw Exception("Failed to fetch data from URL")
        }
    }

}