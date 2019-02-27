package com.dichotome.collapsingbar.ui.databinding

import android.os.Bundle
import androidx.lifecycle.ViewModelProviders
import com.dichotome.collapsingbar.R
import com.dichotome.collapsingbar.ui.core.CoreActivity
import kotlinx.android.synthetic.main.fragment_profile_with_adapters.*
import com.dichotome.collapsingbar.databinding.FragmentProfileWithAdaptersBinding

class ProfileActivityBinding : CoreActivity<ProfileLogic, FragmentProfileWithAdaptersBinding>() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        profileBar.setupWithViewPager(profilePager)
    }
    override fun provideLogic() = ViewModelProviders.of(this).get(ProfileLogic::class.java)

    override fun provideLayout() = LayoutBuilder(R.layout.fragment_profile_with_adapters) {
        logic = this@ProfileActivityBinding.logic
    }
}