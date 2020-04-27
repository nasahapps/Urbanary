package com.nasahapps.urbanary.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.nasahapps.urbanary.R
import com.nasahapps.urbanary.model.Definition
import kotlinx.android.synthetic.main.list_definition.view.*

class DefinitionAdapter(
        private val definitions: List<Definition>
) : RecyclerView.Adapter<DefinitionAdapter.ViewHolder>() {

    class ViewHolder(v: View) : RecyclerView.ViewHolder(v)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layout = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_definition, parent, false)
        return ViewHolder(layout)
    }

    override fun getItemCount() = definitions.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val definition = definitions[position]
        holder.itemView.wordText.text = definition.word
        holder.itemView.definitionText.text = definition.definition?.replace(Regex("[\\[\\]]"), "")
        holder.itemView.thumbsUpCountText.text = definition.thumbsUp.toString()
        holder.itemView.thumbsDownCountText.text = definition.thumbsDown.toString()
    }

}