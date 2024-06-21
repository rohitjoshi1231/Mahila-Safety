package com.domingo.mahila_saftey.ui.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.domingo.mahila_saftey.Repository
import com.domingo.mahila_saftey.data
import com.domingo.mahila_saftey.databinding.FragmentNewsBinding
import com.domingo.mahila_saftey.ui.activities.MainActivity.Companion.TAG
import com.domingo.mahila_saftey.ui.adapters.NewsAdapter
import com.domingo.mahila_saftey.ui.modules.News
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.net.UnknownHostException


class NewsFragment : Fragment() {
    private lateinit var binding: FragmentNewsBinding
    private var newsAdapter: NewsAdapter? = null
    private var news = mutableListOf<News>()
    private var isLoading = false
    private var page = 1

    companion object {
        private const val PAGE_SIZE = 5
    }


    @SuppressLint("NotifyDataSetChanged")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentNewsBinding.inflate(inflater, container, false)
        val newsRecyclerView = binding.newsRecycler
        val progressBar = binding.progressBar

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val fetchedNews =
                    Repository().scrapeNews("https://www.ndtv.com/topic/crime-against-women")
                news.addAll(fetchedNews)
            } catch (e: UnknownHostException) {
                // Handle the UnknownHostException
                Log.e(TAG, "News Fragment: ${e.message}")
                requireActivity().runOnUiThread {
                    binding.noInternet.visibility = View.VISIBLE
                    progressBar.visibility = View.GONE
                    binding.facts.visibility = View.GONE
                    binding.view.visibility = View.GONE
                }
            }
            activity?.runOnUiThread {
                newsAdapter?.notifyDataSetChanged()
            }
        }

        // Initialize RecyclerView and Adapter
        newsAdapter = NewsAdapter(news, requireActivity())
        newsRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        newsRecyclerView.adapter = newsAdapter


        newsRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                val visibleItemCount = layoutManager.childCount
                val totalItemCount = layoutManager.itemCount
                val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()

                if (!isLoading && (visibleItemCount + firstVisibleItemPosition) >= totalItemCount && firstVisibleItemPosition >= 0 && totalItemCount >= PAGE_SIZE) {
                    // Load more news items
                    page++
                    loadMoreNews()
                }
            }
        })

        CoroutineScope(Dispatchers.Main).launch {
            loadNews(progressBar)
        }
        return binding.root
    }

    private fun loadMoreNews() {
        val progressBar = binding.progressBar
        if (!isLoading && news.size % PAGE_SIZE == 0) {
            CoroutineScope(Dispatchers.Main).launch {
                loadNews(progressBar)
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private suspend fun loadNews(progressBar: ProgressBar) {
        isLoading = true
        binding.facts.text = data()
        progressBar.visibility = View.VISIBLE // Show progress bar
        binding.view.visibility = View.VISIBLE
        binding.facts.visibility = View.VISIBLE
        fadeIn(binding.facts)
        fadeOut(binding.facts)
        binding.facts.text = ""
        val url = "https://www.ndtv.com/topic/crime-against-women?page=$page"
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val fetchedNews =
                    Repository().scrapeNews(url)
                news.addAll(fetchedNews)
                withContext(Dispatchers.Main) {
                    newsAdapter?.notifyDataSetChanged()
                    isLoading = false
                    progressBar.visibility = View.GONE
                    binding.facts.visibility = View.GONE
                    binding.view.visibility = View.GONE
                }
            } catch (e: Exception) {
                Log.e(TAG, "News Fragment: ${e.message}")
                withContext(Dispatchers.Main) {
                    progressBar.visibility = View.GONE
                    fadeOut(binding.facts)
                    binding.view.visibility = View.GONE

                }
            }
        }
    }
}

// Fade in animation
private fun fadeIn(view: View) {
    view.alpha = 0f
    view.animate()
        .alpha(1f)
        .setDuration(1000)
        .setListener(null)
}

// Fade out animation
private fun fadeOut(view: View) {
    view.animate()
        .alpha(0f)
        .setDuration(1000)
        .setListener(null)
}
