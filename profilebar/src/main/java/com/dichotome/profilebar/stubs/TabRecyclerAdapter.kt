package com.dichotome.profilebar.stubs

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.request.RequestOptions
import com.dichotome.profilebar.R
import com.dichotome.profilebar.util.extensions.download

open class TabRecyclerHolder(inflater: LayoutInflater, parent: ViewGroup, viewType: Int) : RecyclerView.ViewHolder(
    inflater.inflate(viewType, parent, false)
) {

    val name = itemView.findViewById<TextView>(R.id.itemNameTV)
    val subtitle = itemView.findViewById<TextView>(R.id.subtitleTV)
    val thumbnail = itemView.findViewById<ImageView>(R.id.thumbnail)

    fun bind(data: TabRecyclerItem, isThumbnailCircular: Boolean) {
        name.text = data.name
        if (data is FavRecyclerItem)
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

open class TabRecyclerAdapter(
    var data: List<TabRecyclerItem>,
    val itemViewType: Int,
    val isThumbnailCircular: Boolean
) : RecyclerView.Adapter<TabRecyclerHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = TabRecyclerHolder(
        LayoutInflater.from(parent.context),
        parent,
        itemViewType
    )

    override fun getItemCount() = data.size

    override fun onBindViewHolder(holder: TabRecyclerHolder, position: Int) = holder.bind(
        data[position],
        isThumbnailCircular
    )

    override fun getItemViewType(position: Int) = itemViewType

    fun updateData(newData: List<TabRecyclerItem>) = DiffUtil.calculateDiff(
            TabDiffUtilCallback(data, newData)
        ).apply {
            data = newData
            dispatchUpdatesTo(this@TabRecyclerAdapter)
        }
}

class TabDiffUtilCallback(
    private val oldList: List<TabRecyclerItem>,
    private val newList: List<TabRecyclerItem>
) : DiffUtil.Callback() {
    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int) =
        oldList[oldItemPosition].uuid == newList[newItemPosition].uuid

    override fun getOldListSize() = oldList.size

    override fun getNewListSize() = newList.size

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int) =
        oldList[oldItemPosition].name == newList[newItemPosition].name

}