package com.mini.assessment_profile.data

import android.net.Uri
import androidx.lifecycle.LiveData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Repository for managing user profile data
 */
class UserProfileRepository(private val userProfileDao: UserProfileDao) {

    // Get user profile as LiveData
    val userProfile: LiveData<UserProfile> = userProfileDao.getUserProfile()

    /**
     * Initialize the database with default profile if empty
     */
    suspend fun initializeProfileIfEmpty() {
        withContext(Dispatchers.IO) {
            val currentProfile = userProfileDao.getUserProfileSync()
            if (currentProfile == null) {
                val defaultProfile = UserProfile(
                    name = "Bhrigu Tayal",
                    email = "bhrigutayal053@gmail.com",
                    phone = "7982475557",
                    imageUri = "",
                    memberSince = "May, 2025",
                    creditScore = 757,
                    lifetimeCashback = 3f,
                    coinsBalance = 2646583,
                    cashbackBalance = 0f
                )
                userProfileDao.insertProfile(defaultProfile)
            }
        }
    }

    /**
     * Update user profile information
     */
    suspend fun updateProfileInfo(name: String, email: String, phone: String) {
        withContext(Dispatchers.IO) {
            userProfileDao.updateProfileInfo(name, email, phone)
        }
    }

    /**
     * Update profile image
     */
    suspend fun updateProfileImage(imageUri: String) {
        withContext(Dispatchers.IO) {
            userProfileDao.updateProfileImage(imageUri)
        }
    }

    /**
     * Update credit score
     */
    suspend fun updateCreditScore(score: Int) {
        withContext(Dispatchers.IO) {
            userProfileDao.updateCreditScore(score)
        }
    }

    /**
     * Update lifetime cashback
     */
    suspend fun updateLifetimeCashback(cashback: Float) {
        withContext(Dispatchers.IO) {
            userProfileDao.updateLifetimeCashback(cashback)
        }
    }
}