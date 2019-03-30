package com.dichotome.profilebarbinding.stubs

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.request.RequestOptions
import com.dichotome.profilebar.R
import com.dichotome.profilebar.stubs.FavListItem
import com.dichotome.profilebar.stubs.TabListItem
import com.dichotome.profilebar.util.extensions.download

class TabListHolder(val binding: ViewDataBinding) : RecyclerView.ViewHolder(binding.root) {

    val name = itemView.findViewById<TextView>(R.id.itemNameTV)
    val subtitle = itemView.findViewById<TextView>(R.id.subtitleTV)
    val thumbnail = itemView.findViewById<ImageView>(R.id.thumbnail)

    fun bind(data: TabListItem, isThumbnailCircular: Boolean) {
        name.text = data.name
        if (data is FavListItem)
            subtitle?.let {
                it.text = data.subtitle
            }
        val options = RequestOptions().apply {
            if (isThumbnailCircular)
                circleCrop()
        }
        thumbnail.download(data.thumbnailUrl, options)
    }
}

class TabListAdapter<T : ViewDataBinding>(
    val data: List<TabListItem>,
    val itemViewType: Int,
    val isThumbnailCircular: Boolean
) : RecyclerView.Adapter<TabListHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        LayoutInflater.from(parent.context).let { inflater ->
            DataBindingUtil.inflate<T>(inflater, viewType, parent, false).run {
                executePendingBindings()
                TabListHolder(this)
            }
        }

    override fun getItemCount() = data.size

    override fun onBindViewHolder(holder: TabListHolder, position: Int) = holder.bind(
        data[position],
        isThumbnailCircular
    )

    override fun getItemViewType(position: Int) = itemViewType
}