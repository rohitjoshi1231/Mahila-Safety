package com.domingo.mahila_saftey.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.domingo.mahila_saftey.SharedViewModel
import com.domingo.mahila_saftey.activities.MainActivity
import com.domingo.mahila_saftey.databinding.FragmentSettingsBinding

class SettingsFragment : Fragment() {
    private lateinit var binding: FragmentSettingsBinding
    private val sharedViewModel: SharedViewModel by lazy {
        ViewModelProvider(requireActivity())[SharedViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?,
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentSettingsBinding.inflate(inflater, container, false)
        val sharedPreferences =
            requireContext().getSharedPreferences("sharedContact", Context.MODE_PRIVATE)
        val editor = sharedPreferences?.edit()
        binding.loginBtn.setOnClickListener {
            val number = binding.contact.text
            sharedViewModel.sharedNumber = number.toString()
            editor?.putString("sharedNumber", sharedViewModel.sharedNumber)?.apply()
            Toast.makeText(
                context,
                "Emergency contact updated.",
                Toast.LENGTH_SHORT
            ).show()
        }

        binding.knowYourRights.setOnClickListener {
            try {
                requireActivity().supportFragmentManager.beginTransaction()
                    .replace(container!!.id, LawsAndRightsFragment()).addToBackStack(null).commit()
            } catch (e: Exception) {
                Toast.makeText(requireContext(), e.message.toString(), Toast.LENGTH_SHORT).show()
            }

        }

        binding.privacyLayout.setOnClickListener {
            try {
                requireActivity().supportFragmentManager.beginTransaction()
                    .replace(container!!.id, NavigateFragment()).addToBackStack(null).commit()
            } catch (e: Exception) {
                Toast.makeText(requireContext(), e.message.toString(), Toast.LENGTH_SHORT).show()
            }

        }
        binding.crimeAgainstWomen.setOnClickListener {
            try {
                requireActivity().supportFragmentManager.beginTransaction()
                    .replace(container!!.id, NewsFragment()).addToBackStack(null).commit()
            } catch (e: Exception) {
                Toast.makeText(requireContext(), e.message.toString(), Toast.LENGTH_SHORT).show()
            }

        }

        return binding.root
    }

    override fun onResume() {
        super.onResume()
        (activity as? MainActivity)?.disableGestureDetection()
    }

    override fun onPause() {
        super.onPause()
        (activity as? MainActivity)?.enableGestureDetection()
    }
}
