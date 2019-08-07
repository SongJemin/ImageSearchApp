package com.jem.imagesearchapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.jem.imagesearchapp.base.BaseViewModel
import com.jem.imagesearchapp.data.get.response.GetImageSearchResponse
import com.jem.imagesearchapp.util.network.NetworkService
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers


class MainViewModel(val networkService: NetworkService) : BaseViewModel() {

    val KAKAO_REST_API_KEY = "KakaoAK a8ef77758e604843a6cf06a0162a2544"

    private val _imageSearchResponseLiveData = MutableLiveData<GetImageSearchResponse>()
    val imageSearchLiveData : LiveData<GetImageSearchResponse> = _imageSearchResponseLiveData

    fun imageSearch(inputString : String, count : Int){
        addDisposable(networkService.getImageSearch(KAKAO_REST_API_KEY, inputString, count.toString())
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                it.run{
                    if (documents.size >= 0) {
                        _imageSearchResponseLiveData.postValue(this)
                    }
                }
            },{}))
    }


}