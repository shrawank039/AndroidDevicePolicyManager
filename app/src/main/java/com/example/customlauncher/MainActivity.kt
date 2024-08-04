package com.example.customlauncher

import android.app.admin.DevicePolicyManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var devicePolicyManager: DevicePolicyManager
    private lateinit var compName: ComponentName

    private val duressPassword = "12345" // Replace with your duress password
    private val correctPassword = "54321" // Replace with your correct password

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        devicePolicyManager = getSystemService(Context.DEVICE_POLICY_SERVICE) as DevicePolicyManager
        compName = ComponentName(this, AdminReceiver::class.java)

        val passwordInput = findViewById<EditText>(R.id.passwordInput)
        val loginButton = findViewById<Button>(R.id.loginButton)

        loginButton.setOnClickListener {
            val enteredPassword = passwordInput.text.toString()
            if (enteredPassword == duressPassword) {
                Log.d("MainActivity", "Duress password entered, wiping data.")
               // devicePolicyManager.lockNow()
                devicePolicyManager.wipeData(DevicePolicyManager.WIPE_EXTERNAL_STORAGE)
            } else if (enteredPassword == correctPassword) {
                Log.d("MainActivity", "Correct password entered.")
                // Proceed with normal operations
                passwordInput.setText("")
            } else {
                Log.d("MainActivity", "Incorrect password entered.")
                // Handle incorrect password
                passwordInput.setText("")
            }
        }

        enableDeviceAdmin()
    }

    private fun enableDeviceAdmin() {
        val intent = Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN).apply {
            putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, compName)
            putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION, "Device Admin Required for Security Features")
        }
        startActivityForResult(intent, 1)
    }
}
