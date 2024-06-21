package com.domingo.mahila_saftey.ui.adapters

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.domingo.mahila_saftey.R

class EmergencyContactAdapter(
    private val context: Context,
    private val data: HashMap<String, String>

) : RecyclerView.Adapter<EmergencyContactAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val contactDescription = view.findViewById<TextView>(R.id.contact)!!
        val contact = view.findViewById<TextView>(R.id.txtContactDescription)!!
        val call = view.findViewById<ImageView>(R.id.loginBtn)!!
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.emergency_contact_layout, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount() = data.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val keys = ArrayList(data.keys)
        val key = keys[position]
        val value = data[key]
        holder.contact.text = key
        holder.contactDescription.text = value

        holder.call.setOnClickListener {
            if (ContextCompat.checkSelfPermission(
                    context, Manifest.permission.CALL_PHONE
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    context as Activity, arrayOf(Manifest.permission.CALL_PHONE), 101
                )
            } else {
                val intent = Intent(Intent.ACTION_CALL).apply {
                    Log.d("Mahila", value.toString())
                    data = Uri.parse("tel:$value")
                }
                context.startActivity(intent)
            }
        }
    }
}
