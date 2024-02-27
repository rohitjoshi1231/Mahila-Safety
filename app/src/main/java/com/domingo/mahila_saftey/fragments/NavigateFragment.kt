package com.domingo.mahila_saftey.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.domingo.mahila_saftey.activities.MainActivity
import com.domingo.mahila_saftey.databinding.FragmentNavigateBinding


class NavigateFragment : Fragment() {

    private lateinit var binding: FragmentNavigateBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentNavigateBinding.inflate(inflater, container, false)




        binding.privacyPolicy.setOnClickListener {
            try {
                requireActivity().supportFragmentManager.beginTransaction()
                    .replace(container!!.id, PrivacyPolicyFragment()).addToBackStack(null).commit()
            } catch (e: Exception) {
                Toast.makeText(requireContext(), e.message.toString(), Toast.LENGTH_SHORT).show()
            }

        }

        binding.appInfo.setOnClickListener {
            try {
                requireActivity().supportFragmentManager.beginTransaction()
                    .replace(container!!.id, AppInfoFragment()).addToBackStack(null).commit()
            } catch (e: Exception) {
                Toast.makeText(requireContext(), e.message.toString(), Toast.LENGTH_SHORT).show()
            }

        }
        binding.instructionLayout.setOnClickListener {
            try {
                requireActivity().supportFragmentManager.beginTransaction()
                    .replace(container!!.id, InstructionsFragment()).addToBackStack(null).commit()

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