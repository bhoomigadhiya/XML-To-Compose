package com.example.xmltocompose

import android.content.Context
import com.example.xmltocompose.models.WidgetInfo
import com.google.gson.Gson


object DataManager {

    // The data array to store WidgetInfo objects
    var data = emptyArray<WidgetInfo>()

    // Function to load data from a JSON file in assets folder
    fun loadAssetsFromFile(context: Context) {

        // Open and read the JSON file from assets
        val inputStream = context.assets.open("widgets.json")
        val size = inputStream.available()
        val buffer = ByteArray(size)
        inputStream.read(buffer)
        inputStream.close()

        // Convert the JSON data to a string
        val json = String(buffer, Charsets.UTF_8)

        // Initialize Gson to parse JSON data
        val gson = Gson()

        // Parse JSON data and populate the data array
        data = gson.fromJson(json, Array<WidgetInfo>::class.java)

        // Sort the data array by widget name in alphabetical order (A to Z)
        data= data.sortedBy { it.widget }.toTypedArray()
    }
}