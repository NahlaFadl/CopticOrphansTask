package com.example.nahla_base.ui

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.viewModelScope
import com.example.nahla_base.base.BaseViewModel
import com.example.nahla_base.data.remote.dto.RepositoriesData
import com.example.nahla_base.data.repositry.MainRepository
import com.example.nahla_base.utils.AppManger
import kotlinx.coroutines.launch

class MainViewModel(
    override val appManger: AppManger,
    private val mainRepository: MainRepository,
) : BaseViewModel(appManger) {

    //Response
    val repositoriesResponse = MediatorLiveData<List<RepositoriesData>?>()
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
}