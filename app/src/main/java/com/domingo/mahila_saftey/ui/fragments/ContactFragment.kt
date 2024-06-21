package com.domingo.mahila_saftey.ui.fragments

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import android.text.InputType
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.domingo.mahila_saftey.R
import com.domingo.mahila_saftey.Utils
import com.domingo.mahila_saftey.ui.adapters.ContactAdapter
import com.domingo.mahila_saftey.databinding.FragmentContactBinding
import com.domingo.mahila_saftey.ui.modules.Contact
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class ContactFragment : Fragment() {
    private lateinit var binding: FragmentContactBinding
    private val mContactList = mutableListOf<Contact>()
    private var contactAdapter: ContactAdapter? = null
    private lateinit var sharedPreferences: SharedPreferences
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?,
    ): View {
        binding = FragmentContactBinding.inflate(inflater, container, false)

        val lgn = binding.loginBtn
        val contactList = binding.contactRecyclerView

        sharedPreferences = requireContext().getSharedPreferences("Contact", Context.MODE_PRIVATE)

        // Load existing contacts from SharedPreferences
        val contactsJson = sharedPreferences.getString("contacts", "[]")
        val contactsList = Gson().fromJson<List<Contact>>(
            contactsJson, object : TypeToken<List<Contact>>() {}.type
        )

        // Initialize RecyclerView with existing contacts
        mContactList.addAll(contactsList)
        contactAdapter = ContactAdapter(requireContext(), mContactList)
        contactList.adapter = contactAdapter
        contactList.layoutManager = LinearLayoutManager(requireContext())

        lgn.setOnClickListener {
            createContact()
        }

        return binding.root
    }


    @SuppressLint("NotifyDataSetChanged")
    private fun createContact() {
        val builder: AlertDialog.Builder =
            AlertDialog.Builder(requireContext(), R.style.CustomAlertDialogTheme)
        builder.setTitle("Create new contact")

        val layout = LinearLayout(requireContext())
        layout.orientation = LinearLayout.VERTICAL
        layout.gravity = Gravity.CENTER_VERTICAL

        val params = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT
        )

        val firstName = EditText(requireContext())
        firstName.setHintTextColor(Utils.customColor(requireContext(), R.color.subtext))
        firstName.setTextColor(Color.WHITE)
        firstName.inputType = InputType.TYPE_CLASS_TEXT
        firstName.background = null
        firstName.hint = "First name"

        val lastName = EditText(requireContext())
        lastName.setHintTextColor(Utils.customColor(requireContext(), R.color.subtext))
        lastName.setTextColor(Color.WHITE)
        lastName.inputType = InputType.TYPE_CLASS_TEXT
        lastName.background = null
        lastName.hint = "Last name"

        val contactNumber = EditText(requireContext())
        contactNumber.setHintTextColor(
            Utils.customColor(
                requireContext(), R.color.subtext
            )
        )
        contactNumber.setTextColor(Color.WHITE)
        contactNumber.inputType = InputType.TYPE_CLASS_PHONE
        contactNumber.background = null
        contactNumber.hint = "Phone number"

        layout.addView(firstName, params)
        layout.addView(lastName, params)
        layout.addView(contactNumber, params)

        builder.setView(layout)

        builder.setPositiveButton("Create") { _, _ ->
            val firstNameText = firstName.text.toString()
            val lastNameText = lastName.text.toString()
            val contactNumberText = contactNumber.text.toString()

            if (firstNameText.isNotBlank() && contactNumberText.isNotBlank()) {
                // Retrieve existing contacts from SharedPreferences
                val contactsJson = sharedPreferences.getString("contacts", "[]")
                val contactsList = Gson().fromJson(
                    contactsJson, object : TypeToken<List<Contact>>() {}.type
                ) as MutableList<Contact>

                // Add the new contact to the list
                val newContact = Contact(firstNameText, lastNameText, contactNumberText)
                contactsList.add(newContact)

                // Save the updated contacts list back to SharedPreferences
                val editor = sharedPreferences.edit()
                editor.putString("contacts", Gson().toJson(contactsList))
                editor.apply()

                // Update the RecyclerView
                mContactList.clear()
                mContactList.addAll(contactsList)
                contactAdapter?.notifyDataSetChanged()

                Toast.makeText(requireContext(), "Contact Created Success", Toast.LENGTH_SHORT)
                    .show()
            } else {
                Toast.makeText(requireContext(), "All fields are required", Toast.LENGTH_SHORT)
                    .show()
                if (firstNameText.isBlank()) firstName.error = "First name is required"
                if (contactNumberText.isBlank()) contactNumber.error = "Contact number is required"
            }
        }

        builder.setNegativeButton("Cancel") { dialog, _ ->
            dialog.dismiss()
        }

        builder.show()
    }

}

