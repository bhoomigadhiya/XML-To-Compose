package com.example.xmltocompose.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MainViewModel : ViewModel() {

    val isExpanded = MutableLiveData<Boolean>()
    val selectedWidget = MutableLiveData<String>()
    val equivalentComposable = MutableLiveData<String>()
    val composableSyntax = MutableLiveData<String>()
    val composableLink = MutableLiveData<String>()

    fun updateExpandedValue(isExpandedState: Boolean) {
        isExpanded.value = isExpandedState
    }

    fun updateSelectedWidget(widget: String) {
        selectedWidget.value = widget
    }

    fun updateEquivalentComposable(composable: String) {
        equivalentComposable.value = composable
    }

    fun updateComposableSyntax(syntax: String) {
        composableSyntax.value = syntax
    }

    fun updateComposableLink(link: String) {
        composableLink.value = link
    }


}