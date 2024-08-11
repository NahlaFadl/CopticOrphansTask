package com.example.firebaseapp.ui.activity

import android.os.Bundle
import android.widget.AbsListView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.firebaseapp.R
import com.example.firebaseapp.databinding.ActivitySearchBinding
import com.example.firebaseapp.base.BaseActivity
import com.example.firebaseapp.ui.MainViewModel
import com.example.firebaseapp.ui.adapter.RepoAdapter
import com.example.firebaseapp.ui.adapter.SearchAdapter
import com.example.firebaseapp.utils.Constants
import kotlin.reflect.KClass

class SearchActivity : BaseActivity<ActivitySearchBinding, MainViewModel>() {

    lateinit var searchAdapter: SearchAdapter
    var isLoading = false
    var isLastPage = false
    var isScrolling = false

    override fun resourceId(): Int = R.layout.activity_search

    override fun viewModelClass(): KClass<MainViewModel> = MainViewModel::class

    override fun setUI(savedInstanceState: Bundle?) {
        window.statusBarColor = ContextCompat.getColor(this,R.color.black)
        setupRecyclerView()
    }

    override fun clicks() {
        dataBinding.ivSearch.setOnClickListener {
            viewModel.getSearch(this, dataBinding.edSearch.text.toString())
        }
    }

    override fun callApis() {
    }

    override fun observer() {
        viewModel.searchResponse.observe(this) {
            searchAdapter.setData(it.items.toMutableList())
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
            val isTotalMoreThanVisible = totalItemCount >= Constants.QUERY_PAGE_SIZE

            val shouldPaginate =
                isNotLoadingAndNoteLastPage && isAtLastItem && isNotAtBegging && isTotalMoreThanVisible && isScrolling
            if (shouldPaginate) {
                viewModel.getSearch(this@SearchActivity,dataBinding.edSearch.text.toString())
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
        searchAdapter= SearchAdapter()
        dataBinding.rvSearch.apply {
            adapter = searchAdapter
            layoutManager = LinearLayoutManager(this@SearchActivity)
            addOnScrollListener(this@SearchActivity.scrollListener)
        }
    }
}