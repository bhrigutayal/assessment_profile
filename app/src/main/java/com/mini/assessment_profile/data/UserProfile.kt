package com.mini.assessment_profile.data

import android.net.Uri
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Entity representing user profile data in the database
 */
@Entity(tableName = "user_profile")
data class UserProfile(
    @PrimaryKey(autoGenerate = false)
    val id: Int = 1, // Single user profile, so fixed ID
    val name: String,
    val email: String,
    val phone: String,
    val imageUri: String?,
    val memberSince: String,
    val creditScore: Int,
    val lifetimeCashback: Float,
    val coinsBalance: Int,
    val cashbackBalance: Float,
    val lastUpdated: Long = System.currentTimeMillis()
)