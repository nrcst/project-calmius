package com.l5.calmius.Navigation

interface NavGraph {
    val route : String
    val titleRes: Int
}

sealed class Screen(val route: String) {
    object Meditation : Screen("meditation")
    object Journal : Screen("journal")
}