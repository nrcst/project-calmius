package com.l5.calmius.features.meditation

import android.app.Application
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import meditation.data.DatabaseProvider

class MeditationApp : Application() {
    private val applicationScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    val database by lazy { DatabaseProvider.getDatabase(this, applicationScope) }
}
