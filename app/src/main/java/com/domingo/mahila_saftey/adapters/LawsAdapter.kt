package com.domingo.mahila_saftey.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.domingo.mahila_saftey.R

class LawsAdapter(
    private val data: HashMap<String, String>,

    ) : RecyclerView.Adapter<LawsAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val title = view.findViewById<TextView>(R.id.txtTitle)!!
        val description = view.findViewById<TextView>(R.id.txtDescription)!!
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.laws_layout, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount() = data.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val keys = ArrayList(data.keys)
        val key = keys[position]
        val value = data[key]
        holder.title.text = value
        holder.description.text = key
    }


}



