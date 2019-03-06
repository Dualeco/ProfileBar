package com.dichotome.profilebarapp.ui.mainBinding

import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import com.dichotome.profilebarapp.R
import com.dichotome.profilebarapp.databinding.FragmentProfileWithAdaptersBinding
import com.dichotome.profilebarapp.util.constant.drw
import com.dichotome.profilephoto.ui.ZoomingImageView
import com.dichotome.profileshared.extensions.dpToPx
import kotlinx.android.synthetic.main.fragment_profile_with_adapters.*

class ProfileBindingActivity : AppCompatActivity() {
    private val layoutId = R.layout.fragment_profile_with_adapters

    private var lastX = 0
    private var lastY = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        createBinding()

        profileBar.setupWithViewPager(profilePager)
    }

    private fun createBinding() = DataBindingUtil.setContentView<FragmentProfileWithAdaptersBinding>(this, layoutId)
        .apply {
            lifecycleOwner = this@ProfileBindingActivity
            logic = provideLogic()
            executePendingBindings()
        }

    private fun provideLogic() = ViewModelProviders.of(this@ProfileBindingActivity).get(ProfileBindingLogic::class.java)
}