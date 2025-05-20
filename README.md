# CRED Profile UI Clone

This project is an Android application that recreates the Profile Activity UI from the CRED app. It's been implemented using Kotlin and XML layouts.

## Screenshots

<table>
  <tr>
    <td><img src="https://github.com/user-attachments/assets/900e4fd7-dd08-459d-809f-66fa7b8af12a" width="300"/></td>
    <td><img src="https://github.com/user-attachments/assets/6db08418-810b-45a9-82d7-c66eb72ac471" width="300"/></td>
    <td><img src="https://github.com/user-attachments/assets/e4fecb84-dac9-4628-a19a-74d5293f3315" width="300"/></td>
    <td><img src="https://github.com/user-attachments/assets/91c68e81-084c-4f0f-857c-ce499e538982" width="300"/></td>
    <td><img src="https://github.com/user-attachments/assets/04a30382-4013-4cc7-8648-5f7076bac569" width="300"/></td>
  </tr>
</table>

## Features

- Clean UI implementation using ConstraintLayout, CardView, and other Material Design components
- Custom rounded corners and circular backgrounds
- Vector icons for a crisp display on any screen density
- Properly structured layouts with appropriate spacing
- Dark theme implementation that matches the CRED app's aesthetic

## Implementation Details

### Technologies Used

- Kotlin programming language
- XML layouts for UI design
- Material Design components
- CircleImageView library for rounded profile image

### Project Structure

```
app/
├── build/
│   ├── classes/
│   │   └── com/mini/assessment_profile/
│   │       ├── CashbackActivity.class
│   │       ├── CreditScoreActivity.class
│   │       ├── GarageActivity.class
│   │       ├── MainActivity.class
│   │       ├── ProfileActivity*.class
│   │       ├── ProfileApplication.class
│   │       ├── ProfileViewModel*.class
│   │       ├── UpdateResult*.class
│   │       └── data/
│   │           ├── AppDatabase*.class
│   │           ├── UserProfile*.class
│   │           ├── UserProfileDao*.class
│   │           └── UserProfileRepository*.class
│   ├── debugAndroidTest/
│   │   └── com/mini/assessment_profile/
│   │       └── ExampleInstrumentedTest.class
│   └── debugUnitTest/
│       └── com/mini/assessment_profile/
│           └── ExampleUnitTest.class
│
├── src/
│   ├── main/
│   │   ├── AndroidManifest.xml
│   │   ├── java/com/mini/assessment_profile/
│   │   │   ├── CashBackActivity.kt
│   │   │   ├── CreditScoreActivity.kt
│   │   │   ├── GarageActivity.kt
│   │   │   ├── MainActivity.kt
│   │   │   ├── ProfileActivity.kt
│   │   │   ├── ProfileApplication.kt
│   │   │   ├── ProfileViewModel.kt
│   │   │   └── data/
│   │   │       ├── AppDatabase.kt
│   │   │       ├── UserProfile.kt
│   │   │       ├── UserProfileDao.kt
│   │   │       └── UserProfileRepository.kt
│   │   └── res/
│   │       ├── drawable/
│   │       │   ├── *.xml (icons & shapes)
│   │       ├── layout/
│   │       │   ├── activity_cash_back.xml
│   │       │   ├── activity_credit_score.xml
│   │       │   ├── activity_garage.xml
│   │       │   ├── activity_main.xml
│   │       │   ├── activity_profile.xml
│   │       │   ├── bottom_sheet_update_cashback.xml
│   │       │   ├── bottom_sheet_update_credit_score.xml
│   │       │   └── bottom_sheet_update_profile.xml
│   │       ├── mipmap-{anydpi,hdpi,mdpi,xhdpi,xxhdpi,xxxhdpi}/
│   │       │   ├── ic_launcher.* / ic_launcher_round.*
│   │       ├── values/
│   │       │   ├── colors.xml
│   │       │   ├── strings.xml
│   │       │   └── themes.xml
│   │       ├── values-night/
│   │       │   └── themes.xml
│   │       └── xml/
│   │           ├── backup_rules.xml
│   │           └── data_extraction_rules.xml
│
│   ├── androidTest/java/com/mini/assessment_profile/
│   │   └── ExampleInstrumentedTest.kt
│   └── test/java/com/mini/assessment_profile/
│       └── ExampleUnitTest.kt
│
└── gradle/

```

## Key UI Components

1. **Profile Section**
   - Circular profile image
   - User name and membership information
   - Edit button with circular background

2. **Vehicle/Garage Card**
   - CardView with dark background
   - Car icon with text and navigation arrow

3. **Profile Information Sections**
   - Credit score (757)
   - Lifetime cashback (₹3)
   - Bank balance section

4. **Rewards & Benefits Section**
   - Cashback balance (₹0)
   - Coins balance (26,46,583)
   - Referral opportunity

5. **Transactions & Support Section**
   - All transactions option

## Setup Instructions

1. Clone this repository:
```
git clone https://github.com/yourusername/cred-profile-ui.git
```

2. Open the project in Android Studio

3. Build and run the application on an emulator or physical device

## Dependencies

```groovy
dependencies {
    // Core Android dependencies
    implementation 'androidx.core:core-ktx:1.9.0'
    implementation 'androidx.appcompat:appcompat:1.5.1'
    implementation 'com.google.android.material:material:1.7.0'
    implementation 'androidx.constraintlayout:constraintlayout:2.1.4'
    
    // Circle ImageView for rounded profile picture
    implementation 'de.hdodenhof:circleimageview:3.1.0'
}
```

## Usage

When you run the app, you'll first see a simple home screen with a button to navigate to the Profile Activity. Clicking this button will take you to the fully implemented Profile UI that matches the design in the provided screenshot.
