package com.domingo.mahila_saftey.activities

import android.os.Bundle
import android.view.View
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.domingo.mahila_saftey.R
import com.domingo.mahila_saftey.Utils
import com.domingo.mahila_saftey.databinding.ActivityWebViewBinding

class WebViewActivity : AppCompatActivity() {
    private lateinit var binding: ActivityWebViewBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWebViewBinding.inflate(layoutInflater)
        setContentView(binding.root)
        window.statusBarColor = Utils.customColor(this, R.color.white)

        val newsLink = intent.getStringExtra("newsLink")

        if (newsLink != null) {
            binding.progressBar.visibility = View.VISIBLE

            // Load URL in WebView with WebViewClient
            binding.webView.webViewClient = object : WebViewClient() {
                override fun onPageFinished(view: android.webkit.WebView?, url: String?) {
                    super.onPageFinished(view, url)
                    // Hide progress bar after page finishes loading
                    binding.progressBar.visibility = View.GONE
                }
            }

            // Load URL
            binding.webView.loadUrl(newsLink)
        } else {
            Toast.makeText(this, "No Data Available", Toast.LENGTH_SHORT).show()
        }
    }
}
