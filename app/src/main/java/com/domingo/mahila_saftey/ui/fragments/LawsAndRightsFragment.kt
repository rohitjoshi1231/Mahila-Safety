package com.domingo.mahila_saftey.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.domingo.mahila_saftey.ui.adapters.LawsAdapter
import com.domingo.mahila_saftey.databinding.FragmentLawsAndRightsBinding

class LawsAndRightsFragment : Fragment() {
    private var lawsAdapter: LawsAdapter? = null
    private lateinit var binding: FragmentLawsAndRightsBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentLawsAndRightsBinding.inflate(inflater, container, false)

        val lawsRecyclerView = binding.lawsRecyclerView

        val laws = hashMapOf(
            "Guarantees equality before the law and prohibits discrimination based on sex." to "The Constitution of India",
            "Giving or taking dowry illegal and punishable by law." to "The Dowry Prohibition Act, 1961",
            "Offers protection to women from physical, emotional, sexual, and economic abuse within their homes." to "The Protection of Women from Domestic Violence Act, 2005",
            "Creates a safe and respectful work environment for women by prohibiting and addressing sexual harassment at workplaces." to "The Sexual Harassment of Women at Workplace Act, 2013",
            "Introduced stricter punishments for sexual offenses, including the death penalty for rape of a minor below 12 years old" to "The Criminal Law (Amendment) Act, 2013",
            "Guarantees free and compulsory education to all children between the ages of 6 and 14 years, promoting gender equality in education" to "The Right to Education Act, 2009"

        )

        lawsAdapter = LawsAdapter(laws)
        lawsRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        lawsRecyclerView.adapter = lawsAdapter


        return binding.root
    }

}