package com.example.aplikasigit2

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.aplikasigit2.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private var dataUser = listOf<User>()
    private val mainViewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        println("AAAA")

        val layoutManager = LinearLayoutManager(this)
        binding.rvUsers.layoutManager = layoutManager
        val itemDecoration = DividerItemDecoration(this, layoutManager.orientation)
        binding.rvUsers.addItemDecoration(itemDecoration)


        mainViewModel.user.observe(this, { user ->
            dataUser = user
            //saat sedang insearch, rv sedang menampilkan searchUser list
            if (!inSearch()) setUserData(dataUser)
        })
        mainViewModel.isLoading.observe(this, {
            showLoading(it)
        })

        mainViewModel.searchUser.observe(this, {
            setUserData(it)
        })

    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu_items, menu)

        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchItem = menu.findItem(R.id.search)
        val searchView = searchItem.actionView as SearchView
        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        searchView.queryHint = resources.getString(R.string.search_hint)

        //code di bawah untuk mempertahankan query search
        val priorText =
            mainViewModel.newText
        if (priorText.isNotEmpty()) {
            searchItem.expandActionView()
            searchView.setQuery(priorText, false)
            mainViewModel.newText = priorText
            setUserData(mainViewModel.filtered)
        }

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                mainViewModel.newText = newText
                if (!inSearch()) setUserData(dataUser)
                return false
            }
        })
        return true
    }

    private fun setUserData(user: List<User>) {
        val listUserAdapter = ListUserAdapter(user)
        binding.rvUsers.adapter = listUserAdapter

        listUserAdapter.setOnItemClickCallback(object : ListUserAdapter.OnItemClickCallback {
            override fun onItemClicked(data: User) {
                sendSelectedUser(data)
            }
        })
    }

    private fun sendSelectedUser(user: User) {
        val intent = Intent(this@MainActivity, DetailActivity::class.java)
        intent.putExtra(DetailActivity.EXTRA_USER, user)
        startActivity(intent)
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
            binding.rvUsers.alpha = 0.0F
        } else {
            binding.progressBar.visibility = View.GONE
            binding.rvUsers.alpha = 1F
        }
    }

    private fun inSearch(): Boolean = mainViewModel.newText != ""
}