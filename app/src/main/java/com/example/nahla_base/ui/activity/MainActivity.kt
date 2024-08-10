package com.example.nahla_base.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.AbsListView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.nahla_base.R
import com.example.nahla_base.base.BaseActivity
import com.example.nahla_base.databinding.ActivityMainBinding
import com.example.nahla_base.ui.MainViewModel
import com.example.nahla_base.ui.adapter.RepoAdapter
import com.example.nahla_base.utils.Constants.QUERY_PAGE_SIZE
import kotlin.reflect.KClass

class MainActivity : BaseActivity<ActivityMainBinding, MainViewModel>() {

    private lateinit var repoAdapter: RepoAdapter
    var isLoading = false
    var isLastPage = false
    var isScrolling = false

    override fun resourceId(): Int = R.layout.activity_main

    override fun viewModelClass(): KClass<MainViewModel> = MainViewModel::class

    override fun setUI(savedInstanceState: Bundle?) {
        setupRecyclerView()
    }

    override fun clicks() {
    }

    override fun callApis() {
        viewModel.getRepositories()
    }

    override fun observer() {
        viewModel.repositoriesResponse.observe(this){
            repoAdapter.setData(it!!.toMutableList())
        }
    }



    val scrollListener = object : RecyclerView.OnScrollListener() {

        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)

            val layoutManager = recyclerView.layoutManager as LinearLayoutManager
            val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()
            val visibleItemCount = layoutManager.childCount
            val totalItemCount = layoutManager.itemCount

            val isNotLoadingAndNoteLastPage = !isLoading && !isLastPage
            val isAtLastItem = firstVisibleItemPosition + visibleItemCount >= totalItemCount
            val isNotAtBegging = firstVisibleItemPosition > 0
            val isTotalMoreThanVisible = totalItemCount >= QUERY_PAGE_SIZE

            val shouldPaginate =
                isNotLoadingAndNoteLastPage && isAtLastItem && isNotAtBegging && isTotalMoreThanVisible && isScrolling
            if (shouldPaginate) {
                viewModel.getRepositories()
                isScrolling = false
            }
        }

        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)

            if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                isScrolling = true
            }
        }

    }

    private fun setupRecyclerView() {
        repoAdapter=RepoAdapter()
        dataBinding.rvRepo.apply {
            adapter = repoAdapter
            layoutManager = LinearLayoutManager(this@MainActivity)
            addOnScrollListener(this@MainActivity.scrollListener)
        }
    }
}