package com.domingo.mahila_saftey.adapters

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.domingo.mahila_saftey.modules.Contact
import com.domingo.mahila_saftey.R
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class ContactAdapter(
    private val context: Context, private val itemList: MutableList<Contact>
) : RecyclerView.Adapter<ContactAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val firstName = view.findViewById<TextView>(R.id.txtRecycleFirstName)!!
        val contact = view.findViewById<TextView>(R.id.txtRecyclePhoneNumber)!!
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.contact_list, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val sharedPreferences = context.getSharedPreferences("Contact", Context.MODE_PRIVATE)

        val fullName = itemList[position].firstName + " " + itemList[position].surname
        holder.firstName.text = fullName
        holder.contact.text = itemList[position].phoneNumber

        holder.itemView.setOnClickListener {
            val builder: AlertDialog.Builder =
                AlertDialog.Builder(context, R.style.CustomAlertDialogTheme)
            builder.setTitle("Ready to make a call? ☎\uFE0F")


            builder.setPositiveButton("Sure") { _, _ ->

                if (ContextCompat.checkSelfPermission(
                        context, Manifest.permission.CALL_PHONE
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    ActivityCompat.requestPermissions(
                        context as Activity, arrayOf(Manifest.permission.CALL_PHONE), 101
                    )
                } else {
                    val intent = Intent(Intent.ACTION_CALL).apply {
                        data = Uri.parse("tel:${itemList[position].phoneNumber}")
                    }
                    context.startActivity(intent)
                }

            }

            builder.setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }

            builder.show()

        }
        holder.itemView.setOnLongClickListener {
            val editor = sharedPreferences.edit()

            // Remove contact from SharedPreferences based on its position
            val contactsJson = sharedPreferences.getString("contacts", "[]")
            val contactsList = Gson().fromJson(
                contactsJson, object : TypeToken<List<Contact>>() {}.type
            ) as MutableList<Contact>

            if (contactsList.isNotEmpty()) { // Check if the list is not empty
                contactsList.removeAt(position)
                editor.putString("contacts", Gson().toJson(contactsList))
                editor.apply()

                // Remove the contact from the list
                itemList.removeAt(position)
                notifyItemRemoved(position)
                Toast.makeText(
                    context,
                    "Contact removed successfully",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                Toast.makeText(
                    context, "Contact list is empty", Toast.LENGTH_SHORT
                ).show()
            }
            true
        }

    }

}

