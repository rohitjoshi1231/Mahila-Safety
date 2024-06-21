@file:Suppress("DEPRECATION")

package com.domingo.mahila_saftey.ui.activities

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.view.GestureDetector
import android.view.Gravity
import android.view.MotionEvent
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.domingo.mahila_saftey.R
import com.domingo.mahila_saftey.Repository
import com.domingo.mahila_saftey.Utils
import com.domingo.mahila_saftey.databinding.ActivityMainBinding
import com.domingo.mahila_saftey.ui.fragments.ContactFragment
import com.domingo.mahila_saftey.ui.fragments.HomeFragment
import com.domingo.mahila_saftey.ui.fragments.SettingsFragment
import com.domingo.mahila_saftey.viewmodels.MainActivityViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlin.math.abs

class MainActivity : AppCompatActivity() {
    private var isLocationEnable = false

    companion object {
        const val TAG = "MahilaSafety"
        const val REQUEST_CODE_PERMISSIONS = 1
    }

    private var isGestureEnabled = true
    private var isWhatsappIntentActive = false
    private var whatsappIntent: Intent? = null
    private lateinit var gestureDetector: GestureDetector
    private var job: Job? = null
    private val fusedLocationClient: FusedLocationProviderClient by lazy {
        LocationServices.getFusedLocationProviderClient(this)
    }
    private lateinit var binding: ActivityMainBinding
    private lateinit var toolbar: androidx.appcompat.widget.Toolbar
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        window.statusBarColor = Utils.customColor(this, R.color.app_background)
        toolbar = binding.toolbar
        loadFragment(HomeFragment(), "Home")

        sharedPreferences = getSharedPreferences("sharedContact", Context.MODE_PRIVATE)


        val builder: AlertDialog.Builder = AlertDialog.Builder(
            this@MainActivity, R.style.CustomAlertDialogTheme
        )


        binding.bottomNav.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.home -> loadFragment(HomeFragment(), "Home")
                R.id.contact -> loadFragment(ContactFragment(), "Contact")
                R.id.settings -> loadFragment(SettingsFragment(), "Settings")
            }
            true
        }


        fun tem(title: String, type: String) {
//        Image view
            val imageView = ImageView(this@MainActivity)
//        Edit text view
            val editText = TextView(this@MainActivity)
//        Layout
            val layout = LinearLayout(this@MainActivity)
            layout.orientation = LinearLayout.HORIZONTAL
            layout.gravity = Gravity.CENTER_HORIZONTAL
            layout.gravity = Gravity.CENTER_VERTICAL

            if (type == "call") {
                imageView.setImageResource(R.drawable.baseline_call_24)
            } else {
                imageView.setImageResource(R.drawable.whatsapp)
            }


            editText.text = title
            editText.setTextColor(
                ContextCompat.getColor(
                    this@MainActivity, R.color.white
                )
            )

            editText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20f)

            val params = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT
            )
            params.setMargins(16, 70, 10, 20)
            imageView.layoutParams = params
            editText.layoutParams = params

            layout.addView(imageView)
            layout.addView(editText)

            builder.setView(layout)

        }

        requestPermissions()

        if (!requestPermissions()) {
            Toast.makeText(this, "All permission are required", Toast.LENGTH_SHORT).show()
        } else {
            requestPermissions()
        }

        gestureDetector = GestureDetector(this, object : GestureDetector.SimpleOnGestureListener() {
            @SuppressLint("SetTextI18n")
            override fun onLongPress(e: MotionEvent) {
                super.onLongPress(e)
                tem("Send your current location", "")
                builder.setPositiveButton("Sure") { _, _ ->
                    getLocation()
                }

                builder.setNegativeButton("Cancel") { dialog, _ ->
                    dialog.dismiss()
                }

                builder.show()
            }

            @SuppressLint("SetTextI18n")
            override fun onScroll(
                e1: MotionEvent?, e2: MotionEvent, distanceX: Float, distanceY: Float,
            ): Boolean {
                if (abs(distanceY) > abs(distanceX)) {
                    tem("Call Now", "call")

                    builder.setPositiveButton("Sure") { _, _ ->
                        val contact = sharedPreferences.getString("sharedNumber", "")

                        if (contact != "") {
                            makeCall(contact.toString())
                        } else {
                            Toast.makeText(
                                this@MainActivity, "No contact to call", Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                    builder.setNegativeButton("Cancel") { dialog, _ ->
                        dialog.dismiss()
                    }

                    builder.show()
                }
                return super.onScroll(e1, e2, distanceX, distanceY)

            }


        })

        if (isLocationEnable) {
            Toast.makeText(
                this, "Location services are disabled, please enable location", Toast.LENGTH_SHORT
            ).show()
        }

// Call and Send Location button

        binding.call.setOnClickListener {

            tem("Call Now", "call")

            builder.setPositiveButton("Sure") { _, _ ->
                val contact = sharedPreferences.getString("sharedNumber", "")

                if (contact != "") {
                    makeCall(contact.toString())
                } else {
                    Toast.makeText(
                        this@MainActivity, "No contact to call", Toast.LENGTH_SHORT
                    ).show()
                }
            }
            builder.setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }

            builder.show()


        }

        binding.location.setOnClickListener {

            tem("Send your current location", "")
            builder.setPositiveButton("Sure") { _, _ ->
                getLocation()
            }

            builder.setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }

            builder.show()

        }


    }

    private fun loadFragment(fragment: Fragment, title: String) {
        setUpToolbar(title)
        supportFragmentManager.beginTransaction().replace(R.id.container, fragment).commit()
    }

    private fun setUpToolbar(title: String) {
        setSupportActionBar(toolbar)
        supportActionBar?.title = title
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        // Pass touch events to gesture detector
        if (event != null && isGestureEnabled) {
            gestureDetector.onTouchEvent(event)
        }
        return super.onTouchEvent(event)
    }

    private fun requestPermissions(): Boolean {
        val permissionsToRequest = mutableListOf<String>()

        // Add permissions you need to request
        val permissions = arrayOf(
            Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.CALL_PHONE
        )

        permissions.forEach { permission ->
            if (ContextCompat.checkSelfPermission(
                    this, permission
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                permissionsToRequest.add(permission)
            }
        }

        // Request all permissions together if any are missing
        return if (permissionsToRequest.isNotEmpty()) {
            ActivityCompat.requestPermissions(
                this, permissionsToRequest.toTypedArray(), REQUEST_CODE_PERMISSIONS
            )
            false // Permissions are not granted
        } else {
            true // All permissions are granted
        }
    }


    private fun getLocation() {

        val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
                LocationManager.NETWORK_PROVIDER
            )
        ) {
            job = CoroutineScope(Dispatchers.IO).launch {
                while (isActive) {
                    isWhatsappIntentActive = true
                    startLocationUpdates()
                    delay(5000)
                }
            }
        } else {
            Toast.makeText(this, "Location services are disabled", Toast.LENGTH_SHORT).show()
        }

    }

    fun makeCall(number: String) {
        Repository().makeCall(number, "call", "") { intent ->
            startActivity(intent)

        }
    }

    private fun sendWhatsapp(number: String, message: String) {
        try {
            Repository().makeCall(number, "whatsapp", message) { intent ->
                startActivity(intent)
            }
        } catch (e: Exception) {
            Log.e(
                TAG, "Error opening WhatsApp: ${e.message}"
            )
            Toast.makeText(this, "Error opening WhatsApp", Toast.LENGTH_SHORT).show()
        }
    }

    private fun startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }

        fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
            val sharedPreferences: SharedPreferences =
                getSharedPreferences("sharedContact", Context.MODE_PRIVATE)
            // Handle the retrieved location
            if (location != null && isWhatsappIntentActive) { // Ensure WhatsApp intent is not active
                isWhatsappIntentActive = true
                val latitude = location.latitude
                val longitude = location.longitude
                val url = "https://www.google.com/maps?q=$latitude,$longitude"
                val time = MainActivityViewModel().getCurrentTime()
                val message = "$url\n$time"
                val contactNumber = sharedPreferences.getString("sharedNumber", "")

                if (contactNumber != null) {
                    sendWhatsapp(contactNumber, message)
                } else {
                    Toast.makeText(this@MainActivity, "Contact is Empty", Toast.LENGTH_SHORT).show()
                }
            }

        }.addOnFailureListener { exception: Exception ->
            Log.e("Error", exception.message.toString())
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        if (isWhatsappIntentActive) {
            // WhatsApp intent is active, cancel it
            isWhatsappIntentActive = false
            job?.cancel()
            Toast.makeText(this, "WhatsApp message sending cancelled", Toast.LENGTH_SHORT).show()
        } else {
            // WhatsApp intent is not active, proceed with default back button behavior
            super.onBackPressed()
        }
    }

    override fun onDestroy() {
        job?.cancel()
        // Reset WhatsApp intent state
        isWhatsappIntentActive = false
        whatsappIntent = null

        super.onDestroy()
    }

    override fun onPause() {
        isWhatsappIntentActive = false
        job?.cancel()
        super.onPause()
    }

    fun enableGestureDetection() {
        isGestureEnabled = true
    }

    fun disableGestureDetection() {
        isGestureEnabled = false
    }

}
