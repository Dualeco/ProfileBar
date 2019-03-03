package com.dichotome.profilebar.viewContentAdapters

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.dichotome.profilebar.R
import com.dichotome.profilebar.stubFragments.FavListItem
import com.dichotome.profilebar.stubFragments.TabListItem
import com.dichotome.profilebar.util.view.extensions.download

class TabListHolder(inflater: LayoutInflater, parent: ViewGroup, viewType: Int) : RecyclerView.ViewHolder(
    inflater.inflate(viewType, parent, false)
) {

    val name = itemView.findViewById<TextView>(R.id.itemNameTV)
    val subtitle = itemView.findViewById<TextView>(R.id.subtitleTV)
    val thumbnail = itemView.findViewById<ImageView>(R.id.thumbnail)

    fun bind(data: TabListItem, isThumbnailCircular: Boolean) {
        name.text = data.name
        if (data is FavListItem)
            subtitle?.let {
                it.text = data.subtitle
            }

        thumbnail.download(data.thumbnailUrl, isThumbnailCircular)
    }
}

class TabListAdapter(
    private val data: List<TabListItem>,
    private val itemViewType: Int,
    private val isThumbnailCircular: Boolean
) : RecyclerView.Adapter<TabListHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TabListHolder {
        val inflater = LayoutInflater.from(parent.context)

        return TabListHolder(inflater, parent, itemViewType)
    }

    override fun getItemCount() = data.size

    override fun onBindViewHolder(holder: TabListHolder, position: Int) {
        val sub = data[position]
        holder.bind(sub, isThumbnailCircular)
    }

    override fun getItemViewType(position: Int) = itemViewType
}