package com.example.dessertclicker.ui

import androidx.annotation.DrawableRes

data class DessertUiState (
    @DrawableRes
    val currentDessertImageId: Int = 0,
    val dessertsSoldCount: Int = 0,
    val totalRevenue: Int = 0,
    val currentDessertPrice: Int = 5,
    val currentDessertIndex: Int = 0,
)