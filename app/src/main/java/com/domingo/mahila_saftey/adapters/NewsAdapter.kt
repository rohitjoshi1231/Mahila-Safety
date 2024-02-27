package com.domingo.mahila_saftey.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.domingo.mahila_saftey.R
import com.domingo.mahila_saftey.activities.WebViewActivity
import com.domingo.mahila_saftey.modules.News
import com.squareup.picasso.Picasso

class NewsAdapter(
    private val newList: MutableList<News>,
    private val context: Context,
) : RecyclerView.Adapter<NewsAdapter.ViewHolder>(
) {
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val newsCoverImage: de.hdodenhof.circleimageview.CircleImageView =
            view.findViewById(R.id.coverImage)
        val newsTitle: TextView = view.findViewById(R.id.txtTitle)
        val newsDescription: TextView = view.findViewById(R.id.txtDescription)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.news_layout, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount() = newList.size


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val details = newList[position]

        holder.itemView.setOnClickListener {
            val intent = Intent(context, WebViewActivity::class.java)
            intent.putExtra("newsLink", details.newsLink)
            context.startActivity(intent)
        }

        Picasso.get().load(details.coverImage).placeholder(R.drawable.outline_image_24)
            .error(R.drawable.outline_broken_image_24)
            .into(holder.newsCoverImage)
        holder.newsTitle.text = details.title
        holder.newsDescription.text = details.description


    }
}