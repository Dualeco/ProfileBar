package com.dichotome.profilebarbinding.stubFragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import com.dichotome.profilebar.stubFragments.TabFragment
import com.dichotome.profilebar.stubFragments.FavListItem
import com.dichotome.profilebarbinding.R
import com.dichotome.profilebarbinding.databinding.ItemTabFavouritesBinding
import com.dichotome.profilebarbinding.viewContentAdapters.TabListAdapter
import kotlinx.android.synthetic.main.fragment_profile_tab_binding.*

class FavouritesTabFragment() : TabFragment() {
    companion object {
        fun newInstance(tabTitle: String) = FavouritesTabFragment().apply {
            title = tabTitle
        }
    }
    val favList = arrayListOf(
        FavListItem(
            "The Amazing Spider-Man",
            "#5, 2018",
            "https://cdn.pastemagazine.com/www/system/images/photo_albums/bestcomiccoversof2018/large/amazing-spider-man--2-cover-art-by-ryan-ottley.png?1384968217"
        ),
        FavListItem(
            "Star Wars",
            "#55, 2008",
            "https://cdn.pastemagazine.com/www/system/images/photo_albums/bestcomiccoversof2018/large/star-wars--55-cover-art-by-david-marquez.png?1384968217"
        ),
        FavListItem(
            "Esteemed comic book author",
            "#214, 2017",
            "https://cdn.pastemagazine.com/www/system/images/photo_albums/bestcomiccoversof2018/large/amazing-spider-man--2-cover-art-by-ryan-ottley.png?1384968217"
        ),
        FavListItem(
            "Superior comic book writer",
            "#3, 2019",
            "https://cdn.pastemagazine.com/www/system/images/photo_albums/bestcomiccoversof2018/large/star-wars--55-cover-art-by-david-marquez.png?1384968217"
        ),
        FavListItem(
            "Batman: Rebirth",
            "#4, 2011",
            "https://cdn.pastemagazine.com/www/system/images/photo_albums/bestcomiccovers2017/large/batman26-mikeljanin.png?1384968217"
        ),
        FavListItem(
            "The Amazing Spider-Man",
            "#5, 2018",
            "https://cdn.pastemagazine.com/www/system/images/photo_albums/bestcomiccoversof2018/large/amazing-spider-man--2-cover-art-by-ryan-ottley.png?1384968217"
        ),
        FavListItem(
            "Star Wars",
            "#55, 2008",
            "https://cdn.pastemagazine.com/www/system/images/photo_albums/bestcomiccoversof2018/large/star-wars--55-cover-art-by-david-marquez.png?1384968217"
        ),
        FavListItem(
            "Esteemed comic book author",
            "#214, 2017",
            "https://cdn.pastemagazine.com/www/system/images/photo_albums/bestcomiccoversof2018/large/amazing-spider-man--2-cover-art-by-ryan-ottley.png?1384968217"
        ),
        FavListItem(
            "Superior comic book writer",
            "#3, 2019",
            "https://cdn.pastemagazine.com/www/system/images/photo_albums/bestcomiccoversof2018/large/star-wars--55-cover-art-by-david-marquez.png?1384968217"
        ),
        FavListItem(
            "Batman: Rebirth",
            "#4, 2011",
            "https://cdn.pastemagazine.com/www/system/images/photo_albums/bestcomiccovers2017/large/batman26-mikeljanin.png?1384968217"
        )
    )

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) =
        inflater.inflate(R.layout.fragment_profile_tab_binding, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val recyclerView = tabRecyclerViewBinding

        recyclerView.layoutManager = GridLayoutManager(context, 3)
        recyclerView.adapter =
            TabListAdapter<ItemTabFavouritesBinding>(
                favList,
                R.layout.item_tab_favourites,
                false
            )
    }
}