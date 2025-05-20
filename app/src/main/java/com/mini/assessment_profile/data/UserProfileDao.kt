package com.mini.assessment_profile.data

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

/**
 * Data Access Object for UserProfile entity
 */
@Dao
interface UserProfileDao {

    @Query("SELECT * FROM user_profile WHERE id = 1")
    fun getUserProfile(): LiveData<UserProfile>

    @Query("SELECT * FROM user_profile WHERE id = 1")
    suspend fun getUserProfileSync(): UserProfile?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProfile(profile: UserProfile)

    @Update
    suspend fun updateProfile(profile: UserProfile)

    @Query("UPDATE user_profile SET name = :name, email = :email, phone = :phone, lastUpdated = :timestamp WHERE id = 1")
    suspend fun updateProfileInfo(name: String, email: String, phone: String, timestamp: Long = System.currentTimeMillis())

    @Query("UPDATE user_profile SET imageUri = :imageUri, lastUpdated = :timestamp WHERE id = 1")
    suspend fun updateProfileImage(imageUri: String, timestamp: Long = System.currentTimeMillis())

    @Query("UPDATE user_profile SET creditScore = :score, lastUpdated = :timestamp WHERE id = 1")
    suspend fun updateCreditScore(score: Int, timestamp: Long = System.currentTimeMillis())

    @Query("UPDATE user_profile SET lifetimeCashback = :cashback, lastUpdated = :timestamp WHERE id = 1")
    suspend fun updateLifetimeCashback(cashback: Float, timestamp: Long = System.currentTimeMillis())
}