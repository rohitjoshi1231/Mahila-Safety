package com.domingo.mahila_saftey.activities

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.net.Uri
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
import com.domingo.mahila_saftey.Utils
import com.domingo.mahila_saftey.databinding.ActivityMainBinding
import com.domingo.mahila_saftey.fragments.ContactFragment
import com.domingo.mahila_saftey.fragments.HomeFragment
import com.domingo.mahila_saftey.fragments.SettingsFragment
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import java.net.URLEncoder
import java.util.Calendar
import java.util.Locale
import kotlin.math.abs

@Suppress("DEPRECATION")
class MainActivity : AppCompatActivity() {
    private var isLocationEnable = false

    companion object {
        const val TAG = "MahilaSafety"
        const val REQUEST_LOCATION_PERMISSION = 1
    }

    var isGestureEnabled = true

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

        sharedPreferences =
            getSharedPreferences("sharedContact", Context.MODE_PRIVATE)

        binding.bottomNav.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.home -> loadFragment(HomeFragment(), "Home")
                R.id.contact -> loadFragment(ContactFragment(), "Contact")
                R.id.settings -> loadFragment(SettingsFragment(), "Settings")
            }
            true
        }

        gestureDetector = GestureDetector(this, object : GestureDetector.SimpleOnGestureListener() {
            override fun onLongPress(e: MotionEvent) {
                super.onLongPress(e)


                val builder: AlertDialog.Builder =
                    AlertDialog.Builder(this@MainActivity, R.style.CustomAlertDialogTheme)

                val layout = LinearLayout(this@MainActivity)
                layout.orientation = LinearLayout.HORIZONTAL
                layout.gravity = Gravity.CENTER_HORIZONTAL
                layout.gravity = Gravity.CENTER_VERTICAL


                val imageView = ImageView(this@MainActivity)
                imageView.setImageResource(R.drawable.whatsapp)

                val editText = TextView(this@MainActivity)
                editText.text = "Send your current location"
                editText.setTextColor(
                    ContextCompat.getColor(
                        this@MainActivity, R.color.white
                    )
                )
                editText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20f)

                val params = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                )
                params.setMargins(16, 70, 10, 20)
                imageView.layoutParams = params
                editText.layoutParams = params

                layout.addView(imageView)
                layout.addView(editText)

                builder.setView(layout)



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
                    if (distanceY > 0) {
                        val builder: AlertDialog.Builder =
                            AlertDialog.Builder(this@MainActivity, R.style.CustomAlertDialogTheme)

                        val layout = LinearLayout(this@MainActivity)
                        layout.orientation = LinearLayout.HORIZONTAL
                        layout.gravity = Gravity.CENTER_HORIZONTAL
                        layout.gravity = Gravity.CENTER_VERTICAL


                        val imageView = ImageView(this@MainActivity)
                        imageView.setImageResource(R.drawable.baseline_call_24)

                        val editText = TextView(this@MainActivity)
                        editText.text = "Call Now"
                        editText.setTextColor(
                            ContextCompat.getColor(
                                this@MainActivity, R.color.white
                            )
                        )
                        editText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20f)

                        val params = LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.WRAP_CONTENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT
                        )
                        params.setMargins(16, 70, 10, 20)
                        imageView.layoutParams = params
                        editText.layoutParams = params

                        layout.addView(imageView)
                        layout.addView(editText)

                        builder.setView(layout)

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
                }
                return super.onScroll(e1, e2, distanceX, distanceY)
            }
        })

        if (isLocationEnable) {
            Toast.makeText(
                this, "Location services are disabled, please enable location", Toast.LENGTH_SHORT
            ).show()
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


    private fun getLocation() {
        if (ContextCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), REQUEST_LOCATION_PERMISSION
            )
        } else {
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
    }

    fun makeCall(number: String) {
        if (ContextCompat.checkSelfPermission(
                this, Manifest.permission.CALL_PHONE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this, arrayOf(Manifest.permission.CALL_PHONE), 101
            )
        } else {
            val intent = Intent(Intent.ACTION_CALL).apply {
                data = Uri.parse("tel:$number")
            }
            startActivity(intent)
        }
    }

    private fun sendWhatsapp(number: String, message: String) {
        try {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(
                "whatsapp://send?phone=$number&text=${
                    URLEncoder.encode(
                        message, "UTF-8"
                    )
                }"
            )
            startActivity(intent)
        } catch (e: Exception) {
            Log.e(TAG, "Error opening WhatsApp: ${e.message}")
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
                val time = getCurrentTime()
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

    private fun getCurrentTime(): String {
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault())
        val day = c.get(Calendar.DAY_OF_MONTH)
        val hour = c.get(Calendar.HOUR_OF_DAY)
        val minute = c.get(Calendar.MINUTE)
        val second = c.get(Calendar.SECOND)
        val amOrPm = c.get(Calendar.AM_PM)
        val period = if (amOrPm == Calendar.AM) "AM" else "PM"

        return "$day/$month/$year - $hour:$minute:$second $period"
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
