package com.example.dessertclicker.ui

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import com.example.dessertclicker.R
import com.example.dessertclicker.data.Datasource
import com.example.dessertclicker.model.Dessert
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class DessertViewModel() : ViewModel() {

    var desserts: List<Dessert> = Datasource.dessertList

    private val _uiState = MutableStateFlow(DessertUiState())
    val uiState = _uiState.asStateFlow()

    init {
        determineDessertToShow()
    }


    fun determineDessertToShow(
        desserts: List<Dessert> = this.desserts,
        dessertsSold: Int = _uiState.value.dessertsSoldCount
    ): Dessert {
        var dessertToShow = desserts.first()
        for (dessert in desserts) {
            if (dessertsSold >= dessert.startProductionAmount) {
                dessertToShow = dessert
                updateCurrentDessert(dessertToShow.price, dessertToShow.imageId)
            } else {
                // The list of desserts is sorted by startProductionAmount. As you sell more desserts,
                // you'll start producing more expensive desserts as determined by startProductionAmount
                // We know to break as soon as we see a dessert who's "startProductionAmount" is greater
                // than the amount sold.
                break
            }
        }

        return dessertToShow
    }


    fun shareSoldDessertsInformation(
        intentContext: Context,
        dessertsSold: Int,
        revenue: Int
    ) {
        val sendIntent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(
                Intent.EXTRA_TEXT,
                intentContext.getString(R.string.share_text, dessertsSold, revenue)
            )
            type = "text/plain"
        }

        val shareIntent = Intent.createChooser(sendIntent, null)

        try {
            ContextCompat.startActivity(intentContext, shareIntent, null)
        } catch (e: ActivityNotFoundException) {
            Toast.makeText(
                intentContext,
                intentContext.getString(R.string.sharing_not_available),
                Toast.LENGTH_LONG
            ).show()
        }
    }


    fun incrementRevenue() {
        _uiState.update {
            it.copy(
                totalRevenue = it.totalRevenue + it.currentDessertPrice,
                dessertsSoldCount = it.dessertsSoldCount + 1
            )
        }
    }

    fun updateCurrentDessert(dessertPrice: Int, dessertImageId: Int) {
        _uiState.update {
            it.copy(
                currentDessertPrice = dessertPrice,
                currentDessertImageId = dessertImageId,
            )
        }
    }
}