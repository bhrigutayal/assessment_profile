package com.mini.assessment_profile

import android.app.Application
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.mini.assessment_profile.data.UserProfile
import kotlinx.coroutines.launch

class ProfileViewModel(application: Application) : AndroidViewModel(application) {

    // Get repository from Application class
    private val repository = (application as ProfileApplication).repository

    // Profile information from repository
    val userProfile: LiveData<UserProfile> = repository.userProfile

    // Update status LiveData
    private val _updateStatus = MutableLiveData<UpdateResult>()
    val updateStatus: LiveData<UpdateResult> = _updateStatus

    /**
     * Update profile information and save to database
     */
    fun updateProfileInfo(name: String, email: String, phone: String) {
        viewModelScope.launch {
            _updateStatus.value = UpdateResult.Loading

            try {
                repository.updateProfileInfo(name, email, phone)
                _updateStatus.value = UpdateResult.Success("Profile updated successfully")
            } catch (e: Exception) {
                _updateStatus.value = UpdateResult.Error("Failed to update profile: ${e.message}")
            }
        }
    }

    /**
     * Update profile image and save to database
     */
    fun updateProfileImage(uri: String) {
        viewModelScope.launch {
            _updateStatus.value = UpdateResult.Loading

            try {
                repository.updateProfileImage(uri)
                _updateStatus.value = UpdateResult.Success("Profile picture updated successfully")
            } catch (e: Exception) {
                _updateStatus.value = UpdateResult.Error("Failed to update profile picture: ${e.message}")
            }
        }
    }

    /**
     * Update credit score and save to database
     */
    fun updateCreditScore(score: Int) {
        viewModelScope.launch {
            try {
                repository.updateCreditScore(score)
            } catch (e: Exception) {
                // Handle error
            }
        }
    }

    /**
     * Update lifetime cashback and save to database
     */
    fun updateLifetimeCashback(cashback: Float) {
        viewModelScope.launch {
            try {
                repository.updateLifetimeCashback(cashback)
            } catch (e: Exception) {
                // Handle error
            }
        }
    }
}

// Sealed class for update results
sealed class UpdateResult {
    object Loading : UpdateResult()
    data class Success(val message: String) : UpdateResult()
    data class Error(val message: String) : UpdateResult()
}