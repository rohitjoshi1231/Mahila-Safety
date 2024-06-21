package com.domingo.mahila_saftey.ui.fragments

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.domingo.mahila_saftey.databinding.FragmentAppInfoBinding

class AppInfoFragment : Fragment() {

    private lateinit var binding: FragmentAppInfoBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentAppInfoBinding.inflate(inflater, container, false)


        binding.btnSetInstagram.setOnClickListener {
            openSocialMediaProfile("https://m.instagram.com/_rohit_5250")
        }

        binding.linkedIn.setOnClickListener {
            openSocialMediaProfile("https://www.linkedin.com/in/rohit-joshi-4678a6258/")
        }

        binding.gmail.setOnClickListener {
            val gmailAddress = "rohitjoshi51832@gmail.com"
            val clipboard =
                requireContext().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager

            val clip = ClipData.newPlainText("Gmail Address", gmailAddress)
            clipboard.setPrimaryClip(clip)
            Toast.makeText(
                requireContext(),
                "Gmail address copied to clipboard",
                Toast.LENGTH_SHORT
            ).show()
        }

        return binding.root
    }

    private fun openSocialMediaProfile(url: String) {
        if (url.isNotEmpty()) {
            val uri = Uri.parse(url)
            val intent = Intent(Intent.ACTION_VIEW, uri)
            startActivity(intent)
        }
    }
}