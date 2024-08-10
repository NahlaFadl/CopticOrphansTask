package com.example.nahla_base.base

import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.example.nahla_base.R
import com.example.nahla_base.data.remote.RetrofitApi
import com.example.nahla_base.data.remote.dto.ResultDto
import com.example.nahla_base.data.remote.networkHandling.NetworkResult
import com.example.nahla_base.data.remote.networkHandling.NetworkStatus
import com.example.nahla_base.utils.AppManger
import com.example.nahla_base.utils.MyUtils.dialogMessage
import com.google.gson.Gson
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.getViewModel
import retrofit2.Response
import kotlin.reflect.KClass

abstract class BaseActivity<T : ViewDataBinding, V : BaseViewModel> : AppCompatActivity(),
    NetworkStatus {

    protected lateinit var dataBinding: T

    protected val viewModel: V by lazy { getViewModel(null,viewModelClass(),null) }

    protected val appManger: AppManger by inject()

    private var dialog: AlertDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        init()
        baseObserver()
        setUI(savedInstanceState)
        clicks()
        callApis()
        observer()
    }

    override fun onResume() {
        super.onResume()
        NetworkResult.observeNetworkStatus(this)
    }

    private fun init() {
        dataBinding = DataBindingUtil.setContentView(this, resourceId())
        dataBinding.lifecycleOwner = this
    }

    private fun baseObserver() {
        viewModel.loading.observe(this) {
            toggleLoadingDialog(it)
        }

        viewModel.showMassage.observe(this) {
            dialogMessage(it)
        }
    }

    private fun toggleLoadingDialog(show: Boolean) {
        if (dialog == null) {
            dialog = AlertDialog.Builder(this)
                .setView(R.layout.progress)
                .setCancelable(false)
                .create()
            dialog?.window?.setBackgroundDrawableResource(android.R.color.transparent)
        }

        if (show) dialog?.show()
        else dialog?.dismiss()
    }

    abstract fun resourceId(): Int
    abstract fun viewModelClass(): KClass<V>
    abstract fun setUI(savedInstanceState: Bundle?)
    abstract fun clicks()
    abstract fun callApis()
    abstract fun observer()

    override fun onNoInternet() {
        dialogMessage("No Internet Connection!")
    }

    override fun onNotVerifyRequest(exception: String?) {
        dialogMessage("Not Verify Request!")
    }

    override fun onApiNotFound() {
        dialogMessage("Not Found!")
    }

    override fun onNotAllowed() {

    }

    override fun onMakeAction(exception: String?) {
        val data = Gson().fromJson(exception, ResultDto::class.java)
        dialogMessage(data.message)
    }

    override fun <T> onDynamicCode(response: Response<T>) {
        dialogMessage(response.message().toString())
    }

    override fun onConnectionFail(exception: String?) {
        val msg = if (exception.toString()
                .contains(RetrofitApi.getBaseUrl().replace("https://", "").replace("/api/", ""))
        ) {
            exception.toString()
                .replace(RetrofitApi.getBaseUrl().replace("https://", "").replace("/api/", ""), "server")
        } else {
            exception.toString()
        }

        dialogMessage(msg)
    }

    override fun onNotAuthorized(exception: String?) {
        dialogMessage(exception.toString())
    }

    override fun onServerSideError(exception: String?) {
        val data = Gson().fromJson(exception, ResultDto::class.java)
        dialogMessage(data.message)
    }

    override fun onBadRequest(exception: String?) {
        val data = Gson().fromJson(exception, ResultDto::class.java)
        dialogMessage(data.message)
    }

    override fun onTooManyRequests(exception: String?){
        val data = Gson().fromJson(exception, ResultDto::class.java)
        dialogMessage(data.message)
    }

    override fun onPause() {
        super.onPause()
        dialog?.dismiss()
    }

    override fun onStop() {
        super.onStop()
        dialog?.dismiss()
    }
}