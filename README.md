# BLE Scanner

![BLE Scanner App](/images/scanning_mockup.png)

## General
Bluetooth Low Energy scanner app written in Kotlin using Material3 Adaptive Theming and Jetpack Compose. This application was written as part of a semester project. It was developed for Android 13 and was not tested on prior versions of Android - we can thus not guarantee that it will work on older versions of Android.

## Usage
To use the application, download the .apk file from the latest release. Please allow all permissions that are asked (nearby devices and fine location). These permissions are required by Android for an app to be able to use Bluetooth/Bluetooth Low Energy functionality. 

The application functions as a normal Bluetooth Low Energy Scanner for any device, displaying it's name (when defined), it's Address, RSSI and its bonding status, among other information. For the MS1089 temperature sensor by Microdul, the app also has specific functionality to work with it. It recognizes the device when advertised under the name "MS1089" and calculates the temperature advertised by the sensor. 

## Acknowledgments
The mockups shown in this README were generated under the Creative Commons 2.5 Attribution License using
the device art generator created by the Android Open Source Projects available [here](https://developer.android.com/distribute/marketing-tools/device-art-generator) (accessed on
23.07.2023)

![https://img.shields.io/badge/Android-3DDC84?style=for-the-badge&logo=android&logoColor=white](https://img.shields.io/badge/Android-3DDC84?style=for-the-badge&logo=android&logoColor=white) ![https://img.shields.io/badge/Kotlin-0095D5?&style=for-the-badge&logo=kotlin&logoColor=white](https://img.shields.io/badge/Kotlin-0095D5?&style=for-the-badge&logo=kotlin&logoColor=white)
