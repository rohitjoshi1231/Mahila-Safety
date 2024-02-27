package com.domingo.mahila_saftey.fragments

import android.os.Bundle
import android.view.GestureDetector
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.domingo.mahila_saftey.R
import com.domingo.mahila_saftey.adapters.EmergencyContactAdapter
import com.domingo.mahila_saftey.data
import com.domingo.mahila_saftey.databinding.FragmentHomeBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

class HomeFragment : Fragment() {
    private var emergencyContactAdapter: EmergencyContactAdapter? = null
    private lateinit var binding: FragmentHomeBinding
    private var job: Job? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?,
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)


        val dataRecyclerView = binding.contactRecyclerView

        val data = hashMapOf(
            "Emergency Helpline" to "112",
            "Police Control Room" to "100",
            "Women\'s Helpline" to "181",
            "Report harassment" to "1091",
            "Child Distress Helpline" to "1098",
        )

        emergencyContactAdapter = EmergencyContactAdapter(requireActivity(), data)
        dataRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        dataRecyclerView.adapter = emergencyContactAdapter

        val textView = binding.fact
        job = CoroutineScope(Dispatchers.Main).launch {
            while (isActive) {
                textView.text = data()
                fadeIn(textView)
                delay(10000)
                fadeOut(textView)
                textView.text = ""
            }
        }
        return binding.root
    }


    private fun fadeIn(textView: TextView) {
        val fadeIn = AnimationUtils.loadAnimation(textView.context, R.anim.fade_in)
        textView.startAnimation(fadeIn)
    }

    private fun fadeOut(textView: TextView) {
        val fadeOut = AnimationUtils.loadAnimation(textView.context, R.anim.fade_out)
        textView.startAnimation(fadeOut)
    }

    override fun onPause() {
        super.onPause()
        job?.cancel()
    }


}