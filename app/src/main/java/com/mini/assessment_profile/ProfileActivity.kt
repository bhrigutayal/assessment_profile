package com.mini.assessment_profile

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import de.hdodenhof.circleimageview.CircleImageView
import java.io.File
import java.io.FileOutputStream

class ProfileActivity : AppCompatActivity() {

    private lateinit var profileImage: CircleImageView
    private lateinit var nameTextView: TextView
    private lateinit var membershipTextView: TextView
    private lateinit var editButton: ImageView
    private lateinit var updateProfileButton: TextView
    private lateinit var progressBar: ProgressBar
    private lateinit var creditScoreTextView: TextView
    private lateinit var lifetimeCashbackTextView: TextView

    private val viewModel: ProfileViewModel by viewModels()

    // Activity result launcher for selecting an image
    private val pickImageLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        if(uri != null) {
            val savedImagePath = saveImageFromUriToInternalStorage(this, uri!!)
            profileImage.setImageURI(uri)
            viewModel.updateProfileImage(savedImagePath!!)
        }
    }
    fun saveImageFromUriToInternalStorage(context: Context, imageUri: Uri): String? {
        return try {
            // 1. Convert URI to Bitmap
            val bitmap: Bitmap = MediaStore.Images.Media.getBitmap(context.contentResolver, imageUri)

            // 2. Create a unique file name, e.g. "profile_image_<timestamp>.png"
            val fileName = "profile_image_${System.currentTimeMillis()}.png"
            val file = File(context.filesDir, fileName)

            // 3. Save Bitmap to file as PNG
            FileOutputStream(file).use { outStream ->
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, outStream)
            }

            // 4. Return saved file's absolute path
            file.absolutePath

        } catch (e: Exception) {
            e.printStackTrace()
            null // Return null if any failure occurs
        }
    }

    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            if (isGranted) {
                pickImageLauncher.launch("image/*")
            } else {
                Toast.makeText(this, "Storage permission denied!", Toast.LENGTH_SHORT).show()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        // Initialize views
        profileImage = findViewById(R.id.profile_image)
        nameTextView = findViewById(R.id.tv_name)
        membershipTextView = findViewById(R.id.tv_membership)
        editButton = findViewById(R.id.iv_edit)
        updateProfileButton = findViewById(R.id.tv_support)

        // Add these views to your layout if not already present
        creditScoreTextView = findViewById(R.id.tv_credit_score)
        lifetimeCashbackTextView = findViewById(R.id.tv_lifetime_cashback)

        // Add a progress bar (you'll need to add this to your layout)
        progressBar = findViewById<ProgressBar>(R.id.progress_bar).apply {
            visibility = View.GONE
        }

        // Setup toolbar with back button
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
            setDisplayShowTitleEnabled(false)
        }

        window.statusBarColor = ContextCompat.getColor(this, R.color.black)

        // Set up observers
        setupObservers()

        val garageButton = findViewById<ImageView>(R.id.garage_arrow)

        garageButton.setOnClickListener {
            val intent = Intent(this, GarageActivity::class.java)
            startActivity(intent)
        }

        val creditScoreButton = findViewById<TextView>(R.id.tv_credit_score)

        creditScoreButton.setOnClickListener {
            val intent = Intent(this, CreditScoreActivity::class.java)
            startActivity(intent)
        }


        // Add click listener to edit button
        editButton.setOnClickListener {
            showUpdateProfileOptions()
        }

        // Configure update profile button
        updateProfileButton.apply {
            text = "Update Profile"
            setOnClickListener {
                showProfileUpdateDialog()
            }
        }
    }

    private fun setupObservers() {
        // Observe user profile data changes from repository
        viewModel.userProfile.observe(this) { profile ->
            profile?.let {
                // Update UI with profile data
                nameTextView.text = it.name
                membershipTextView.text = it.memberSince ?: "Standard Member"

                // Set profile image if available
                it.imageUri?.let { uri ->
                    val bitmap = BitmapFactory.decodeFile(uri)
                    if(bitmap != null) {
                        profileImage.setImageBitmap(bitmap)
                    } else {
                        // fallback or placeholder if bitmap couldn't be decoded
                        profileImage.setImageResource(R.drawable.profile_placeholder)
                    }

                }

                // Update credit score and cashback if views are present
                creditScoreTextView.text = "${it.creditScore}"
                lifetimeCashbackTextView.text = "${it.lifetimeCashback}"
            }
        }

        // Observe update status
        viewModel.updateStatus.observe(this) { result ->
            when (result) {
                is UpdateResult.Loading -> {
                    progressBar.visibility = View.VISIBLE
                    updateProfileButton.isEnabled = false
                }
                is UpdateResult.Success -> {
                    progressBar.visibility = View.GONE
                    updateProfileButton.isEnabled = true
                    Snackbar.make(findViewById(android.R.id.content), result.message, Snackbar.LENGTH_SHORT).show()
                }
                is UpdateResult.Error -> {
                    progressBar.visibility = View.GONE
                    updateProfileButton.isEnabled = true
                    Snackbar.make(findViewById(android.R.id.content), result.message, Snackbar.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun showUpdateProfileOptions() {
        val options = arrayOf("Update Profile Picture", "Update Profile Information", "Update Credit Score", "Update Lifetime Cashback")
        AlertDialog.Builder(this)
            .setTitle("Update Profile")
            .setItems(options) { _, which ->
                when (which) {
                    0 -> openGallery()
                    1 -> showProfileUpdateDialog()
                    2 -> showCreditScoreUpdateDialog()
                    3 -> showLifetimeCashbackUpdateDialog()
                }
            }
            .show()
    }
    private fun checkPermissionAndPickImage() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            // Android 13+ only needs READ_MEDIA_IMAGES
            requestPermissionLauncher.launch(Manifest.permission.READ_MEDIA_IMAGES)
        } else {
            // Android 12 and below needs READ_EXTERNAL_STORAGE
            requestPermissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
        }
    }

    private fun openGallery() {
        checkPermissionAndPickImage()
    }

    private fun showProfileUpdateDialog() {
        val dialog = BottomSheetDialog(this)
        val view = layoutInflater.inflate(R.layout.bottom_sheet_update_profile, null)

        val nameEditText = view.findViewById<TextInputEditText>(R.id.et_name)
        val emailEditText = view.findViewById<TextInputEditText>(R.id.et_email)
        val phoneEditText = view.findViewById<TextInputEditText>(R.id.et_phone)
        val updateButton = view.findViewById<Button>(R.id.btn_update_profile)

        // Pre-fill with current data from user profile
        viewModel.userProfile.value?.let { profile ->
            nameEditText.setText(profile.name)
            emailEditText.setText(profile.email)
            phoneEditText.setText(profile.phone)
        }

        updateButton.setOnClickListener {
            val newName = nameEditText.text.toString()
            val email = emailEditText.text.toString()
            val phone = phoneEditText.text.toString()

            if (newName.isBlank()) {
                nameEditText.error = "Name cannot be empty"
                return@setOnClickListener
            }

            if (!isValidEmail(email) && email.isNotBlank()) {
                emailEditText.error = "Please enter a valid email"
                return@setOnClickListener
            }

            // Update profile information via ViewModel
            viewModel.updateProfileInfo(newName, email, phone)
            dialog.dismiss()
        }

        dialog.setContentView(view)
        dialog.show()
    }

    private fun showCreditScoreUpdateDialog() {
        val dialog = BottomSheetDialog(this)
        val view = layoutInflater.inflate(R.layout.bottom_sheet_update_credit_score, null)

        val creditScoreEditText = view.findViewById<TextInputEditText>(R.id.et_credit_score)
        val updateButton = view.findViewById<Button>(R.id.btn_update_credit_score)

        // Pre-fill with current credit score if available
        viewModel.userProfile.value?.creditScore?.let {
            creditScoreEditText.setText(it.toString())
        }

        updateButton.setOnClickListener {
            val creditScoreText = creditScoreEditText.text.toString()

            if (creditScoreText.isBlank()) {
                creditScoreEditText.error = "Credit score cannot be empty"
                return@setOnClickListener
            }

            try {
                val creditScore = creditScoreText.toInt()
                if (creditScore < 300 || creditScore > 850) {
                    creditScoreEditText.error = "Credit score must be between 300 and 850"
                    return@setOnClickListener
                }

                viewModel.updateCreditScore(creditScore)
                dialog.dismiss()
            } catch (e: NumberFormatException) {
                creditScoreEditText.error = "Please enter a valid number"
            }
        }

        dialog.setContentView(view)
        dialog.show()
    }

    private fun showLifetimeCashbackUpdateDialog() {
        val dialog = BottomSheetDialog(this)
        val view = layoutInflater.inflate(R.layout.bottom_sheet_update_cashback, null)

        val cashbackEditText = view.findViewById<TextInputEditText>(R.id.et_cashback)
        val updateButton = view.findViewById<Button>(R.id.btn_update_cashback)

        // Pre-fill with current cashback if available
        viewModel.userProfile.value?.lifetimeCashback?.let {
            cashbackEditText.setText(it.toString())
        }

        updateButton.setOnClickListener {
            val cashbackText = cashbackEditText.text.toString()

            if (cashbackText.isBlank()) {
                cashbackEditText.error = "Cashback amount cannot be empty"
                return@setOnClickListener
            }

            try {
                val cashback = cashbackText.toFloat()
                if (cashback < 0) {
                    cashbackEditText.error = "Cashback cannot be negative"
                    return@setOnClickListener
                }

                viewModel.updateLifetimeCashback(cashback)
                dialog.dismiss()
            } catch (e: NumberFormatException) {
                cashbackEditText.error = "Please enter a valid number"
            }
        }

        dialog.setContentView(view)
        dialog.show()
    }

    private fun isValidEmail(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            onBackPressed()
            return true
        }
        return super.onOptionsItemSelected(item)
    }
    override fun attachBaseContext(newBase: Context) {
        adjustFontScale(newBase, this@ProfileActivity)
        super.attachBaseContext(newBase)
    }
    fun adjustFontScale(newBase: Context, context: Context) {
        if (newBase.resources.configuration.fontScale > 0.9) {
            val override = Configuration(newBase.resources.configuration)
            override.fontScale = 0.9f
            (context as Activity).applyOverrideConfiguration(override)
        }
    }
}