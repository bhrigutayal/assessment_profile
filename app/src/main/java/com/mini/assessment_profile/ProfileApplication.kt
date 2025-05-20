package com.mini.assessment_profile

import android.app.Application
import com.mini.assessment_profile.data.AppDatabase
import com.mini.assessment_profile.data.UserProfileRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

class ProfileApplication : Application() {

    // Application scope
    private val applicationScope = CoroutineScope(SupervisorJob())

    // Database instance
    private val database by lazy { AppDatabase.getDatabase(this) }

    // Repository instance
    val repository by lazy { UserProfileRepository(database.userProfileDao()) }

    override fun onCreate() {
        super.onCreate()

        // Initialize the database with default data if empty
        applicationScope.launch {
            repository.initializeProfileIfEmpty()
        }
    }
}