package com.example.aplikasigit2

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.aplikasigit2.databinding.ActivityDetailBinding
import com.example.aplikasigit2.ui.main.SectionsPagerAdapter
import com.google.android.material.tabs.TabLayoutMediator


class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding
    private val detailViewModel: DetailViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val sectionsPagerAdapter = SectionsPagerAdapter(this)
        binding.viewPager.adapter = sectionsPagerAdapter

        TabLayoutMediator(binding.tabs, binding.viewPager) { tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()

        if (detailViewModel.userlogin.isEmpty()) {
            val user = intent.getParcelableExtra<User>(EXTRA_USER) as User
            detailViewModel.userlogin = user.login
        }

        detailViewModel.detailuser.observe(this, {
            resetTabPosition()
            setDetailUser(it)
        })
        detailViewModel.isLoading.observe(this, {
            showLoading(it)
        })


    }

    private fun setDetailUser(detailuser: DetailUser) {
        binding.tvDetailNama.text = detailuser.name ?: " - "
        binding.tvRepo.text = detailuser.publicRepos
        binding.tvLocation.text = detailuser.location ?: " - "
        binding.tvFollower.text = detailuser.followers
        binding.tvFollowing.text = detailuser.following
        binding.tvCompany.text = detailuser.company ?: " - "
        val actionBar = supportActionBar
        actionBar!!.title = detailuser.login.toUsernameFormat()
        Glide.with(this)
            .load(detailuser.avatarUrl)
            .placeholder(R.drawable.ic_launcher_foreground)
            .into(binding.imgAvatar)

    }

    private fun String.toUsernameFormat(): String {
        return StringBuilder("@").append(this).toString()
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
            resetDetailUser()
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }

    private fun resetDetailUser() {
        binding.tvDetailNama.text = ""
        binding.tvRepo.text = ""
        binding.tvLocation.text = ""
        binding.tvFollower.text = ""
        binding.tvFollowing.text = ""
        binding.tvCompany.text = ""
        val actionBar = supportActionBar
        actionBar!!.title = this.title
        Glide.with(this)
            .load(R.drawable.ic_launcher_foreground)
            .into(binding.imgAvatar)
    }

    private fun resetTabPosition() {
        binding.tabs.getTabAt(0)?.select()
    }

    companion object {
        const val EXTRA_USER = "extra_user"

        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.followers,
            R.string.following
        )
    }

}