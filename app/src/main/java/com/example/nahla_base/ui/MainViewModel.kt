package com.example.nahla_base.ui

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.viewModelScope
import com.example.nahla_base.base.BaseViewModel
import com.example.nahla_base.data.remote.dto.RepositoriesData
import com.example.nahla_base.data.remote.dto.SearchData
import com.example.nahla_base.data.repositry.MainRepository
import com.example.nahla_base.utils.AppManger
import kotlinx.coroutines.launch

class MainViewModel(
    override val appManger: AppManger,
    private val mainRepository: MainRepository,
) : BaseViewModel(appManger) {

    //Response
    val repositoriesResponse = MediatorLiveData<List<RepositoriesData>?>()
    val searchResponse = MediatorLiveData<SearchData>()

    var reposPage = 1

    fun getRepositories(){
        loading.value=true
        viewModelScope.launch {
            mainRepository.getRepositories(reposPage).let {
                loading.value=false
                reposPage++
                    val oldRepos = it.data?.toMutableList()
                    val newRepos = it.data
                    oldRepos?.addAll(newRepos!!.toMutableList())

                it.data?.let {
                    repositoriesResponse.postValue(oldRepos)
                }
            }
        }
    }

    var searchPage = 1
    var searchDataResponse: SearchData? = null

    fun getSearch(context: Context,txtSearch:String){
        if (txtSearch.isEmpty()){
            Toast.makeText(context, "enter search data", Toast.LENGTH_SHORT).show()
        }
        loading.value=true
        viewModelScope.launch {
            mainRepository.getSearch(txtSearch,searchPage).let {
                loading.value=false
                searchPage++
                if (searchDataResponse == null) {
                    searchDataResponse = it.data
                }
                else {
                    val oldArticles = searchDataResponse?.items
                    val newArticles = it.data?.items
                    oldArticles?.addAll(newArticles!!.toMutableList())
                }
                it.data?.let {
                    searchResponse.postValue(searchDataResponse)
                }
            }
        }
    }
}